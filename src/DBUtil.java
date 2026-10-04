import java.io.File;
//import java.net.URL;
//import java.net.URLClassLoader;
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
        ensureSqliteDriver();

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

    private static void ensureSqliteDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    new ClassNotFoundException("org.sqlite.JDBC", e)
            );
        }
    }

    public static Connection getConnection() throws SQLException {
        ensureSqliteDriver();
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
                    "played_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");
            ensureStatisticsPlayedAtColumn(conn);
        }
    }

    private static void ensureStatisticsPlayedAtColumn(Connection conn) throws SQLException {
        boolean hasPlayedAt = false;
        try (Statement stmt = conn.createStatement();
             ResultSet columns = stmt.executeQuery("PRAGMA table_info(statistics)")) {
            while (columns.next()) {
                if ("played_at".equalsIgnoreCase(columns.getString("name"))) {
                    hasPlayedAt = true;
                    break;
                }
            }
        }

        if (!hasPlayedAt) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("ALTER TABLE statistics ADD COLUMN played_at DATETIME");
                stmt.execute("UPDATE statistics SET played_at = CURRENT_TIMESTAMP WHERE played_at IS NULL");
            }
        }
    }

    // convenience: return the DB file path for debugging
    public static String getDbFilePath() {
        return DB_PATH;
    }
}
