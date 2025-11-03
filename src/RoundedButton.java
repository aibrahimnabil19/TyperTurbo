import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private int radius = 20; // Adjust for roundness
    private final String arrow = "▶"; // Right arrow symbol
    private boolean showArrow;

    public RoundedButton(String text) {
        this(text, true);
    }

    public RoundedButton(String text, boolean showArrow) {
        super(text);
        this.showArrow = showArrow;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
//        setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left
        setMargin(new Insets(0, 20, 0, 20)); // Add left padding
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded rectangle background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

//         Draw text (left-aligned)
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int textX = 20; // Left padding for text
        int textY = (getHeight() + fm.getAscent()) / 2 - 2;
        g2.drawString(getText(), textX, textY);

//         Draw arrow (right-aligned)
        if (showArrow) {
            int arrowX = getWidth() - fm.stringWidth(arrow) - 20;
            g2.drawString(arrow, arrowX, textY);
        }

        g2.dispose();
    }
}
