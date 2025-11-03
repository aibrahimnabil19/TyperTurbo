import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ThemeManager {
    public enum Theme { LIGHT, DARK }
    private static Theme current = Theme.LIGHT;

    private static final Color LIGHT_COLOR = new Color(240,224,208);
    private static final Color DARK_COLOR  = new Color(195, 88, 42);

    /**
     * Switches the Look-and-Feel (FlatLaf) and applies custom color swaps and image recoloring.
     */
    public static void applyTheme(Theme t) {
        current = t;
        try {
            UIManager.setLookAndFeel(
                    t == Theme.DARK ? new FlatDarkLaf() : new FlatLightLaf()
            );
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Update Swing defaults
        for (Window w : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(w);
        }

        // Apply custom color swapping and image recoloring
        swapCustomColors();
    }

    /**
     * Applies only the Look-and-Feel without swapping custom colors (useful before UI creation).
     */
    public static void applyLafOnly(Theme t) {
        current = t;
        try {
            UIManager.setLookAndFeel(
                    t == Theme.DARK ? new FlatDarkLaf() : new FlatLightLaf()
            );
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        for (Window w : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(w);
        }
    }

    /**
     * Performs the custom-color swap pass and image recoloring across all windows.
     */
    public static void swapCustomColors() {
        for (Window w : Window.getWindows()) {
            walkAndSwap(w);
        }
    }

    private static void walkAndSwap(Component c) {
        if (c instanceof Container) {
            for (Component child : ((Container)c).getComponents()) {
                walkAndSwap(child);
            }
        }

        // Swap background
        if (LIGHT_COLOR.equals(c.getBackground()) && current == Theme.DARK) {
            c.setBackground(DARK_COLOR);
        } else if (DARK_COLOR.equals(c.getBackground()) && current == Theme.LIGHT) {
            c.setBackground(LIGHT_COLOR);
        }

        // Swap foreground
        if (LIGHT_COLOR.equals(c.getForeground()) && current == Theme.DARK) {
            c.setForeground(DARK_COLOR);
        } else if (DARK_COLOR.equals(c.getForeground()) && current == Theme.LIGHT) {
            c.setForeground(LIGHT_COLOR);
        }

        // Image icon recoloring
//        if (c instanceof JLabel) {
//            Icon icon = ((JLabel)c).getIcon();
//            if (icon instanceof ImageIcon) {
//                ImageIcon ii = (ImageIcon)icon;
//                BufferedImage img = toBufferedImage(ii.getImage());
//                BufferedImage swapped = swapImageColors(img);
//                ((JLabel)c).setIcon(new ImageIcon(swapped));
//            }
//        }

        if (c instanceof JComponent) {
            ((JComponent)c).revalidate();
            ((JComponent)c).repaint();
        }
    }

    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) return (BufferedImage)img;
        BufferedImage b = new BufferedImage(
                img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = b.createGraphics();
        g.drawImage(img,0,0,null);
        g.dispose();
        return b;
    }

    private static BufferedImage swapImageColors(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = src.getRGB(x, y);
                if (rgb == LIGHT_COLOR.getRGB() && current == Theme.DARK) {
                    out.setRGB(x, y, DARK_COLOR.getRGB());
                } else if (rgb == DARK_COLOR.getRGB() && current == Theme.LIGHT) {
                    out.setRGB(x, y, LIGHT_COLOR.getRGB());
                } else {
                    out.setRGB(x, y, rgb);
                }
            }
        }
        return out;
    }

    /**
     * Returns the currently active theme (LIGHT or DARK).
     */
    public static Theme getCurrentTheme() {
        return current;
    }
}
