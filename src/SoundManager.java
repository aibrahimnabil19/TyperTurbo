import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;
import javax.sound.sampled.*;

/**
 * Resource-aware SoundManager.
 *
 * Usage:
 *  - Filesystem path: "C:\\...\\Click.WAV"
 *  - Classpath resource path: "/Audio/Click.WAV"  (leading slash required)
 *
 * Background music:
 *  - playBackgroundRandom(List<String>) accepts a mix of filesystem and resource paths.
 *  - Resource audio is copied to temp files and passed to musicPlayer (so existing file-based players still work).
 *
 * Effects:
 *  - playEffect(String) supports WAV/PCM from file OR classpath resource.
 *  - MP3 is not supported by javax.sound.sampled for effects (will log a warning).
 */
public class SoundManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(SoundManager.class);
    private static final String PREF_MUSIC = "backgroundMusicEnabled";
    private static final String PREF_EFFECTS = "effectsEnabled";

    private static volatile boolean backgroundMusicEnabled;
    private static volatile boolean effectsEnabled;

    // Your existing MusicPlayer (JLayer etc) — assumed to accept List<String> file paths for playRandom(...)
    private static volatile MusicPlayer musicPlayer;

    // Remember the last playlist used so we can restart when re-enabled (original paths: may be resource or file)
    private static volatile List<String> lastPlaylist = null;

    // Temp files created for resource-backed background tracks (so we can clean them up)
    private static volatile List<File> lastTempFiles = null;

    static {
        backgroundMusicEnabled = prefs.getBoolean(PREF_MUSIC, true);
        effectsEnabled = prefs.getBoolean(PREF_EFFECTS, true);
    }

    public static synchronized void setMusicPlayer(MusicPlayer player) {
        if (musicPlayer != null && musicPlayer != player) {
            musicPlayer.stop();
            cleanupTempFiles();
        }
        musicPlayer = player;
        if (!backgroundMusicEnabled && musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.stop();
        }
    }

    public static boolean isBackgroundMusicEnabled() {
        return backgroundMusicEnabled;
    }

    public static void setBackgroundMusicEnabled(boolean enabled) {
        backgroundMusicEnabled = enabled;
        prefs.putBoolean(PREF_MUSIC, enabled);

        if (musicPlayer == null) return;

        if (!enabled) {
            if (musicPlayer.isPlaying()) musicPlayer.stop();
            cleanupTempFiles(); // ensure temp files are removed if background stopped
        } else {
            // enabled == true: restart if nothing is playing and we have a playlist
            if (!musicPlayer.isPlaying() && lastPlaylist != null && !lastPlaylist.isEmpty()) {
                playBackgroundRandom(lastPlaylist);
            }
        }
    }

    public static boolean isEffectsEnabled() {
        return effectsEnabled;
    }

    public static void setEffectsEnabled(boolean enabled) {
        effectsEnabled = enabled;
        prefs.putBoolean(PREF_EFFECTS, enabled);
    }

    /**
     * Play background music chosen randomly from the provided list.
     * Each entry may be:
     *  - absolute or relative filesystem path (e.g. "C:\\Music\\t.wav")
     *  - classpath resource path starting with '/' (e.g. "/Audio/track.mp3" or "/Audio/track.wav")
     *
     * If a resource path is provided, we copy it to a temporary file and pass that path to musicPlayer.
     */
    public static synchronized void playBackgroundRandom(List<String> songs) {
        if (songs == null || songs.isEmpty()) return;

        List<String> resolvedSongs = new ArrayList<>();
        for (String s : songs) {
            if (s == null) continue;
            resolvedSongs.add(AssetResolver.resolve(s));
        }

        lastPlaylist = new ArrayList<>(resolvedSongs); // remember original list
        // Respect toggle
        if (!backgroundMusicEnabled) return;

        if (musicPlayer == null) return;

        // Build list of playable file paths: real files are passed through; resources are copied to temp files.
        List<String> playablePaths = new ArrayList<>();
        List<File> tempFiles = new ArrayList<>();

        for (String s : resolvedSongs) {
            if (s == null) continue;
            if (isClasspathResource(s)) {
                try {
                    File tmp = copyResourceToTempFile(s);
                    if (tmp != null) {
                        playablePaths.add(tmp.getAbsolutePath());
                        tempFiles.add(tmp);
                    } else {
                        System.err.println("SoundManager: failed to copy resource: " + s);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // filesystem path - check existence before adding
                File f = new File(s);
                if (f.exists()) {
                    playablePaths.add(f.getAbsolutePath());
                } else {
                    System.err.println("SoundManager: background file not found: " + s);
                }
            }
        }

        // If we didn't find any playable files, give up
        if (playablePaths.isEmpty()) {
            cleanupTempFiles(); // clean previously created temps (if any)
            return;
        }

        // store temp files so they can be removed on stop
        lastTempFiles = tempFiles;

        // delegate to your musicPlayer (assumed to accept List<String> of file paths)
        // choose a random track to start with (musicPlayer.playRandom should handle playlist/randomization;
        // if your MusicPlayer expects a single path, you can pick one random element instead)
        try {
            musicPlayer.playRandom(playablePaths);
        } catch (Exception e) {
            // protect the app from any musicPlayer thrown exceptions
            e.printStackTrace();
            cleanupTempFiles();
        }
    }

    /**
     * Stop background music and clean up any temp files created for resource-based tracks.
     */
    public static synchronized void stopBackgroundMusic() {
        if (musicPlayer != null) {
            try {
                musicPlayer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cleanupTempFiles();
    }

    /**
     * Play a short effect. Accepts either filesystem path or classpath resource (leading '/').
     * Recommended: use WAV/PCM for effects. MP3 for effects is not supported by javax.sound.sampled.
     */
    public static void playEffect(String pathOrResource) {
        if (!effectsEnabled) return;
        if (pathOrResource == null) return;

        String resolvedPath = AssetResolver.resolve(pathOrResource);

        // If MP3 -> not supported by javax.sound.sampled for effects
        String ext = getExtension(resolvedPath).toLowerCase();
        if ("mp3".equals(ext)) {
            System.err.println("SoundManager: MP3 for effects is not supported by javax.sound.sampled. Use WAV or JavaFX for MP3.");
            return;
        }

        // Start playback on background thread
        new Thread(() -> {
            AudioInputStream ais = null;
            try {
                if (isClasspathResource(resolvedPath)) {
                    InputStream ris = AssetResolver.openResourceStream(resolvedPath);
                    // wrap to support mark/reset used by AudioSystem
                    BufferedInputStream bis = new BufferedInputStream(ris);
                    ais = AudioSystem.getAudioInputStream(bis);
                } else {
                    File file = new File(resolvedPath);
                    if (!file.exists()) {
                        System.err.println("SoundManager: effect file not found: " + resolvedPath);
                        return;
                    }
                    ais = AudioSystem.getAudioInputStream(file);
                }

                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });

            } catch (UnsupportedAudioFileException e) {
                System.err.println("SoundManager: Unsupported audio file (maybe MP3?): " + pathOrResource);
                e.printStackTrace();
            } catch (LineUnavailableException | IOException e) {
                e.printStackTrace();
            } finally {
                // AudioInputStream will be closed by try-with-resources when created from File,
                // but when using the stream path we should close it explicitly here
                if (ais != null) {
                    try {
                        ais.close();
                    } catch (IOException ignored) {}
                }
            }
        }, "SoundManager-EffectThread").start();
    }

    private static boolean isClasspathResource(String path) {
        if (path == null) return false;
        String normalized = path.trim().replace('\\', '/');
        return normalized.startsWith("/")
                || normalized.startsWith("main/resources/")
                || normalized.startsWith("Audio/")
                || normalized.startsWith("Images/")
                || normalized.contains("/main/resources/")
                || normalized.contains("/Audio/")
                || normalized.contains("/Images/");
    }

    private static String getExtension(String path) {
        if (path == null) return "";
        int i = path.lastIndexOf('.');
        return (i > 0) ? path.substring(i + 1) : "";
    }

    /**
     * Copy a classpath resource to a temporary file and return the File.
     * The caller is responsible for deleting the file later (cleanupTempFiles() will attempt that).
     */
    private static File copyResourceToTempFile(String resourcePath) throws IOException {
        if (!isClasspathResource(resourcePath)) return null;
        String normalized = resourcePath.trim().replace('\\', '/');
        String ext = getExtension(normalized);
        if (ext.isEmpty()) ext = "tmp";
        File tmp = Files.createTempFile("sound-", "." + ext).toFile();
        tmp.deleteOnExit();

        try (InputStream ris = AssetResolver.openResourceStream(normalized);
             OutputStream os = new FileOutputStream(tmp)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = ris.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            os.flush();
            return tmp;
        } catch (IOException e) {
            tmp.delete();
            throw e;
        }
    }

    /**
     * Delete temp files created for background playback.
     */
    private static synchronized void cleanupTempFiles() {
        if (lastTempFiles != null) {
            for (File f : lastTempFiles) {
                try {
                    if (f != null && f.exists()) {
                        boolean deleted = f.delete();
                        if (!deleted) {
                            // leave to deleteOnExit as fallback
                        }
                    }
                } catch (Exception ignored) {}
            }
            lastTempFiles = null;
        }
    }

    // Optional: explicitly pre-register a default playlist (original paths - may be resources)
    public static void setDefaultPlaylist(List<String> playlist) {
        lastPlaylist = playlist == null ? null : new ArrayList<>(playlist);
    }
}
