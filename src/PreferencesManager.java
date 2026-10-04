import java.util.prefs.Preferences;

public class PreferencesManager {
    private static final Preferences prefs =
            Preferences.userNodeForPackage(PreferencesManager.class);
    private static final String KEY_THEME = "appTheme";
    private static final String KEY_LANGUAGE = "appLanguage";

    public static void saveThemeChoice(String theme) {
        prefs.put(KEY_THEME, theme);
    }

    public static String loadThemeChoice() {
        // default to Light if nothing saved yet
        return prefs.get(KEY_THEME, ThemeUtils.THEME_LIGHT);
    }

    public static void saveLanguageChoice(String language) {
        prefs.put(KEY_LANGUAGE, language);
    }

    public static String loadLanguageChoice() {
        return prefs.get(KEY_LANGUAGE, I18n.ENGLISH);
    }
}
