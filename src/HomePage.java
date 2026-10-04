import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomePage {
    private static final String DB_URL = DBUtil.DB_URL;
    private JButton userButton;
    private JFrame frame;
    private static String selectedUsername;
    private static JComboBox<String> userComboBox;
    private static final String ADD_USER_ENTRY = "+ Add new user…";
    String lastUsed;
    private MusicPlayer musicPlayer = new MusicPlayer();
    private JComboBox dropdown;
    private List<String> homepageSongs = List.of(
            "main/resources/Audio/tokyo-music-walker-sunset-drive.mp3",
            "Audio/Carefree.mp3"
    );

    HomePage() throws IOException {
        frame = new JFrame(I18n.t("HOMEPAGE"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        // Register the music player with the SoundManager so toggles will take effect instantly
        SoundManager.setMusicPlayer(musicPlayer);
        frame.setContentPane(new BackgroundPanel("main/resources/Images/BACKGROUND.jpg"));
        frame.getContentPane().setBackground(new Color(240, 224, 208));
        frame.repaint();
        frame.revalidate();

        playSound();

        userComboBox = new JComboBox<>();
        userComboBox.setBounds(565, 20, 200, 30);
        userComboBox.setFocusable(false);
        userComboBox.setBackground(Color.white);
        userComboBox.setForeground(new Color(195, 88, 41));
        userComboBox.setOpaque(false);

        userComboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("\u25BC"); // Unicode for downward arrow
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                button.setOpaque(true);
                button.setBackground(new Color(195, 88, 41));
                button.setForeground(Color.WHITE);
                return button;
            }
        });
        frame.add(userComboBox);
        userComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // top, left, bottom, right padding
//                label.setBackground(Color.WHITE);
                label.setForeground(new Color(195,88,41));
//                if (isSelected) {
//                    label.setBackground(new Color(230, 230, 230)); // light gray when selected (optional)
//                }
                label.setOpaque(true);
                return label;
            }
        });


        userComboBox.addActionListener(e -> {
            String sel = (String) userComboBox.getSelectedItem();
            if (I18n.t(ADD_USER_ENTRY).equals(sel)) {
                addNewUser();                  // go straight to “add”
            } else {
                selectedUsername = sel;        // switch users
                updateLastUsedUser(sel);
            }
        });

        ImageIcon info = ResourceUtils.createScaledIcon("main/resources/Images/INFO.png", 27, 27,true);
        ImageIcon infoHoverbtn = ImageUtils.createScaledIcon("main/resources/Images/INFO2.png", 27, 27,true);
        JButton infolbl = new JButton(info);
        infolbl.setBounds(19,19,27,27);
        infolbl.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        infolbl.setContentAreaFilled(false);  // Make the background transparent
        infolbl.setFocusPainted(false);  // Remove focus border on click
        infolbl.setBorderPainted(false);  // Remove border paint
        infolbl.setRolloverIcon(infoHoverbtn);
        infolbl.addActionListener(e -> {
            try {
                createShadowPanel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        JPanel shadowPanel = createShadowPanel();

        ImageIcon settingsImg = ImageUtils.createScaledIcon("main/resources/Images/settings.png", 27, 27,true);
        ImageIcon settingsHover = ImageUtils.createScaledIcon("main/resources/Images/settings 2.png", 27, 27,true);
        JButton settingslbl = new JButton(settingsImg);
        settingslbl.setBounds(56,19,27,27);
        settingslbl.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        settingslbl.setContentAreaFilled(false);  // Make the background transparent
        settingslbl.setFocusPainted(false);  // Remove focus border on click
        settingslbl.setBorderPainted(false);  // Remove border paint
        settingslbl.setRolloverIcon(settingsHover);
        settingslbl.addActionListener(e -> {
            SoundManager.playEffect("main/resources/Audio/Click.WAV");
            new Settings(() -> {
                SoundManager.playEffect("main/resources/Audio/Click.WAV");
                frame.setVisible(true);
            }, () -> {
                SoundManager.stopBackgroundMusic();
                frame.dispose();
                try {
                    new HomePage();
                } catch (IOException ex) {
                    throw new IllegalStateException("Could not reopen the homepage", ex);
                }
            });
            ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
        });

        // Add mouse listener to the button to show/hide the shadow panel
        infolbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Show the shadow panel when mouse enters the button
                shadowPanel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Hide the shadow panel when mouse exits the button
                shadowPanel.setVisible(false);
            }
        });


        ImageIcon startbtn = ImageUtils.createScaledIcon("main/resources/Images/LANCER.png", 48, 48,true);
        ImageIcon startHoverbtn = ImageUtils.createScaledIcon("main/resources/Images/LANCER2.png", 48, 48,true);
        JButton startbtnlbl = new JButton(startbtn);
        startbtnlbl.setBounds(370,375,48,48);
        startbtnlbl.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        startbtnlbl.setContentAreaFilled(false);  // Make the background transparent
        startbtnlbl.setFocusPainted(false);  // Remove focus border on click
        startbtnlbl.setBorderPainted(false);  // Remove border paint
        startbtnlbl.setRolloverIcon(startHoverbtn);
        startbtnlbl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playEffect("main/resources/Audio/Click.WAV");
                SoundManager.stopBackgroundMusic();
                try {
                    new Gamemode();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
                ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
            }
        });

        JLabel startlbl = new JLabel(I18n.t("Start"));
        startlbl.setFont(new Font("Century Gothic",Font.PLAIN,20));
        startlbl.setHorizontalAlignment(SwingConstants.CENTER);
        startlbl.setBounds(344, 415, 100, 50);
        startlbl.setForeground(new Color(195, 88, 41));

//        JLabel langlbl = new JLabel("Langue: ");
//        langlbl.setFont(new Font("Century Gothic",Font.BOLD,20));
//        langlbl.setForeground(new Color(195,88,42));
//        langlbl.setBounds(600,450,150,30);
//        String[] options = {"English", "French"};
//        dropdown = new JComboBox<>(options);
//        dropdown.setBounds(690, 450, 90, 30);
//        dropdown.setFont(new Font("Century Gothic", Font.PLAIN, 14));
//        dropdown.setUI(new BasicComboBoxUI() {
//            @Override
//            protected JButton createArrowButton() {
//                JButton button = new JButton("\u25BC"); // Unicode for downward arrow
//                button.setBorder(BorderFactory.createEmptyBorder());
//                button.setContentAreaFilled(false);
//                button.setOpaque(true);
//                button.setBackground(new Color(195, 88, 42));
////                button.setForeground(Color.WHITE);
//                return button;
//            }
//        });
////        dropdown.addActionListener();
//
//        frame.add(langlbl);
//        frame.add(dropdown);
        JButton languageButton = new JButton(I18n.isFrench() ? "English" : "Français");
        languageButton.setToolTipText(I18n.t("Switch language"));
        languageButton.setBounds(650, 60, 115, 30);
        languageButton.setForeground(Color.WHITE);
        languageButton.setBackground(new Color(195, 88, 41));
        languageButton.setFocusPainted(false);
        languageButton.addActionListener(e -> {
            I18n.setLanguage(I18n.isFrench() ? I18n.ENGLISH : I18n.FRENCH);
            SoundManager.stopBackgroundMusic();
            frame.dispose();
            try {
                new HomePage();
            } catch (IOException ex) {
                throw new IllegalStateException("Could not reopen the homepage", ex);
            }
        });
        frame.add(languageButton);

        frame.add(startbtnlbl);
        frame.add(startlbl);
        frame.add(infolbl);
        frame.add(settingslbl);
        frame.add(shadowPanel);

        loadLastUsedUser();
        frame.setVisible(true);
    }

    private void loadLastUsedUser() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT UNIQUE NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS last_used_user (username TEXT UNIQUE NOT NULL)");

            // get last used
            ResultSet rs = stmt.executeQuery("SELECT username FROM last_used_user LIMIT 1");
            lastUsed = null;
            if (rs.next()) {
                lastUsed = rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // now load into combo
        refreshUserCombo(lastUsed);

    }

    private static void refreshUserCombo(String preferredUsername) {
        List<String> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username FROM users")) {
            while (rs.next()) users.add(rs.getString("username"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        if (users.isEmpty()) {
            model.addElement(I18n.t(ADD_USER_ENTRY));
            selectedUsername = null;
        } else {
            users.forEach(model::addElement);
            model.addElement(I18n.t(ADD_USER_ENTRY));

            if (preferredUsername != null && users.contains(preferredUsername)) {
                selectedUsername = preferredUsername;
            } else {
                selectedUsername = users.get(0); // pick the first available
                updateLastUsedUser(selectedUsername);
            }
        }

        userComboBox.setModel(model);
        if (selectedUsername != null) {
            userComboBox.setSelectedItem(selectedUsername);
        } else {
            userComboBox.setSelectedItem(I18n.t(ADD_USER_ENTRY));
        }
    }


    private void showUserSelectionDialog() {
        List<String> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username FROM users")) {
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (users.isEmpty()) {
            addNewUser();
        } else {
            String[] userArray = users.toArray(new String[0]);
            String selected = (String) JOptionPane.showInputDialog(frame, I18n.t("Choose your user:"), I18n.t("User Selection"),
                    JOptionPane.PLAIN_MESSAGE, null, userArray, userArray[0]);

            if (selected != null) {
                selectedUsername = selected;
                updateLastUsedUser(selectedUsername);
                userComboBox.setSelectedItem(selectedUsername);
            }
        }
    }

    private void addNewUser() {
        String typed = JOptionPane.showInputDialog(frame, I18n.t("Enter your username:"));
        if (typed == null) {
            return;
        }

        String newUsername = typed.trim();
        if (newUsername.isEmpty()) {
            JOptionPane.showMessageDialog(frame, I18n.t("Username cannot be empty."), I18n.t("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement check = conn.prepareStatement("SELECT id FROM users WHERE LOWER(username) = LOWER(?)")) {
            check.setString(1, newUsername);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(frame, I18n.t("Username already exists! Please choose another."), I18n.t("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, I18n.t("Could not validate username: ") + e.getMessage(), I18n.t("Database Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username) VALUES (?)")) {
            stmt.setString(1, newUsername);
            stmt.executeUpdate();
            selectedUsername = newUsername;
            updateLastUsedUser(newUsername);
            userComboBox.setSelectedItem(selectedUsername);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, I18n.t("Username already exists! Please choose another."), I18n.t("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        refreshUserCombo(selectedUsername);
        userComboBox.setSelectedItem(selectedUsername);
    }

    private static void updateLastUsedUser(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM last_used_user");
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO last_used_user (username) VALUES (?)")) {
                pstmt.setString(1, username);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getSelectedUsername() {
        return selectedUsername;
    }

    public static void setSelectedUsername(String username) {
        selectedUsername = username;
        updateLastUsedUser(username);
        refreshUserCombo(username);
    }

    private JPanel createShadowPanel() throws IOException {
        // Create the panel to act as the shadow
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(0, 0, 0, 150));  // Semi-transparent black background
        panel.setBounds(10, 10, 350, 500);  // Position the shadow panel below the button
        panel.setVisible(false);  // Initially hidden

        // Load the image to show in the panel
        ImageIcon image = ImageUtils.createScaledIcon("main/resources/Images/handKey.png", 250, 150,true);
        JLabel imageLabel = new JLabel(image);

        // Add the image to the top of the panel
        panel.add(imageLabel, BorderLayout.NORTH);

        // Create the text to show below the image
        JTextArea textArea = new JTextArea();
        textArea.setText(I18n.isFrench()
                ? "1. « Position des mains » :\n" +
                "   - Gardez les poignets droits et alignés avec le clavier.\n" +
                "   - Posez légèrement les doigts sur les touches de base (A, S, D, F, J, K, L, M).\n\n" +
                "2. « Placement des doigts » :\n" +
                "   - Main gauche : auriculaire sur A, annulaire sur S, majeur sur D, index sur F.\n" +
                "   - Main droite : index sur J, majeur sur K, annulaire sur L, auriculaire sur M.\n" +
                "   - Reposez les pouces légèrement sur la barre d’espace.\n\n" +
                "3. « Technique de frappe » :\n" +
                "   - Utilisez tous vos doigts et évitez de regarder le clavier.\n" +
                "   - Appuyez doucement sur les touches et laissez-les remonter.\n"
                : "1. \"Proper Hand Positioning\":\n" +
                "   - \"Wrists\": Keep them straight and level with the keyboard to prevent strain.\n" +
                "   - \"Fingers\": Rest lightly on the \"home row\" keys (A, S, D, F, J, K, L, ;).\n\n" +
                "2. \"Finger Placement\":\n" +
                "   - \"Left Hand\": Pinky on \"A,\" ring on \"S,\" middle on \"D,\" index on \"F.\"\n" +
                "   - \"Right Hand\": Index on \"J,\" middle on \"K,\" ring on \"L,\" pinky on \";.\"\n" +
                "   - \"Thumbs\": Rest lightly on the space bar.\n\n" +
                "3. \"Typing Technique\":\n" +
                "   - \"Use all fingers\": Each should cover specific keys for speed and accuracy.\n" +
                "   - \"Avoid looking at keys\": Muscle memory will improve fluency.\n" +
                "   - \"Key Press\": Type gently and let keys return before pressing again.\n");

        textArea.setEditable(false);
        textArea.setBackground(new Color(0, 0, 0, 0));  // Transparent background for the text area
        textArea.setForeground(Color.WHITE);  // Set text color to white
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Add the text below the image in the panel
        panel.add(textArea, BorderLayout.CENTER);

        return panel;
//        panel.setVisible(true);
    }
    public void playSound() {
        // let SoundManager decide if music should play or not
        SoundManager.playBackgroundRandom(homepageSongs);
    }



    public static void main(String[] args) {
        I18n.setLanguage(PreferencesManager.loadLanguageChoice());
        // figure out Dark vs Light
        ThemeManager.Theme startup =
                PreferencesManager.loadThemeChoice().equalsIgnoreCase("Dark")
                        ? ThemeManager.Theme.DARK
                        : ThemeManager.Theme.LIGHT;

        SwingUtilities.invokeLater(() -> {
            try {
                new HomePage();                   // build the UI
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ThemeManager.applyTheme(startup); // now flip L&F + do your scan
        });
    }
}


// Custom JPanel to draw an image as background
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            String resolvedPath = AssetResolver.resolve(imagePath);
            backgroundImage = new ImageIcon(ImageUtils.loadImage(resolvedPath)).getImage();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load background image: " + imagePath, e);
        }
        setLayout(null); // Allows absolute positioning if needed
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;

            // Enable high-quality rendering
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw image with high quality scaling
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
