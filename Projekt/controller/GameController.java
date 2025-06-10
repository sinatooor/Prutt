package controller;

import model.*;
import view.GameView;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Stack;

public class GameController {
    
    // private boolean animationInProgress = false; // <-- DENNA TAS BORT
    private Board board;
    private GameView gameView;
    private HighScoreManager highScoreManager;
    private JLabel scoreLabel;
    private JFrame mainFrame;
    private boolean gameOver = false;
    private Stack<GameState> history;
    private AutoPlayerStrategy autoPlayer;

    private static final String SAVE_FILE_PATH = "gamestate.ser";

    public GameController(Board board, GameView gameView, HighScoreManager highScoreManager) {
        this.board = board;
        this.gameView = gameView;
        this.gameView.setBoard(this.board);
        this.highScoreManager = highScoreManager;
        this.history = new Stack<>();
    }

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

    private void handleKeyPress(int keyCode) {
        // if (this.animationInProgress) { // <-- DENNA TAS BORT
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

                // === ÄNDRING HÄR: Korrekt hantering av animation ===
                // 1. Starta "flashen" i vyn.
                gameView.flashNewTile(board.getLastAddedPos());
                
                // 2. Uppdatera poängen manuellt.
                scoreLabel.setText("Score: " + board.getScore());
                
                // 3. Ta bort allt som har med Thread.sleep och animationInProgress att göra.
                // === SLUT PÅ ÄNDRING ===
                
                if (board.isGameOver()) {
                    gameOver = true;
                    handleGameOver();
                }
            }
        }
    }

    public void updateGameView() {
        scoreLabel.setText("Score: " + board.getScore());
        gameView.repaint();
    }

    // --- ALL KOD NEDANFÖR ÄR OFÖRÄNDRAD ---

    public void restartGame() {
        board.reset();
        history.clear();
        gameOver = false;
        updateGameView();
        gameView.requestFocusInWindow();
        System.out.println("Game Restarted!");
    }

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