import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Random;

public class LevelUpGame {
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
    private boolean isPaused = false;
    private long startTime;
    private boolean startedTyping = false;
    private Timer movementTimer;
    private Timer timeElapsedTimer;
    private String username;

    private String[] speeches = {
            "Believe in yourself and all that you are!",
            "Hard work beats talent when talent doesn't work hard.",
            "Success is not final, failure is not fatal.",
            "Don't watch the clock; do what it does. Keep going.",
            "Dream big and dare to fail.",
            "The only limit to our realization of tomorrow is our doubts of today.",
            "Act as if what you do makes a difference. It does.",
            "Keep your face always toward the sunshine and shadows will fall behind you.",
            "Opportunities don't happen. You create them.",
            "Do what you can, with what you have, where you are.",
            "If you can dream it, you can do it.",
            "The future belongs to those who believe in the beauty of their dreams.",
            "The way to get started is to quit talking and begin doing.",
            "Don't wait for opportunity. Create it.",
            "Failure is not the opposite of success; it's part of success.",
            "What lies behind us and what lies before us are tiny matters compared to what lies within us."
    };


    public LevelUpGame() {
        JFrame frame = new JFrame("Level Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setUndecorated(true);
//        frame.getContentPane().setBackground(new Color(240, 224, 208));

        JPanel linePanel = new JPanel();
        linePanel.setBounds(0,56,800,2);
        linePanel.setBackground(Color.BLACK);
        frame.add(linePanel);

        timerLabel = new JLabel("Time: 0s");
        timerLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
        timerLabel.setBounds(350, 500, 150, 30);
        frame.add(timerLabel);

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

//        String result = getHighScore(username);
//        JLabel highScorelbl = new JLabel(result);
//        highScorelbl.setBounds(620, 19, 150, 30);
//        frame.add(highScorelbl);

        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        new LevelUpGame();
    }
}