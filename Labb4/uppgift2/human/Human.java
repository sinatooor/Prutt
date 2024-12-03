package human;

public abstract class Human {

    protected String name;
    protected String pnr;
    protected String gender;

    Human(String name, String pnr) {
        this.name = name;
        this.pnr = pnr;
    }

    public static Human create(String name, String pnr) {
        int nr = Character.getNumericValue(pnr.charAt(9));
        System.out.println(nr);
        if (nr == 0) {
            return new NonBinary(name, pnr);
        } else if (nr % 2 == 0) {
            return new Woman(name, pnr);
        } else {
            return new Man(name, pnr);
        }
    }

    @Override
    public String toString() {
        return name + " : " + gender + " : " + pnr;
    }
}