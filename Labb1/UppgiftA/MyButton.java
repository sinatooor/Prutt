// Imports:
import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton{

    Color[] colors;
    String[] labels;
    int state = 0;

    public MyButton(Color[] colors, String[] labels) {
        super();
        this.colors = colors;
        this.labels = labels;
        
        //this.setBounds(50,50,100,20);;
        //this.setBorderPainted(false);
        this.setBackground(colors[0]);
        this.setText(labels[0]);
    }

    public void toogleState() {
        this.state += 1;
        this.setBackground(colors[state % colors.length]);
        this.setText(labels[state % labels.length]);
    }


}
