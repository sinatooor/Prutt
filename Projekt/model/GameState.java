package model;

import java.io.Serializable; // Nödvändigt för spara/ladda

// Representerar ett sparat tillstånd av spelet (för ångra, spara/ladda)
// Denna klass måste vara "immutable" eller så måste getGrid returnera en kopia
// för att garantera att historiken inte kan ändras oavsiktligt.
// För enkelhetens skull gör vi fälten final och returnerar referenser,
// men förutsätter att den som tar emot staten inte modifierar den.
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L; // För serialisering

    private final Tile[][] grid; // En **djup kopia** av brädet vid skapandet
    private final int score;

    // Konstruktorn tar emot en kopia av brädet och poängen
    // Viktigt: Denna konstruktor måste ta emot en *faktisk kopia*, inte originalreferensen.
    // Anroparen (t.ex. Board.getState()) ansvarar för att skapa kopian.
    public GameState(Tile[][] gridDeepCopy, int score) {
        this.grid = gridDeepCopy;
        this.score = score;
    }

    // Returnerar kopian av brädet som lagrades
    public Tile[][] getGrid() {
        // Returnerar referensen till den lagrade kopian.
        // För ökad säkerhet skulle man kunna returnera ytterligare en kopia här,
        // men det är oftast overkill och ineffektivt.
        return grid;
    }

    public int getScore() {
        return score;
    }
}