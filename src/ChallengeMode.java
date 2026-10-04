import javax.swing.*;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.Normalizer;
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
    private int activeModeIndex = -1;
    private JButton challengePauseButton;

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
    private final List<String> frenchSentences = List.of(
            "Le renard brun traverse rapidement le jardin.",
            "La pratique régulière améliore la vitesse de frappe.",
            "Chaque jour apporte une nouvelle occasion de progresser.",
            "Les étoiles brillent dans le ciel au-dessus de la ville.",
            "Un doux vent traverse les arbres en fleurs.",
            "Le petit oiseau chante près de la fenêtre.",
            "La rivière serpente au milieu de la vallée.",
            "Une bonne méthode aide à apprendre plus rapidement.",
            "Le boulanger prépare du pain frais chaque matin.",
            "Les nuages avancent lentement au-dessus des montagnes.",
            "Elle ouvre le livre et lit pendant des heures.",
            "La pluie tombe doucement sur le toit.",
            "Le jardin est rempli de fleurs colorées.",
            "Un voyage commence souvent par un petit pas.",
            "La patience et l’entraînement mènent au progrès."
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
    private final List<String> frenchTriviaQuestions = List.of(
            "Quelle est la capitale de la France ?;Paris",
            "Quelle planète est surnommée la planète rouge ?;Mars",
            "Combien y a-t-il de continents ?;7",
            "Qui a peint la Joconde ?;Léonard de Vinci",
            "Quel est le plus grand océan ?;Pacifique",
            "Quelle est la formule chimique de l’eau ?;H2O",
            "Quel gaz les plantes absorbent-elles ?;Dioxyde de carbone",
            "Quelle planète possède des anneaux célèbres ?;Saturne",
            "Combien font 12 × 12 ?;144",
            "Quel est le plus grand mammifère ?;Baleine bleue",
            "Quel est le point de congélation de l’eau en Celsius ?;0",
            "Quelle est la capitale du Japon ?;Tokyo",
            "Quel est le plus petit nombre premier ?;2",
            "Quel est le plus haut sommet du monde ?;Everest",
            "Combien de côtés possède un hexagone ?;6"
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
        frame.setTitle(I18n.t("Typing Challenge Mode"));
        frame.setSize(900, 620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Root card layout
        rootPanel.setLayout(cardLayout);
        rootPanel.add(menuPanel, "menu");
        rootPanel.add(gameContainer, "game");

        // Top back button
        RoundedButton backBtn = new RoundedButton("← " + I18n.t("Back"), false);
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
        challengePauseButton = new JButton(I18n.t("Pause"));
        challengePauseButton.setFocusable(false);
        challengePauseButton.setVisible(false);
        challengePauseButton.addActionListener(e -> pauseCurrentChallenge());

        // Header panel (back + title)
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        header.add(backBtn, BorderLayout.WEST);
        header.add(challengePauseButton, BorderLayout.EAST);

        JLabel title = new JLabel(I18n.t("Challenge Modes"));
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
        activeModeIndex = -1;
        challengePauseButton.setVisible(false);
        cardLayout.show(rootPanel, "menu");
        frame.setTitle(I18n.t("Typing Challenge Mode"));
    }

    private void showGame(String modeTitle) {
        cardLayout.show(rootPanel, "game");
        challengePauseButton.setVisible(true);
        frame.setTitle(I18n.t("Typing Challenge Mode") + " - " + modeTitle);
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

    private boolean showChallengeBriefing(int index, String title) {
        return showInstructionDialog(
                I18n.t("Challenge Instructions"), title, challengeDescription(index),
                I18n.t("Start Challenge"), I18n.t("Cancel")) == 0;
    }

    private int showInstructionDialog(String dialogTitle, String headingText, String instructions,
                                      String primaryLabel, String secondaryLabel) {
        JDialog dialog = new JDialog(frame, dialogTitle, true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);

        JPanel content = new JPanel(new BorderLayout(18, 16));
        content.setBorder(BorderFactory.createEmptyBorder(26, 30, 24, 30));
        content.setBackground(new Color(255, 248, 240));
        content.setPreferredSize(new Dimension(700, 390));

        JLabel heading = new JLabel(headingText, SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(160, 70, 35));
        content.add(heading, BorderLayout.NORTH);

        JTextArea description = new JTextArea(instructions);
        description.setFont(new Font("Segoe UI", Font.PLAIN, 19));
        description.setForeground(new Color(58, 55, 53));
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setFocusable(false);
        description.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        description.setBackground(Color.WHITE);
        JScrollPane descriptionScroll = new JScrollPane(description);
        descriptionScroll.setBorder(BorderFactory.createLineBorder(new Color(235, 218, 202), 1, true));
        content.add(descriptionScroll, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 4));
        actions.setOpaque(false);
        JButton primary = new JButton(primaryLabel);
        primary.setFont(new Font("Segoe UI", Font.BOLD, 18));
        primary.setForeground(Color.WHITE);
        primary.setBackground(new Color(195, 88, 42));
        primary.setOpaque(true);
        primary.setBorder(BorderFactory.createEmptyBorder(12, 28, 12, 28));
        primary.setFocusPainted(false);
        primary.setPreferredSize(new Dimension(220, 54));
        JButton secondary = new JButton(secondaryLabel);
        secondary.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        secondary.setPreferredSize(new Dimension(170, 54));
        secondary.setFocusPainted(false);
        actions.add(primary);
        actions.add(secondary);
        content.add(actions, BorderLayout.SOUTH);

        int[] result = {-1};
        primary.addActionListener(e -> {
            result[0] = 0;
            dialog.dispose();
        });
        secondary.addActionListener(e -> {
            result[0] = 1;
            dialog.dispose();
        });
        dialog.getRootPane().setDefaultButton(primary);
        dialog.getRootPane().registerKeyboardAction(
                e -> dialog.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.setContentPane(content);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        return result[0];
    }

    private String challengeDescription(int index) {
        String[] english = {
                "Type the displayed sentence as quickly and accurately as possible. The timer tracks your completion time.",
                "Restore the original sentence from its typo-filled version. Submit your answer to check it.",
                "Type each displayed word before its countdown reaches zero. The timer gets shorter after every correct word.",
                "Memorize the sentence before it disappears, then type it from memory.",
                "Unscramble the letters and enter the original word.",
                "Type the sentence while the E and A keys are disabled.",
                "Type each falling word before it reaches the bottom of the screen.",
                "Read the sentence backwards, then enter it in its original order.",
                "Type the displayed word using only your left hand.",
                "The sentence vanishes as soon as you begin typing. Reproduce it from memory.",
                "Type the sentence while the simulated autocorrect interferes with your input.",
                "Answer a general-knowledge question. Answers are not case-sensitive.",
                "Decode the Morse code sequence and enter the represented letters.",
                "Interpret the emoji clue and type the phrase it represents.",
                "Write a sentence without using the forbidden letter shown on screen."
        };
        String[] french = {
                "Recopiez la phrase affichée aussi vite et précisément que possible. Le chronomètre mesure votre temps.",
                "Retrouvez la phrase d’origine à partir de sa version contenant des fautes, puis validez votre réponse.",
                "Saisissez chaque mot avant la fin du compte à rebours. Le temps diminue après chaque bonne réponse.",
                "Mémorisez la phrase avant sa disparition, puis saisissez-la de mémoire.",
                "Remettez les lettres dans le bon ordre pour retrouver le mot.",
                "Saisissez la phrase alors que les touches E et A sont désactivées.",
                "Saisissez chaque mot qui tombe avant qu’il n’atteigne le bas de l’écran.",
                "Lisez la phrase à l’envers, puis saisissez-la dans le bon sens.",
                "Saisissez le mot affiché en n’utilisant que la main gauche.",
                "La phrase disparaît dès que vous commencez à écrire. Reproduisez-la de mémoire.",
                "Saisissez la phrase malgré les interférences du correcteur automatique simulé.",
                "Répondez à une question de culture générale. La casse n’est pas prise en compte.",
                "Décodez la séquence en Morse et saisissez les lettres représentées.",
                "Interprétez l’indice en émojis et saisissez l’expression correspondante.",
                "Écrivez une phrase sans utiliser la lettre interdite affichée."
        };
        return (I18n.isFrench() ? french : english)[index];
    }

    private void pauseCurrentChallenge() {
        if (activeModeIndex < 0) return;
        List<Timer> runningTimers = new ArrayList<>();
        for (Timer timer : activeTimers) {
            if (timer.isRunning()) {
                runningTimers.add(timer);
                timer.stop();
            }
        }

        int choice = showInstructionDialog(
                I18n.t("Challenge Instructions"), I18n.t("Paused"),
                challengeDescription(activeModeIndex),
                I18n.t("Resume"), I18n.t("Back to Challenges"));
        if (choice == 1) {
            stopCurrentGameAndShowMenu();
            return;
        }
        if (choice == 0) {
            for (Timer timer : runningTimers) {
                if (activeTimers.contains(timer)) timer.start();
            }
            if (currentFallingPanel != null) currentFallingPanel.requestFocusInWindow();
        }
    }

    private void prepareChallengeScreen(JPanel container) {
        container.removeAll();
        container.setLayout(null);
        container.setBackground(new Color(250, 247, 243));
        container.setOpaque(true);
        container.setBorder(BorderFactory.createEmptyBorder());

        JLabel title = new JLabel(I18n.t(modeNames.get(activeModeIndex)), SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(new Color(160, 70, 35));
        title.setBounds(25, 16, 830, 34);
        container.add(title);

        JLabel instructions = new JLabel(challengeDescription(activeModeIndex), SwingConstants.CENTER);
        instructions.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instructions.setForeground(new Color(85, 76, 70));
        instructions.setBounds(32, 52, 815, 40);
        container.add(instructions);
    }

    private void styleChallengeInput(JTextField input) {
        input.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        input.setMargin(new Insets(6, 10, 6, 10));
        input.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 146, 101), 2, true),
                BorderFactory.createEmptyBorder(3, 7, 3, 7)));
    }

    private void styleChallengeLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(new Color(58, 55, 53));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void requestChallengeFocus() {
        SwingUtilities.invokeLater(() -> {
            if (currentFallingPanel != null) {
                currentFallingPanel.requestFocusInWindow();
                return;
            }
            for (Component component : gameContainer.getComponents()) {
                if (component instanceof JTextField textField) {
                    textField.requestFocusInWindow();
                    return;
                }
            }
        });
    }

    private void buildMenu() {
        // Search bar on top of menu
        JPanel topSearch = new JPanel(new BorderLayout(8, 8));
        topSearch.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // --- Replace your existing searchField creation with this ---
        JTextField searchField = new JTextField();
        searchField.setColumns(18);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setToolTipText(I18n.t("Search"));

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
            String label = I18n.t(modeNames.get(i));
            JButton b = createModeButton(label, () -> {
                if (!showChallengeBriefing(idx, label)) return;
                startChallenge(idx);
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
        btn.setToolTipText(I18n.t("Play: ") + text);
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

    private void startChallenge(int index) {
        activeModeIndex = index;
        stopCurrentGame();
        gameContainer.removeAll();
        switch (index) {
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
            default -> throw new IllegalArgumentException("Unknown challenge index: " + index);
        }
        showGame(I18n.t(modeNames.get(index)));
        requestChallengeFocus();
    }

    private void installAnswerCheck(JTextField input, String expected, JPanel container, Runnable afterCorrect) {
        installAnswerCheck(input, expected, container, () -> {}, afterCorrect);
    }

    private void installAnswerCheck(JTextField input, String expected, JPanel container,
                                   Runnable beforeCorrect, Runnable afterCorrect) {
        Object oldListener = input.getClientProperty("answerDocumentListener");
        if (oldListener instanceof javax.swing.event.DocumentListener listener) {
            input.getDocument().removeDocumentListener(listener);
        }
        Object oldAction = input.getClientProperty("answerActionListener");
        if (oldAction instanceof ActionListener listener) input.removeActionListener(listener);

        boolean[] completed = {false};
        Runnable checkAnswer = () -> {
            if (completed[0] || !matchesAnswer(input.getText(), expected)) return;
            completed[0] = true;
            input.setEnabled(false);
            beforeCorrect.run();
            JOptionPane.showMessageDialog(container, I18n.t("Correct!"));
            afterCorrect.run();
        };
        javax.swing.event.DocumentListener documentListener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { SwingUtilities.invokeLater(checkAnswer); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { SwingUtilities.invokeLater(checkAnswer); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { SwingUtilities.invokeLater(checkAnswer); }
        };
        ActionListener actionListener = e -> {
            if (!completed[0]) JOptionPane.showMessageDialog(container, I18n.t("Incorrect!"));
        };
        input.getDocument().addDocumentListener(documentListener);
        input.addActionListener(actionListener);
        input.putClientProperty("answerDocumentListener", documentListener);
        input.putClientProperty("answerActionListener", actionListener);
    }

    private void restartActiveChallenge() {
        if (activeModeIndex >= 0) startChallenge(activeModeIndex);
    }

    // -------------------------
    // Game mode implementations
    // Each now accepts a container panel where we'll add UI
    // and registers timers into activeTimers for cleanup.
    // -------------------------

    private void speedDemon(JPanel container) {
        prepareChallengeScreen(container);

        String text = getRandomSampleSentence();
        JLabel label = new JLabel(text);
        styleChallengeLabel(label);
        label.setBounds(60, 120, 780, 60);

        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 215, 700, 48);
        JLabel timerLabel = new JLabel(I18n.t("Time:") + " 0.0s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        timerLabel.setBounds(100, 280, 700, 30);

        container.add(label);
        container.add(input);
        container.add(timerLabel);

        final long start = System.currentTimeMillis();
        Timer fadeTimer = new Timer(100, e -> {
            double sec = (System.currentTimeMillis() - start) / 1000.0;
            float alpha = Math.max(0, 1f - (float) sec / 10);
            label.setForeground(new Color(195/255f, 88/255f, 42/255f, alpha));
            timerLabel.setText(I18n.t("Time:") + String.format(" %.1fs", sec));
        });
        fadeTimer.start();
        activeTimers.add(fadeTimer);

        installAnswerCheck(input, text, container, () -> {
            fadeTimer.stop();
            activeTimers.remove(fadeTimer);
        }, this::restartActiveChallenge);

        container.revalidate();
        container.repaint();
    }

    private void typoTrouble(JPanel container) {
        prepareChallengeScreen(container);

        String original = getRandomSampleSentence();
        String typo = original.replaceAll("o", "0").replaceAll("e", "3");

        JLabel label = new JLabel(typo);
        styleChallengeLabel(label);
        label.setBounds(60, 125, 780, 60);

        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 220, 700, 48);

        container.add(label);
        container.add(input);

        installAnswerCheck(input, original, container, this::restartActiveChallenge);

        container.revalidate();
        container.repaint();
    }

    private void wordBomb(JPanel container) {
        prepareChallengeScreen(container);

        JLabel wordLabel = new JLabel();
        styleChallengeLabel(wordLabel);
        wordLabel.setBounds(60, 135, 780, 60);

        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 225, 700, 48);

        JLabel timerLabel = new JLabel(I18n.t("Time:") + " 0.0s");
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        timerLabel.setBounds(100, 290, 700, 30);

        container.add(wordLabel);
        container.add(input);
        container.add(timerLabel);

        runBombRound(wordLabel, timerLabel, input, 3000, container);

        container.revalidate();
        container.repaint();
    }

    private void runBombRound(JLabel wordLabel, JLabel timerLabel, JTextField input, int timeLeft, JPanel container) {
        String word = randomWord();
        wordLabel.setText(word);
        input.setText("");
        input.setEnabled(true);
        input.requestFocus();

        Timer bombTimer = new Timer(100, null);
        activeTimers.add(bombTimer);

        bombTimer.addActionListener(new ActionListener() {
            int time = timeLeft;
            @Override public void actionPerformed(ActionEvent e) {
                time -= 100;
                timerLabel.setText(I18n.t("Time:") + String.format(" %.1fs", time / 1000.0));
                if (time <= 0) {
                    bombTimer.stop();
                    activeTimers.remove(bombTimer);
                    input.setEnabled(false);
                    JOptionPane.showMessageDialog(container, I18n.t("Time's up!") + " " + I18n.t("Game Over!"));
                }
            }
        });
        bombTimer.start();

        installAnswerCheck(input, word, container, () -> {
            bombTimer.stop();
            activeTimers.remove(bombTimer);
        }, () -> runBombRound(wordLabel, timerLabel, input, Math.max(300, timeLeft - 200), container));
    }

    private void disappearingText(JPanel container) {
        prepareChallengeScreen(container);

        String sentence = getRandomSampleSentence();
        JLabel label = new JLabel(sentence);
        styleChallengeLabel(label);
        label.setBounds(60, 130, 780, 60);

        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 220, 700, 48);

        container.add(label);
        container.add(input);

        Timer t = new Timer(3000, e -> label.setText(""));
        t.setRepeats(false);
        t.start();
        activeTimers.add(t);

        installAnswerCheck(input, sentence, container, this::restartActiveChallenge);

        container.revalidate();
        container.repaint();
    }

    private void scrambleSprint(JPanel container) {
        prepareChallengeScreen(container);

        String word = randomWord();
        String scrambled = shuffle(word);

        JLabel label = new JLabel(scrambled);
        styleChallengeLabel(label);
        label.setBounds(60, 135, 780, 60);

        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 225, 700, 48);

        container.add(label);
        container.add(input);

        installAnswerCheck(input, word, container, this::restartActiveChallenge);

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
        prepareChallengeScreen(container);

        List<Character> broken = List.of('E', 'A');

        String sentence = I18n.isFrench() ? "Un lynx gris bondit." : "Myths fly by rhythm.";
        JLabel label = new JLabel(sentence);
        styleChallengeLabel(label);
        label.setBounds(60, 125, 780, 60);

        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 220, 700, 48);

        container.add(label);
        container.add(input);

        KeyAdapter ka = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (broken.contains(Character.toUpperCase(e.getKeyChar()))) e.consume();
            }
        };
        input.addKeyListener(ka);

        installAnswerCheck(input, sentence, container, this::restartActiveChallenge);

        container.revalidate();
        container.repaint();
    }

    private void fallingWords(JPanel container) {
        prepareChallengeScreen(container);

        currentFallingPanel = new FallingPanel();
        currentFallingPanel.setBounds(25, 105, 830, 390);
        currentFallingPanel.setBackground(new Color(255, 255, 255));
        currentFallingPanel.setBorder(BorderFactory.createLineBorder(new Color(214, 146, 101), 2, true));
        container.add(currentFallingPanel);
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
            timer = new Timer(35, this);
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
            words.add(new FallWord(randomWord()));
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
            g.drawString(I18n.t("Type: ") + current, 10, getHeight() - 10);
        }

        public void keyTyped(KeyEvent e) {
            current += e.getKeyChar();
            Iterator<FallWord> it = words.iterator();
            while (it.hasNext()) {
                FallWord w = it.next();
                if (matchesAnswer(current, w.word)) {
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
        prepareChallengeScreen(container);
        String sentence = getRandomSampleSentence();
        String rev = new StringBuilder(sentence).reverse().toString();
        JLabel label = new JLabel(rev);
        styleChallengeLabel(label);
        label.setBounds(60, 135, 780, 60);
        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 225, 700, 48);
        container.add(label);
        container.add(input);
        installAnswerCheck(input, sentence, container, this::restartActiveChallenge);
        container.revalidate();
        container.repaint();
    }

    private void oneHandChallenge(JPanel container) {
        prepareChallengeScreen(container);
        String word = "saga";
        JLabel label = new JLabel(word);
        styleChallengeLabel(label);
        label.setBounds(60, 135, 780, 60);
        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 225, 700, 48);
        container.add(label);
        container.add(input);
        installAnswerCheck(input, word, container, this::restartActiveChallenge);
        container.revalidate();
        container.repaint();
    }

    private void blindType(JPanel container) {
        prepareChallengeScreen(container);
        String sentence = getRandomSampleSentence();
        JLabel label = new JLabel(sentence);
        styleChallengeLabel(label);
        label.setBounds(60, 135, 780, 60);
        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 225, 700, 48);
        container.add(label);
        container.add(input);
        KeyAdapter ka = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                label.setText("");
                input.removeKeyListener(this);
            }
        };
        input.addKeyListener(ka);
        installAnswerCheck(input, sentence, container, this::restartActiveChallenge);
        container.revalidate();
        container.repaint();
    }

    private void autoCorrectChaos(JPanel container) {
        prepareChallengeScreen(container);
        String sentence = getRandomSampleSentence();
        JLabel label = new JLabel(sentence);
        styleChallengeLabel(label);
        label.setBounds(60, 135, 780, 60);
        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 225, 700, 48);
        container.add(label);
        container.add(input);
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
        installAnswerCheck(input, sentence, container, this::restartActiveChallenge);
        container.revalidate();
        container.repaint();
    }

    private void typingTrivia(JPanel container) {
        prepareChallengeScreen(container);
        List<String> questions = I18n.isFrench() ? frenchTriviaQuestions : triviaQuestions;
        String[] qa = questions.get(random.nextInt(questions.size())).split(";");
        JLabel label = new JLabel(qa[0]);
        styleChallengeLabel(label);
        label.setBounds(60, 135, 780, 60);
        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 225, 700, 48);
        container.add(label);
        container.add(input);
        installAnswerCheck(input, qa[1], container, this::restartActiveChallenge);
        container.revalidate();
        container.repaint();
    }

    private void morseMayhem(JPanel container) {
        prepareChallengeScreen(container);
        StringBuilder code = new StringBuilder();
        StringBuilder answer = new StringBuilder();
        String[] letters = getRandomSampleSentence().replaceAll("[^A-Za-z]", "").split("");
        for (int i = 0; i < 5; i++) {
            String letter = letters[random.nextInt(letters.length)];
            code.append(morseMap.get(letter.toUpperCase(Locale.ROOT))).append(" ");
            answer.append(letter);
        }
        JLabel label = new JLabel(code.toString());
        styleChallengeLabel(label);
        label.setBounds(60, 135, 780, 60);
        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 225, 700, 48);
        container.add(label);
        container.add(input);
        installAnswerCheck(input, answer.toString(), container, this::restartActiveChallenge);
        container.revalidate();
        container.repaint();
    }

    private void emojiWords(JPanel container) {
        prepareChallengeScreen(container);
        String emojis = "⏰🏃‍♂️";
        JLabel label = new JLabel(emojis);
        styleChallengeLabel(label);
        label.setBounds(60, 135, 780, 60);
        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 225, 700, 48);
        container.add(label);
        container.add(input);
        installAnswerCheck(input, I18n.isFrench() ? "en retard" : "running late",
                container, this::restartActiveChallenge);
        container.revalidate();
        container.repaint();
    }

    private void forbiddenLetter(JPanel container) {
        prepareChallengeScreen(container);
        char banned = I18n.isFrench() ? 'e' : 'e';
        JLabel label = new JLabel(I18n.t("Avoid the letter: ") + banned);
        styleChallengeLabel(label);
        label.setBounds(60, 135, 780, 60);
        JTextField input = new JTextField();
        styleChallengeInput(input);
        input.setBounds(100, 225, 700, 48);

        container.add(label);
        container.add(input);
        input.addActionListener(e -> {
            if (input.getText().toLowerCase().indexOf(banned) >= 0)
                JOptionPane.showMessageDialog(container, I18n.t("Incorrect!"));
            else {
                input.setEnabled(false);
                JOptionPane.showMessageDialog(container, I18n.t("Correct!"));
                restartActiveChallenge();
            }
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
        List<String> sentences = I18n.isFrench() ? frenchSentences : sampleSentence;
        return sentences.get(random.nextInt(sentences.size()));
    }

    private String randomWord() {
        List<String> words = I18n.isFrench()
                ? List.of("pomme", "banane", "cerise", "datte", "figue", "raisin", "orange", "melon")
                : wordList;
        return words.get(random.nextInt(words.size()));
    }

    private boolean matchesAnswer(String input, String expected) {
        return normalizeAnswer(input).equals(normalizeAnswer(expected));
    }

    private String normalizeAnswer(String value) {
        String decomposed = Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD);
        StringBuilder normalized = new StringBuilder(decomposed.length());
        boolean pendingSpace = false;
        for (int i = 0; i < decomposed.length(); i++) {
            char ch = decomposed.charAt(i);
            int type = Character.getType(ch);
            if (type == Character.NON_SPACING_MARK || type == Character.COMBINING_SPACING_MARK
                    || type == Character.ENCLOSING_MARK) {
                continue;
            }
            if (Character.isLetterOrDigit(ch)) {
                if (pendingSpace && normalized.length() > 0) normalized.append(' ');
                normalized.append(Character.toLowerCase(ch));
                pendingSpace = false;
            } else if (Character.isWhitespace(ch)) {
                pendingSpace = true;
            } else if (isPunctuation(type)) {
                if (!isApostrophe(ch)) pendingSpace = true;
            }
        }
        return normalized.toString();
    }

    private boolean isPunctuation(int type) {
        return type == Character.CONNECTOR_PUNCTUATION
                || type == Character.DASH_PUNCTUATION
                || type == Character.START_PUNCTUATION
                || type == Character.END_PUNCTUATION
                || type == Character.INITIAL_QUOTE_PUNCTUATION
                || type == Character.FINAL_QUOTE_PUNCTUATION
                || type == Character.OTHER_PUNCTUATION;
    }

    private boolean isApostrophe(char ch) {
        return ch == '\'' || ch == '\u2018' || ch == '\u2019' || ch == '\u02BC';
    }


    // -------------------------
    // Utilities & main
    // -------------------------
    public static void main(String[] args) {
        I18n.setLanguage(PreferencesManager.loadLanguageChoice());
        // 1) read saved theme
        String savedTheme = PreferencesManager.loadThemeChoice();

        // 2) apply it before building any UI!
        ThemeUtils.applyTheme(savedTheme);

        SwingUtilities.invokeLater(ChallengeMode::new);
    }
}
