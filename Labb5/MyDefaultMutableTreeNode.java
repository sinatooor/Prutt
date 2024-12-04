import javax.swing.tree.DefaultMutableTreeNode;

public class MyDefaultMutableTreeNode extends DefaultMutableTreeNode{
    private String description;
    private String rank;
    
    MyDefaultMutableTreeNode(String rank, String name, String description) {
        super(name);
        this.description = description;
        this.rank = rank;
    }

    public String getRank() {
        return this.rank;
    }

    public String getDescription() {
        return this.description;
    }
    
    static String lineToName(String s) {
        return s.split(String.valueOf('"'))[1];
    }

    static String lineToLevel(String s) {
        return s.split(">")[0].replaceFirst("<", "").split(" ")[0];
    }

    static String lineToDescription(String s) {
        return s.split(">")[1].strip();
    }

}
