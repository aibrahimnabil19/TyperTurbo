import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.sql.*;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.util.Vector;

public class Settings  {
    JFrame frame = new JFrame();
    private JPanel mainPanel, menuPanel;
    private CardLayout cardLayout;
//    private final HomePage homePage;
    String username;
    private final Runnable onBack;

    public Settings(Runnable onBack) {
        this.onBack = onBack;
        this.username = HomePage.getSelectedUsername();
        frame.setTitle("Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        JLabel versionlbl = new JLabel("Version 1.0");
        JLabel copyright = new JLabel("copyright @Aishatek 2025");
        versionlbl.setFont(new Font("Century Gothic",Font.PLAIN,20));
        copyright.setFont(new Font("Century Gothic",Font.ITALIC,15));
        versionlbl.setBounds(350,400,200,100);
        copyright.setBounds(320,430,200,100);
        frame.add(versionlbl);
        frame.add(copyright);

        RoundedButton backBtn = new RoundedButton("← Back",false);
        backBtn.setBounds(10, 5, 100, 20);
        backBtn.addActionListener(e -> {
            frame.dispose();      // close settings
            onBack.run();         // call whoever opened us
        });
        frame.getContentPane().add(backBtn);


        // Create the main menu panel with buttons
        menuPanel = new JPanel(null);
        String[] options = {
                "Profile",
                "Typing Statistics",
                "Game Modes",
                "Themes",
                "Sound & Effects",
                "Credits",
                "Help & Support"
        };

        int x = 10, y = 67, width = 765, height = 36;
        int space = 47;

        for (String option : options) {
            RoundedButton button = new RoundedButton(option,true);
//            JButton button = new JButton(option);
            button.setBounds(x, y, width, height);
            button.setBorder(BorderFactory.createEmptyBorder());  // Remove border
//            button.setContentAreaFilled(false);  // Make the background transparent
            button.setFocusPainted(false);  // Remove focus border on click
            button.setBorderPainted(false);
            button.setBackground(new Color(240,224,208));
//            new Color(240,224,208)
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showPanel(option);
//                    backBtn.setVisible(false);
                    ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
                }
            });
            menuPanel.add(button);
            y += space;
        }

        mainPanel.add(menuPanel, "MainMenu");
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void showPanel(String name) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBounds(10, 67, 765, 360);
        contentPanel.setBackground(Color.white);

        JLabel title = new JLabel(name);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBounds(20, 20, 500, 40);
        title.setForeground(new Color(195,88,42));
        contentPanel.add(title);

        JButton closeButton = new JButton("X");
        closeButton.setBounds(720, 10, 50, 30);
        closeButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        contentPanel.add(closeButton);

        JTextArea content = new JTextArea();
        content.setFont(new Font("Century Gothic",Font.PLAIN,15));
        content.setBackground(Color.white);
        content.setForeground(new Color(42, 52, 57));
//        content.setEnabled(false);
        content.setEditable(false);
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setBounds(10,80,765,360);
        // Add custom content based on option name
        switch (name) {
            case "Profile":
                username = HomePage.getSelectedUsername();

                JLabel userLabel = new JLabel("Username: " + username);
                userLabel.setFont(new Font("Arial", Font.BOLD, 18));
                userLabel.setBounds(250, 100, 400, 30);
                userLabel.setForeground(new Color(50, 50, 50));
                contentPanel.add(userLabel);

                JButton renameButton = new JButton("Rename My Account");
                renameButton.setBounds(250, 160, 250, 40);
                renameButton.setFocusPainted(false);
                renameButton.setBackground(new Color(195, 88, 42));
                renameButton.setForeground(Color.white);
                renameButton.setFont(new Font("SansSerif", Font.BOLD, 14));
                renameButton.setOpaque(true);
                renameButton.setBorder(new RoundedBorder(20));
                renameButton.addActionListener(e -> {
                    String newName = JOptionPane.showInputDialog(
                            contentPanel,
                            "Enter your new username:",
                            username
                    );
                    if (newName != null && !newName.trim().isEmpty()) {
                        int userId = Settings.getUserId(username);
                        if (userId != -1) {
                            boolean ok = Settings.renameUser(userId, newName.trim());
                            if (ok) {
                                username = newName.trim();
                                JOptionPane.showMessageDialog(contentPanel, "Username changed!");
                                HomePage.setSelectedUsername(newName.trim());
                                userLabel.setText("Username: " + newName.trim());
                            } else {
                                JOptionPane.showMessageDialog(contentPanel,
                                        "Failed to rename (maybe already in use).",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });
                contentPanel.add(renameButton);

                JButton deleteButton = new JButton("Delete My Account");
                deleteButton.setBounds(250, 220, 250, 40);
                deleteButton.setFocusPainted(false);
                deleteButton.setBackground(new Color(220, 20, 60));
                deleteButton.setForeground(Color.white);
                deleteButton.setFont(new Font("SansSerif", Font.BOLD, 14));
//                deleteButton.setOpaque(true);
                deleteButton.setBorder(new RoundedBorder(20));
//                deleteButton.setFocusPainted(false);
//                deleteButton.setBackground(Color.WHITE);
                deleteButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(contentPanel,
                            "Are you sure you want to delete your account?\nThis action cannot be undone.",
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        int userId = Settings.getUserId(username);
                        if (userId != -1) {
                            Settings.deleteUserCompletely(userId);
                            JOptionPane.showMessageDialog(contentPanel, "Account deleted.");
                            SwingUtilities.getWindowAncestor(contentPanel).dispose();
                        }
                    }
                });
                contentPanel.add(deleteButton);

                break;


            case "Typing Statistics":
                int userId = getUserId(username);

                // 1) Define table columns
                Vector<String> columnNames = new Vector<>();
                columnNames.add("Date");
                columnNames.add("WPM");
                columnNames.add("Accuracy");
                columnNames.add("Errors");
                columnNames.add("Words");

                // 2) Load data & accumulate sums
                Vector<Vector<Object>> data = new Vector<>();
                String sql = """
        SELECT played_at, wpm, accuracy, errorCount, wordCount
          FROM statistics
         WHERE user_id = ?
         ORDER BY played_at DESC
    """;

                double sumWpm = 0, sumAcc = 0;
                int    sumErr = 0, sumWords = 0, rowCount = 0;

                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:game_scores.db");
                     PreparedStatement ps = conn.prepareStatement(sql)) {

                    ps.setInt(1, userId);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String  date     = rs.getString("played_at");
                        double  wpm      = rs.getDouble("wpm");
                        double  acc      = rs.getDouble("accuracy");
                        int     errs     = rs.getInt("errorCount");
                        int     words    = rs.getInt("wordCount");

                        Vector<Object> row = new Vector<>();
                        row.add(date);
                        row.add(wpm);
                        row.add(acc);
                        row.add(errs);
                        row.add(words);
                        data.add(row);

                        // accumulate
                        sumWpm   += wpm;
                        sumAcc   += acc;
                        sumErr   += errs;
                        sumWords += words;
                        rowCount++;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                            "Could not load typing stats:\n" + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }

                // 3) Build the table in a scroll pane
                JTable table = new JTable(data, columnNames);
                table.setFillsViewportHeight(true);
                JScrollPane scroll = new JScrollPane(table);
                scroll.setBounds(20, 80, 725, 260);
                contentPanel.add(scroll);

                // 4) Compute & display averages if we have any rows
                if (rowCount > 0) {
                    double avgWpm      = sumWpm   / rowCount;
                    double avgAcc      = sumAcc   / rowCount;
                    double avgErr      = sumErr   / (double)rowCount;
                    double avgWords    = sumWords / (double)rowCount;

                    int labelY = 360;
                    int labelH = 25;
                    // Labels for each stat
                    JLabel avgLabel = new JLabel("Averages over " + rowCount + " sessions:");
                    avgLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                    avgLabel.setBounds(20, labelY, 250, labelH);
                    contentPanel.add(avgLabel);

                    JLabel wpmLabel = new JLabel(String.format("WPM: %.1f", avgWpm));
                    wpmLabel.setBounds(280, labelY, 120, labelH);
                    contentPanel.add(wpmLabel);

                    JLabel accLabel = new JLabel(String.format("Accuracy: %.1f%%", avgAcc));
                    accLabel.setBounds(400, labelY, 140, labelH);
                    contentPanel.add(accLabel);

                    JLabel errLabel = new JLabel(String.format("Errors: %.1f", avgErr));
                    errLabel.setBounds(550, labelY, 120, labelH);
                    contentPanel.add(errLabel);

                    JLabel wordsLabel = new JLabel(String.format("Words: %.1f", avgWords));
                    wordsLabel.setBounds(650, labelY, 120, labelH);
                    contentPanel.add(wordsLabel);
                } else {
                    JLabel none = new JLabel("No typing sessions recorded yet.");
                    none.setBounds(20, 360, 300, 25);
                    contentPanel.add(none);
                }

                break;


            case "Game Modes":
                // panel to hold 2×2 buttons
                JPanel modesPanel = new JPanel(new GridLayout(2, 2, 20, 20));
                modesPanel.setBounds(100, 100, 565, 260);
                modesPanel.setBackground(Color.white);

                // mode names
                String[] modes = { "Endless", "Challenge", "Vs Clock", "Level Up" };
                // detailed HTML descriptions
                String[] descriptions = {
                        // Endless
                        "<html><h2>Endless Mode</h2>"
                                + "<ul>"
                                + "<li><b>Objective:</b> Type as many words as you can without losing all hearts.</li>"
                                + "<li>Words scroll in from the right at an increasing speed.</li>"
                                + "<li>You start with 3 hearts; each typo or missed word costs one heart.</li>"
                                + "<li>Every 60 seconds your level increases and speed jumps up.</li>"
                                + "<li>Keep going until you run out of hearts!</li>"
                                + "</ul></html>",

                        // Challenge
                        "<html><h2>Challenge Mode</h2>"
                                + "<ul>"
                                + "<li><b>Objective:</b> Complete a series of random typing mini-games.</li>"
                                + "<li>Each round you’ll face a different challenge (e.g. rapid-fire letters, scrambled words, or timed bursts).</li>"
                                + "<li>You have a fixed time per round—beat the goal to advance to the next.</li>"
                                + "<li>See how many rounds you can clear in one session.</li>"
                                + "</ul></html>",

                        // Vs Clock
                        "<html><h2>Vs Clock Mode</h2>"
                                + "<ul>"
                                + "<li><b>Objective:</b> Type as many whole words as possible before the clock hits zero.</li>"
                                + "<li>Choose your time limit (60s, 120s, or 180s) and difficulty (Easy→Pro).</li>"
                                + "<li>Each correctly-typed word adds one point; typos don’t penalize hearts but slow you down.</li>"
                                + "<li>Track your high score per difficulty/time combo.</li>"
                                + "</ul></html>",

                        // Level Up
                        "<html><h2>Level Up Mode</h2>"
                                + "<ul>"
                                + "<li><b>Objective:</b> Progress through stages by typing increasingly complex passages.</li>"
                                + "<li>Each level unlocks a new text with higher word counts, punctuation, and speed requirements.</li>"
                                + "<li>Earn bonus multipliers for perfect accuracy streaks.</li>"
                                + "<li><i>Mode not available in this release—stay tuned!</i></li>"
                                + "<li>Coming in 1.1: full Level Up experience with unlockable achievements.</li>"
                                + "</ul></html>"
                };

                for (int i = 0; i < modes.length; i++) {
                    JButton btn = new RoundedButton(modes[i]);
                    btn.setFont(new Font("Century Gothic", Font.BOLD, 18));
                    btn.setBackground(new Color(240,224,208));
                    btn.setFocusPainted(false);

                    final String modeName = modes[i];
                    final String desc     = descriptions[i];
                    btn.addActionListener(e -> {
                        JOptionPane.showMessageDialog(
                                contentPanel,
                                desc,
                                modeName + " Instructions",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    });

                    modesPanel.add(btn);
                }

                contentPanel.add(modesPanel);
                break;


            case "Themes":
                String[] opts = { "Light", "Dark" };
                String choice = (String) JOptionPane.showInputDialog(
                        frame, "Choose theme:", "Themes",
                        JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]
                );
                if (choice != null) {
                    ThemeManager.applyTheme(
                            choice.equals("Dark")
                                    ? ThemeManager.Theme.DARK
                                    : ThemeManager.Theme.LIGHT
                    );
                    PreferencesManager.saveThemeChoice(choice);
                }
                return;

            case "Sound & Effects":
                content.setVisible(false);

                // Checkbox for Background Music
                JCheckBox backgroundMusicCheckBox = new JCheckBox("Background Music");
                backgroundMusicCheckBox.setForeground(new Color(195, 88, 41));
                backgroundMusicCheckBox.setSelected(SoundManager.isBackgroundMusicEnabled());
                backgroundMusicCheckBox.addActionListener(e -> {
                    boolean selected = backgroundMusicCheckBox.isSelected();
                    SoundManager.setBackgroundMusicEnabled(selected);
                    // Optionally auto-start music when enabling:
                    if (selected) {
                        // If you want to auto-play when enabled, uncomment the next line:
                        // SoundManager.playBackgroundRandom(homepageSongs);
                    }
                });
                backgroundMusicCheckBox.setBounds(20, 80, 200, 30);
                contentPanel.add(backgroundMusicCheckBox);

                // Checkbox for Effects Sounds
                JCheckBox effectsCheckBox = new JCheckBox("Effects Sounds");
                effectsCheckBox.setForeground(new Color(195, 88, 41));
                effectsCheckBox.setSelected(SoundManager.isEffectsEnabled());
                effectsCheckBox.addActionListener(e -> {
                    SoundManager.setEffectsEnabled(effectsCheckBox.isSelected());
                });
                effectsCheckBox.setBounds(20, 120, 200, 30);
                contentPanel.add(effectsCheckBox);
                break;

            case "Credits":
                // 1) Disable the shared 'content' so it doesn't cover our scroll pane:
                content.setVisible(false);

                // 2) Create a brand-new JTextArea for credits
                String creditsText =
                        "Aishatek Typing Suite – Version 1.0 (April 2025)\n\n" +
                                "■ Project Leadership\n" +
                                "  • Nabil – Lead Developer & Technical Architect\n" +
                                "  • Nassirou – Product Owner & UI/UX Design Lead\n\n" +
                                "■ Development Team\n" +
                                "  • Nabil – Core Java Swing Engine, Database & Persistence\n" +
                                "  • Nassirou – Visual Assets, Layouts & Theming\n\n" +
                                "■ Design & User Experience\n" +
                                "  • Nassirou – Screen Flows, Typography, Iconography\n" +
                                "  • Guest Designer – Logo & Branding Concepts\n\n" +
                                "■ Audio & Effects\n" +
                                "  • Carefree – Kevin MacLeod (incompetech.com) — licensed under Creative Commons Attribution 3.0 (CC BY-3.0)\n" +
                                "  • On & On (feat. Daniel Levi) – Cartoon & Jéja (NoCopyrightSounds) — free to use with attribution (NCS rules)\n" +
                                "  • Bad Karma – Axel Thesleff — copyrighted; permission required for use\n" +
                                "  • 8 Bit Adventure – AdhesiveWombat from Marsupial Madness (2013) — all rights reserved \n" +
                                "  • Blue (KNY Factory Remix) – Eiffel 65 (trap remix) — all rights reserved; licensing via Bliss Corporation (GZ2538)\n" +
                                "  • Stressed Out (Tomsize Remix) – Twenty One Pilots / Tomsize — all rights reserved\n\n" +
                                "■ Quality Assurance\n" +
                                "  • Community Testers – Bug Reporting & Usability Feedback\n" +
                                "  • Automated Tests – Core Functionality Validation\n\n" +
                                "■ Special Thanks\n" +
                                "  • SQLite (via the JDBC driver)\n" +
                                "  • All open-source contributors whose work we leveraged\n" +
                                "  • Our families and friends for their encouragement\n\n" +
                                "© 2025 Aishatek. All rights reserved.\n" +
                                "For inquiries, feedback or to report issues:\n" +
                                "  contact@aishatek.com";

                JTextArea creditsArea = new JTextArea(creditsText);
                creditsArea.setFont(new Font("Century Gothic", Font.PLAIN, 15));
                creditsArea.setEditable(false);
                creditsArea.setLineWrap(true);
                creditsArea.setWrapStyleWord(true);

                // 3) Wrap the new area in its own scroll pane
                JScrollPane creditsScroll = new JScrollPane(creditsArea);
                creditsScroll.setBounds(10, 80, 755, 360);
                creditsScroll.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
                );

                contentPanel.add(creditsScroll);
                break;


            case "Help & Support":
                // hide the shared textarea
                content.setVisible(false);

                // 1) FAQ HTML in a JEditorPane
                String faqHtml = """
      <html>
       <h1 style="color:#C3582A;">Help & Support</h1>
       <h3>Q1: How do I reset my statistics?</h3>
       <p>Delete your account in <b>Profile</b> and re-create it.</p>
       <h3>Q2: Can I change theme mid-game?</h3>
       <p>Themes apply on next launch only. Change under <b>Themes</b> and restart.</p>
       <h3>Q3: What’s coming in next release?</h3>
       <p>Level Up mode, new mini-games, more stats charts.</p>
       <h3>Q4: How do I report a bug?</h3>
       <p>Use the button below to send us a detailed report.</p>
      </html>
    """;

                JEditorPane faqPane = new JEditorPane("text/html", faqHtml);
                faqPane.setEditable(false);
                faqPane.setOpaque(false);
                faqPane.putClientProperty(
                        JEditorPane.HONOR_DISPLAY_PROPERTIES,
                        Boolean.TRUE
                );
                faqPane.setFont(new Font("Century Gothic", Font.PLAIN, 14));

                JScrollPane faqScroll = new JScrollPane(faqPane);
                faqScroll.setBounds(10, 80, 745, 220);
                faqScroll.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                );
                faqScroll.setBorder(BorderFactory.createEmptyBorder());
                contentPanel.add(faqScroll);

                // 2) “Send Feedback” button
                JButton sendBtn = new RoundedButton("Send Feedback");
                sendBtn.setFont(new Font("Century Gothic", Font.BOLD, 16));
                sendBtn.setBackground(new Color(195, 88, 42));
                sendBtn.setForeground(Color.white);
                sendBtn.setFocusPainted(false);
                sendBtn.setBounds(130, 320, 250, 40);

                sendBtn.addActionListener(ev -> {
                    // show an input dialog
                    JTextArea input = new JTextArea(6, 40);
                    input.setLineWrap(true);
                    input.setWrapStyleWord(true);
                    int choices = JOptionPane.showConfirmDialog(
                            contentPanel,
                            new JScrollPane(input),
                            "Describe your issue or suggestion",
                            JOptionPane.OK_CANCEL_OPTION
                    );
                    if (choices == JOptionPane.OK_OPTION) {
                        String msg = input.getText().trim();
                        if (msg.isEmpty()) {
                            JOptionPane.showMessageDialog(
                                    contentPanel,
                                    "Please enter a message.",
                                    "No Content",
                                    JOptionPane.WARNING_MESSAGE
                            );
                            return;
                        }
                        // send email in background thread
                        new Thread(() -> {
                            try {
                                MAILER.send(
                                        "aibrahimnabil@gmail.com",           // recipient
                                        "Aishatek Feedback from " + username,
                                        msg
                                );
                                SwingUtilities.invokeLater(() ->
                                        JOptionPane.showMessageDialog(
                                                contentPanel,
                                                "Thank you! Your feedback was sent.",
                                                "Sent",
                                                JOptionPane.INFORMATION_MESSAGE
                                        )
                                );
                            } catch (MessagingException mex) {
                                mex.printStackTrace();
                                SwingUtilities.invokeLater(() ->
                                        JOptionPane.showMessageDialog(
                                                contentPanel,
                                                "Failed to send feedback:\n" + mex.getMessage(),
                                                "Mail Error",
                                                JOptionPane.ERROR_MESSAGE
                                        )
                                );
                            }
                        }).start();
                    }
                });
                contentPanel.add(sendBtn);

                JButton sendButton = new RoundedButton("Report a Problem/Ask a qstn");
                sendButton.setFont(new Font("Century Gothic", Font.BOLD, 16));
                sendButton.setBackground(new Color(195, 88, 42));
                sendButton.setForeground(Color.white);
                sendButton.setFocusPainted(false);
                sendButton.setBounds(400, 320, 250, 40);
                sendButton.addActionListener(e -> {
                    try {
                        // opens default mail client
                        Desktop.getDesktop().mail(new URI(
                                "mailto:aibrahimnabil@gmail.com"
                                        + "?subject=Aishatek%20Feedback"
                                        + "&body=Please%20describe%20your%20issue%20or%20suggestion%20here."
                        ));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                contentPanel,
                                "Could not launch email client.\nPlease send to contact@aishatek.com.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
                contentPanel.add(sendButton);
                break;

            default:
                contentPanel.add(new JLabel("Coming soon..."));
        }
        contentPanel.add(content);

        mainPanel.add(contentPanel, name);
        cardLayout.show(mainPanel, name);
    }
    public static int getUserId(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:game_scores.db");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void deleteUserCompletely(int userId) {
        String[] tables = {"level_up_scores", "endless_scores", "challenge_scores", "vs_clock_scores", "users"};
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:game_scores.db");
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);
            for (String table : tables) {
                String sql = table.equals("users") ?
                        "DELETE FROM users WHERE id = " + userId :
                        "DELETE FROM " + table + " WHERE user_id = " + userId;
                stmt.executeUpdate(sql);
            }
            conn.commit();
            System.out.println("User and data deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean renameUser(int userId, String newUsername) {
        String sql = "UPDATE users SET username = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:game_scores.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setInt(2, userId);
            int updated = pstmt.executeUpdate();
            return updated == 1;
        } catch (SQLException e) {
            // handle UNIQUE constraint violation, etc.
            e.printStackTrace();
        }
        return false;
    }

    // near the top of Settings.java:
    private static final EmailSender MAILER = new EmailSender(
            "smtp.gmail.com",
            587,
            "aibrahimnabil@gmail.com",      // SMTP username
            "hnvj pxfs yqwx gymv",          // SMTP app-specific password
            "aibrahimnabil@gmail.com"       // fromAddress
    );


    public static void main(String[] args) {
        ThemeManager.Theme startup =
                PreferencesManager.loadThemeChoice().equalsIgnoreCase("Dark")
                        ? ThemeManager.Theme.DARK
                        : ThemeManager.Theme.LIGHT;

        SwingUtilities.invokeLater(() -> {
            try {
                new Settings((Runnable) new HomePage());                 // build Settings UI
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ThemeManager.applyTheme(startup); // immediately repaint & swap
        });
    }
}
