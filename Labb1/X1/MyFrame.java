// Imports 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyFrame extends JFrame implements ActionListener{

    MyButton[] buttons;

    public MyFrame(String s, String[] labels) {
        super(s);
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.getContentPane().setBackground(Color.green);

        int n = Integer.parseInt(labels[0]);
        this.buttons = new MyButton[n];
        for (int i=0; i<n; i++) {
            this.buttons[i] = new MyButton(new String[]{labels[2*i+1], labels[2*i+2]});
            this.add(this.buttons[i]);
            this.buttons[i].addActionListener(this);
        }

        this.setLayout(new FlowLayout());
        
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        for (MyButton button : buttons) {
            button.toogleState();
        }
    } 

    public static void main(String[] u) {
        MyFrame window = new MyFrame("Malte Bandmann", u);
    }
    
}