

// Imports 
import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    MyButton[] buttons = new MyButton[2];

    public MyFrame(String s) {
        super(s);
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.getContentPane().setBackground(Color.green);
        this.buttons[0] = new MyButton(new Color[]{Color.blue, Color.red}, new String[]{"Klicka för Röd", "Klicka för Blå"});
        this.buttons[1] = new MyButton(new Color[]{Color.CYAN, Color.orange}, new String[]{"Klicka för Orange", "Klicka för Cyan"});

        for (MyButton button : this.buttons) {
            this.add(button);
        }

        this.setLayout(new FlowLayout());
        
        this.setVisible(true);
    }

    public static void main(String[] u) {
        MyFrame window = new MyFrame("Sina Rajaeeian & Malte Bandmann");
    }
    
}