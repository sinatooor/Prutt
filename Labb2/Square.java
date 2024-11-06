import java.awt.*;
import javax.swing.*;

public class Square extends JButton{

    int[] coordinates = new int[2];

    public Square(int x, int y) {
        coordinates[0] = x;
        coordinates[1] = y;
    }

    //method overriding:
    /*Customize or extend the functionality of a method provided by the superclass.
    Modify the behavior of the inherited method to better suit the needs of the subclass. */

    @Override
    public void setText(String text) {
        super.setText(text); // Calls the original setText from JButton
        if (text == "  ") {
            super.setBackground(Color.BLACK);
        } else {
            super.setBackground(Color.red);
        }
    }
    
}
