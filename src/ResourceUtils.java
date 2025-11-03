import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.io.IOException;

public final class ResourceUtils {
    private ResourceUtils() {}

    /** Returns a URL for a classpath resource (or null if not found) */
    public static URL getResourceUrl(String resourcePath) {
        // Leading slash expected: "/Images/xxx.png"
        return ResourceUtils.class.getResource(resourcePath);
    }

    /** Create a scaled ImageIcon from a resource path (returns placeholder if not found) */
    public static ImageIcon createScaledIcon(String resourcePath, int width, int height, boolean preserveAspect) throws IOException {
        URL url = getResourceUrl(resourcePath);
        if (url == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage();
        Image scaled;
        if (preserveAspect) {
            scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } else {
            scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }
        return new ImageIcon(scaled);
    }

    /** Load raw Image (unscaled) or null if not found */
    public static Image loadImage(String resourcePath) {
        URL url = getResourceUrl(resourcePath);
        return (url != null) ? new ImageIcon(url).getImage() : null;
    }
}
