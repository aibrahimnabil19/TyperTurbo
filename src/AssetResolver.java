import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

public final class AssetResolver {
    private AssetResolver() {
    }

    public static String resolve(String path) {
        if (path == null || path.trim().isEmpty()) {
            return path;
        }

        String normalized = path.trim().replace('\\', '/');
        if (normalized.contains("/src/main/resources/")) {
            return normalized.substring(normalized.lastIndexOf("/src/main/resources/") + "/src/".length());
        }
        if (normalized.contains("src/main/resources/")) {
            return normalized.substring(normalized.indexOf("src/main/resources/") + "src/".length());
        }

        File existingFile = new File(normalized);
        if (existingFile.exists()) {
            return normalized;
        }

        if (normalized.startsWith("/main/resources/")) {
            return normalized.substring(1);
        }
        if (normalized.startsWith("main/resources/")) {
            return normalized;
        }
        if (normalized.startsWith("/Audio/") || normalized.startsWith("/Images/")) {
            return "main/resources" + normalized;
        }
        if (normalized.startsWith("Audio/") || normalized.startsWith("Images/")) {
            return "main/resources/" + normalized;
        }
        if (normalized.contains("/Audio/")) {
            return "main/resources/Audio/" + normalized.substring(normalized.lastIndexOf("/Audio/") + "/Audio/".length());
        }
        if (normalized.contains("/Images/")) {
            return "main/resources/Images/" + normalized.substring(normalized.lastIndexOf("/Images/") + "/Images/".length());
        }

        return normalized;
    }

    public static File resolveToFile(String path) {
        String resolved = resolve(path);
        File file = new File(resolved);
        if (file.exists()) {
            return file;
        }
        return file;
    }

    public static URL resolveToUrl(String path) throws IOException {
        String normalized = resolve(path);
        String[] candidates = resourceCandidates(normalized);

        for (String candidate : candidates) {
            URL url = AssetResolver.class.getResource(candidate.startsWith("/") ? candidate : "/" + candidate);
            if (url != null) {
                return url;
            }
            ClassLoader loader = AssetResolver.class.getClassLoader();
            if (loader != null) {
                URL classpathUrl = loader.getResource(candidate.startsWith("/") ? candidate.substring(1) : candidate);
                if (classpathUrl != null) {
                    return classpathUrl;
                }
            }
        }

        File file = new File(normalized);
        if (file.exists()) {
            return file.toURI().toURL();
        }

        throw new IOException("Resource not found: " + path);
    }

    public static InputStream openResourceStream(String path) throws IOException {
        String normalized = resolve(path);
        String[] candidates = resourceCandidates(normalized);

        for (String candidate : candidates) {
            InputStream stream = AssetResolver.class.getResourceAsStream(candidate.startsWith("/") ? candidate : "/" + candidate);
            if (stream != null) {
                return stream;
            }
            ClassLoader loader = AssetResolver.class.getClassLoader();
            if (loader != null) {
                InputStream classpathStream = loader.getResourceAsStream(candidate.startsWith("/") ? candidate.substring(1) : candidate);
                if (classpathStream != null) {
                    return classpathStream;
                }
            }
        }

        File file = new File(normalized);
        if (file.exists()) {
            return java.nio.file.Files.newInputStream(file.toPath());
        }

        throw new IOException("Resource not found: " + path);
    }

    private static String[] resourceCandidates(String path) {
        String normalized = path.replace('\\', '/');
        while (normalized.startsWith("/")) normalized = normalized.substring(1);
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(normalized);

        String resourcePath = normalized;
        if (resourcePath.startsWith("main/resources/")) {
            resourcePath = resourcePath.substring("main/resources/".length());
        } else if (resourcePath.startsWith("main/")) {
            resourcePath = resourcePath.substring("main/".length());
        }
        if (resourcePath.startsWith("Audio/") || resourcePath.startsWith("Images/")) {
            candidates.add("main/resources/" + resourcePath);
            candidates.add("main/" + resourcePath);
            candidates.add(resourcePath);
        }

        Set<String> allCandidates = new LinkedHashSet<>();
        for (String candidate : candidates) {
            allCandidates.add(candidate);
            allCandidates.add("/" + candidate);
        }
        return allCandidates.toArray(new String[0]);
    }
}
