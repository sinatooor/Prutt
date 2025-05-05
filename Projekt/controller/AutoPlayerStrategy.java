package controller;

import model.Board;
import model.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Ett interface eller abstrakt klass kan användas för flera strategier.
// Här är ett enkelt exempel på en konkret slumpmässig strategi.
public class AutoPlayerStrategy {

    private Random random = new Random();

    /**
     * Väljer ett slumpmässigt, giltigt drag för det givna brädet.
     * Ett giltigt drag är ett drag som faktiskt ändrar brädets tillstånd.
     * @param board Det nuvarande spelbrädet.
     * @return En slumpmässig giltig Direction, eller null om inga drag är möjliga.
     */
    public Direction chooseMove(Board board) {
        List<Direction> possibleMoves = new ArrayList<>();

        // Testa alla fyra riktningar
        for (Direction dir : Direction.values()) {
            // Skapa en djup kopia av brädet för att testa draget på
            // Kräver att Board har en fungerande kopieringskonstruktor!
            Board testBoard = new Board(board);

            // Utför test-draget på kopian
            boolean moveWasPossible = testBoard.move(dir);

            // Om draget ändrade kopians tillstånd, är det ett giltigt drag
            if (moveWasPossible) {
                possibleMoves.add(dir);
            }
        }

        if (possibleMoves.isEmpty()) {
            return null; // Inget drag är möjligt (borde innebära Game Over)
        }

        // Välj ett slumpmässigt drag från listan av möjliga drag
        int randomIndex = random.nextInt(possibleMoves.size());
        return possibleMoves.get(randomIndex);
    }

    // Man kan lägga till fler metoder här för andra strategier, t.ex.:
    // public Direction chooseGreedyMove(Board board) { ... }
    // public Direction chooseCornerPriorityMove(Board board) { ... }
}