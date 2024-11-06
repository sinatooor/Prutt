// Imports

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewControl extends JFrame implements ActionListener{

    private Boardgame game;
    private int size;
    private Square[][] board;        // Square är subklass till JButton
    private JTextField mess = new JTextField();  // JLabel funkar också
    private JLabel buttonBoard = new JLabel(); // används som en behållare

    ViewControl(Boardgame model, int n) {
        super("Boardgame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLayout(new BorderLayout());

        this.game = model; 
        this.size = n;
        this.board = new Square[this.size][this.size];

        this.mess.setText("Make a move");
        this.mess.setEditable(false);

        this.buttonBoard.setLayout(new GridLayout(this.size, this.size));


        //lägger till knappar till frame
        for (int i=0; i<this.size; i++) {
            for (int j=0; j<this.size; j++) {
                board[i][j] = new Square(i, j);
                board[i][j].addActionListener(this); // lägger till ActionListener
                this.buttonBoard.add(board[i][j], i, j);
            }
        }

        setButtonTexts();

        add(this.buttonBoard, BorderLayout.CENTER);
        add(this.mess, BorderLayout.SOUTH);

        setVisible(true);

    }

    private void setButtonTexts() {
        for (int i=0; i<this.size; i++) {
            for (int j=0; j<this.size; j++) {
                this.board[i][j].setText(game.getStatus(i, j));
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Square clickedButton = (Square) ae.getSource(); //vad är det som har valts och byter till Sqaure
        this.game.move(clickedButton.coordinates[0], clickedButton.coordinates[1]); 
        this.mess.setText(game.getMessage());

        this.setButtonTexts();

    }

    public static void main(String[] args) {
        String choice;
        int n;
        Boardgame model;
        if (args.length == 0) {
            choice = "Fifteen";
        } else {
            choice = args[0];
        }
        switch (choice) {
            case "TicTacToe":
                n = 3;
                model = new TicTacToeModel();
                break;
            
            case "Mock":
                n = 6;
                model = new MockObject();
                break;
            
            default:
                n = 4;
                model = new FifteenModel();
                break;
        }

        ViewControl window = new ViewControl(model, n);
    }

}