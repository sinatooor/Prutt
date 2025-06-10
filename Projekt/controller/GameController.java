package controller;

import model.*;
import view.GameView;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Stack;

/**
 * Manages the game logic, user input, and interaction between the model (Board) and view (GameView).
 * It handles game state changes, scoring, high scores, saving/loading, and auto-play functionality.
 */
public class GameController {
    

    private Board board;
    private GameView gameView;
    private HighScoreManager highScoreManager;
    private JLabel scoreLabel;
    private JFrame mainFrame;
    private boolean gameOver = false;
    private Stack<GameState> history;
    private AutoPlayerStrategy autoPlayer;

    private static final String SAVE_FILE_PATH = "gamestate.ser";

    /**
     * Constructs a new GameController.
     * @param board The game board (model).
     * @param gameView The game view (GUI).
     * @param highScoreManager The manager for high scores.
     */
    public GameController(Board board, GameView gameView, HighScoreManager highScoreManager) {
        this.board = board;
        this.gameView = gameView;
        this.gameView.setBoard(this.board);
        this.highScoreManager = highScoreManager;
        this.history = new Stack<>();
    }

    /**
     * Initializes the controller with the main application frame and score label.
     * Sets up key listeners for user input and ensures the game view has focus.
     * @param frame The main JFrame of the application.
     * @param scoreLabel The JLabel used to display the current score.
     */
    public void initialize(JFrame frame, JLabel scoreLabel) {
        this.mainFrame = frame;
        this.scoreLabel = scoreLabel;
        gameView.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });
        gameView.setFocusable(true);
        frame.addWindowFocusListener(new java.awt.event.WindowAdapter() {
             @Override
             public void windowGainedFocus(java.awt.event.WindowEvent e) {
                 gameView.requestFocusInWindow();
             }
         });
        updateGameView();
    }

    /**
     * Handles key press events from the user.
     * Interprets arrow keys for movement, 'R' for restart, 'U' for undo,
     * 'S' for save, 'L' for load, and 'A' for toggling auto-play.
     * @param keyCode The code of the key that was pressed.
     */
    private void handleKeyPress(int keyCode) {

        //     return;
        // }

        if (gameOver) {
            if (keyCode == KeyEvent.VK_R) {
                restartGame();
            }
            return;
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
            case KeyEvent.VK_A:     toggleAutoPlay(); return;
            default:    return;
        }

        if (direction != null) {
            GameState currentState = board.getState();
            boolean moveMade = board.move(direction);
            
            if (moveMade) {
                history.push(currentState);
                board.addRandomTile();


                // 1. Starta "flashen" i vyn.
                gameView.flashNewTile(board.getLastAddedPos());
                
                // 2. Uppdatera poängen manuellt.
                scoreLabel.setText("Score: " + board.getScore());
                
                // 3. Ta bort allt som har med Thread.sleep och animationInProgress att göra.

                
                if (board.isGameOver()) {
                    gameOver = true;
                    handleGameOver();
                }
            }
        }
    }

    /**
     * Updates the game view by repainting the board and refreshing the score display.
     */
    public void updateGameView() {
        scoreLabel.setText("Score: " + board.getScore());
        gameView.repaint();
    }

  

    /**
     * Restarts the game by resetting the board, clearing history, and updating the view.
     */
    public void restartGame() {
        board.reset();
        history.clear();
        gameOver = false;
        updateGameView();
        gameView.requestFocusInWindow();
        System.out.println("Game Restarted!");
    }

    /**
     * Undoes the last move by reverting the board to its previous state from history.
     * Updates the view and re-enables game play if it was game over.
     */
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
        }
    }

    /**
     * Handles the game over sequence.
     * Updates the view, checks for high scores, and displays a game over message.
     * If a new high score is achieved, prompts the user for their name.
     */
    private void handleGameOver() {
        System.out.println("Game Over!");
        updateGameView();
        int finalScore = board.getScore();
        boolean isHighScore = highScoreManager.isHighScore(finalScore);
        String message = "Game Over! Final Score: " + finalScore;
        if (isHighScore) {
            message += "\nCongratulations! You made it to the leaderboard!";
            String name = JOptionPane.showInputDialog(mainFrame, "Enter your name for the leaderboard:", "New High Score!", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.trim().isEmpty()) { name = "Anonymous"; }
            highScoreManager.addScore(finalScore, name);
            showHighScores();
        } else {
            message += "\nPress 'R' to restart.";
        }
        JOptionPane.showMessageDialog(mainFrame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays the current high scores in a dialog box.
     */
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

    /**
     * Saves the current game state (board and score) to a file.
     * Displays a confirmation or error message to the user.
     */
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
         gameView.requestFocusInWindow();
    }

    /**
     * Loads a previously saved game state from a file.
     * Updates the board, clears history, and refreshes the view.
     * Handles cases where the save file is not found or is corrupted.
     */
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
         gameView.requestFocusInWindow();
    }

    /**
     * Toggles the auto-play feature.
     * If auto-play is not active, it starts a new thread that makes random moves.
     * If auto-play is active, it stops the current auto-play thread.
     * Displays messages to the user indicating the auto-play status.
     */
    public void toggleAutoPlay() {
        if (autoPlayer == null) {
            autoPlayer = new AutoPlayerStrategy();
            autoPlayer.start();
            System.out.println("Auto Play started.");
            JOptionPane.showMessageDialog(mainFrame, "Auto Play started. Press 'A' again to stop.", "Auto Play", JOptionPane.INFORMATION_MESSAGE);
        } else {
            autoPlayer.interrupt();
            autoPlayer = null;
            System.out.println("Auto Play stopped.");
            JOptionPane.showMessageDialog(mainFrame, "Auto Play stopped.", "Auto Play", JOptionPane.INFORMATION_MESSAGE);
        }
        gameView.requestFocusInWindow();
    }

    /**
     * Inner class representing the strategy for automatic gameplay.
     * This thread makes random moves at fixed intervals until interrupted.
     */
    private class AutoPlayerStrategy extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    int[] directions = new int[]{KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT};
                    int randomDirection = directions[(int) (Math.random() * directions.length)];
                    handleKeyPress(randomDirection);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}