package human;

public class NonBinary extends Human{

    protected NonBinary(String name, String pnr) {
        super(name, pnr);
        this.gender = "nonbinary";
    }
}
