//import javax.swing.text.SimpleAttributeSet;
//import javax.swing.text.StyleConstants;
//import javax.swing.text.StyledDocument;
//import java.awt.*;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.sql.*;
//import java.util.Random;
//
//public class Endless {
//    JFrame frame;
//    private JTextPane textArea = new JTextPane();
//    private JLabel textLabel, scoreLabel, timerLabel, heartsLabel,levelLabel,levelImgs;
//    private Timer timer;
//    private int xPosition, elapsedTime, score = 0;;
//    private double speed = 2.0; // Initial speed
//    private double speedIncrement = 0.5; // Gradual speed increase
//    private String currentText;
//    private Random random;
//    int[] index = {0};;
//    private int hearts = 3;
//    private int level = 1;
//    private boolean isPaused = false;
//    private long startTime;
//    private boolean startedTyping = false;
//    private Timer movementTimer;
//    private Timer timeElapsedTimer;
//    private String username;
//
//    private String[] speeches = {
//            "CAT", "DOG", "SUN", "RED", "BLUE", "FISH", "BOOK", "JUMP", "CHAIR", "TABLE",
//            "LIGHT", "MOON", "STAR", "WATER", "GREEN", "HOUSE", "MUSIC", "TREE", "BIRD", "PHONE",
//            "BREAD", "MOUSE", "CLOUD", "RAIN", "SNOW", "WIND", "SMILE", "BALL", "CAKE", "SAND",
//            "TOY", "MILK", "CAR", "PEN", "SHIRT", "GLOVE", "BELL", "SHOE", "PAPER", "CLOCK",
//            "BOAT", "HAT", "ROCK", "BIRD", "DOOR", "KEY", "SKY", "LEAF", "GRASS", "RAIN",
//            "GUITAR", "PIANO", "BRUSH", "LAMP", "FLAG", "SCARF", "SKIRT", "BOOT", "SOCKS", "COIN",
//            "WALLET", "CHAIR", "RIVER", "LAKE", "OCEAN", "HILL", "BEACH", "ISLAND", "ICE", "STORM",
//            "SUNNY", "FOGGY", "MIST", "CLEAR", "DAY", "NIGHT", "CLOCK", "WATCH", "TIME", "HOUR",
//            "MONTH", "YEAR", "WEEK", "TRIP", "ROUTE", "MAP", "TICKET", "PLANE", "BUS", "CAR",
//            "TRAIN", "FERRY", "TAXI", "POLICE", "BOAT", "TRUCK", "SKATE", "FRAME", "PEN", "PENCIL",
//            "ERASER", "CRAYON", "RULER", "CHALK", "RUBBER", "INK", "PAINT", "COLOR", "GLUE",
//            "LETTER", "DIARY", "TICKET", "MENU", "TABLET", "MOUSE", "SCREEN", "RADIO", "PLUG", "MAT",
//            "WALL", "FLOOR", "DOOR", "LOCK", "HANDLE", "CUSHION", "TOY", "PLANE", "BUS", "GOLF",
//            "DARTS", "CHESS", "FENCING", "GYM", "YOGA", "JOGGING", "PUSHUP", "SITUP", "BALANCE",
//            "CARDIO", "COACH", "GOAL", "MATCH", "SCORE", "POINTS", "TEAM", "PLAYER", "GAME", "PLAY",
//            "GARDEN", "PENCIL", "WINTER", "SUNSET", "ROCKET", "PLANET", "SILENT", "FRIEND", "PURPLE", "BANANA",
//            "PAINTER", "PICTURE", "BREEZE", "BUTTERFLY", "DIAMOND", "BALANCE", "FEATHER", "PUZZLE", "LANTERN", "LIBRARY",
//            "WHISTLE", "TUNNEL", "CANDLE", "FESTIVAL", "TREASURE", "CHIMNEY", "JOURNEY", "SHADOW", "MACHINE", "GLACIER",
//            "ELEPHANT", "KEYBOARD", "MOUNTAIN", "UMBRELLA", "WHISPER", "VELOCITY", "TRIANGLE", "FURNITURE", "BICYCLE", "STRATEGY",
//            "AVALANCHE", "HORIZON", "MICROSCOPE", "PARALLEL", "SKELETON", "LIGHTHOUSE", "ADVENTURE", "SYNERGY", "TERRACOTTA", "INVISIBLE",
//            "BLUEPRINT", "TELESCOPE", "SIGNATURE", "CAPTIVATE", "SPECTACLE", "MARATHON", "PENDULUM",
//            "philosophy", "encyclopedia", "silhouette", "hypothesis", "quarantine", "astronomy", "synchronize", "architecture", "vocabulary", "consequence",
//            "metamorphosis", "phenomenon", "circumference", "catastrophe", "bureaucracy", "juxtaposition", "unprecedented", "paradoxical", "sophisticated", "perspective",
//            "conglomerate", "transcendence", "psychology", "ambidextrous", "conundrum", "misconception", "extrapolate", "onomatopoeia", "polyphonic", "hallucination"
//    };
//
//
//    public Endless(String username) {
//        this.username = username;
//        frame = new JFrame("Endless Mode");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800, 550);
//        frame.setLocationRelativeTo(null);
//        frame.setLayout(null);
//        frame.setUndecorated(true);
////        frame.getContentPane().setBackground(new Color(240, 224, 208));
//
//        JPanel linePanel = new JPanel();
//        linePanel.setBounds(0,56,800,2);
//        linePanel.setBackground(Color.BLACK);
//        frame.add(linePanel);
//
//        timerLabel = new JLabel("Time: 0s");
//        timerLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
//        timerLabel.setBounds(350, 500, 150, 30);
//        frame.add(timerLabel);
//
//        scoreLabel = new JLabel("SCORE: 0");
//        scoreLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
//        scoreLabel.setBounds(350, 66, 150, 30);
//        frame.add(scoreLabel);
//
//        ImageIcon levelUpimg = new ImageIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\levelupEnd.png");
//        Image img = levelUpimg.getImage();
//        Image resizedImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
//        ImageIcon resLevelUp = new ImageIcon(resizedImg);
//
//        levelImgs = new JLabel(resLevelUp);
//        levelImgs.setBounds(700,66,30,30);
//        frame.add(levelImgs);
//
//        ImageIcon clockimg = new ImageIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\clockinclock.png");
//        Image imgclock = clockimg.getImage();
//        Image resizedclockImg = imgclock.getScaledInstance(80, 95, Image.SCALE_SMOOTH);
//        ImageIcon resclockimg = new ImageIcon(resizedclockImg);
//
//        JLabel clocklbl = new JLabel(resclockimg);
//        clocklbl.setBounds(367,400,80,95);
//        frame.add(clocklbl);
//
//        levelLabel = new JLabel("1");
//        levelLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
//        levelLabel.setBounds(740, 69, 200, 30); // Adjust X/Y to your layout
//        frame.add(levelLabel);
//
//        heartsLabel = new JLabel();
//        heartsLabel.setBounds(100, 66, 150, 30); // Adjust position as needed
//        updateHeartsDisplay(); // Initialize display
//        frame.add(heartsLabel);
//
//        JPanel linePanel1 = new JPanel();
//        linePanel1.setBounds(250, 300, 300, 2);
//        linePanel1.setBackground(Color.BLACK);
//        frame.add(linePanel1);
//        JPanel linePanel2 = new JPanel();
//        linePanel2.setBounds(300, 310, 200, 2);
//        linePanel2.setBackground(Color.BLACK);
//        frame.add(linePanel2);
//
//        textLabel = new JLabel("");
//        textLabel.setFont(new Font("Century Gothic", Font.BOLD, 40));
//        frame.add(textLabel);
//
//        String result = getHighScore(username);
//        JLabel highScorelbl = new JLabel(result);
//        highScorelbl.setBounds(620, 19, 150, 30);
//        frame.add(highScorelbl);
//
//
//        random = new Random();
//        startNewText();
//        frame.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                if (!startedTyping) {
//                    startedTyping = true;
//                    startTime = System.currentTimeMillis();
//                }
//                char typedChar = e.getKeyChar();
//                if (index[0] < currentText.length()) { // Ensure index is within bounds
//                    StringBuilder formattedText = new StringBuilder("<html><div style='white-space: nowrap;'>");
//                    boolean mistakeMade = false;
//
//                    for (int i = 0; i < currentText.length(); i++) {
//                        if (i < index[0] || (i == index[0]  && Character.toLowerCase(typedChar) == Character.toLowerCase(currentText.charAt(index[0])))) {
//                            formattedText.append("<font color='green'>").append(currentText.charAt(i)).append("</font>");
//                        } else if (i == index[0] && Character.toLowerCase(typedChar) != Character.toLowerCase(currentText.charAt(index[0]))) {
//                            formattedText.append("<font color='red'>").append(currentText.charAt(i)).append("</font>");
//                            mistakeMade = true;
//                            hearts--;
//                            updateHeartsDisplay();
//
//                            Color orig = frame.getContentPane().getBackground();
//                            frame.getContentPane().setBackground(Color.RED);
//                            new Timer(200, ev -> {
//                                ((Timer)ev.getSource()).stop();
//                                frame.getContentPane().setBackground(orig);
//                            }).start();
//
//                            if (hearts <= 0) {
//                                gameOver();
////                                saveEndlessScore(username,level,score);
//                                return;
//                            }
//
//                        } else {
//                            formattedText.append(currentText.charAt(i));
//                        }
//                    }
//                    formattedText.append("</html>");
//                    textLabel.setText(formattedText.toString());
//
//                    if (!mistakeMade) {
//                        index[0]++;
//                        score++;
//                        scoreLabel.setText("Score: " + score);
//                    }
//                    if (index[0] >= currentText.length()) {
//                        startNewText();
//                        return;      // bail out of this keystroke
//                    }
//                }
//            }
//        });
//        timeElapsedTimer = new Timer(1000, e -> {
//            if (!isPaused) {
//                elapsedTime++;
//                timerLabel.setText("Time: " + elapsedTime + "s");
//
//                if (elapsedTime % 50 == 0) {  // Every 60 seconds
//                    isPaused = true;
//                    pauseGameAndShowLevelPanel();
//                }
//            }
//        });
//
//        timeElapsedTimer.start();
//        frame.setVisible(true);
//    }
//
//    private void startNewText() {
//        index[0] = 0;
//        currentText = speeches[random.nextInt(speeches.length)];
//        textLabel.setText(currentText);
//        xPosition = 800; // Start from the right
//
//        if (movementTimer != null) {
//            movementTimer.stop();
//        }
//
//        movementTimer = new Timer(50, e -> {
//            if (!isPaused) moveText();
//        });
//        movementTimer.start();
//    }
//
//
//    private void moveText() {
//        xPosition -= speed; // Keep constant speed for this speech
//        int textWidth = getTextWidth(currentText);
//        textLabel.setBounds(xPosition, 245, textWidth, 54);
//
//        if (xPosition + textLabel.getWidth() < -0) { // Wait until fully off-screen
//            startNewText(); // Start new text immediately
//        }
//    }
//    private int getTextWidth(String text) {
//        // Calculate the width of the text based on the font and string length
//        FontMetrics fontMetrics = textLabel.getFontMetrics(textLabel.getFont());
//        return fontMetrics.stringWidth(text);
//    }
//
//    private void pauseGameAndShowLevelPanel() {
//        if (movementTimer != null) movementTimer.stop();
//
//        level++; // Increment level
//        levelLabel.setText(String.valueOf(level));
//        JOptionPane.showMessageDialog(null, "Level " + level + " Reached!", "Level Up", JOptionPane.INFORMATION_MESSAGE);
//
//        // Increase speed only when new level is reached
//        speed += speedIncrement;
//
//        isPaused = false;
//        startNewText(); // Resume with new speech
//    }
//    public static void saveEndlessScore(String username, int score) {
//        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:game_scores.db")) {
//            // 1) get user_id
//            PreparedStatement getUserId = conn.prepareStatement(
//                    "SELECT id FROM users WHERE username = ?");
//            getUserId.setString(1, username);
//            ResultSet rsUser = getUserId.executeQuery();
//            if (!rsUser.next()) return;
//            int userId = rsUser.getInt("id");
//
//            // 2) see if we already have a score
//            PreparedStatement sel = conn.prepareStatement(
//                    "SELECT score FROM endless_scores WHERE user_id = ?");
//            sel.setInt(1, userId);
//            ResultSet rs = sel.executeQuery();
//
//            if (rs.next()) {
//                int oldScore = rs.getInt("score");
//                if (score > oldScore) {
//                    // only update the score
//                    PreparedStatement upd = conn.prepareStatement(
//                            "UPDATE endless_scores SET score = ? WHERE user_id = ?");
//                    upd.setInt(1, score);
//                    upd.setInt(2, userId);
//                    upd.executeUpdate();
//                }
//            } else {
//                // insert first‐time score
//                PreparedStatement ins = conn.prepareStatement(
//                        "INSERT INTO endless_scores(user_id, score) VALUES (?, ?)");
//                ins.setInt(1, userId);
//                ins.setInt(2, score);
//                ins.executeUpdate();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, e.getMessage(),
//                    "Database Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//
//    public static String getHighScore(String username) {
//        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:game_scores.db")) {
//            PreparedStatement stmt = conn.prepareStatement("""
//            SELECT MAX(v.score) AS high_score
//            FROM endless_scores v
//            JOIN users u ON v.user_id = u.id
//            WHERE u.username = ?
//        """);
//            stmt.setString(1, username);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next() && rs.getInt("high_score") > 0) {
//                int score = rs.getInt("high_score");
//                return "High Score: " + score;
//            } else {
//                return "High Score: 0";
//            }
//        } catch (SQLException ex) {
//            return "Error retrieving score: " + ex.getMessage();
//        }
//    }
//
//    private void updateHeartsDisplay() {
//        StringBuilder heartIcons = new StringBuilder("<html>");
//        for (int i = 0; i < hearts; i++) {
//            heartIcons.append("<img src='file:C:\\Users\\User\\Documents\\untitled\\src\\Images\\heart.png'> ");
//        }
//        heartsLabel.setText(heartIcons.toString());
//    }
//
//    private void gameOver() {
//        if (movementTimer != null) movementTimer.stop();
//        if (timeElapsedTimer != null) timeElapsedTimer.stop();
//        saveEndlessScore(username,score);
//
//        JOptionPane.showMessageDialog(null, "Game Over!\nYour Score: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
//        System.exit(0); // Close the game
//    }
//
//
//    public static void main(String[] args) {
//        new Endless("");
//    }
//}"