import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;


public class RPSSkel extends JFrame implements ActionListener {
    Gameboard myboard, computersboard;
    JButton closebutton;
    int counter = 0; // För att räkna ETT... TVÅ... TRE
    RPSModel model;

    public RPSSkel() {
        super("Malte&Sina"); //sätter titel
        model = new RPSModel();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        closebutton = new JButton("Avsluta");

        // Skapa spelplaner
        myboard = new Gameboard("Sina&Malte", this); // 'this' är lyssnare för spelknapparna
        computersboard = new Gameboard("Datorn");

        // Lägg till spelplanerna i en panel
        JPanel boards = new JPanel();
        boards.setLayout(new GridLayout(1, 2));
        boards.add(myboard);
        boards.add(computersboard);

        // Lägg till lyssnare för "Avsluta"-knappen med en anonym inre klass
        closebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.closeConnection();
                System.exit(0);
            }
        });

        // Lägg till komponenterna till ramen
        add(boards, BorderLayout.CENTER);
        add(closebutton, BorderLayout.SOUTH);

        setSize(350, 650);
        setVisible(true);
    }

    // Implementera actionPerformed för spelknapparna
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        counter++;

        if (counter == 1) {
            myboard.resetColor();
            computersboard.resetColor();
            myboard.setLower("ETT...");
            computersboard.setLower("ETT...");
        } else if (counter == 2) {
            myboard.setLower("TVÅ...");
            computersboard.setLower("TVÅ...");
        } else if (counter == 3) {
            // Registrera spelarens drag
            myboard.markPlayed(command);
            myboard.setUpper(command);

            // Skicka spelarens drag till servern och få serverns drag
            String serverMove = model.getServerMove(command);

            // Visa serverns drag
            computersboard.markPlayed(serverMove);
            computersboard.setUpper(serverMove);

            // Avgör vinnaren
            String result = model.determineWinner(command, serverMove);

            // Uppdatera resultat
            myboard.setLower(result);
            computersboard.setLower(result);

            // Uppdatera poäng
            if (result.equals("Du vann!")) {
                myboard.wins();
            } else if (result.equals("Du förlorade!")) {
                computersboard.wins();
            }

            counter = 0; // Återställ räknaren
        }
    }

    public static void main(String[] args) {
        new RPSSkel();
    }
}
