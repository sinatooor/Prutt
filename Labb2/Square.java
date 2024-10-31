import java.awt.*;
import javax.swing.*;

public class Square extends JButton{

    int[] coordinates = new int[2];

    public Square(int x, int y) {
        coordinates[0] = x;
        coordinates[1] = y;
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        if (text == "  ") {
            super.setBackground(Color.BLACK);
        } else {
            super.setBackground(Color.red);
        }
    }
    
}
