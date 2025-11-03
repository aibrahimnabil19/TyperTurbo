// LightTheme.java
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIDefaults;
import java.awt.Color;

public class LightTheme extends FlatLightLaf {
    public static final String NAME = "Aishatek Light";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected void initComponentDefaults(UIDefaults defaults) {
        super.initComponentDefaults(defaults);

        // now override any UI defaults you need
        defaults.put("Settings.menuButton.bg",    new Color(240, 224, 208));
        defaults.put("Settings.menuButton.fg",    Color.BLACK);
        defaults.put("Settings.content.bg",       Color.WHITE);
        defaults.put("Settings.title.fg",         new Color(195,  88,  42));
        defaults.put("Settings.text.fg",          new Color( 42,  52,  57));
        // …and so on for all of your custom colours…
    }
}
