import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Robust image utility: reads images, scales with high quality (multi-step),
 * offers aspect-ratio preserving options, and fixes DPI conversion.
 */
public final class ImageUtils {

    private ImageUtils() {}

    /** Load an image from file path or classpath resource. */
    public static BufferedImage loadImage(String path) throws IOException {
        // Try as file first
        File f = new File(path);
        if (f.exists()) {
            return ImageIO.read(f);
        }
        // Try as resource on the classpath
        try (InputStream is = ImageUtils.class.getResourceAsStream(path.startsWith("/") ? path : "/" + path)) {
            if (is != null) return ImageIO.read(is);
        }
        // Last attempt: let ImageIO try with URL-like path
        return ImageIO.read(new File(path));
    }

    /**
     * Scale an image to target dimensions. If preserveAspect is true, the image will be
     * scaled to fit into width x height while keeping aspect ratio (letterbox).
     * Returns a BufferedImage (TYPE_INT_ARGB).
     */
    public static BufferedImage getScaledImage(BufferedImage src, int targetWidth, int targetHeight, boolean preserveAspect) {
        if (src == null) throw new IllegalArgumentException("Source image is null");
        if (targetWidth <= 0 || targetHeight <= 0) throw new IllegalArgumentException("Target size must be > 0");

        int srcW = src.getWidth();
        int srcH = src.getHeight();

        // Compute final target preserving aspect if requested
        int destW = targetWidth;
        int destH = targetHeight;
        if (preserveAspect) {
            double srcRatio = (double) srcW / srcH;
            double targetRatio = (double) targetWidth / targetHeight;
            if (srcRatio > targetRatio) {
                // source is wider -> fit width
                destW = targetWidth;
                destH = (int) Math.round(targetWidth / srcRatio);
            } else {
                // source is taller -> fit height
                destH = targetHeight;
                destW = (int) Math.round(targetHeight * srcRatio);
            }
        }

        // If scaling up or only a small change, do single-step high-quality resize
        if (destW >= srcW || destH >= srcH) {
            return resizeOneStep(src, destW, destH);
        } else {
            // High-quality multi-step downscaling
            return multiStepDownscale(src, destW, destH);
        }
    }

    /** Convenience overload: load then scale. */
    public static BufferedImage getScaledImage(String path, int targetWidth, int targetHeight, boolean preserveAspect) throws IOException {
        BufferedImage src = loadImage(path);
        return getScaledImage(src, targetWidth, targetHeight, preserveAspect);
    }

    /** Single-step high-quality resize (use for upscaling or small changes). */
    private static BufferedImage resizeOneStep(BufferedImage src, int w, int h) {
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        setHighQualityHints(g2);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();
        return dest;
    }

    /** Multi-step downscaling (progressive halving) for best quality. */
    private static BufferedImage multiStepDownscale(BufferedImage src, int targetW, int targetH) {
        int type = (src.getTransparency() == Transparency.OPAQUE)
                ? BufferedImage.TYPE_INT_RGB
                : BufferedImage.TYPE_INT_ARGB;

        BufferedImage img = src;
        int currentW = img.getWidth();
        int currentH = img.getHeight();

        // Loop: scale down by half until near target, then final pass to exact size
        while (currentW / 2 >= targetW || currentH / 2 >= targetH) {
            int nextW = Math.max(targetW, currentW / 2);
            int nextH = Math.max(targetH, currentH / 2);

            BufferedImage tmp = new BufferedImage(nextW, nextH, type);
            Graphics2D g2 = tmp.createGraphics();
            setHighQualityHints(g2);
            g2.drawImage(img, 0, 0, nextW, nextH, null);
            g2.dispose();

            img = tmp;
            currentW = img.getWidth();
            currentH = img.getHeight();
        }

        // Final resize to exact dimensions if needed
        if (currentW != targetW || currentH != targetH) {
            BufferedImage tmp = new BufferedImage(targetW, targetH, type);
            Graphics2D g2 = tmp.createGraphics();
            setHighQualityHints(g2);
            g2.drawImage(img, 0, 0, targetW, targetH, null);
            g2.dispose();
            img = tmp;
        }

        // Ensure result is ARGB so you can create ImageIcon safely with alpha support
        if (img.getType() != BufferedImage.TYPE_INT_ARGB) {
            BufferedImage converted = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = converted.createGraphics();
            setHighQualityHints(g2);
            g2.drawImage(img, 0, 0, null);
            g2.dispose();
            img = converted;
        }

        return img;
    }

    private static void setHighQualityHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    }

    /** Convert centimeters to pixels using current screen DPI (correct formula). */
    public static int cmToPx(double cm) {
        double dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        return (int) Math.round((cm / 2.54) * dpi);
    }

    /**
     * Calculate X position for centering a new element of width 'widthNew' inside a container
     * whose left origin is 'x' and original width is 'widthOr' (keeps centers aligned).
     */
    public static int lblX(int x, int widthOr, int widthNew) {
        double widthCalc = (widthOr - widthNew) / 2.0;
        double xFinal = widthCalc + x;
        return (int) Math.round(xFinal);
    }

    /**
     * Calculate width of text using FontMetrics (accurate). You must provide a Font.
     * This does not need a visible component; it uses an offscreen image to get FontMetrics.
     */
    public static int calculateTextWidth(String text, Font font) {
        if (text == null || font == null) return 0;
        BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tmp.createGraphics();
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int width = fm.stringWidth(text);
        g2.dispose();
        return width;
    }

    /** Helper: create an ImageIcon directly from a path scaled to the given size. */
    public static ImageIcon createScaledIcon(String path, int width, int height, boolean preserveAspect) throws IOException {
        BufferedImage scaled = getScaledImage(path, width, height, preserveAspect);
        return new ImageIcon(scaled);
    }

    /** Save image (choose format "png" for lossless). */
    public static void save(BufferedImage img, String format, File out) throws IOException {
        ImageIO.write(img, format, out);
    }
}
