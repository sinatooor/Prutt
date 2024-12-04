package component;
import java.util.ArrayList;

public abstract class Component {

    protected String name;
    protected double weight;
    protected ArrayList<Component> children;

    public Component(String name, double weight) {
        children = new ArrayList<>();
        this.name = name;
        this.weight = weight;
    }


    abstract public void add(Component component);
    abstract public boolean remove(Component component);
    abstract public Component remove(int index);

    abstract public Component getChild(int index);
    abstract public String toString();
    abstract public String displayInfo();
    abstract public double getWeight();
}
