import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RPSSkel extends JFrame {
    private Gameboard playerBoard;
    private Gameboard serverBoard;
    private GameClient client;

    public RPSSkel(GameClient client) {
        this.client = client;
        setTitle("Rock Paper Scissors");
        setLayout(new BorderLayout());

        playerBoard = new Gameboard("Player");
        serverBoard = new Gameboard("Server");

        add(playerBoard, BorderLayout.WEST);
        add(serverBoard, BorderLayout.EAST);

        // Add listeners to player buttons
        playerBoard.getRockButton().addActionListener(new MoveListener("STEN"));
        playerBoard.getPaperButton().addActionListener(new MoveListener("PÅSE"));
        playerBoard.getScissorsButton().addActionListener(new MoveListener("SAX"));

        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Inner class for handling move events
    private class MoveListener implements ActionListener {
        private String move;

        public MoveListener(String move) {
            this.move = move;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String serverMove = client.sendMove(move);
            playerBoard.updateMove(move);
            serverBoard.updateMove(serverMove);
            updateResult(move, serverMove);  // Decide winner and update UI
        }
    }

    private void updateResult(String playerMove, String serverMove) {
        // Calculate the result (win/lose/draw) and update player and server boards
        // Example logic to be added here
    }
}
