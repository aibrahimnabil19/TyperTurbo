import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class ThemeUtils {
    // save these keys somewhere centrally
    public static final String THEME_LIGHT = "Light";
    public static final String THEME_DARK  = "Dark";

    /**
     * 1) sets the L&F;
     * 2) updates every Window currently open
     */
    public static void applyTheme(String themeName) {
        try {
            LookAndFeel laf = THEME_DARK.equals(themeName)
                    ? new FlatDarkLaf()
                    : new FlatLightLaf();
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
            return;
        }

        // repaint every window/dialog
        for (Window w : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(w);
            // if you have custom popups/tooltip UIs, you may also:
            // SwingUtilities.updateComponentTreeUI(ToolTipManager.sharedInstance());
        }
    }
}
