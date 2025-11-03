import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:game_scores.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Create Users table
            String usersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "username TEXT UNIQUE NOT NULL)";
            stmt.execute(usersTable);

            // Create Level Up Scores table
            String levelUpTable = "CREATE TABLE IF NOT EXISTS level_up_scores ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "user_id INTEGER, "
                    + "level INTEGER NOT NULL, "
                    + "score INTEGER NOT NULL, "
                    + "FOREIGN KEY (user_id) REFERENCES users(id), "
                    + "UNIQUE(user_id, level))";
            stmt.execute(levelUpTable);

            // Create Endless Scores table
            String endlessTable = "CREATE TABLE IF NOT EXISTS endless_scores ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "user_id INTEGER UNIQUE, "
                    + "score INTEGER NOT NULL, "
                    + "FOREIGN KEY (user_id) REFERENCES users(id))";
            stmt.execute(endlessTable);

            // Create Challenge Scores table
            String challengeTable = "CREATE TABLE IF NOT EXISTS challenge_scores ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "user_id INTEGER, "
                    + "score INTEGER NOT NULL, "
                    + "level INTEGER NOT NULL, "
                    + "FOREIGN KEY (user_id) REFERENCES users(id))";
            stmt.execute(challengeTable);

            String vsClockTable =
                    "CREATE TABLE IF NOT EXISTS vs_clock_scores ("
                            + "  id         INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "  user_id    INTEGER, "
                            + "  difficulty TEXT CHECK(difficulty IN ('Easy','Intermediate','Hard','Pro')) NOT NULL, "
                            + "  time_limit INTEGER CHECK(time_limit IN (60,120,180)) NOT NULL, "
                            + "  score      INTEGER NOT NULL, "
                            + "  FOREIGN KEY(user_id) REFERENCES users(id), "
                            + "  UNIQUE(user_id, difficulty, time_limit) ON CONFLICT REPLACE"
                            + ");";
            stmt.execute(vsClockTable);

            String statsTable =
                    "CREATE TABLE IF NOT EXISTS statistics ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "user_id INTEGER, "
                    + "wpm REAL NOT NULL, "
                    + "accuracy REAL NOT NULL, "
                    + "errorCount INTEGER NOT NULL, "
                    + "wordCount INTEGER NOT NULL, "
                    + "played_at  DATETIME    NOT NULL  DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (user_id) REFERENCES users(id))";
            stmt.execute(statsTable);

            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}
