// Imports:
import java.awt.event.*;

public class Ear implements ActionListener{
    MyButton button;

    public Ear(MyButton button) {
        this.button = button;
    }

    public void actionPerformed(ActionEvent ae) {
        this.button.toogleState();
    }
}
