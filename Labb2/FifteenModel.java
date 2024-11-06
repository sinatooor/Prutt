// Imports:
import java.util.ArrayList;
import java.util.Collections;

public class FifteenModel implements Boardgame{
    
    private int[][] board;
    // matrix for the game
    private int[] emptyPosition = new int[2];
    private boolean lastMoveDone;
    private String lastMoveMessage;
    private int size = 4;

    // Konstruktor
    public FifteenModel() {
        board = new int[this.size][this.size];
        // konstruera spelplanen
        // slumpar en lista med n^2 siffror
        ArrayList<Integer> randomList = new ArrayList<>();
        for (Integer i = 0; i<Math.pow(this.size, 2) ; i++) {
            randomList.add(i);
        }
        Collections.shuffle(randomList);

        int index = 0;
        for (int i=0; i<this.size; i++) {
            for (int j=0; j<this.size; j++) {
                this.board[i][j] = randomList.get(index); //adds randomlist to matrix 
                if (this.board[i][j] == 0) {
                    emptyPosition[0] = i;
                    emptyPosition[1] = j;
                }
                index++;
            }

        }
    
    }

    public boolean move(int x, int y) {
        boolean insideBoard = (x >= 0) && (x < this.size) && (y >= 0) && (y < this.size);
        boolean moveablePeace = ((x == this.emptyPosition[0] + 1 || x == this.emptyPosition[0] - 1) && (y == this.emptyPosition[1])) ||
                                ((y == this.emptyPosition[1] + 1 || y == this.emptyPosition[1] - 1) && (x == this.emptyPosition[0]));
                                // om x är samma och y varierar och om y är samma och x ändras
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
            this.board[this.emptyPosition[0]][this.emptyPosition[1]] = this.board[x][y]; //rutan med noll omvandlas till den valda rutan
            this.board[x][y] = 0; //den valda blir noll
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
                s = " " + String.valueOf(this.board[x][y]); //byter till string 
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
