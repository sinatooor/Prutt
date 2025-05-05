package controller;

import model.*; // Importerar alla klasser från model-paketet
import view.GameView;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*; // För serialisering
import java.util.Stack;

public class GameController {

    private Board board;
    private GameView gameView; // Nu en JPanel
    private HighScoreManager highScoreManager;
    private JLabel scoreLabel; // Swing JLabel
    private JFrame mainFrame; // Referens till huvudfönstret (för KeyListener)
    private boolean gameOver = false;
    private Stack<GameState> history; // För Ångra-funktion

    private static final String SAVE_FILE_PATH = "gamestate.ser";

    public GameController(Board board, GameView gameView, HighScoreManager highScoreManager) {
        this.board = board;
        this.gameView = gameView;
        this.gameView.setBoard(this.board); // Ge vyn en referens till brädet direkt
        this.highScoreManager = highScoreManager;
        this.history = new Stack<>();
    }

    // Kopplar KeyListener och referens till scoreLabel & frame
    public void initialize(JFrame frame, JLabel scoreLabel) {
        this.mainFrame = frame;
        this.scoreLabel = scoreLabel;

        // Lägg till KeyListener till GameView-panelen (eller JFrame)
        // GameView måste vara focusable (satt i dess konstruktor)
        gameView.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });
        // Se till att GameView kan få fokus
        gameView.setFocusable(true);
        // Ofta bra att be om fokus när fönstret visas
        frame.addWindowFocusListener(new java.awt.event.WindowAdapter() {
             @Override
             public void windowGainedFocus(java.awt.event.WindowEvent e) {
                 gameView.requestFocusInWindow();
             }
         });

        updateGameView(); // Rita brädet initialt
    }

    // Hanterar tangenttryckningar
    private void handleKeyPress(int keyCode) {
        if (gameOver) {
            if (keyCode == KeyEvent.VK_R) {
                restartGame();
            }
            return; // Ignorera andra tangenter när spelet är över
        }

        Direction direction = null;
        switch (keyCode) {
            case KeyEvent.VK_UP:    direction = Direction.UP; break;
            case KeyEvent.VK_DOWN:  direction = Direction.DOWN; break;
            case KeyEvent.VK_LEFT:  direction = Direction.LEFT; break;
            case KeyEvent.VK_RIGHT: direction = Direction.RIGHT; break;
            case KeyEvent.VK_R:     restartGame(); return;
            case KeyEvent.VK_U:     undoMove(); return;
            case KeyEvent.VK_S:     saveGame(); return;
            case KeyEvent.VK_L:     loadGame(); return;
            default:    return; // Ignorera andra tangenter
        }

        if (direction != null) {
            GameState currentState = board.getState();
            boolean moveMade = board.move(direction);

            if (moveMade) {
                history.push(currentState);
                board.addRandomTile();
                updateGameView();

                if (board.isGameOver()) {
                    gameOver = true;
                    handleGameOver(); // Anropar JOptionPane härifrån
                }
            }
        }
    }


    // Uppdaterar GameView (repaint) och scoreLabel
    public void updateGameView() {
        scoreLabel.setText("Score: " + board.getScore());
        gameView.repaint(); // Be Swing att rita om GameView-panelen
    }

    // Startar om spelet
    public void restartGame() {
        board.reset();
        history.clear();
        gameOver = false;
        updateGameView();
        gameView.requestFocusInWindow(); // Se till att panelen har fokus igen
        System.out.println("Game Restarted!");
    }

    // Ångrar senaste draget
    public void undoMove() {
        if (!history.isEmpty()) {
            GameState previousState = history.pop();
            board.setState(previousState);
            gameOver = false;
            updateGameView();
            gameView.requestFocusInWindow();
            System.out.println("Move Undone!");
        } else {
            System.out.println("No moves to undo.");
            // Visa ett meddelande till användaren (valfritt)
            // JOptionPane.showMessageDialog(mainFrame, "No moves to undo.", "Undo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Hanterar slutet av spelet med JOptionPane
    private void handleGameOver() {
        System.out.println("Game Over!");
        updateGameView(); // Rita Game Over overlay

        int finalScore = board.getScore();
        boolean isHighScore = highScoreManager.isHighScore(finalScore);
        String message = "Game Over! Final Score: " + finalScore;

        if (isHighScore) {
            message += "\nCongratulations! You made it to the leaderboard!";
            String name = JOptionPane.showInputDialog(mainFrame, "Enter your name for the leaderboard:", "New High Score!", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.trim().isEmpty()) {
                name = "Anonymous";
            }
            highScoreManager.addScore(finalScore, name);
            // Visa topplistan (enkelt)
            showHighScores();

        } else {
            message += "\nPress 'R' to restart.";
        }

        JOptionPane.showMessageDialog(mainFrame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        // Fokus försvinner ofta med dialogrutor, men det spelar mindre roll när spelet är över.
    }

     // Visar topplistan i en enkel dialogruta
    public void showHighScores() {
         StringBuilder sb = new StringBuilder("--- High Scores ---\n");
         if (highScoreManager.getHighScores().isEmpty()) {
             sb.append("No scores yet!");
         } else {
             highScoreManager.getHighScores().forEach(entry ->
                 sb.append(entry.getName()).append(": ").append(entry.getScore()).append("\n")
             );
         }
         JOptionPane.showMessageDialog(mainFrame, sb.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
     }

     // Spara spelstatus (enkel serialisering)
    public void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
            oos.writeObject(board.getState());
            System.out.println("Game saved to " + SAVE_FILE_PATH);
            JOptionPane.showMessageDialog(mainFrame, "Game state saved successfully.", "Game Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "Could not save game state: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
         gameView.requestFocusInWindow(); // Återställ fokus
    }

    // Ladda spelstatus (enkel serialisering)
    public void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE_PATH))) {
            GameState loadedState = (GameState) ois.readObject();
            board.setState(loadedState);
            history.clear();
            gameOver = board.isGameOver();
            updateGameView();
            System.out.println("Game loaded from " + SAVE_FILE_PATH);
             JOptionPane.showMessageDialog(mainFrame, "Game state loaded successfully.", "Game Loaded", JOptionPane.INFORMATION_MESSAGE);
             if (gameOver) {
                 handleGameOver();
             }
        } catch (FileNotFoundException e) {
             System.out.println("No save file found: " + SAVE_FILE_PATH);
             JOptionPane.showMessageDialog(mainFrame, "No save file found.", "Load Error", JOptionPane.WARNING_MESSAGE);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
             JOptionPane.showMessageDialog(mainFrame, "Could not load game state: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
         gameView.requestFocusInWindow(); // Återställ fokus
    }
}