import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class RPSSkel extends JFrame {
    Gameboard myboard, computersboard;
    JButton closebutton;
    RPSController controller;

    public RPSSkel(RPSController controller) {
        this.controller = controller;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        closebutton = new JButton("Avsluta");
        closebutton.addActionListener(new CloseListener());

        // Skapa spelplaner
        myboard = new Gameboard("Du", new ButtonListener());
        computersboard = new Gameboard("Datorn");

        JPanel boards = new JPanel();
        boards.setLayout(new GridLayout(1, 2));
        boards.add(myboard);
        boards.add(computersboard);

        add(boards, BorderLayout.CENTER);
        add(closebutton, BorderLayout.SOUTH);
        setSize(350, 650);
        setVisible(true);
    }

    // Uppdatera vyn baserat på modellens data
    public void updateView(String playerMove, String serverMove, String result) {
        myboard.markPlayed(playerMove);
        myboard.setUpper(playerMove);

        computersboard.markPlayed(serverMove);
        computersboard.setUpper(serverMove);

        myboard.setLower(result);
        computersboard.setLower(result);

        if (result.equals("Du vann!")) {
            myboard.wins();
        } else if (result.equals("Du förlorade!")) {
            computersboard.wins();
        }
    }

    // Lyssnare för spelknapparna
    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.handlePlayerMove(e.getActionCommand());
        }
    }

    // Lyssnare för Avsluta-knappen
    class CloseListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.closeConnection();
            System.exit(0);
        }
    }
}
