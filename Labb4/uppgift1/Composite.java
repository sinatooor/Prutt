
import java.util.ArrayList;

public class Composite extends Component{
    private ArrayList<Component> children = new ArrayList<>();
    private String name;
    private double weight;

    public Composite(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    @Override
    public void add(Component component) {
        children.add(component);
    }

    @Override
    public boolean remove(Component component) {
        return children.remove(component);
    }

    @Override
    public Component remove(int index) {
        return children.remove(index);
    }

    @Override
    public Component getChild(int index) {
        return children.get(index);
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
}
