package component;

import java.util.Iterator;

public class Composite extends Component implements Iterable<Component>{

    public Composite(String name, double weight) {
        super(name, weight);
    }

    @Override
    public void add(Component component) {
        this.children.add(component);
    }

    @Override
    public boolean remove(Component component) {
        return this.children.remove(component);
    }

    @Override
    public Component remove(int index) {
        return this.children.remove(index);
    }

    @Override
    public Component getChild(int index) {
        return this.children.get(index);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String displayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name + ", ");

        for (Component component : children) {
            sb.append(component.displayInfo());
        }
        return sb.toString();
    }

    @Override
    public double getWeight() {
        double totalweight = this.weight;

        for (Component component : children) {
            totalweight += component.getWeight();
        }
        return totalweight;
    }

    @Override
    public Iterator<Component> iterator() {
        //return new BreddenFörstIterator(this);
        return new DjupetFörstIterator(this);
    }
}
