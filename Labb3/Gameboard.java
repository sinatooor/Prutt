import javax.swing.*;
import java.awt.*;

public class Gameboard extends JPanel {
    private JLabel nameLabel;
    private JLabel moveLabel;
    private JButton rockButton, paperButton, scissorsButton;
    private JLabel resultLabel;
    private JLabel scoreLabel;
    private int score;

    public Gameboard(String playerName) {
        nameLabel = new JLabel("Player: " + playerName);
        moveLabel = new JLabel("Last Move: ");
        resultLabel = new JLabel("Result: ");
        scoreLabel = new JLabel("Score: 0");

        rockButton = new JButton("STEN");
        paperButton = new JButton("PÅSE");
        scissorsButton = new JButton("SAX");

        setLayout(new GridLayout(3, 2));
        add(nameLabel);
        add(moveLabel);
        add(rockButton);
        add(paperButton);
        add(scissorsButton);
        add(resultLabel);
        add(scoreLabel);
    }

    // Update move display
    public void updateMove(String move) {
        moveLabel.setText("Last Move: " + move);
    }

    // Update result and score display
    public void updateResult(String result, boolean win) {
        resultLabel.setText("Result: " + result);
        if (win) score++;
        scoreLabel.setText("Score: " + score);
    }

    public JButton getRockButton() { return rockButton; }
    public JButton getPaperButton() { return paperButton; }
    public JButton getScissorsButton() { return scissorsButton; }
}
