// Imports:
import java.util.ArrayList;
import java.util.Collections;

public class FifteenModel implements Boardgame{
    
    private int[][] board = new int[4][4];
    private int[] emptyPosition = new int[2];
    private boolean lastMoveDone;
    private String lastMoveMessage;

    // Konstruktor
    public FifteenModel() {

        // konstruera spelplanen
        // slumpar en lista med 16 siffror
        ArrayList<Integer> randomList = new ArrayList<>();
        for (Integer i = 0; i<16; i++) {
            randomList.add(i);
        }
        Collections.shuffle(randomList);

        int index = 0;
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                this.board[i][j] = randomList.get(index);
                if (this.board[i][j] == 0) {
                    emptyPosition[0] = i;
                    emptyPosition[1] = j;
                }
                index++;
            }

        }
    
    }

    public boolean move(int x, int y) {
        boolean insideBoard = (x >= 0) && (x <= 3) && (y >= 0) && (y <= 3);
        boolean moveablePeace = ((x == this.emptyPosition[0] + 1 || x == this.emptyPosition[0] - 1) && (y == this.emptyPosition[1])) ||
                                ((y == this.emptyPosition[1] + 1 || y == this.emptyPosition[1] - 1) && (x == this.emptyPosition[0]));
        if (insideBoard == false) {
            this.lastMoveDone = false;
            this.lastMoveMessage = "Please choose a position within the board!";
        } else if (moveablePeace == false) {
            this.lastMoveDone = false;
            this.lastMoveMessage = "Please choose a position next to the empty one!";
        } else {
            this.lastMoveDone = true;
            this.lastMoveMessage = "OK";
            // make the move
            this.board[this.emptyPosition[0]][this.emptyPosition[1]] = this.board[x][y];
            this.board[x][y] = 0;
            this.emptyPosition[0] = x;
            this.emptyPosition[1] = y;
        }
        return this.lastMoveDone;
    }
    
    public String getStatus(int x, int y) {
        String s;
        if (x == this.emptyPosition[0] && y == this.emptyPosition[1]) {
            s = "  ";
        } else {
            if (this.board[x][y] <= 9) {
                s = " " + String.valueOf(this.board[x][y]);
            } else {
                s = String.valueOf(this.board[x][y]);
            }
        }
        return s;
    }    
    
    public String getMessage() {
        return this.lastMoveMessage;
    }

}
