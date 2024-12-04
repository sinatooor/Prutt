package component;

import java.util.ArrayList;
import java.util.Iterator;

public class DjupetFörstIterator implements Iterator<Component>{

    ArrayList<Component> list = new ArrayList<>();

    DjupetFörstIterator(Component co) {
        createList(co);
    }

    private void createList(Component co) {
        this.list.add(co);
        if (co instanceof Composite) {
            for (Component component : co.children) {
                createList(component);
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !list.isEmpty();
    }

    @Override
    public Component next() {
        Component nexComponent = list.get(0);
        list.remove(0);
        return nexComponent;
    }
    
}
