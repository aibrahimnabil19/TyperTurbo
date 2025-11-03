// DarkTheme.java
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIDefaults;
import java.awt.Color;

public class DarkTheme extends FlatDarkLaf {
    public static final String NAME = "Aishatek Dark";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected void initComponentDefaults(UIDefaults defaults) {
        super.initComponentDefaults(defaults);

        // map those same keys to dark‐mode values
        defaults.put("Settings.menuButton.bg",    new Color( 30,  30,  30));
        defaults.put("Settings.menuButton.fg",    new Color(220, 220, 220));
        defaults.put("Settings.content.bg",       new Color( 45,  45,  45));
        defaults.put("Settings.title.fg",         new Color(255, 128,  80));
        defaults.put("Settings.text.fg",          new Color(200, 200, 200));
        // …and your other custom keys…
    }
}
