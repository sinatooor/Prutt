import java.util.Iterator;
import component.*;

public class IteratorTest {
    public static void main(String[] args) {
        Composite suitcase = new Composite("suitcase", 4);
        Client.fillTheSuitcase(suitcase);

        for (Component co : suitcase) {
            System.out.println(co);
        }

        Iterator<Component> iter = suitcase.iterator();
        for ( ; iter.hasNext(); ) {
            System.err.println(iter.next());
        }
    }
}