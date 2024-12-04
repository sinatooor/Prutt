import component.*;

public class Client {

    public static Composite fillTheSuitcase(Composite suitcase) {
        
        // create a toiletrybag
        Composite toiletrybag = new Composite("necessär", 0.2);
        
        String[] toiletrys = new String[]{"tandborste", "parfym", "tandkräm", "Hårborste"};
        double[] toiletrysWeights = new double[]{0.050, 0.100, 0.090, 0.3};
        
        for (int i=0; i<toiletrys.length; i++) {
            toiletrybag.add(new Leaf(toiletrys[i], toiletrysWeights[i]));
        }
        
        // create a plasic bag with a soap and shampoo in it
        Composite plasticBag = new Composite("plastic bag", 0.002);
        plasticBag.add(new Leaf("tvål", 0.010));
        plasticBag.add(new Leaf("schampo", 0.4));
    
        // adding plastic bag to toiletrybag
        toiletrybag.add(plasticBag);

        // adding toiletrybag to suitcase
        suitcase.add(toiletrybag);
    
        // adding some clothes
        String[] clothes = new String[]{"jacka", "jeans", "skor", "t-shirt", "hoodie"};
        double[] clothesWeights = new double[]{1.4, 1.1, 0.9, 0.7, 0.96};
        
        for (int i=0; i<clothes.length; i++) {
            suitcase.add(new Leaf(clothes[i], clothesWeights[i]));
        }

        return suitcase;
    }

    public static Composite removeFromSuitcase(Composite suitcase) {
        // ta bort tandborsten ur neccesären
        suitcase.getChild(0).remove(0);

        // ta bort schampo
        suitcase.getChild(0).getChild(3).remove(1);

        // ta bort jeans
        suitcase.remove(2);

        return suitcase;
    }
    
    public static void main(String[] args) {
        Composite suitcase = new Composite("suitcase", 4);
        fillTheSuitcase(suitcase);

        System.out.println(suitcase.displayInfo());
        System.out.println(suitcase.getWeight());

        removeFromSuitcase(suitcase);

        System.out.println(suitcase.displayInfo());
        System.out.println(suitcase.getWeight());
        


    }
}
