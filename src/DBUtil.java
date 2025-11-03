import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public final class DBUtil {
    private static final String DB_FILE = "game_scores.db";
    private static final String APP_DIR;
    private static final String DB_PATH;
    public static final String DB_URL;

    static {
        String appdir;
        String maybeAppData = System.getenv("APPDATA");
        if (maybeAppData != null && !maybeAppData.isBlank()) {
            appdir = maybeAppData + File.separator + "TyperTurbo";
        } else {
            appdir = System.getProperty("user.home") + File.separator + ".typerturbo";
        }
        APP_DIR = appdir;

        String tempPath;
        try {
            Path dir = Paths.get(APP_DIR);
            if (!Files.exists(dir)) Files.createDirectories(dir);
            tempPath = Paths.get(APP_DIR, DB_FILE).toString();
        } catch (Exception ex) {
            // fallback to current working dir
            tempPath = DB_FILE;
        }
        DB_PATH = tempPath;
        DB_URL = "jdbc:sqlite:" + DB_PATH;

        // Ensure schema exists on startup
        try {
            initializeSchema();
        } catch (SQLException e) {
            // log to console / file as needed
            e.printStackTrace();
        }
    }

    private DBUtil() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private static void initializeSchema() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // keep your tables consistent with the rest of the app
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT UNIQUE NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS last_used_user (username TEXT UNIQUE NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS endless_scores (user_id INTEGER NOT NULL, score INTEGER NOT NULL, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS statistics (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, wpm REAL, accuracy REAL, errorCount INTEGER, wordCount INTEGER, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");
        }
    }

    // convenience: return the DB file path for debugging
    public static String getDbFilePath() {
        return DB_PATH;
    }
}
