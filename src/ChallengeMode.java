import javax.swing.*;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Improved ChallengeMode:
 * - CardLayout: "menu" (buttons) and "game" (game UI)
 * - Back button: if on menu -> new Gamemode(); if in game -> stop game and go to menu
 * - Improved buttons UI with search/filter and hover styling
 * - Cancels/stops active timers and falling panel when leaving a game
 */
public class ChallengeMode {
    private final JFrame frame = new JFrame();
    private final JPanel rootPanel = new JPanel();         // card layout root
    private final JPanel menuPanel = new JPanel(new BorderLayout());
    private final JPanel gameContainer = new JPanel(new BorderLayout()); // holds current game UI
    private final CardLayout cardLayout = new CardLayout();
    private final Random random = new Random();

    // For cleanup when leaving a game
    private final java.util.List<Timer> activeTimers = new ArrayList<>();
    private FallingPanel currentFallingPanel = null;

    private final List<String> sampleSentence = List.of(
            "The quick brown fox jumps over the lazy dog.",
            "Sphinx of black quartz, judge my vow.",
            "Pack my box with five dozen liquor jugs.",
            "How vexingly quick daft zebras jump!",
            "Two driven jocks help fax my big quiz.",
            "Crazy Frederick bought many very exquisite opal jewels.",
            "We promptly judged antique ivory buckles for the next prize.",
            "Amazingly few discotheques provide jukeboxes.",
            "Quick zephyrs blow, vexing daft Jim.",
            "Five quacking zephyrs jolt my wax bed.",
            "The five boxing wizards jump quickly.",
            "Bright vixens jump; dozy fowl quack.",
            "Heavy boxes perform quick waltzes and jigs.",
            "Just keep examining every low bid quoted for zinc.",
            "Typing quickly is a useful everyday skill.",
            "Practice makes progress, not perfection.",
            "Always look ahead and prepare for surprises.",
            "Small steps every day lead to big changes.",
            "Sunlight danced across the rippling water.",
            "She sells sea shells by the seashore.",
            "Many happy returns to you on your special day.",
            "A soft breeze carried the scent of jasmine.",
            "The cat chased shadows across the hallway.",
            "Autumn leaves fell silently in the courtyard.",
            "Learning to type well saves time and frustration.",
            "The tiny bird perched on the windowsill and sang.",
            "The baker kneaded dough until it was smooth and elastic.",
            "Clouds drift lazily over the mountain peaks.",
            "He read the old map by candlelight in the attic.",
            "Move quickly when opportunity knocks upon your door.",
            "She opened the book and lost herself for hours.",
            "A gentle rain began to patter on the roof.",
            "The orchestra tuned their instruments before the concert.",
            "Bright stars lit the midnight sky over the city.",
            "An owl hooted softly from a distant tree.",
            "He took careful notes during the long lecture.",
            "The river carved its path through the ancient valley."
    );
    private final List<String> wordList = List.of(
            "apple","banana","cherry","date","elderberry","fig","grape","honeydew");
    private final List<String> triviaQuestions = List.of(
            "What is the capital of France?;Paris",
            "What planet is known as the Red Planet?;Mars",
            "How many continents are there?;7",
            "Who painted the Mona Lisa?;Leonardo da Vinci",
            "What is the largest ocean on Earth?;Pacific Ocean",
            "What is the chemical symbol for water?;H2O",
            "What gas do plants absorb from the atmosphere?;Carbon dioxide",
            "Which planet has a ring system?;Saturn",
            "Who wrote 'Romeo and Juliet'?;William Shakespeare",
            "What is the square root of 64?;8",
            "In which country are the pyramids of Giza located?;Egypt",
            "What is the largest mammal?;Blue whale",
            "What is the freezing point of water in Celsius?;0",
            "Who was the first person to walk on the Moon?;Neil Armstrong",
            "What is the capital of Japan?;Tokyo",
            "What is the smallest prime number?;2",
            "How many weeks are in a year?;52",
            "What currency is used in the United Kingdom?;Pound",
            "What language is primarily spoken in Brazil?;Portuguese",
            "Which element has the atomic number 1?;Hydrogen",
            "What is the fastest land animal?;Cheetah",
            "In computing, what does 'CPU' stand for?;Central Processing Unit",
            "What is the largest planet in our solar system?;Jupiter",
            "Which ocean is on the U.S. west coast?;Pacific Ocean",
            "What is the tallest mountain in the world?;Mount Everest",
            "Who invented the telephone?;Alexander Graham Bell",
            "What is the capital city of Canada?;Ottawa",
            "Which organ pumps blood around the body?;Heart",
            "What is the main language spoken in Spain?;Spanish",
            "How many sides does a hexagon have?;6",
            "What is the name of the scientist who proposed the theory of relativity?;Albert Einstein",
            "Which metal is liquid at room temperature?;Mercury",
            "What year did World War II end?;1945",
            "Which planet is closest to the Sun?;Mercury",
            "What is the process by which plants make food using sunlight?;Photosynthesis",
            "Who wrote '1984' and 'Animal Farm'?;George Orwell",
            "What is the capital of Australia?;Canberra",
            "Which blood type is known as the universal donor?;O negative",
            "What is the currency of Japan?;Yen",
            "Who discovered penicillin?;Alexander Fleming",
            "Which continent is the Sahara Desert located on?;Africa",
            "How many players on a soccer team (on the field per side)?;11",
            "What is 12 × 12?;144",
            "Which planet is known for its large red spot?;Jupiter",
            "What is the largest internal organ in the human body?;Liver",
            "What device do we use to look at stars?;Telescope",
            "What is the boiling point of water at sea level in Celsius?;100",
            "Which famous scientist is known for the laws of motion?;Isaac Newton",
            "What is the capital of Italy?;Rome",
            "Which element is represented by the symbol 'O'?;Oxygen",
            "How many hearts does an octopus have?;3",
            "Which city hosted the 2016 Summer Olympics?;Rio de Janeiro",
            "What is the largest country by area?;Russia",
            "What is the chemical formula for table salt?;NaCl",
            "Which instrument has 88 keys?;Piano",
            "What do bees collect and use to make honey?;Nectar",
            "Who is known as the 'Father of Computers'?;Charles Babbage",
            "What is the primary gas in Earth's atmosphere?;Nitrogen",
            "Which African country has the largest population?;Nigeria",
            "What is the capital of Germany?;Berlin",
            "How many bones are in the adult human body?;206",
            "Which famous ship sank in 1912 after hitting an iceberg?;Titanic",
            "Which mountain range runs along the west coast of South America?;Andes",
            "How many degrees are in a right angle?;90",
            "What is the study of living organisms called?;Biology",
            "What does 'HTTP' stand for?;HyperText Transfer Protocol",
            "Which planet is nicknamed Earth's 'twin' because of similar size?;Venus",
            "Which element is required for combustion to occur?;Oxygen",
            "Which sport uses a shuttlecock?;Badminton",
            "What is the tallest building in the world as of 2025?;Burj Khalifa",
            "Which U.S. state is nicknamed the 'Sunshine State'?;Florida"
    );
    private final Map<String, String> morseMap = Map.ofEntries(
            Map.entry("A", ".-"), Map.entry("B", "-..."), Map.entry("C", "-.-."), Map.entry("D", "-.."),
            Map.entry("E", "."), Map.entry("F", "..-."), Map.entry("G", "--."), Map.entry("H", "...."),
            Map.entry("I", ".."), Map.entry("J", ".---"), Map.entry("K", "-.-"), Map.entry("L", ".-.."),
            Map.entry("M", "--"), Map.entry("N", "-."), Map.entry("O", "---"), Map.entry("P", ".--."),
            Map.entry("Q", "--.-"), Map.entry("R", ".-."), Map.entry("S", "..."), Map.entry("T", "-"),
            Map.entry("U", "..-"), Map.entry("V", "...-"), Map.entry("W", ".--"), Map.entry("X", "-..-"),
            Map.entry("Y", "-.--"), Map.entry("Z", "--..")
    );

    // mode names and actions (keeps your functions)
    private final List<String> modeNames = List.of(
            "Speed Demon", "Typo Trouble", "Word Bomb", "Disappearing Text", "Scramble Sprint",
            "Ghost Keys", "Falling Words", "Reversal", "One-Hand Challenge", "Blind Type",
            "AutoCorrect Chaos", "Typing Trivia", "Morse Mayhem", "Emoji Words", "Forbidden Letter"
    );

    public ChallengeMode() {
        frame.setTitle("Typing Challenge Mode");
        frame.setSize(900, 620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Root card layout
        rootPanel.setLayout(cardLayout);
        rootPanel.add(menuPanel, "menu");
        rootPanel.add(gameContainer, "game");

        // Top back button
        RoundedButton backBtn = new RoundedButton("← Back", false);
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.addActionListener(e -> {
            // if currently showing menu -> open Gamemode (original behavior)
            // otherwise stop the current game and return to menu
            if (isShowingMenu()) {
                try {
                    new Gamemode();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
                ThemeManager.applyTheme(ThemeManager.getCurrentTheme());
            } else {
                stopCurrentGameAndShowMenu();
            }
        });

        // Header panel (back + title)
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        header.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Challenge Modes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title, BorderLayout.CENTER);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(header, BorderLayout.NORTH);
        frame.getContentPane().add(rootPanel, BorderLayout.CENTER);

        buildMenu(); // build the menuPanel (buttons + search)
        showMenu();

        frame.setVisible(true);
    }

    private boolean isShowingMenu() {
        // CardLayout doesn't have a getter for current card, so we track visually by checking which is visible
        for (Component comp : rootPanel.getComponents()) {
            if (comp.isVisible() && comp == menuPanel) return true;
        }
        return false;
    }

    private void showMenu() {
        stopCurrentGame(); // be safe
        cardLayout.show(rootPanel, "menu");
        frame.setTitle("Typing Challenge Mode");
    }

    private void showGame(String modeTitle) {
        cardLayout.show(rootPanel, "game");
        frame.setTitle("Typing Challenge Mode - " + modeTitle);
    }

    private void stopCurrentGameAndShowMenu() {
        stopCurrentGame();
        gameContainer.removeAll();
        gameContainer.revalidate();
        gameContainer.repaint();
        showMenu();
    }

    // Stop all tracked timers and falling panel
    private void stopCurrentGame() {
        // stop timers
        for (Timer t : new ArrayList<>(activeTimers)) {
            if (t != null) {
                t.stop();
            }
        }
        activeTimers.clear();

        // stop falling words if active
        if (currentFallingPanel != null) {
            currentFallingPanel.stop();
            currentFallingPanel = null;
        }
    }

    private void buildMenu() {
        // Search bar on top of menu
        JPanel topSearch = new JPanel(new BorderLayout(8, 8));
        topSearch.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // --- Replace your existing searchField creation with this ---
        JTextField searchField = new JTextField();
        searchField.setColumns(18);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

// keep whatever default background & foreground the L&F gave you
        Color bg = searchField.getBackground();
        Color fg = searchField.getForeground();

// ensure the field is actually painted and editable
        searchField.setOpaque(true);
        searchField.setBackground(bg);
        searchField.setForeground(fg);
        searchField.setEnabled(true);
        searchField.setEditable(true);

// make caret visible and selection readable
        searchField.setCaretColor(fg);
        searchField.setSelectionColor(new Color(214, 146, 101));        // nice accent
        searchField.setSelectedTextColor(getContrastColor(searchField.getSelectionColor(), bg));

        topSearch.add(searchField, BorderLayout.WEST);

        // Container for buttons (grid) inside a scroll pane
            JPanel buttonGrid = new JPanel(new WrapLayout(FlowLayout.CENTER, 16, 16));
        buttonGrid.setBorder(BorderFactory.createEmptyBorder(12, 20, 20, 20));
        buttonGrid.setBackground(Color.WHITE);

        // store buttons so we can filter them
        java.util.List<JButton> modeButtons = new ArrayList<>();

        for (int i = 0; i < modeNames.size(); i++) {
            final int idx = i;
            String label = modeNames.get(i);
            JButton b = createModeButton(label, () -> {
                // Before launching new game: cleanup, then run
                stopCurrentGame();
                gameContainer.removeAll();
                // run the mode builder which will add UI into gameContainer
                switch (idx) {
                    case 0 -> speedDemon(gameContainer);
                    case 1 -> typoTrouble(gameContainer);
                    case 2 -> wordBomb(gameContainer);
                    case 3 -> disappearingText(gameContainer);
                    case 4 -> scrambleSprint(gameContainer);
                    case 5 -> ghostKeys(gameContainer);
                    case 6 -> fallingWords(gameContainer);
                    case 7 -> reversal(gameContainer);
                    case 8 -> oneHandChallenge(gameContainer);
                    case 9 -> blindType(gameContainer);
                    case 10 -> autoCorrectChaos(gameContainer);
                    case 11 -> typingTrivia(gameContainer);
                    case 12 -> morseMayhem(gameContainer);
                    case 13 -> emojiWords(gameContainer);
                    case 14 -> forbiddenLetter(gameContainer);
                    default -> {}
                }
                showGame(label);
            });
            modeButtons.add(b);
            buttonGrid.add(b);
        }

        // search/filter logic
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String q = searchField.getText().trim().toLowerCase();
                buttonGrid.removeAll();
                for (JButton btn : modeButtons) {
                    if (q.isEmpty() || btn.getText().toLowerCase().contains(q)) {
                        buttonGrid.add(btn);
                    }
                }
                buttonGrid.revalidate();
                buttonGrid.repaint();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        // put in scroll pane
        JScrollPane scroll = new JScrollPane(buttonGrid,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setViewportBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        customizeScrollBar(scroll);

        menuPanel.add(topSearch, BorderLayout.NORTH);
        menuPanel.add(scroll, BorderLayout.CENTER);
    }

    // Helper to create a styled button with hover behavior
    private RoundedButton createModeButton(String text, Runnable onClick) {
        RoundedButton btn = new RoundedButton(text,false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(220, 80));
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(214, 146, 101));
        btn.setBorder(new RoundedBorder(12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setToolTipText("Play: " + text);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> onClick.run());

        // hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(btn.getBackground().brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(214, 146, 101));
            }
        });
        return btn;
    }

    private void customizeScrollBar(JScrollPane scrollPane) {
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setPreferredSize(new Dimension(12, Integer.MAX_VALUE));
        vBar.setUnitIncrement(16);
        vBar.setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(255, 255, 255);
                trackColor = new Color(214, 146, 101);
            }
            @Override protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int reducedHeight = Math.max(30, thumbBounds.height / 2);
                int x = thumbBounds.x;
                int y = thumbBounds.y + (thumbBounds.height - reducedHeight) / 2;
                int width = thumbBounds.width;
                int height = reducedHeight;
                g2.setColor(thumbColor);
                g2.fillRoundRect(x, y, width, height, 0, 0);
                g2.dispose();
            }
            @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }
        });
    }

    // -------------------------
    // Game mode implementations
    // Each now accepts a container panel where we'll add UI
    // and registers timers into activeTimers for cleanup.
    // -------------------------

    private void speedDemon(JPanel container) {
        container.removeAll();
        container.setLayout(null);

        String text = getRandomSampleSentence();
        JLabel label = new JLabel(text);
        label.setFont(new Font("Century Gothic", Font.BOLD, 30));
        label.setForeground(new Color(195/255f, 88/255f, 42/255f, 1.0f));
        label.setBounds(60, 80, 800, 54);

        JTextField input = new JTextField();
        input.setBounds(60, 160, 760, 36);

        JLabel timerLabel = new JLabel("Time: 0.0s");
        timerLabel.setBounds(60, 220, 200, 30);

        container.add(label);
        container.add(input);
        container.add(timerLabel);

        final long start = System.currentTimeMillis();
        Timer fadeTimer = new Timer(100, e -> {
            double sec = (System.currentTimeMillis() - start) / 1000.0;
            float alpha = Math.max(0, 1f - (float) sec / 10);
            label.setForeground(new Color(195/255f, 88/255f, 42/255f, alpha));
            timerLabel.setText(String.format("Time: %.1fs", sec));
        });
        fadeTimer.start();
        activeTimers.add(fadeTimer);

        input.addActionListener(e -> {
            fadeTimer.stop();
            activeTimers.remove(fadeTimer);
            long end = System.currentTimeMillis();
            input.setEnabled(false);
            String result = input.getText().equals(text) ? "Success" : "Failed";
            JOptionPane.showMessageDialog(container, result + " in " + ((end - start) / 1000.0) + "s");
        });

        container.revalidate();
        container.repaint();
    }

    private void typoTrouble(JPanel container) {
        container.removeAll();
        container.setLayout(null);

        String original = getRandomSampleSentence();
        String typo = original.replaceAll("o", "0").replaceAll("e", "3");

        JLabel label = new JLabel(typo);
        label.setFont(new Font("Century Gothic", Font.BOLD, 30));
        label.setForeground(new Color(195/255f, 88/255f, 42/255f, 1.0f));
        label.setBounds(60, 80, 800, 54);

        JTextField input = new JTextField();
        input.setBounds(60, 160, 760, 36);
        input.setBorder(null);

        container.add(label);
        container.add(input);

        input.addActionListener(e -> {
            JOptionPane.showMessageDialog(container,
                    input.getText().equals(original) ? "Correct!" : "Try Again.");
            input.setText("");
        });

        container.revalidate();
        container.repaint();
    }

    private void wordBomb(JPanel container) {
        container.removeAll();
        container.setLayout(null);

        JLabel wordLabel = new JLabel();
        wordLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
        wordLabel.setForeground(new Color(0.0f, 0.0f, 1.0f, 1.0f));
        wordLabel.setBounds(60, 80, 800, 54);

        JTextField input = new JTextField();
        input.setBounds(60, 160, 760, 36);
        input.setBorder(null);

        JLabel timerLabel = new JLabel("Time: 0.0s");
        timerLabel.setBounds(60, 220, 318, 30);

        container.add(wordLabel);
        container.add(input);
        container.add(timerLabel);

        runBombRound(wordLabel, timerLabel, input, 3000, container);

        container.revalidate();
        container.repaint();
    }

    private void runBombRound(JLabel wordLabel, JLabel timerLabel, JTextField input, int timeLeft, JPanel container) {
        String word = wordList.get(random.nextInt(wordList.size()));
        wordLabel.setText(word);
        input.setText("");
        input.requestFocus();

        Timer bombTimer = new Timer(100, null);
        activeTimers.add(bombTimer);

        bombTimer.addActionListener(new ActionListener() {
            int time = timeLeft;
            @Override public void actionPerformed(ActionEvent e) {
                time -= 100;
                timerLabel.setText(String.format("Time: %.1fs", time / 1000.0));
                if (time <= 0) {
                    bombTimer.stop();
                    activeTimers.remove(bombTimer);
                    JOptionPane.showMessageDialog(container, "Time's up! Game Over.");
                }
            }
        });
        bombTimer.start();

        // Remove previous action listeners to avoid stacking if reused
        for (ActionListener al : input.getActionListeners()) input.removeActionListener(al);
        input.addActionListener(e -> {
            bombTimer.stop();
            activeTimers.remove(bombTimer);
            if (input.getText().equals(word)) {
                int nextTime = Math.max(300, timeLeft - 200);
                runBombRound(wordLabel, timerLabel, input, nextTime, container);
            } else {
                JOptionPane.showMessageDialog(container, "Wrong! Game Over.");
            }
        });
    }

    private void disappearingText(JPanel container) {
        container.removeAll();
        container.setLayout(null);

        JLabel label = new JLabel(getRandomSampleSentence());
        label.setFont(new Font("Century Gothic", Font.BOLD, 30));
        label.setForeground(new Color(0.0f, 0.0f, 1.0f, 1.0f));
        label.setBounds(60, 80, 800, 54);

        JTextField input = new JTextField();
        input.setBounds(60, 160, 760, 36);
        input.setBorder(null);

        container.add(label);
        container.add(input);

        Timer t = new Timer(3000, e -> label.setText(""));
        t.setRepeats(false);
        t.start();
        activeTimers.add(t);

        input.addActionListener(e -> JOptionPane.showMessageDialog(container,
                input.getText().equals(sampleSentence) ? "Success" : "Try Again"));

        container.revalidate();
        container.repaint();
    }

    private void scrambleSprint(JPanel container) {
        container.removeAll();
        container.setLayout(null);

        String word = wordList.get(random.nextInt(wordList.size()));
        String scrambled = shuffle(word);

        JLabel label = new JLabel(scrambled);
        label.setFont(new Font("Century Gothic", Font.BOLD, 30));
        label.setForeground(new Color(0.0f, 0.0f, 1.0f, 1.0f));
        label.setBounds(60, 80, 800, 54);

        JTextField input = new JTextField();
        input.setBounds(60, 160, 760, 36);
        input.setBorder(null);

        container.add(label);
        container.add(input);

        input.addActionListener(e -> JOptionPane.showMessageDialog(container,
                input.getText().equals(word) ? "Correct" : "Wrong!"));

        container.revalidate();
        container.repaint();
    }

    private String shuffle(String s) {
        List<Character> chars = new ArrayList<>();
        for(char c : s.toCharArray()) chars.add(c);
        Collections.shuffle(chars);
        StringBuilder sb = new StringBuilder();
        for(char c : chars) sb.append(c);
        return sb.toString();
    }

    private void ghostKeys(JPanel container) {
        container.removeAll();
        container.setLayout(null);

        List<Character> broken = List.of('E', 'A');

        JLabel label = new JLabel(getRandomSampleSentence());
        label.setFont(new Font("Century Gothic", Font.BOLD, 30));
        label.setForeground(new Color(0.0f, 0.0f, 1.0f, 1.0f));
        label.setBounds(60, 80, 800, 54);

        JTextField input = new JTextField();
        input.setBounds(60, 160, 760, 36);
        input.setBorder(null);

        container.add(label);
        container.add(input);

        KeyAdapter ka = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (broken.contains(Character.toUpperCase(e.getKeyChar()))) e.consume();
            }
        };
        input.addKeyListener(ka);

        input.addActionListener(e -> JOptionPane.showMessageDialog(container,
                input.getText().equals(sampleSentence) ? "Success" : "Try Again"));

        container.revalidate();
        container.repaint();
    }

    private void fallingWords(JPanel container) {
        container.removeAll();

        currentFallingPanel = new FallingPanel();
        container.add(currentFallingPanel, BorderLayout.CENTER);
        currentFallingPanel.start();

        container.revalidate();
        container.repaint();
    }

    // Falling panel class with stop() method to cleanly stop the timer & key listener
    class FallingPanel extends JPanel implements ActionListener, KeyListener {
        List<FallWord> words = new ArrayList<>();
        Timer timer;
        String current = "";

        public FallingPanel() {
            setFocusable(true);
            addKeyListener(this);
            timer = new Timer(50, this);
        }

        public void start() {
            timer.start();
            activeTimers.add(timer);
            spawn();
            requestFocusInWindow();
        }

        public void stop() {
            if (timer != null) {
                timer.stop();
                activeTimers.remove(timer);
            }
            // remove keylistener and clear words
            try {
                removeKeyListener(this);
            } catch (Exception ignored) {}
        }

        private void spawn() {
            words.add(new FallWord(wordList.get(random.nextInt(wordList.size()))));
        }

        public void actionPerformed(ActionEvent e) {
            for (FallWord w : words) w.y += 2;
            repaint();
            words.removeIf(w -> w.y > getHeight());
            if (random.nextInt(20) == 0) spawn();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(new Font("Monospaced", Font.BOLD, 24));
            for (FallWord w : words) g.drawString(w.word, w.x, w.y);
            g.drawString("Type: " + current, 10, getHeight() - 10);
        }

        public void keyTyped(KeyEvent e) {
            current += e.getKeyChar();
            Iterator<FallWord> it = words.iterator();
            while (it.hasNext()) {
                FallWord w = it.next();
                if (current.equals(w.word)) {
                    it.remove();
                    current = "";
                    break;
                }
            }
        }

        public void keyPressed(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
    }

    class FallWord { String word; int x, y; public FallWord(String w) { word = w; x = random.nextInt(700); y = 0;} }

    private void reversal(JPanel container) {
        container.removeAll();
        container.setLayout(new BorderLayout());
        String rev = new StringBuilder(getRandomSampleSentence()).reverse().toString();
        JLabel label = new JLabel(rev);
        JTextField input = new JTextField();
        container.add(label, BorderLayout.NORTH);
        container.add(input, BorderLayout.CENTER);
        input.addActionListener(e -> JOptionPane.showMessageDialog(container,
                input.getText().equals(sampleSentence) ? "Correct" : "Wrong"));
        container.revalidate();
        container.repaint();
    }

    private void oneHandChallenge(JPanel container) {
        container.removeAll();
        container.setLayout(new BorderLayout());
        String word = "sagas"; // left-hand only example
        JLabel label = new JLabel(word);
        JTextField input = new JTextField();
        container.add(label, BorderLayout.NORTH);
        container.add(input, BorderLayout.CENTER);
        input.addActionListener(e -> JOptionPane.showMessageDialog(container,
                input.getText().equals(word) ? "Nice!" : "Nope"));
        container.revalidate();
        container.repaint();
    }

    private void blindType(JPanel container) {
        container.removeAll();
        container.setLayout(new BorderLayout());
        JLabel label = new JLabel(getRandomSampleSentence());
        JTextField input = new JTextField();
        container.add(label, BorderLayout.NORTH);
        container.add(input, BorderLayout.CENTER);
        KeyAdapter ka = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                label.setText("");
                input.removeKeyListener(this);
            }
        };
        input.addKeyListener(ka);
        input.addActionListener(e -> JOptionPane.showMessageDialog(container,
                input.getText().equals(sampleSentence) ? "Success" : "Try Again"));
        container.revalidate();
        container.repaint();
    }

    private void autoCorrectChaos(JPanel container) {
        container.removeAll();
        container.setLayout(new BorderLayout());
        JLabel label = new JLabel(getRandomSampleSentence());
        JTextField input = new JTextField();
        container.add(label, BorderLayout.NORTH);
        container.add(input, BorderLayout.CENTER);
        // We intentionally don't try to mutate KeyEvent in a cross-platform way; we'll simulate by inserting a char in Document
        input.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (random.nextBoolean()) {
                    // insert random char into the field instead of the typed one
                    SwingUtilities.invokeLater(() -> {
                        try {
                            int pos = input.getCaretPosition();
                            String next = String.valueOf((char) ('a' + random.nextInt(26)));
                            input.getDocument().insertString(pos, next, null);
                            // consume original
                        } catch (Exception ignored) {}
                    });
                    e.consume();
                }
            }
        });
        input.addActionListener(e -> JOptionPane.showMessageDialog(container,
                input.getText().equals(sampleSentence) ? "Beat Autocorrect!" : "Close, but no."));
        container.revalidate();
        container.repaint();
    }

    private void typingTrivia(JPanel container) {
        container.removeAll();
        container.setLayout(new BorderLayout());
        String[] qa = triviaQuestions.get(random.nextInt(triviaQuestions.size())).split(";");
        JLabel label = new JLabel(qa[0]);
        JTextField input = new JTextField();
        container.add(label, BorderLayout.NORTH);
        container.add(input, BorderLayout.CENTER);
        input.addActionListener(e -> JOptionPane.showMessageDialog(container,
                input.getText().equalsIgnoreCase(qa[1]) ? "Right!" : "Wrong!"));
        container.revalidate();
        container.repaint();
    }

    private void morseMayhem(JPanel container) {
        container.removeAll();
        container.setLayout(new BorderLayout());
        StringBuilder code = new StringBuilder();
        String[] letters = getRandomSampleSentence().replaceAll("[^A-Za-z]", "").split("");
        for (int i = 0; i < 5; i++) {
            code.append(morseMap.get(letters[random.nextInt(letters.length)])).append(" ");
        }
        JLabel label = new JLabel(code.toString());
        JTextField input = new JTextField();
        container.add(label, BorderLayout.NORTH);
        container.add(input, BorderLayout.CENTER);
        input.addActionListener(e -> JOptionPane.showMessageDialog(container,
                "Your input: " + input.getText()));
        container.revalidate();
        container.repaint();
    }

    private void emojiWords(JPanel container) {
        container.removeAll();
        container.setLayout(new BorderLayout());
        String emojis = "⏰🏃‍♂️";
        JLabel label = new JLabel(emojis);
        JTextField input = new JTextField();
        container.add(label, BorderLayout.NORTH);
        container.add(input, BorderLayout.CENTER);
        input.addActionListener(e -> JOptionPane.showMessageDialog(container,
                input.getText().equalsIgnoreCase("running late") ? "Good!" : "Think Emoji."));
        container.revalidate();
        container.repaint();
    }

    private void forbiddenLetter(JPanel container) {
        container.removeAll();
        container.setLayout(new BorderLayout());
        char banned = 'e';
        JLabel label = new JLabel("Avoid the letter: " + banned);
        JTextField input = new JTextField();

        container.add(label, BorderLayout.NORTH);
        container.add(input, BorderLayout.CENTER);
        input.addActionListener(e -> {
            if (input.getText().toLowerCase().indexOf(banned) >= 0)
                JOptionPane.showMessageDialog(container,"You used the forbidden letter!");
            else JOptionPane.showMessageDialog(container,"Well done!");
        });
        container.revalidate();
        container.repaint();
    }

    private Color getContrastColor(Color c, Color background) {
        // if selection color is too close to background, pick black/white against background
        double lum = (0.299*background.getRed() + 0.587*background.getGreen() + 0.114*background.getBlue()) / 255.0;
        return lum > 0.5 ? Color.BLACK : Color.WHITE;
    }
    private String getRandomSampleSentence() {
        return sampleSentence.get(random.nextInt(sampleSentence.size()));
    }


    // -------------------------
    // Utilities & main
    // -------------------------
    public static void main(String[] args) {
        // 1) read saved theme
        String savedTheme = PreferencesManager.loadThemeChoice();

        // 2) apply it before building any UI!
        ThemeUtils.applyTheme(savedTheme);

        SwingUtilities.invokeLater(ChallengeMode::new);
    }
}
