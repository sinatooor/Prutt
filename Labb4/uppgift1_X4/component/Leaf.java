package component;

public class Leaf extends Component{

    public Leaf(String name, double weight) {
        super(name, weight);
    }

    @Override
    public void add(Component component) {
        System.out.println("You can not add to a leaf");
    }

    @Override
    public boolean remove(Component component) {
        System.out.println("You can not add to a leaf");
        return false;
    }

    @Override
    public Component remove(int index) {
        System.out.println("You can not remove from a leaf");
        return this;
    }

    @Override
    public Component getChild(int index) {
        System.out.println("A leaf doesn't have any children");
        return this;
    }

    @Override
    public String toString() {
        return this.name;

    }

    @Override
    public String displayInfo() {
        return this.toString() + ", ";
    }

    @Override
    public double getWeight() {
        return this.weight;
    }
    
}
