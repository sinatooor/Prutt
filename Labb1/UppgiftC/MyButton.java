// Imports:
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyButton extends JButton {

    Ear ear = new Ear(this);
    Color[] colors;
    String[] labels;
    int state = 0;

    public MyButton(Color[] colors, String[] labels) {
        super();
        this.colors = colors;
        this.labels = labels;

        this.addActionListener(this.ear);
        
        this.setBackground(colors[0]);
        this.setText(labels[0]);
    }

    public void toogleState() {
        this.state += 1;
        this.setBackground(colors[state % colors.length]);
        this.setText(labels[state % this.labels.length]);
    }


}
