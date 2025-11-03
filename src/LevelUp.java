import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class LevelUp {
    public static void main(String[] args) {
        // 1) read saved theme
        String savedTheme = PreferencesManager.loadThemeChoice();

        // 2) apply it before building any UI!
        ThemeUtils.applyTheme(savedTheme);

        SwingUtilities.invokeLater(LevelUp::new);
    }

    private final JFrame frame;

    public LevelUp() {
        frame = new JFrame("Level Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);

        // Use BorderLayout so the scroll pane fills the frame
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(255, 255, 255));

        // Panel with GridLayout: 5 columns, dynamic rows, gaps between buttons
        JPanel buttonPanel = new JPanel(new GridLayout(0, 5, 20, 20));
        buttonPanel.setBackground(new Color(255, 255, 255));

        // Create 50 rounded buttons
        for (int i = 1; i <= 50; i++) {
            JButton btn = new JButton(String.valueOf(i));
            btn.setBackground(new Color(214, 146, 101));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setBorder(new RoundedBorder(12));
            buttonPanel.add(btn);
        }

        // Put the panel inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(buttonPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // Remove default outer border
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
//        // Add padding around content
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(56, 20, 0, 20));

        // Customize vertical scrollbar
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setPreferredSize(new Dimension(12, Integer.MAX_VALUE));
        vBar.setUnitIncrement(16); // scroll speed
        vBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(255, 255, 255);
                trackColor = new Color(214, 146, 101);
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Set smaller thumb height
                int reducedHeight = Math.max(30, thumbBounds.height / 2); // adjust "30" to control min height
                int x = thumbBounds.x;
                int y = thumbBounds.y + (thumbBounds.height - reducedHeight) / 2;
                int width = thumbBounds.width;
                int  height = reducedHeight;

                g2.setColor(thumbColor);
                g2.fillRoundRect(x, y, width, height, 0, 0);
                g2.dispose();
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }
        });


        // Add scroll pane to frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}