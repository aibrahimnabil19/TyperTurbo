import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Random;

public class Endless {
    JFrame frame;
    private JTextPane textArea = new JTextPane();
    private JLabel textLabel, scoreLabel, timerLabel, heartsLabel,levelLabel,levelImgs;
    private Timer timer;
    private int xPosition, elapsedTime, score = 0;;
    private double speed = 2.0; // Initial speed
    private double speedIncrement = 0.5; // Gradual speed increase
    private String currentText;
    private Random random;
    int[] index = {0};;
    private int hearts = 3;
    private int level = 1;
    private int errorCount = 0;
    private boolean isPaused = false;
    private long startTime;
    private boolean startedTyping = false;
    private Timer movementTimer;
    private Timer timeElapsedTimer;
    private String username;
    static JPanel shadowPanel;
    private MusicPlayer musicPlayer = new MusicPlayer();
    private java.util.List<String> gamemodeSongs = java.util.List.of(
            "C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Axel Thesleff - Bad Karma.mp3",
            "C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Cartoon, Jéja - On & On (feat. Daniel Levi) _ Electronic Pop _ NCS - Copyright F.mp3",
            "C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Eiffel 65 - Blue (KNY Factory Remix).mp3",
            "C:\\Users\\User\\Documents\\untitled\\src\\Audio\\The Chainsmokers - Don't Let Me Down (Illenium Remix).mp3",
            "C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Twenty One Pilots - Stressed Out (Tomsize Remix).mp3"
    );

    private String[] speeches = {
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
            "CARDIO", "COACH", "GOAL", "MATCH", "SCORE", "POINTS", "TEAM", "PLAYER", "GAME", "PLAY",
            "GARDEN", "PENCIL", "WINTER", "SUNSET", "ROCKET", "PLANET", "SILENT", "FRIEND", "PURPLE", "BANANA",
            "PAINTER", "PICTURE", "BREEZE", "BUTTERFLY", "DIAMOND", "BALANCE", "FEATHER", "PUZZLE", "LANTERN", "LIBRARY",
            "WHISTLE", "TUNNEL", "CANDLE", "FESTIVAL", "TREASURE", "CHIMNEY", "JOURNEY", "SHADOW", "MACHINE", "GLACIER",
            "ELEPHANT", "KEYBOARD", "MOUNTAIN", "UMBRELLA", "WHISPER", "VELOCITY", "TRIANGLE", "FURNITURE", "BICYCLE", "STRATEGY",
            "AVALANCHE", "HORIZON", "MICROSCOPE", "PARALLEL", "SKELETON", "LIGHTHOUSE", "ADVENTURE", "SYNERGY", "TERRACOTTA", "INVISIBLE",
            "BLUEPRINT", "TELESCOPE", "SIGNATURE", "CAPTIVATE", "SPECTACLE", "MARATHON", "PENDULUM",
            "PHILOSOPHY", "ENCYCLOPEDIA", "SILHOUETTE", "HYPOTHESIS", "QUARANTINE", "ASTRONOMY", "SYNCHRONIZE", "ARCHITECTURE", "VOCABULARY", "CONSEQUENCE",
            "METAMORPHOSIS", "PHENOMENON", "CIRCUMFERENCE", "CATASTROPHE", "BUREAUCRACY", "JUXTAPOSITION", "UNPRECEDENTED", "PARADOXICAL", "SOPHISTICATED", "PERSPECTIVE",
            "CONGLOMERATE", "TRANSCENDENCE", "PSYCHOLOGY", "AMBIDEXTROUS", "CONUNDRUM", "MISCONCEPTION", "EXTRAPOLATE", "ONOMATOPOEIA", "POLYPHONIC", "HALLUCINATION"
    };


    public Endless(String username) throws IOException {
        this.username = username;
        frame = new JFrame("Endless Mode");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setUndecorated(true);
//        frame.getContentPane().setBackground(new Color(240, 224, 208));

        SoundManager.setMusicPlayer(musicPlayer);
        playSound();
        JPanel linePanel = new JPanel();
        linePanel.setBounds(0,56,800,2);
        linePanel.setBackground(Color.BLACK);
        frame.add(linePanel);
        ImageIcon pause = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\pause.png", 29, 27,true);
        ImageIcon pauseHoverbtn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\pause hover.png", 29, 27,true);
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
        JButton resumeButton = new JButton("Resume");
        resumeButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
        resumeButton.setBounds(325, 150, 150, 50);
        shadowPanel.add(resumeButton);

// Resume button logic
        resumeButton.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            playSound();
            shadowPanel.setVisible(false);
            frame.requestFocusInWindow(); // regain key focus   1`q
            timeElapsedTimer.start();
//            if (movementTimer == null) movementTimer.start();
            movementTimer.start();
            isPaused = false;
        });
        JButton settingsButton = new JButton("Settings");
        settingsButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
        settingsButton.setBounds(325, 250, 150, 50);
        shadowPanel.add(settingsButton);
        settingsButton.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            new Settings(() -> {
                SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
                // for example: re-show the HomePage
                shadowPanel.setVisible(true);
            });
            ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
        });

        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
        quitButton.setBounds(325, 350, 150, 50);
        shadowPanel.add(quitButton);
        quitButton.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            String[] options = {"Homepage", "Exit Game"};
            int choice = JOptionPane.showOptionDialog(
                    frame,                                       // parent component
                    "What would you like to do?",                // message
                    "Quit Game",                                 // title
                    JOptionPane.DEFAULT_OPTION,                  // optionType
                    JOptionPane.QUESTION_MESSAGE,                // messageType
                    null,                                        // icon
                    options,                                     // the choices
                    options[0]                                   // default choice
            );

            if (choice == 0) {
                // Go back to your main menu / gamemode screen
                SoundManager.stopBackgroundMusic();
                movementTimer.stop();
                timeElapsedTimer.stop();
                elapsedTime = 0;
                try {
                    new HomePage();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
                ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
            }
            else if (choice == 1) {
                // Quit the JVM entirely
                System.exit(0);
            }
        });

        frame.getContentPane().remove(shadowPanel);

// 2) put it straight into the layered pane at the MODAL layer
        frame.getLayeredPane().add(shadowPanel, JLayeredPane.MODAL_LAYER);

// 3) make sure it covers the whole frame
        shadowPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        shadowPanel.setVisible(false);
        pausebtn.setRolloverIcon(pauseHoverbtn);
        pausebtn.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            if (!isPaused) {
                isPaused = true;
                SoundManager.stopBackgroundMusic();
                // stop both the elapsed‐time *and* the movement timers:
                timeElapsedTimer.stop();
                if (movementTimer != null) movementTimer.stop();
                shadowPanel.setVisible(true);
            }
        });
        frame.add(pausebtn);

        RoundedButton backBtn = new RoundedButton("← Back",false);
        backBtn.setBounds(5, 19, 100, 30);
        backBtn.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            try {
                new Gamemode();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.dispose();
            SoundManager.stopBackgroundMusic();
            movementTimer.stop();
            timeElapsedTimer.stop();
            elapsedTime = 0;
            ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
        });
        frame.getContentPane().add(backBtn);

        timerLabel = new JLabel("Time: 0s");
        timerLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
        timerLabel.setBounds(350, 500, 150, 30);
        frame.add(timerLabel);

        scoreLabel = new JLabel("SCORE: 0");
        scoreLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
        scoreLabel.setBounds(350, 66, 150, 30);
        frame.add(scoreLabel);

        ImageIcon levelUpimg = new ImageIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\levelupEnd.png");
        Image img = levelUpimg.getImage();
        Image resizedImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resLevelUp = new ImageIcon(resizedImg);

        levelImgs = new JLabel(resLevelUp);
        levelImgs.setBounds(700,66,30,30);
        frame.add(levelImgs);

        ImageIcon clockimg = new ImageIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\clockinclock.png");
        Image imgclock = clockimg.getImage();
        Image resizedclockImg = imgclock.getScaledInstance(80, 95, Image.SCALE_SMOOTH);
        ImageIcon resclockimg = new ImageIcon(resizedclockImg);

        JLabel clocklbl = new JLabel(resclockimg);
        clocklbl.setBounds(367,400,80,95);
        frame.add(clocklbl);

        levelLabel = new JLabel("1");
        levelLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
        levelLabel.setBounds(740, 69, 200, 30); // Adjust X/Y to your layout
        frame.add(levelLabel);

        heartsLabel = new JLabel();
        heartsLabel.setBounds(100, 66, 150, 30); // Adjust position as needed
        updateHeartsDisplay(); // Initialize display
        frame.add(heartsLabel);

        JPanel linePanel1 = new JPanel();
        linePanel1.setBounds(250, 300, 300, 2);
        linePanel1.setBackground(Color.BLACK);
        frame.add(linePanel1);
        JPanel linePanel2 = new JPanel();
        linePanel2.setBounds(300, 310, 200, 2);
        linePanel2.setBackground(Color.BLACK);
        frame.add(linePanel2);

        textLabel = new JLabel("");
        textLabel.setFont(new Font("Century Gothic", Font.BOLD, 40));
        frame.add(textLabel);

        String result = getHighScore(username);
        JLabel highScorelbl = new JLabel(result);
        highScorelbl.setBounds(620, 100, 150, 30);
        frame.add(highScorelbl);

        frame.setFocusable(true);
        frame.requestFocusInWindow();

        random = new Random();
        startNewText();
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    e.consume();  // Prevent default button click
                    if (!isPaused) {
                        SoundManager.stopBackgroundMusic();
                        isPaused = true;
                        // stop both the elapsed‐time *and* the movement timers:
                        timeElapsedTimer.stop();
                        if (movementTimer != null) movementTimer.stop();
                        shadowPanel.setVisible(true);
                    } else {
                        // resume
                        isPaused = false;
                        playSound();
                        shadowPanel.setVisible(false);
                        timeElapsedTimer.start();
                        movementTimer.start();
                        frame.requestFocusInWindow();
                    }
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {
                char typedChar = e.getKeyChar();
                if (typedChar == ' ') {
                    return;
                }
                if (!startedTyping) {
                    startedTyping = true;
                    startTime = System.currentTimeMillis();
                }
                if (index[0] < currentText.length()) { // Ensure index is within bounds
                    StringBuilder formattedText = new StringBuilder("<html><div style='white-space: nowrap;'>");
                    boolean mistakeMade = false;

                    for (int i = 0; i < currentText.length(); i++) {
                        if (i < index[0] || (i == index[0]  && Character.toLowerCase(typedChar) == Character.toLowerCase(currentText.charAt(index[0])))) {
                            formattedText.append("<font color='green'>").append(currentText.charAt(i)).append("</font>");
                        } else if (i == index[0] && Character.toLowerCase(typedChar) != Character.toLowerCase(currentText.charAt(index[0]))) {
                            formattedText.append("<font color='red'>").append(currentText.charAt(i)).append("</font>");
                            playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\0429.WAV");
                            mistakeMade = true;
                            hearts--;
                            errorCount++;
                            updateHeartsDisplay();

                            Color orig = frame.getContentPane().getBackground();
                            frame.getContentPane().setBackground(Color.RED);
                            new Timer(200, ev -> {
                                ((Timer)ev.getSource()).stop();
                                frame.getContentPane().setBackground(orig);
                            }).start();

                            if (hearts <= 0) {
                                try {
                                    gameOver();
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
//                                saveEndlessScore(username,level,score);
                                return;
                            }

                        } else {
                            formattedText.append(currentText.charAt(i));
                        }
                    }
                    formattedText.append("</html>");
                    textLabel.setText(formattedText.toString());

                    if (!mistakeMade) {
                        index[0]++;
                        score++;
                        scoreLabel.setText("Score: " + score);
                    }
                    if (index[0] >= currentText.length()) {
                        startNewText();
                        return;      // bail out of this keystroke
                    }
                }
            }
        });
        timeElapsedTimer = new Timer(1000, e -> {
            if (!isPaused) {
                elapsedTime++;
                timerLabel.setText("Time: " + elapsedTime + "s");

                if (elapsedTime % 50 == 0) {  // Every 60 seconds
                    isPaused = true;
                    pauseGameAndShowLevelPanel();
                }
            }
        });

        timeElapsedTimer.start();
        frame.setVisible(true);
    }

    private void startNewText() {
        index[0] = 0;
        currentText = speeches[random.nextInt(speeches.length)];
        textLabel.setText(currentText);
        xPosition = 800; // Start from the right
        int textWidth = getTextWidth(currentText);
        // move it instantly to the far right:
        textLabel.setBounds(xPosition, 245, textWidth, 54);

        if (movementTimer != null) {
//            SoundManager.stopBackgroundMusic();
            movementTimer.stop();
        }

        movementTimer = new Timer(50, e -> {
            if (!isPaused) {
                try {
                    moveText();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        movementTimer.start();
    }


    private void moveText() throws IOException {
        xPosition -= speed; // Keep constant speed for this speech
        int textWidth = getTextWidth(currentText);
        textLabel.setBounds(xPosition, 245, textWidth, 54);

        if (xPosition + textLabel.getWidth() < 0) {
            // ► START RED FLASH
            Color orig = frame.getContentPane().getBackground();
            frame.getContentPane().setBackground(Color.RED);
            new Timer(200, ev -> {
                ((Timer)ev.getSource()).stop();
                frame.getContentPane().setBackground(orig);
            }).start();
            // ◄ END RED FLASH
            hearts--;
            updateHeartsDisplay();
             if (hearts <= 0) {
              gameOver();        // end the game if no lives left
             } else {
              startNewText();    // otherwise, drop in a fresh word
             }
        }
    }
    private int getTextWidth(String text) {
        // Calculate the width of the text based on the font and string length
        FontMetrics fontMetrics = textLabel.getFontMetrics(textLabel.getFont());
        return fontMetrics.stringWidth(text);
    }

    private void pauseGameAndShowLevelPanel() {
        if (movementTimer != null) movementTimer.stop();

        level++; // Increment level
        levelLabel.setText(String.valueOf(level));
        JOptionPane.showMessageDialog(null, "Level " + level + " Reached!", "Level Up", JOptionPane.INFORMATION_MESSAGE);

        // Increase speed only when new level is reached
        speed += speedIncrement;

        isPaused = false;
        startNewText(); // Resume with new speech
    }
    public static void saveEndlessScore(String username, int score) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:game_scores.db")) {
            // 1) get user_id
            PreparedStatement getUserId = conn.prepareStatement(
                    "SELECT id FROM users WHERE username = ?");
            getUserId.setString(1, username);
            ResultSet rsUser = getUserId.executeQuery();
            if (!rsUser.next()) return;
            int userId = rsUser.getInt("id");

            // 2) see if we already have a score
            PreparedStatement sel = conn.prepareStatement(
                    "SELECT score FROM endless_scores WHERE user_id = ?");
            sel.setInt(1, userId);
            ResultSet rs = sel.executeQuery();

            if (rs.next()) {
                int oldScore = rs.getInt("score");
                if (score > oldScore) {
                    // only update the score
                    PreparedStatement upd = conn.prepareStatement(
                            "UPDATE endless_scores SET score = ? WHERE user_id = ?");
                    upd.setInt(1, score);
                    upd.setInt(2, userId);
                    upd.executeUpdate();
                }
            } else {
                // insert first‐time score
                PreparedStatement ins = conn.prepareStatement(
                        "INSERT INTO endless_scores(user_id, score) VALUES (?, ?)");
                ins.setInt(1, userId);
                ins.setInt(2, score);
                ins.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static String getHighScore(String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:game_scores.db")) {
            PreparedStatement stmt = conn.prepareStatement("""
            SELECT MAX(v.score) AS high_score
            FROM endless_scores v
            JOIN users u ON v.user_id = u.id
            WHERE u.username = ?
        """);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt("high_score") > 0) {
                int score = rs.getInt("high_score");
                return "High Score: " + score;
            } else {
                return "High Score: 0";
            }
        } catch (SQLException ex) {
            return "Error retrieving score: " + ex.getMessage();
        }
    }

    private void updateHeartsDisplay() {
        StringBuilder heartIcons = new StringBuilder("<html>");
        for (int i = 0; i < hearts; i++) {
            heartIcons.append("<img src='file:C:\\Users\\User\\Documents\\untitled\\src\\Images\\heart.png'> ");
        }
        heartsLabel.setText(heartIcons.toString());
    }

    private void gameOver() throws IOException {
        SoundManager.stopBackgroundMusic();
        if (movementTimer != null) movementTimer.stop();
        if (timeElapsedTimer != null) timeElapsedTimer.stop();

        // Compute stats
        double minutes = elapsedTime / 60.0;
        int totalWords = score;              // or however you want to count words
//        int errors = /* you’ll need to track this separately */;
        double wpm = totalWords / minutes;
        double accuracy = 100.0 * (1.0 - (errorCount / (double)(totalWords + errorCount)));

        saveEndlessScore(username,score);
        saveStatistics(username, wpm, accuracy, errorCount, totalWords);

        JOptionPane.showMessageDialog(null, "Game Over!\nYour Score: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        new Gamemode();
        frame.setVisible(false);// Close the game
    }
    public void playEffect(String soundPath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundPath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
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
        String url = "jdbc:sqlite:game_scores.db";
        try (Connection conn = DriverManager.getConnection(url)) {
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
                    "INSERT INTO statistics (user_id, wpm, accuracy, errorCount, wordCount) " +
                            "VALUES (?, ?, ?, ?, ?)");
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
                    "Could not save statistics:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void playSound() {
        // let SoundManager decide if music should play or not
        SoundManager.playBackgroundRandom(gamemodeSongs);
    }

    public static void main(String[] args) {
        // figure out Dark vs Light
        ThemeManager.Theme startup =
                PreferencesManager.loadThemeChoice().equalsIgnoreCase("Dark")
                        ? ThemeManager.Theme.DARK
                        : ThemeManager.Theme.LIGHT;

        SwingUtilities.invokeLater(() -> {
            try {
                new Endless("");                   // build the UI
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ThemeManager.applyTheme(startup); // now flip L&F + do your scan
        });
    }
}