import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    

    public MyFrame() {

        setTitle("Författare: Ditt Namn Här");
        

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        getContentPane().setBackground(Color.CYAN);
        
        // storleken på fönstret
        setSize(400, 300);
        

        setVisible(true);
    }


    public static void main(String[] args) {
        // Skapa ett nytt MyFrame-objekt
        new MyFrame();
    }
}
