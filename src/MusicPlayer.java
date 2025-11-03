import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.util.List;
import java.util.Random;

public class MusicPlayer {
    private Player player;
    private Thread playerThread;
    private volatile boolean isPlaying = false;

    public synchronized void play(String filePath) {
        stop(); // Stop current if any

        playerThread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(filePath)) {
                player = new Player(fis);
                isPlaying = true;
                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isPlaying = false;
            }
        }, "MusicPlayer-Thread");
        playerThread.start();
    }

    public void playRandom(List<String> playlist) {
        if (playlist == null || playlist.isEmpty()) return;
        String randomSong = playlist.get(new Random().nextInt(playlist.size()));
        play(randomSong);
    }

    public synchronized void stop() {
        try {
            if (player != null) {
                player.close(); // JLayer stops playback
            }
        } catch (Exception ignored) {}
        if (playerThread != null && playerThread.isAlive()) {
            playerThread.interrupt();
        }
        isPlaying = false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
