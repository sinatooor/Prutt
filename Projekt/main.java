import controller.GameController;
import model.Board;
import model.HighScoreManager;
import view.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        // Kör GUI-skapandet på Event Dispatch Thread (EDT) - viktigt för Swing!
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // Skapa huvudfönstret
        JFrame frame = new JFrame("2048 Game - Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Skapa modell, vy och controller
        Board board = new Board();
        GameView gameView = new GameView(); // JPanel
        HighScoreManager highScoreManager = new HighScoreManager("resources/leaderboard.csv");
        // Skicka med alla nödvändiga komponenter till controllern
        GameController gameController = new GameController(board, gameView, highScoreManager);

        // Skapa UI-element
        JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Skapa knappar (valfritt, eftersom vi har tangentbordskontroller)
        JPanel buttonPanel = new JPanel(new FlowLayout()); // En rad med knappar
        JButton restartButton = new JButton("Restart (R)");
        JButton undoButton = new JButton("Undo (U)");
        JButton saveButton = new JButton("Save (S)");
        JButton loadButton = new JButton("Load (L)");
        JButton scoresButton = new JButton("High Scores");

        buttonPanel.add(restartButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(scoresButton);

        // Lägg till ActionListeners för knapparna
        restartButton.addActionListener(e -> gameController.restartGame());
        undoButton.addActionListener(e -> gameController.undoMove());
        saveButton.addActionListener(e -> gameController.saveGame());
        loadButton.addActionListener(e -> gameController.loadGame());
        scoresButton.addActionListener(e -> gameController.showHighScores());


        // Sätt layout för fönstret (BorderLayout är enkel)
        frame.setLayout(new BorderLayout(10, 10)); // 10px gap
        frame.add(scoreLabel, BorderLayout.NORTH);
        frame.add(gameView, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Padding runt hela innehållet (valfritt)
        ((JPanel)frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        // Initialisera controllern (koppla KeyListener etc.)
        // Måste göras *efter* att komponenterna lagts till i fönstret
        gameController.initialize(frame, scoreLabel);

        // Anpassa fönstrets storlek till innehållet
        frame.pack();
        // Centrera fönstret på skärmen
        frame.setLocationRelativeTo(null);
        // Visa fönstret
        frame.setVisible(true);

         // Försök sätta fokus på spelpanelen direkt när fönstret visas
         gameView.requestFocusInWindow();
    }
}