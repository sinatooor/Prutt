// Imports:
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyButton extends JButton implements ActionListener{

    Color[] colors;
    String[] labels;
    int state = 0;

    public MyButton(Color[] colors, String[] labels) {
        super();
        this.colors = colors;
        this.labels = labels;

        this.addActionListener(this);
        
        this.setBackground(colors[0]);
        this.setText(labels[0]);
    }

    public void actionPerformed(ActionEvent ae) {
        this.toogleState();
    }

    public void toogleState() {
        this.state += 1;
        this.setBackground(colors[state % colors.length]);
        this.setText(labels[state % this.labels.length]);
    }


}
