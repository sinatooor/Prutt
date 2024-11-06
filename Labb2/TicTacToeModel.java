public class TicTacToeModel implements Boardgame{

    private boolean lastMoveDone;
    private String lastMoveMessage;
    private int size = 4;
    private String[][] board = new String[this.size][this.size];

    private int round = 1;
    private int turn = 0;

    private String[] symbols = {"X", "O"};

    private int[] movePiece = null;

    public TicTacToeModel() {
        for (int i=0; i<this.size; i++) {
            for(int j=0; j<this.size; j++) {
                this.board[i][j] = "  ";
            }
        }

    }

    public boolean move(int x, int y) {
        boolean isInsideboard = (x>=0 && x<=2 && y>=0 && y<=2);
        
        if (isInsideboard == false) {
            this.lastMoveDone = false;
            this.lastMoveMessage = "Please choose a position within the board!";
        } else {
            if (round <= 3) {
                this.moveFase1(x, y);
            } else {
                this.moveFase2(x, y);
            }
        }
        return this.lastMoveDone;
    }
    
    private void moveFase1(int x, int y) {
        boolean emptySpace = (board[x][y] ==  "  ");
        
        if (emptySpace == false) {
            this.lastMoveDone = false;
            this.lastMoveMessage = "Please choose an empty space! It is " + symbols[turn] + "'s turn.";
        } else {
            this.board[x][y] = this.symbols[turn];
            this.nextTurn();
            this.lastMoveMessage = "It is " + symbols[turn] + "'s turn.";
        }
        
    }
    
    private void moveFase2(int x, int y) {
        boolean ownPiece = (this.board[x][y] == this.symbols[this.turn]);

        // Om ingen egen symbol är vald
        if (this.movePiece == null) {
            if (ownPiece == false) {
                this.lastMoveDone = false;
                this.lastMoveMessage = "choose your own piece It is " + symbols[turn] + "'s turn.";
            } else{
                this.movePiece = new int[]{x, y};
                this.lastMoveDone = true;
                this.lastMoveMessage = "Choose where to move your symbol. It is " + symbols[turn] + "'s turn.";
            }
        } else { // Om ens egna symbol är vald
            boolean emptySpace = (board[x][y] == "  ");
            boolean adjacentSpace = (Math.abs(x-movePiece[0]) <= 1 && Math.abs(y - movePiece[1]) <= 1);
            if (ownPiece == true) {
                this.movePiece = new int[]{x, y};
                this.lastMoveDone = true;
                this.lastMoveMessage = "Choose where to move your symbol. It is " + symbols[turn] + "'s turn.";
            } else if (emptySpace == false) {
                this.lastMoveDone = false;
                this.lastMoveMessage = "Please choose an empty space! It is " + symbols[turn] + "'s turn.";
            } else if (adjacentSpace == false) {
                this.lastMoveDone = false;
                this.lastMoveMessage = "Please choose a space adjecent to your choosen piece. It is " + symbols[turn] + "'s turn.";
            } else {
                this.board[movePiece[0]][movePiece[1]] = "  ";
                this.board[x][y] = symbols[turn];
                this.nextTurn();
                this.lastMoveDone = true;
                this.lastMoveMessage = "It is " + symbols[turn] + "'s turn.";
                this.movePiece = null; //nullar inför nästa runda 

            }
        }
    }

    private void nextTurn() {
        if (this.turn == 1) {
            this.round++;
        }
        this.turn = (this.turn + 1) % 2;
    }

    public String getStatus(int x, int y) {
        return board[x][y];
    }

    public String getMessage() {
        return this.lastMoveMessage;

    }
    
}
