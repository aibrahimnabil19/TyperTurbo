import java.util.prefs.Preferences;

public class PreferencesManager {
    private static final Preferences prefs =
            Preferences.userNodeForPackage(PreferencesManager.class);
    private static final String KEY_THEME = "appTheme";

    public static void saveThemeChoice(String theme) {
        prefs.put(KEY_THEME, theme);
    }

    public static String loadThemeChoice() {
        // default to Light if nothing saved yet
        return prefs.get(KEY_THEME, ThemeUtils.THEME_LIGHT);
    }
}
