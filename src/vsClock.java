import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.*;
import java.util.Random;


public class vsClock {
    static JFrame gameFrame;
    static Random random = new Random();
    static  int num;
    static boolean isPaused = false;
    static Timer countdownTimer; // Make this class-level so it's accessible outside openGameFrame
    static JPanel shadowPanel;
    private static MusicPlayer musicPlayer = new MusicPlayer();
    private static java.util.List<String> vsClockSongs = java.util.List.of(
            "main/resources/Audio/Easy Cheesy.mp3"
    );
public static void openGameFrame(String difficulty, String timeLimit, String username) throws IOException {
    gameFrame = new JFrame(I18n.t("Clock"));
    gameFrame.setLayout(null);
    gameFrame.setSize(800, 550);
    gameFrame.setLocationRelativeTo(null);
    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameFrame.setResizable(false);
    gameFrame.setFocusable(true);
    gameFrame.requestFocusInWindow();

    SoundManager.setMusicPlayer(musicPlayer);
    playSound();

    RoundedButton backBtn = new RoundedButton("← " + I18n.t("Back"),false);
    backBtn.setBounds(5, 19, 100, 30);
    backBtn.addActionListener(e -> {
        SoundManager.stopBackgroundMusic();
        try {
            new Gamemode();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        gameFrame.dispose();
        ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
    });
    gameFrame.getContentPane().add(backBtn);

    ImageIcon pause = ImageUtils.createScaledIcon("main/resources/Images/pause.png", 29, 27,true);
    ImageIcon pauseHoverbtn = ImageUtils.createScaledIcon("main/resources/Images/pause hover.png", 29, 27,true);
    JButton pausebtn = new JButton(pause);
    pausebtn.setBounds(743,20,29, 27);
    pausebtn.setBorder(BorderFactory.createEmptyBorder());  // Remove border
    pausebtn.setContentAreaFilled(false);  // Make the background transparent
    pausebtn.setFocusPainted(false);  // Remove focus border on click
    pausebtn.setBorderPainted(false);  // Remove border paint
        // Shadow panel
        shadowPanel = new JPanel();
        shadowPanel.setBounds(0, 0, 800, 550);
        shadowPanel.setBackground(new Color(0, 0, 0, 150)); // semi-transparent black
        shadowPanel.setLayout(null);
        shadowPanel.setVisible(false);

// Example resume button on shadow panel
        JButton resumeButton = new JButton(I18n.t("Resume"));
        resumeButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
        resumeButton.setBounds(325, 150, 150, 50);
        shadowPanel.add(resumeButton);

        JButton quitButton = new JButton(I18n.t("Quit"));
        quitButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
        quitButton.setBounds(325, 350, 150, 50);
        shadowPanel.add(quitButton);
        quitButton.addActionListener(e -> {
            String[] options = {I18n.t("Homepage"), I18n.t("Exit Game")};
            int choice = JOptionPane.showOptionDialog(
                    gameFrame,                                       // parent component
                    I18n.t("What would you like to do?"),        // message
                    I18n.t("Quit Game"),                         // title
                    JOptionPane.DEFAULT_OPTION,                  // optionType
                    JOptionPane.QUESTION_MESSAGE,                // messageType
                    null,                                        // icon
                    options,                                     // the choices
                    options[0]                                   // default choice
            );

            if (choice == 0) {
                // Go back to your main menu / gamemode screen
                try {
                    new HomePage();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                gameFrame.dispose();
                ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
            }
            else if (choice == 1) {
                // Quit the JVM entirely
                System.exit(0);
            }
        });

// Resume button logic
        resumeButton.addActionListener(e -> {
            shadowPanel.setVisible(false);
            playSound();
            gameFrame.requestFocusInWindow(); // regain key focus
            countdownTimer.start();
            isPaused = false;
        });
    JButton settingsButton = new JButton(I18n.t("Settings"));
    settingsButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
    settingsButton.setBounds(325, 250, 150, 50);
    shadowPanel.add(settingsButton);
    settingsButton.addActionListener(e -> {
        new Settings(() -> {
            shadowPanel.setVisible(true);
        }, () -> {
            SoundManager.stopBackgroundMusic();
            if (countdownTimer != null) countdownTimer.stop();
            gameFrame.dispose();
            try {
                new HomePage();
            } catch (IOException ex) {
                throw new IllegalStateException("Could not reopen the homepage", ex);
            }
        });
        ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
    });

        gameFrame.add(shadowPanel);
    pausebtn.setRolloverIcon(pauseHoverbtn);
        pausebtn.addActionListener(e -> {
            if (!isPaused) {
                isPaused = true;
                countdownTimer.stop();
                SoundManager.stopBackgroundMusic();
                shadowPanel.setVisible(true);
            }
        });
        gameFrame.add(pausebtn);

    JPanel linePanel = new JPanel();
    linePanel.setBounds(0,56,800,2);
    linePanel.setBackground(Color.BLACK);
    gameFrame.add(linePanel);

    JPanel linePanel1 = new JPanel();
    linePanel1.setBounds(250, 300, 300, 2);
    linePanel1.setBackground(Color.BLACK);
    gameFrame.add(linePanel1);
    JPanel linePanel2 = new JPanel();
    linePanel2.setBounds(300, 310, 200, 2);
    linePanel2.setBackground(Color.BLACK);
    gameFrame.add(linePanel2);

    String[] words = getWordsBasedOnDifficulty(difficulty);
    WordCycle wordCycle = new WordCycle(words, random);
    final String[] currentWord = {wordCycle.next()};

    JLabel wordLabel = new JLabel(currentWord[0], SwingConstants.CENTER);
    wordLabel.setFont(new Font("Century Gothic", Font.BOLD, 40));
    wordLabel.setBounds(241, 245, 318, 54);
    gameFrame.add(wordLabel);

    JPanel scorePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(195, 88, 42));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded edges
        }
    };
    scorePanel.setBounds(285, 124, 231, 43);
    scorePanel.setLayout(null);
    scorePanel.setOpaque(false);

    JLabel score = new JLabel(I18n.t("SCORE: "));
    score.setFont(new Font("Century Gothic", Font.BOLD, 30));
    score.setBounds(50, 3, 155, 39);
    score.setForeground(Color.WHITE);

    JLabel scoreNum = new JLabel("0");
    scoreNum.setFont(new Font("Century Gothic", Font.BOLD, 30));
    scoreNum.setBounds(160, 3, 155, 39);
    scoreNum.setForeground(Color.white);

    scorePanel.add(score);
    scorePanel.add(scoreNum);
    gameFrame.add(scorePanel);

    int[] timeRemaining = { Integer.parseInt(timeLimit) };
    JLabel timerLabel = new JLabel(String.valueOf(timeRemaining[0]) + 's');
    timerLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
    timerLabel.setForeground(new Color(255, 255, 255));
    timerLabel.setBounds(380, 465, 100, 30);
    gameFrame.add(timerLabel);

//    Timer countdownTimer = new Timer(1000, new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if (timeRemaining[0] > 0) {
//                timeRemaining[0]--;
//                timerLabel.setText(String.valueOf(timeRemaining[0])+ 's');
//            } else {
//                ((Timer)e.getSource()).stop();
//                timerLabel.setText("Time's up!");
//                timerLabel.setBounds(335, 465, 200, 30);
//                gameFrame.removeKeyListener(gameFrame.getKeyListeners()[0]);
//                // Save the final score to the database
////                saveVsClockScore(username, difficulty, Integer.parseInt(timeLimit), Integer.parseInt(scoreNum.getText()));
//            }
//        }
//    });
    final int[] index = {0};
    final int[] num = {0};
    final int[] errorCount = {0};
    Color normalBackground = gameFrame.getContentPane().getBackground();
    if (normalBackground == null) normalBackground = UIManager.getColor("Panel.background");
    final Color flashBackground = normalBackground;
    Timer redFlashTimer = new Timer(200, e -> gameFrame.getContentPane().setBackground(flashBackground));
    redFlashTimer.setRepeats(false);
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused && timeRemaining[0] > 0) {
                    timeRemaining[0]--;
                    timerLabel.setText(timeRemaining[0] + "s");
                } else if (timeRemaining[0] == 0) {
                    ((Timer)e.getSource()).stop();
                    timerLabel.setText(I18n.t("Time's up!"));
                    timerLabel.setBounds(335, 465, 200, 30);
                    gameFrame.removeKeyListener(gameFrame.getKeyListeners()[0]);
                    double minutes = Integer.parseInt(timeLimit) / 60.0;
                    double wpm     = num[0] / minutes;
                    double accuracy = 100.0 * (1.0 - (errorCount[0] / (double)(num[0] + errorCount[0])));

                    saveStatistics(username, wpm, accuracy, errorCount[0], num[0]);
                }
            }
        });

        countdownTimer.start();

    gameFrame.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            // SPACE toggles pause/resume
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                e.consume();
                if (!isPaused) {
                    // pause
                    isPaused = true;
                    SoundManager.stopBackgroundMusic();
                    countdownTimer.stop();
                    shadowPanel.setVisible(true);
                } else {
                    // resume
                    isPaused = false;
                    playSound();
                    shadowPanel.setVisible(false);
                    countdownTimer.start();
                    gameFrame.requestFocusInWindow();  // regain key focus
                }
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {
            char typedChar = e.getKeyChar();
            if (index[0] < currentWord[0].length()) {
                StringBuilder formattedText = new StringBuilder("<html>");
                boolean mistakeMade = false;

                for (int i = 0; i < currentWord[0].length(); i++) {
                    if (i < index[0] || (i == index[0] && Character.toLowerCase(typedChar) == Character.toLowerCase(currentWord[0].charAt(index[0])))) {
                        formattedText.append("<font color='green'>").append(currentWord[0].charAt(i)).append("</font>");
                    } else if (typedChar == ' ') {
                        return;
                    } else if (i == index[0] && Character.toLowerCase(typedChar) != Character.toLowerCase(currentWord[0].charAt(index[0]))) {
                        formattedText.append("<font color='red'>").append(currentWord[0].charAt(i)).append("</font>");
                        mistakeMade = true;
                        playEffect("main/resources/Audio/0429.WAV");
                        errorCount[0]++;
                        gameFrame.getContentPane().setBackground(Color.RED);
                        redFlashTimer.restart();
                    } else {
                        formattedText.append(currentWord[0].charAt(i));
                    }
                }
                formattedText.append("</html>");
                wordLabel.setText(formattedText.toString());

                if (!mistakeMade) {
                    index[0]++;
                }
            }

            if (index[0] == currentWord[0].length()) {
                num[0]++;
                scoreNum.setText(String.valueOf(num[0]));
                index[0] = 0;
                saveVsClockScore(username, difficulty, Integer.parseInt(timeLimit), Integer.parseInt(scoreNum.getText()));
                currentWord[0] = wordCycle.next();
                wordLabel.setText(currentWord[0]);
            }
        }
    });

    JPanel stopwatchPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(195, 88, 42));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded edges
        }
    };

//    StopwatchPanel stopwatchPanel = new StopwatchPanel();
    stopwatchPanel.setBounds(310,447,180,83); // Position at the bottom
    stopwatchPanel.setOpaque(false);
    gameFrame.add(stopwatchPanel);

    String result = getHighScore(username, difficulty, timeLimit);

    JLabel highScorelbl = new JLabel(result);
    highScorelbl.setBounds(610, 127, 150, 30);
    highScorelbl.setFont(new Font("Century Gothic",Font.BOLD,20));
    gameFrame.add(highScorelbl);

    gameFrame.setFocusable(true);
    gameFrame.setVisible(true);
}

    public static String getHighScore(String username, String difficulty, String timeLimit) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("""
            SELECT MAX(v.score) AS high_score
            FROM vs_clock_scores v
            JOIN users u ON v.user_id = u.id
            WHERE u.username = ? AND v.difficulty = ? AND v.time_limit = ?
        """);
            stmt.setString(1, username);
            stmt.setString(2, difficulty);
            stmt.setInt(3, Integer.parseInt(timeLimit));  // Convert timeLimit to int
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt("high_score") > 0) {
                int score = rs.getInt("high_score");
                return                 I18n.t("High Score:") + " " + score;
            } else {
                return I18n.t("High Score:") + " 0";
            }
        } catch (SQLException ex) {
            return I18n.t("Error retrieving score:") + " " + ex.getMessage();
        }
    }

    public static void saveVsClockScore(String username, String difficulty, int timeLimit, int score) {
        try (Connection conn = DBUtil.getConnection()) {
            // get the user's ID
            PreparedStatement getUserIdStmt = conn.prepareStatement(
                    "SELECT id FROM users WHERE username = ?"
            );
            getUserIdStmt.setString(1, username);
            ResultSet rs = getUserIdStmt.executeQuery();
            if (!rs.next()) return;  // no such user

            int userId = rs.getInt("id");

            // INSERT OR REPLACE will delete any existing row with the same PK/unique constraint
            String sql = """
          INSERT OR REPLACE INTO vs_clock_scores
            (id, user_id, difficulty, time_limit, score)
          VALUES (
            COALESCE((
              SELECT id FROM vs_clock_scores
                WHERE user_id=? AND difficulty=? AND time_limit=?
            ), NULL),
            ?, ?, ?, ?
          );
        """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // first three params look up existing row ID
                ps.setInt(1, userId);
                ps.setString(2, difficulty);
                ps.setInt(3, timeLimit);
                // next four are the values to insert (or replace)
                ps.setInt(4, userId);
                ps.setString(5, difficulty);
                ps.setInt(6, timeLimit);
                ps.setInt(7, score);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    I18n.t("Database Error"), JOptionPane.ERROR_MESSAGE);
        }
    }



    // Method to return words based on the selected difficulty
    public static String[] getWordsBasedOnDifficulty(String difficulty) {
        if (I18n.isFrench()) {
            return switch (difficulty) {
                case "Easy" -> new String[]{"CHAT", "CHIEN", "SOLEIL", "ROUGE", "BLEU", "POISSON", "LIVRE", "TABLE", "LUNE", "ÉTOILE",
                        "EAU", "VERT", "MAISON", "MUSIQUE", "ARBRE", "OISEAU", "PAIN", "PLUIE", "NEIGE", "VENT"};
                case "Intermediate" -> new String[]{"jardin", "crayon", "hiver", "coucher", "fusée", "planète", "silence", "ami", "violet", "banane",
                        "peintre", "image", "brise", "papillon", "diamant", "équilibre", "plume", "énigme", "lanterne", "bibliothèque"};
                case "Hard" -> new String[]{"éléphant", "clavier", "montagne", "parapluie", "chuchoter", "vélocité", "triangle", "meuble", "bicyclette", "stratégie",
                        "avalanche", "horizon", "microscope", "parallèle", "squelette", "phare", "aventure", "terracotta", "invisible", "télescope"};
                default -> new String[]{"philosophie", "encyclopédie", "silhouette", "hypothèse", "quarantaine", "astronomie", "synchroniser", "architecture", "vocabulaire", "conséquence",
                        "métamorphose", "phénomène", "circonférence", "catastrophe", "bureaucratie", "juxtaposition", "inédit", "paradoxal", "sophistiqué", "perspective"};
            };
        }
        switch (difficulty) {
            case "Easy":
                return new String[] {
                        "CAT", "DOG", "SUN", "RED", "BLUE", "FISH", "BOOK", "JUMP", "CHAIR", "TABLE",
                        "LIGHT", "MOON", "STAR", "WATER", "GREEN", "HOUSE", "MUSIC", "TREE", "BIRD", "PHONE",
                        "BREAD", "MOUSE", "CLOUD", "RAIN", "SNOW", "WIND", "SMILE", "BALL", "CAKE", "SAND",
                        "TOY", "MILK", "CAR", "PEN", "SHIRT", "GLOVE", "BELL", "SHOE", "PAPER", "CLOCK",
                        "BOAT", "HAT", "ROCK", "BIRD", "DOOR", "KEY", "SKY", "LEAF", "GRASS", "RAIN",
                        "GUITAR", "PIANO", "BRUSH", "LAMP", "FLAG", "SCARF", "SKIRT", "BOOT", "SOCKS", "COIN",
                        "WALLET", "CHAIR", "RIVER", "LAKE", "OCEAN", "HILL", "BEACH", "ISLAND", "ICE", "STORM",
                        "SUNNY", "FOGGY", "MIST", "CLEAR", "DAY", "NIGHT", "CLOCK", "WATCH", "TIME", "HOUR",
                        "MONTH", "YEAR", "WEEK", "TRIP", "ROUTE", "MAP", "TICKET", "PLANE", "BUS", "CAR",
                        "TRAIN", "FERRY", "TAXI", "POLICE", "BOAT", "TRUCK", "SKATE", "FRAME", "PEN", "PENCIL",
                        "ERASER", "CRAYON", "RULER", "CHALK", "RUBBER", "INK", "PAINT", "COLOR", "GLUE",
                        "LETTER", "DIARY", "TICKET", "MENU", "TABLET", "MOUSE", "SCREEN", "RADIO", "PLUG", "MAT",
                        "WALL", "FLOOR", "DOOR", "LOCK", "HANDLE", "CUSHION", "TOY", "PLANE", "BUS", "GOLF",
                        "DARTS", "CHESS", "FENCING", "GYM", "YOGA", "JOGGING", "PUSHUP", "SITUP", "BALANCE",
                        "CARDIO", "COACH", "GOAL", "MATCH", "SCORE", "POINTS", "TEAM", "PLAYER", "GAME", "PLAY"
                };

            case "Intermediate":
                return new String[] {
                        "garden", "pencil", "winter", "sunset", "rocket", "planet", "silent", "friend", "purple", "banana",
                        "painter", "picture", "breeze", "butterfly", "diamond", "balance", "feather", "puzzle", "lantern", "library",
                        "whistle", "tunnel", "candle", "festival", "treasure", "chimney", "journey", "shadow", "machine", "glacier"
                };
            case "Hard":
                return new String[] {
                        "elephant", "keyboard", "mountain", "umbrella", "whisper", "velocity", "triangle", "furniture", "bicycle", "strategy",
                        "avalanche", "horizon", "microscope", "parallel", "skeleton", "lighthouse", "adventure", "synergy", "terracotta", "invisible",
                        "blueprint", "telescope", "signature", "captivate", "spectacle", "marathon", "pendulum", "symphony", "galaxy", "renaissance"
                };
            case "Pro":
                return new String[] {
                        "philosophy", "encyclopedia", "silhouette", "hypothesis", "quarantine", "astronomy", "synchronize", "architecture", "vocabulary", "consequence",
                        "metamorphosis", "phenomenon", "circumference", "catastrophe", "bureaucracy", "juxtaposition", "unprecedented", "paradoxical", "sophisticated", "perspective",
                        "conglomerate", "transcendence", "psychology", "ambidextrous", "conundrum", "misconception", "extrapolate", "onomatopoeia", "polyphonic", "hallucination"
                };
            default:
                return new String[] { "cat", "dog", "sun", "red", "blue", "fish", "book", "apple", "happy", "jump", "chair" };
        }
    }
    /**
     * Record a typing‐session’s stats into the statistics table.
     *
     * @param username   the player’s username
     * @param wpm        words per minute (e.g. totalWords / (elapsedSeconds/60.0))
     * @param accuracy   accuracy as a percentage (0.0–100.0)
     * @param errorCount total number of mistakes made
     * @param wordCount  total number of words typed
     */
    public static void saveStatistics(String username,
                                      double wpm,
                                      double accuracy,
                                      int errorCount,
                                      int wordCount) {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            // 1) look up the user’s ID
            PreparedStatement lookup = conn.prepareStatement(
                    "SELECT id FROM users WHERE username = ?");
            lookup.setString(1, username);
            ResultSet rs = lookup.executeQuery();
            if (!rs.next()) {
                // no such user—nothing to do
                return;
            }
            int userId = rs.getInt("id");

            // 2) insert the stats row
            PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO statistics (user_id, wpm, accuracy, errorCount, wordCount, played_at) " +
                            "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)");
            ins.setInt(1, userId);
            ins.setDouble(2, wpm);
            ins.setDouble(3, accuracy);
            ins.setInt(4, errorCount);
            ins.setInt(5, wordCount);
            ins.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    I18n.t("Could not save statistics:\n") + e.getMessage(),
                    I18n.t("Database Error"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void playEffect(String soundPath) {
        SoundManager.playEffect(soundPath);
    }
    public static void playSound() {
        // let SoundManager decide if music should play or not
        SoundManager.playBackgroundRandom(vsClockSongs);
    }
}
