// Imports:
import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {

    Color[] colors = {Color.blue, Color.red};
    String[] labels;
    int state = 0;

    public MyButton(String[] labels) {
        super();
        this.labels = labels;
        
        this.setBackground(colors[0]);
        this.setText(labels[0]);
    }

    public void toogleState() {
        this.state += 1;
        this.setBackground(colors[state % colors.length]);
        this.setText(labels[state % this.labels.length]);
    }

}
