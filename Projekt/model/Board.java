package model;

import java.io.Serializable; // För att kunna spara/ladda objektet
import java.util.ArrayList;
import java.util.Arrays; // För att kopiera arrays
import java.util.List;
import java.util.Random;

public class Board implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int SIZE = 4; // Storlek på brädet (4x4)
    private Tile[][] grid;
    private int score;
    private Random random = new Random();

    public Board() {
        grid = new Tile[SIZE][SIZE];
        reset();
    }

    /**
     * Kopieringskonstruktor. Skapar en djup kopia av ett annat bräde.
     * Används för att testa drag utan att påverka originalet (t.ex. i AI).
     * @param other Board-objektet som ska kopieras.
     */
    public Board(Board other) {
        this.grid = new Tile[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (other.grid[r][c] != null) {
                    // Skapa en ny Tile med samma värde
                    this.grid[r][c] = new Tile(other.grid[r][c].getValue());
                } else {
                    this.grid[r][c] = null;
                }
            }
        }
        this.score = other.score;
        this.random = new Random(); // Ge kopian sin egen Random-generator
    }

    // Återställer brädet till startläge
    public void reset() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = null; // Använd null för tomma rutor
            }
        }
        score = 0;
        // Lägg till de två första brickorna
        addRandomTile();
        addRandomTile();
    }

    // Lägger till en ny bricka (2 eller 4) på en slumpmässig tom plats
    public boolean addRandomTile() {
        List<Position> emptyPositions = getEmptyPositions();
        if (emptyPositions.isEmpty()) {
            return false; // Inga tomma platser, kan inte lägga till bricka
        }

        Position randomPos = emptyPositions.get(random.nextInt(emptyPositions.size()));
        int value = random.nextInt(10) == 0 ? 4 : 2; // 10% chans för 4, annars 2
        grid[randomPos.row][randomPos.col] = new Tile(value);
        return true;
    }

    // Returnerar en lista med koordinater för alla tomma rutor
    private List<Position> getEmptyPositions() {
        List<Position> emptyPositions = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] == null) {
                    emptyPositions.add(new Position(r, c));
                }
            }
        }
        return emptyPositions;
    }

    /**
     * Utför ett drag i den angivna riktningen.
     * Brickor flyttas så långt som möjligt. Om två brickor med samma värde kolliderar,
     * slås de ihop till en bricka med dubbla värdet och poängen ökas.
     * En bricka som just skapats genom sammanslagning kan inte slås ihop igen i samma drag.
     *
     * @param direction Riktningen att flytta brickorna.
     * @return true om brädet ändrades (minst en bricka flyttades eller slogs ihop), annars false.
     */
     public boolean move(Direction direction) {
         boolean movedOrMerged = false;
         // merged[][] håller reda på vilka brickor som *redan* slagits ihop i detta drag
         boolean[][] merged = new boolean[SIZE][SIZE];

         // Bestäm iterationsordning baserat på riktning för att processa brickor längst bort först
         int rowStart = 0, rowEnd = SIZE, rowInc = 1;
         int colStart = 0, colEnd = SIZE, colInc = 1;

         if (direction == Direction.DOWN) { rowStart = SIZE - 1; rowEnd = -1; rowInc = -1; }
         if (direction == Direction.RIGHT) { colStart = SIZE - 1; colEnd = -1; colInc = -1; }

         for (int r = rowStart; r != rowEnd; r += rowInc) {
             for (int c = colStart; c != colEnd; c += colInc) {
                 if (grid[r][c] == null) continue; // Hoppa över tomma rutor

                 int currentR = r;
                 int currentC = c;
                 Tile currentTile = grid[r][c];

                 // Hitta nästa position i dragriktningen
                 int nextR = currentR + direction.getDeltaRow();
                 int nextC = currentC + direction.getDeltaCol();

                 // Flytta brickan så långt som möjligt
                 while (isValid(nextR, nextC)) {
                     Tile nextTile = grid[nextR][nextC];

                     if (nextTile == null) {
                         // Flytta till tom plats
                         grid[nextR][nextC] = currentTile;
                         grid[currentR][currentC] = null;
                         currentR = nextR; // Uppdatera nuvarande position
                         currentC = nextC;
                         nextR += direction.getDeltaRow(); // Fortsätt i samma riktning
                         nextC += direction.getDeltaCol();
                         movedOrMerged = true;
                     } else if (nextTile.getValue() == currentTile.getValue() && !merged[nextR][nextC]) {
                         // Slå ihop brickor (om de har samma värde och målbrickan inte redan är ett resultat av en merge i detta drag)
                         int newValue = nextTile.getValue() * 2;
                         nextTile.setValue(newValue); // Uppdatera värdet på målbrickan
                         score += newValue;           // Öka poängen
                         grid[currentR][currentC] = null; // Ta bort den flyttade brickan
                         merged[nextR][nextC] = true; // Markera målbrickan som sammanslagen
                         movedOrMerged = true;
                         break; // Stanna efter sammanslagning, brickan kan inte flytta/slå ihop mer
                     } else {
                         // Kan inte flytta längre (blockerad av annan bricka eller redan merged)
                         break;
                     }
                 }
             }
         }
         return movedOrMerged;
     }


    // Kontrollerar om en position är inom brädets gränser
    private boolean isValid(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    // Kontrollerar om spelet är över (inga tomma platser OCH inga möjliga drag)
    public boolean isGameOver() {
        if (!getEmptyPositions().isEmpty()) {
            return false; // Finns tomma platser, spelet fortsätter
        }

        // Kolla om några sammanslagningar är möjliga horisontellt eller vertikalt
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Tile current = grid[r][c];
                if (current == null) continue; // Bör inte hända om getEmptyPositions är tom, men säkrast att kolla

                // Kolla grannar (om de existerar och har samma värde)
                // Höger
                if (isValid(r, c + 1) && grid[r][c + 1] != null && current.getValue() == grid[r][c + 1].getValue()) {
                    return false;
                }
                // Nedåt
                if (isValid(r + 1, c) && grid[r + 1][c] != null && current.getValue() == grid[r + 1][c].getValue()) {
                    return false;
                }
            }
        }

        return true; // Inga tomma platser och inga möjliga drag kvar
    }

    // Getters
    public int getScore() {
        return score;
    }

    // Returnerar en referens till grid-arrayen (för visning i GameView)
    // Var försiktig så att vyn inte modifierar arrayen direkt.
    public Tile[][] getGrid() {
        return grid;
    }

    /**
     * Skapar och returnerar ett GameState-objekt som representerar
     * brädets nuvarande tillstånd (en djup kopia).
     * Används för Ångra och Spara/Ladda.
     * @return Ett nytt GameState-objekt.
     */
    public GameState getState() {
        // Skapa en djup kopia av grid-arrayen
        Tile[][] gridCopy = new Tile[SIZE][SIZE];
        for(int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] != null) {
                    // Viktigt: skapa en *ny* Tile, inte bara kopiera referensen
                    gridCopy[r][c] = new Tile(grid[r][c].getValue());
                } else {
                    gridCopy[r][c] = null;
                }
            }
        }
        // Skapa GameState med kopian av grid och nuvarande poäng
        return new GameState(gridCopy, score);
    }

    /**
     * Återställer brädets tillstånd från ett givet GameState-objekt.
     * Används för Ångra och Spara/Ladda.
     * @param state GameState-objektet att återställa från.
     */
    public void setState(GameState state) {
        if (state == null) return; // Gör inget om state är null

        Tile[][] stateGrid = state.getGrid();
        if (stateGrid == null || stateGrid.length != SIZE || stateGrid[0].length != SIZE) {
             System.err.println("Warning: Invalid GameState grid provided to setState.");
             return; // Försök inte återställa från ett ogiltigt state
         }

        // Kopiera över värdena från state till detta brädes grid
        // Detta är också en djup kopia för att undvika delade Tile-objekt.
        for(int r = 0; r < SIZE; r++) {
             for (int c = 0; c < SIZE; c++) {
                 if (stateGrid[r][c] != null) {
                     // Skapa en ny Tile i detta bräde
                     this.grid[r][c] = new Tile(stateGrid[r][c].getValue());
                 } else {
                     this.grid[r][c] = null; // Sätt till null om den är tom i state
                 }
             }
         }
        // Återställ poängen
        this.score = state.getScore();
    }

    // Enkel intern klass för att representera en position (rad, kolumn)
    // Behöver inte vara Serializable om den bara används internt i Board.
    private static class Position {
        final int row;
        final int col;
        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}