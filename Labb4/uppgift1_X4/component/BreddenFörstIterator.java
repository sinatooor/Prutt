package component;

import java.util.ArrayList;
import java.util.Iterator;

public class BreddenFörstIterator implements Iterator<Component>{

    private ArrayList<Component> list = new ArrayList<>();

    BreddenFörstIterator(Composite co) {
        this.list.add(co);
    }

    @Override
    public boolean hasNext() {
        return !this.list.isEmpty();
    }

    @Override
    public Component next() {
        Component nextComponent;
        nextComponent = list.get(0);
        if (this.hasNext()) {
            if (nextComponent instanceof Composite) {
                for (Component co : nextComponent.children) {
                    list.add(co);
                }
            }
            list.remove(0);
        }
        return nextComponent;
    }
    
}
