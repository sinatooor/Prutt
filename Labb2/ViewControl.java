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
    private JLabel buttonBoard = new JLabel();

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

        for (int i=0; i<this.size; i++) {
            for (int j=0; j<this.size; j++) {
                board[i][j] = new Square(i, j);
                board[i][j].addActionListener(this);
                this.buttonBoard.add(board[i][j], i, j);
            }
        }

        setButtonTexts();

        add(this.buttonBoard, BorderLayout.CENTER);
        add(this.mess, BorderLayout.SOUTH);

        setVisible(true);

    }

    public void setButtonTexts() {
        for (int i=0; i<this.size; i++) {
            for (int j=0; j<this.size; j++) {
                this.board[i][j].setText(game.getStatus(i, j));
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Square clickedButton = (Square) ae.getSource();
        this.game.move(clickedButton.coordinates[0], clickedButton.coordinates[1]);
        this.mess.setText(game.getMessage());

        this.setButtonTexts();

    }

    public static void main(String[] args) {
        //int n = 4;
        //Boardgame model = new FifteenModel();

        //int n = 6;
        //Boardgame model = new MockObject();

        int n = 3;
        Boardgame model = new TicTacToeModel();

        ViewControl window = new ViewControl(model, n);
    }

}