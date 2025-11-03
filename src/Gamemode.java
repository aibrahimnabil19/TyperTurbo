import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class Gamemode extends JPanel{
    JFrame frame;
    JLabel EndlessClicklbl,levelUpClicklbl,clockClicklbl,challengeClicklbl,highScorelbl;
    JButton Endlessbtn,levelUpbtn,clockbtn,challengebtn,playbtn,homeBtn,sixtySbtn, sixty120btn,sixty180btn,
    one20Sbtn,one20ClickedSbtn,one20180Sbtn,one8Sbtn,one8120Sbtn,one8ClickedSbtn,infolbl;
    JLabel levelUplbl,clocklbl,endlesslbl,challengelbl;
    JComboBox<String> dropdown;
    private JLabel activeClickLabel = null;
    private JPanel secPanel, infoPanel;
    private boolean infoEnabled = false;
    private MusicPlayer musicPlayer = new MusicPlayer();
    private java.util.List<String> gamemodeSongs = List.of(
            "C:\\Users\\User\\Documents\\untitled\\src\\Audio\\AdhesiveWombat - 8 Bit Adventure.mp3"
    );
    Gamemode() throws IOException {
        frame = new JFrame("GAMEMODE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            // OR for classic Metal look:
            // UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        frame.setUndecorated(true);
//        frame.getContentPane().setBackground(new Color(240, 224, 208));
//        frame.setContentPane(new BackgroundPanel("C:\\Users\\User\\Documents\\untitled\\src\\Images\\GAME MODE B EN.jpg"));

        SoundManager.setMusicPlayer(musicPlayer);
        playSound();
        JLabel gameMode = new JLabel("GAMEMODE");
        gameMode.setBounds(164, 74, 600, 100);
        gameMode.setFont(new Font("Century Gothic", Font.BOLD,78));
        gameMode.setForeground(new Color(195,88,41));
        frame.add(gameMode);
        JPanel game1Panel = new JPanel();
        game1Panel.setBounds(83, 100, 22, 55);
        game1Panel.setBackground(new Color(195, 88, 41));
        game1Panel.setLayout(null);
        frame.add(game1Panel);
        JPanel game2Panel = new JPanel();
        game2Panel.setBounds(105, 100, 22, 55);
        game2Panel.setBackground(new Color(214, 146, 101));
        game2Panel.setLayout(null);
        frame.add(game2Panel);
        JPanel game3Panel = new JPanel();
        game3Panel.setBounds(127, 100, 22, 55);
        game3Panel.setBackground(new Color(224, 190, 155));
        game3Panel.setLayout(null);
        frame.add(game3Panel);
        JPanel game4Panel = new JPanel();
        game4Panel.setBounds(149, 100, 22, 55);
        game4Panel.setBackground(new Color(231, 206, 184));
        game4Panel.setLayout(null);
        frame.add(game4Panel);
        JPanel game5Panel = new JPanel();
        game5Panel.setBounds(689, 100, 22, 55);
        game5Panel.setBackground(new Color(195, 88, 41));
        game5Panel.setLayout(null);
        frame.add(game5Panel);
        JPanel game6Panel = new JPanel();
        game6Panel.setBounds(667, 100, 22, 55);
        game6Panel.setBackground(new Color(214, 146, 101));
        game6Panel.setLayout(null);
        frame.add(game6Panel);
        JPanel game7Panel = new JPanel();
        game7Panel.setBounds(645, 100, 22, 55);
        game7Panel.setBackground(new Color(224, 190, 155));
        game7Panel.setLayout(null);
        frame.add(game7Panel);
        JPanel game8Panel = new JPanel();
        game8Panel.setBounds(623, 100, 22, 55);
        game8Panel.setBackground(new Color(231, 206, 184));
        game8Panel.setLayout(null);
        frame.add(game8Panel);

        ImageIcon settingsImg = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\settings.png", 27, 27,true);
        ImageIcon settingsHover = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\settings 2.png", 27, 27,true);
        JButton settingslbl = new JButton(settingsImg);
        settingslbl.setBounds(740,19,27,27);
        settingslbl.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        settingslbl.setContentAreaFilled(false);  // Make the background transparent
        settingslbl.setFocusPainted(false);  // Remove focus border on click
        settingslbl.setBorderPainted(false);  // Remove border paint
        settingslbl.setRolloverIcon(settingsHover);
        settingslbl.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            new Settings(() -> {
                SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
                // for example: re-show the HomePage
                frame.setVisible(true);
            });
            ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
        });

        JPanel homeIcon = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(35, 27);
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // Enable anti-aliasing for smooth edges
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Set color
                g2.setColor(new Color(195, 88, 42));

                // Draw house roof using a triangle (full width 27px)
                Path2D.Double roof = new Path2D.Double();
                roof.moveTo(13.5, 3);   // Top center of the roof
                roof.lineTo(0, 12);     // Bottom left
                roof.lineTo(27, 12);    // Bottom right
                roof.closePath();
                g2.fill(roof);

                // Draw house body with rounded edges
                RoundRectangle2D.Double body = new RoundRectangle2D.Double(4, 12, 19, 13, 4, 4); // Centered body
                g2.fill(body);

                // Add a small centered door
                RoundRectangle2D.Double door = new RoundRectangle2D.Double(11.5, 17, 4, 7, 2, 2); // Centered door
                g2.setColor(Color.WHITE);
                g2.fill(door);
            }
        };
        homeIcon.setSize(homeIcon.getPreferredSize());  // <–– set the actual size
        homeIcon.doLayout();
//        homeIcon.doLayout();
//        ImageIcon rolloverIcon = panelToImageIcon(homeIcon);
        homeBtn = new JButton(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // Enable anti-aliasing for smooth edges
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Set white color
                g2.setColor(Color.WHITE);

                // Draw house roof using a triangle (full width 27px)
                Path2D.Double roof = new Path2D.Double();
                roof.moveTo(13.5, 3);   // Top center of the roof
                roof.lineTo(0, 12);     // Bottom left
                roof.lineTo(27, 12);    // Bottom right
                roof.closePath();
                g2.fill(roof);

                // Draw house body with rounded edges
                RoundRectangle2D.Double body = new RoundRectangle2D.Double(4, 12, 19, 13, 4, 4); // Centered body
                g2.fill(body);

                // Add a small centered door
                RoundRectangle2D.Double door = new RoundRectangle2D.Double(11.5, 17, 4, 7, 2, 2); // Centered door
                g2.setColor(new Color(195, 88, 42)); // Light grey door
                g2.fill(door);
            }
        };
        homeBtn.setLayout(null);
        homeBtn.setOpaque(true);
        homeBtn.setBounds(19,19,35,27);
        homeBtn.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        homeBtn.setContentAreaFilled(false);  // Make the background transparent
        homeBtn.setFocusPainted(false);  // Remove focus border on click
        homeBtn.setBorderPainted(false);  // Remove border paint
        homeBtn.setBackground(new Color(240, 224, 208));
//        ImageIcon rollIcon = panelToImageIcon(homeIcon);
//        homeBtn.setRolloverIcon(rollIcon);
        Color hoverColor = new Color(195, 88, 42);
        Color normalColor = homeBtn.getBackground();

        homeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                homeBtn.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                homeBtn.setBackground(normalColor);
            }
        });
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
                SoundManager.stopBackgroundMusic();
                try {
                    new HomePage();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
                ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
            }
        });
        frame.add(homeBtn);

        int mWidth = ImageUtils.cmToPx(1.13);
        int mLength = ImageUtils.cmToPx(1.13);

        ImageIcon levelUp = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\Level Up.png", 130, 130,true);
        ImageIcon levelUpHoverbtn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\Level Up Hover.png", 130, 130,true);
        int lx = ImageUtils.cmToPx(0.5);
        int ly = ImageUtils.cmToPx(1.76);
        levelUpbtn = new JButton(levelUp);
        levelUpbtn.setBounds(595,195,130,130);
        levelUpbtn.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        levelUpbtn.setContentAreaFilled(false);  // Make the background transparent
        levelUpbtn.setFocusPainted(false);  // Remove focus border on click
        levelUpbtn.setBorderPainted(false);  // Remove border paint
        levelUpbtn.setRolloverIcon(levelUpHoverbtn);
        levelUplbl = new JLabel("Level Up");
        levelUpbtn.setEnabled(false);
        levelUplbl.setFont(new Font("Century Gothic",Font.BOLD,20));
        levelUplbl.setBounds(ImageUtils.lblX(595,130,81), 315, 81, 50);
        levelUplbl.setForeground(Color.lightGray);
        frame.add(levelUplbl);
        JLabel comingSoon = new JLabel(".  .  .");
        comingSoon.setFont(new Font("Century Gothic",Font.BOLD,20));
        comingSoon.setBounds(ImageUtils.lblX(595,130,37), 330, 37, 50);
        comingSoon.setForeground(Color.lightGray);
        frame.add(comingSoon);
        levelUpbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
                hideSecAction();
                hideSecEndless();
                hideSecChallenge();
                // Hide the previously clicked label if it exists
                if (activeClickLabel != null) {
                    activeClickLabel.setVisible(false);
                }

                // Show all other labels
                challengelbl.setVisible(true);
                clocklbl.setVisible(true);
                endlesslbl.setVisible(true);
                levelUplbl.setVisible(false);
                levelUpbtn.setVisible(false);
                clockbtn.setVisible(true);
                Endlessbtn.setVisible(true);
                challengebtn.setVisible(true);

                // Create & display the "clicked" label for Level Up
                ImageIcon levelUpClickbtn = new ImageIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\Level Up Click.png");
                Image img = levelUpClickbtn.getImage();
                Image resizedImg = img.getScaledInstance(130,130,Image.SCALE_SMOOTH);
                ImageIcon resLevelUp = new ImageIcon(resizedImg);

                levelUpClicklbl = new JLabel(resLevelUp);
                levelUpClicklbl.setBounds(595,195,130,130);
                levelUpClicklbl.setBorder(BorderFactory.createEmptyBorder());
                frame.add(levelUpClicklbl);
                frame.revalidate();
                frame.repaint();

                // Update activeClickLabel to the current one
                activeClickLabel = levelUpClicklbl;

            }
        });

        ImageIcon clock = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\Clock.png", 130,130,true);
        ImageIcon clockHoverbtn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\Clock Hover.png", 130,130,true);
        clockbtn = new JButton(clock);
        clockbtn.setBounds(245,195,130,130);
        clockbtn.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        clockbtn.setContentAreaFilled(false);  // Make the background transparent
        clockbtn.setFocusPainted(false);  // Remove focus border on click
        clockbtn.setBorderPainted(false);  // Remove border paint
        clockbtn.setRolloverIcon(clockHoverbtn);
        clocklbl = new JLabel("Clock");
        clocklbl.setFont(new Font("Century Gothic",Font.BOLD,20));
        clocklbl.setBounds(ImageUtils.lblX(245,130,57), 315, 57, 50);
        clocklbl.setForeground(new Color(195,88,41));
        frame.add(clocklbl);
        clockbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
                ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
                hideSecEndless();
                hideSecChallenge();
                // Hide the previously clicked label if it exists
                if (activeClickLabel != null) {
                    activeClickLabel.setVisible(false);
                }
                try {
                    secAction();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                // Show all other labels
                challengelbl.setVisible(true);
                levelUplbl.setVisible(true);
                endlesslbl.setVisible(true);
                clocklbl.setVisible(false);
                clockbtn.setVisible(false);
                levelUpbtn.setVisible(true);
                Endlessbtn.setVisible(true);
                challengebtn.setVisible(true);

                // Create & display the "clicked" label for Level Up
                ImageIcon levelUpClickbtn = new ImageIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\Clock Click.png");
                Image img = levelUpClickbtn.getImage();
                Image resizedImg = img.getScaledInstance(130, 130, Image.SCALE_SMOOTH);
                ImageIcon resLevelUp = new ImageIcon(resizedImg);

                clockClicklbl = new JLabel(resLevelUp);
                clockClicklbl.setBounds(245,195,130,130);
                clockClicklbl.setBorder(BorderFactory.createEmptyBorder());
                frame.add(clockClicklbl);
                frame.revalidate();
                frame.repaint();

                // Update activeClickLabel to the current one
                activeClickLabel = clockClicklbl;

            }
        });

        ImageIcon Endless = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\Endless.png", 130,130,true);
        ImageIcon EndlessHoverbtn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\Endless Hover.png", 130,130,true);
        Endlessbtn = new JButton(Endless);
        Endlessbtn.setBounds(70,195,130,130);
        Endlessbtn.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        Endlessbtn.setContentAreaFilled(false);  // Make the background transparent
        Endlessbtn.setFocusPainted(false);  // Remove focus border on click
        Endlessbtn.setBorderPainted(false);  // Remove border paint
        Endlessbtn.setRolloverIcon(EndlessHoverbtn);
        endlesslbl = new JLabel("Endless");
        endlesslbl.setFont(new Font("Century Gothic",Font.BOLD,20));
        endlesslbl.setBounds(ImageUtils.lblX(70,130,71), 315, 71, 50);
        endlesslbl.setForeground(new Color(195,88,41));
        frame.add(endlesslbl);
        Endlessbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
                ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
                hideSecAction();
                hideSecChallenge();
                // Hide the previously clicked label if it exists
                if (activeClickLabel != null) {
                    activeClickLabel.setVisible(false);
                }

                try {
                    secEndless();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                // Show all other labels
                challengelbl.setVisible(true);
                levelUplbl.setVisible(true);
                clocklbl.setVisible(true);
                endlesslbl.setVisible(false);
                Endlessbtn.setVisible(false);
                levelUpbtn.setVisible(true);
                clockbtn.setVisible(true);
                challengebtn.setVisible(true);

                // Create & display the "clicked" label for Level Up
                ImageIcon levelUpClickbtn = new ImageIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\Endless Click.png");
                Image img = levelUpClickbtn.getImage();
                Image resizedImg = img.getScaledInstance(130, 130, Image.SCALE_SMOOTH);
                ImageIcon resLevelUp = new ImageIcon(resizedImg);

                EndlessClicklbl = new JLabel(resLevelUp);
                EndlessClicklbl.setBounds(70, 195, 130, 130);
                EndlessClicklbl.setBorder(BorderFactory.createEmptyBorder());
                frame.add(EndlessClicklbl);
                frame.revalidate();
                frame.repaint();

                // Update activeClickLabel to the current one
                activeClickLabel = EndlessClicklbl;
            }
        });

        ImageIcon challenge = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\challenge.png", 130,130,true);
        ImageIcon challengeHoverbtn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\challenge Hover.png", 130,130,true);
        challengebtn = new JButton(challenge);
        challengebtn.setBounds(420,195,130,130);
        challengebtn.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        challengebtn.setContentAreaFilled(false);  // Make the background transparent
        challengebtn.setFocusPainted(false);  // Remove focus border on click
        challengebtn.setBorderPainted(false);  // Remove border paint
        challengebtn.setRolloverIcon(challengeHoverbtn);
        challengelbl = new JLabel("Challenge");
        challengelbl.setFont(new Font("Century Gothic",Font.BOLD,20));
        challengelbl.setBounds(ImageUtils.lblX(420,130,101), 315, 101, 50);
        challengelbl.setForeground(new Color(195,88,41));
        frame.add(challengelbl);
        challengebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
                ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
                hideSecAction();
                hideSecEndless();
                // Hide the previously clicked label if it exists
                if (activeClickLabel != null) {
                    activeClickLabel.setVisible(false);
                }
                try {
                    secChallenge();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                // Show all other labels
                endlesslbl.setVisible(true);
                levelUplbl.setVisible(true);
                clocklbl.setVisible(true);
                challengelbl.setVisible(false);
                challengebtn.setVisible(false);
                levelUpbtn.setVisible(true);
                clockbtn.setVisible(true);
                Endlessbtn.setVisible(true);

                // Create & display the "clicked" label for Level Up
                ImageIcon levelUpClickbtn = new ImageIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\challenge Click.png");
                Image img = levelUpClickbtn.getImage();
                Image resizedImg = img.getScaledInstance(130, 130, Image.SCALE_SMOOTH);
                ImageIcon resLevelUp = new ImageIcon(resizedImg);

                challengeClicklbl = new JLabel(resLevelUp);
                challengeClicklbl.setBounds(420,195,130,130);
                challengeClicklbl.setBorder(BorderFactory.createEmptyBorder());
                frame.add(challengeClicklbl);
                frame.revalidate();
                frame.repaint();

                // Update activeClickLabel to the current one
                activeClickLabel = challengeClicklbl;
            }
        });

        frame.add(settingslbl);
//        frame.add(infolbl);
        frame.add(levelUpbtn);
        frame.add(clockbtn);
        frame.add(Endlessbtn);
        frame.add(challengebtn);
        frame.setVisible(true);
    }

    private void secAction() throws IOException {
        ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
        // Set layout to null for absolute positioning
        frame.setLayout(null);

        // Create secPanel and set its layout
        secPanel = new JPanel();
        secPanel.setLayout(null);
        secPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight()); // Set to match frame size
        secPanel.setOpaque(false); // Make background transparent

        // Create images
        ImageIcon sixtyClickedbtn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\60s.png", 65, 29,true);
        ImageIcon sixty120 = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\60s-1.png", 53, 24,true);
        ImageIcon sixty180 = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\60s copie 2.png", 42, 19,true);
        ImageIcon one20S = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\120s copie 3.png", 53, 24,true);
        ImageIcon one20Clickedbtn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\120s.png", 65, 29,true);
        ImageIcon one20180btn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\120s copie 3.png", 53, 24,true);
        ImageIcon one8S = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\180s-2.png", 42, 19,true);
        ImageIcon one8120btn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\180s-1.png", 53, 24,true);
        ImageIcon one8Clickedbtn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\180s.png", 65, 29,true);

        // Create buttons
        sixtySbtn = createButton(sixtyClickedbtn.getImage(), 65, 29, ImageUtils.lblX(245, 130, 65), 330);
        sixty120btn = createButton(sixty120.getImage(), 53, 24, ImageUtils.lblX(245, 130, 53), 330);
        sixty180btn = createButton(sixty180.getImage(), 42, 19, ImageUtils.lblX(245, 130, 42), 330);
        one20Sbtn = createButton(one20S.getImage(), 53, 24, ImageUtils.lblX(245, 130, 53), 365);
        one20ClickedSbtn = createButton(one20Clickedbtn.getImage(), 65, 29, ImageUtils.lblX(245, 130, 65), 360);
        one20180Sbtn = createButton(one20180btn.getImage(), 53, 24, ImageUtils.lblX(245, 130, 53), 355);
        one8Sbtn = createButton(one8S.getImage(), 42, 19, ImageUtils.lblX(245, 130, 42), 395);
        one8120Sbtn = createButton(one8120btn.getImage(), 53, 24, ImageUtils.lblX(245, 130, 53), 395);
        one8ClickedSbtn = createButton(one8Clickedbtn.getImage(), 65, 29, ImageUtils.lblX(245, 130, 65), 385);

        // Set initial visibility
        sixty120btn.setVisible(false);
        one20ClickedSbtn.setVisible(false);
        one20180Sbtn.setVisible(false);
        one8120Sbtn.setVisible(false);
        one8ClickedSbtn.setVisible(false);

        one20Sbtn.addActionListener(e -> {
            // Hide all buttons
            sixtySbtn.setVisible(false);
            sixty180btn.setVisible(false);
            one20Sbtn.setVisible(false);
            one20180Sbtn.setVisible(false);
            one8Sbtn.setVisible(false);
            one8ClickedSbtn.setVisible(false);

            // Show only the required buttons
            sixty120btn.setVisible(true);
            one20ClickedSbtn.setVisible(true);
            one8120Sbtn.setVisible(true);
            refreshHighScore();
        });

        one8Sbtn.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            // Hide all buttons
            sixtySbtn.setVisible(false);
            sixty120btn.setVisible(false);
            one20Sbtn.setVisible(false);
            one20ClickedSbtn.setVisible(false);
            one8Sbtn.setVisible(false);
            one8120Sbtn.setVisible(false);

            // Show only the required buttons
            sixty180btn.setVisible(true);
            one20180Sbtn.setVisible(true);
            one8ClickedSbtn.setVisible(true);
            refreshHighScore();
        });
        sixty120btn.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            // Hide all buttons
            sixty120btn.setVisible(false);
            sixty180btn.setVisible(false);
            one20ClickedSbtn.setVisible(false);
            one20180Sbtn.setVisible(false);
            one8120Sbtn.setVisible(false);
            one8ClickedSbtn.setVisible(false);

            // Show only the required buttons
            sixtySbtn.setVisible(true);
            one20Sbtn.setVisible(true);
            one8Sbtn.setVisible(true);
            refreshHighScore();
        });
        one8120Sbtn.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            // Hide all buttons
            sixtySbtn.setVisible(false);
            sixty120btn.setVisible(false);
            one20Sbtn.setVisible(false);
            one20ClickedSbtn.setVisible(false);
            one8120Sbtn.setVisible(false);
            one8Sbtn.setVisible(false);

            // Show only the required buttons
            sixty180btn.setVisible(true);
            one20180Sbtn.setVisible(true);
            one8ClickedSbtn.setVisible(true);
            refreshHighScore();
        });
        one20180Sbtn.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            // Hide all buttons
            sixtySbtn.setVisible(false);
            sixty180btn.setVisible(false);
            one20Sbtn.setVisible(false);
            one20180Sbtn.setVisible(false);
            one8Sbtn.setVisible(false);
            one8ClickedSbtn.setVisible(false);

            // Show only the required buttons
            sixty120btn.setVisible(true);
            one20ClickedSbtn.setVisible(true);
            one8120Sbtn.setVisible(true);
            refreshHighScore();
        });
        sixty180btn.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            // Hide all buttons
            sixty120btn.setVisible(false);
            sixty180btn.setVisible(false);
            one20ClickedSbtn.setVisible(false);
            one20180Sbtn.setVisible(false);
            one8120Sbtn.setVisible(false);
            one8ClickedSbtn.setVisible(false);

            // Show only the required buttons
            sixtySbtn.setVisible(true);
            one20Sbtn.setVisible(true);
            one8Sbtn.setVisible(true);
            refreshHighScore();
        });

        JLabel difficulty1 = new JLabel("Select your difficulty:");
        difficulty1.setFont(new Font("Century Gothic", Font.BOLD,15));
        difficulty1.setForeground(new Color(195, 88, 41));
        difficulty1.setBounds(10,465,250,30);

        String[] options = {"Easy", "Intermediate", "Hard", "Pro"};
        dropdown = new JComboBox<>(options);
        dropdown.setBounds(180, 465, 150, 30);
        dropdown.setFont(new Font("Century Gothic", Font.PLAIN, 14));
//        dropdown.setForeground(new Color(195, 88, 42));
//        dropdown.setBackground(new Color(240, 224, 208));

        dropdown.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("\u25BC"); // Unicode for downward arrow
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                button.setOpaque(true);
                button.setBackground(new Color(195, 88, 42));
//                button.setForeground(Color.WHITE);
                return button;
            }
        });
        dropdown.addActionListener(e -> refreshHighScore());

        ImageIcon play = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\play.jpg", 260, 65,true);
        ImageIcon playHoverbtn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\play Hover.jpg", 260, 65,true);
        playbtn = new JButton(play);
        playbtn.setBounds(530, 448, 260, 65);
        playbtn.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        playbtn.setContentAreaFilled(false);  // Make the background transparent
        playbtn.setFocusPainted(false);  // Remove focus border on click
        playbtn.setBorderPainted(false);  // Remove border paint
        playbtn.setRolloverIcon(playHoverbtn);
//        HomePage homePage = new HomePage();
        String username = HomePage.getSelectedUsername();
        String difficulty = (String) dropdown.getSelectedItem(); // Get selected difficulty

        // Get time based on selected button
        String timeLimit;
        if (sixtySbtn.isVisible()) {
            timeLimit = "60";  // 60 seconds
        } else if (one20ClickedSbtn.isVisible()) {
            timeLimit = "120"; // 120 seconds
        } else if (one8ClickedSbtn.isVisible()||one8ClickedSbtn.isVisible()||one8Sbtn.isVisible()) {
            timeLimit = "180"; // 180 seconds
        } else {
            timeLimit = "";
        }
        playbtn.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            SoundManager.stopBackgroundMusic();
            try {
                vsClock.openGameFrame(difficulty, timeLimit,username);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.dispose();
            ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
        });
//        String result = getClockHighScore(username, difficulty, timeLimit);

        highScorelbl = new JLabel();
        highScorelbl.setBounds(350, 465, 150, 30);
        highScorelbl.setFont(new Font("Century Gothic",Font.BOLD,15));
        highScorelbl.setForeground(new Color(195, 88, 41));

        secPanel.add(sixtySbtn);
        secPanel.add(sixty120btn);
        secPanel.add(sixty180btn);
        secPanel.add(one20Sbtn);
        secPanel.add(one20ClickedSbtn);
        secPanel.add(one20180Sbtn);
        secPanel.add(one8Sbtn);
        secPanel.add(one8120Sbtn);
        secPanel.add(one8ClickedSbtn);
        secPanel.add(difficulty1);
        secPanel.add(dropdown);
        secPanel.add(playbtn);
        secPanel.add(highScorelbl);

        refreshHighScore();
        // Add secPanel to frame
        frame.add(secPanel);
    }

    private void secEndless() throws IOException {
        // Set layout to null for absolute positioning
        frame.setLayout(null);

        // Create secPanel and set its layout
        secPanel = new JPanel();
        secPanel.setLayout(null);
        secPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight()); // Set to match frame size
        secPanel.setOpaque(false); // Make background transparent

        ImageIcon play = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\play.jpg", 260, 65,true);
        ImageIcon playHoverbtn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\play Hover.jpg", 260, 65,true);
        playbtn = new JButton(play);
        playbtn.setBounds(530, 448, 260, 65);
        playbtn.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        playbtn.setContentAreaFilled(false);  // Make the background transparent
        playbtn.setFocusPainted(false);  // Remove focus border on click
        playbtn.setBorderPainted(false);  // Remove border paint
        playbtn.setRolloverIcon(playHoverbtn);
        String username = HomePage.getSelectedUsername();
        String result = getEndlessHighScore(username);
        JLabel highScorelbl = new JLabel(result);
        highScorelbl.setBounds(50, 465, 150, 30);
        highScorelbl.setFont(new Font("Century Gothic",Font.BOLD,15));
        highScorelbl.setForeground(new Color(195, 88, 41));
        playbtn.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            SoundManager.stopBackgroundMusic();
            try {
                new Endless(username);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.dispose();
            ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
        });

        secPanel.add(playbtn);
        secPanel.add(highScorelbl);

        // Add secPanel to frame
        frame.add(secPanel);
    }

    private void secChallenge() throws IOException {
        // Set layout to null for absolute positioning
        frame.setLayout(null);

        // Create secPanel and set its layout
        secPanel = new JPanel();
        secPanel.setLayout(null);
        secPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight()); // Set to match frame size
        secPanel.setOpaque(false); // Make background transparent

        ImageIcon play = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\play.jpg", 260, 65,true);
        ImageIcon playHoverbtn = ImageUtils.createScaledIcon("C:\\Users\\User\\Documents\\untitled\\src\\Images\\play Hover.jpg", 260, 65,true);
        playbtn = new JButton(play);
        playbtn.setBounds(530, 448, 260, 65);
        playbtn.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        playbtn.setContentAreaFilled(false);  // Make the background transparent
        playbtn.setFocusPainted(false);  // Remove focus border on click
        playbtn.setBorderPainted(false);  // Remove border paint
        playbtn.setRolloverIcon(playHoverbtn);
        String username = HomePage.getSelectedUsername(); // Incase there is a need for the username in the future
//        String result = getEndlessHighScore(username);
//        JLabel highScorelbl = new JLabel(result);
//        highScorelbl.setBounds(50, 465, 150, 30);
//        highScorelbl.setFont(new Font("Century Gothic",Font.BOLD,15));
//        highScorelbl.setForeground(new Color(195, 88, 42));
        playbtn.addActionListener(e -> {
            SoundManager.playEffect("C:\\Users\\User\\Documents\\untitled\\src\\Audio\\Click.WAV");
            SoundManager.stopBackgroundMusic();
            new ChallengeMode();
            frame.dispose();
            ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
        });

        secPanel.add(playbtn);
//        secPanel.add(highScorelbl);

        // Add secPanel to frame
        frame.add(secPanel);
    }

    // Utility function to create a JButton
    private JButton createButton(Image img, int width, int height, int x, int y) {
        JButton button = new JButton(new ImageIcon(img));
        button.setBounds(x, y, width, height);
        button.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        button.setContentAreaFilled(false); // Make the background transparent
        button.setFocusPainted(false); // Remove focus border on click
        button.setBorderPainted(false); // Remove border paint
        return button;
    }
    private void hideSecAction() {
        if (secPanel != null) {
            secPanel.setVisible(false);
        }
        if (playbtn != null) {
            playbtn.setVisible(false);
        }
        frame.revalidate();
        frame.repaint();
    }
    private void hideSecEndless() {
        if (secPanel != null) {
            secPanel.setVisible(false);
        }
        if (playbtn != null) {
            playbtn.setVisible(false);
        }
        frame.revalidate();
        frame.repaint();
    }

    private void hideSecChallenge() {
        if (secPanel != null) {
            secPanel.setVisible(false);
        }
        if (playbtn != null) {
            playbtn.setVisible(false);
        }
        frame.revalidate();
        frame.repaint();
    }

    private ImageIcon panelToImageIcon(JPanel panel) {
        int width = panel.getWidth();
        int height = panel.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        panel.printAll(g2);
        g2.dispose();
        return new ImageIcon(image);
    }
    public static String getClockHighScore(String username, String difficulty, String timeLimit) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:game_scores.db")) {
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
                return "High Score: " + score;
            } else {
                return "High Score: 0";
            }
        } catch (SQLException ex) {
            return "Error retrieving score: " + ex.getMessage();
        }
    }
    public static String getEndlessHighScore(String username) {
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

    private void refreshHighScore() {
        String username   = HomePage.getSelectedUsername();
        String difficulty = (String) dropdown.getSelectedItem();

        String timeLimit;
        if      (sixtySbtn.isVisible())     timeLimit = "60";
        else if (one20ClickedSbtn.isVisible()) timeLimit = "120";
        else                                 timeLimit = "180";

        String result = getClockHighScore(username, difficulty, timeLimit);
        highScorelbl.setText(result);
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
                new Gamemode();                   // build the UI
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ThemeManager.applyTheme(startup); // now flip L&F + do your scan
        });
    }
}