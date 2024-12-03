
public abstract class Component {

    abstract public void add(Component component);
    abstract public boolean remove(Component component);
    abstract public Component remove(int index);

    abstract public Component getChild(int index);
    abstract public String toString();
    abstract public String displayInfo();
    abstract public double getWeight();
}
