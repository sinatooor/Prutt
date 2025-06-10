package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    public static final int SIZE = 4;
    private Tile[][] grid;
    private int score;
    private Random random = new Random();
    private Position lastAddedPos; // Håller koll på den nya brickan

    public Board() {
        grid = new Tile[SIZE][SIZE];
        reset();
    }

    public Board(Board other) {
        this.grid = new Tile[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (other.grid[r][c] != null) {
                    this.grid[r][c] = new Tile(other.grid[r][c].getValue());
                } else {
                    this.grid[r][c] = null;
                }
            }
        }
        this.score = other.score;
        this.random = new Random();
    }

    public void reset() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = null;
            }
        }
        score = 0;
        addRandomTile();
        addRandomTile();
    }

    public boolean addRandomTile() {
        List<Position> emptyPositions = getEmptyPositions();
        if (emptyPositions.isEmpty()) {
            return false;
        }
        this.lastAddedPos = emptyPositions.get(random.nextInt(emptyPositions.size()));
        int value = random.nextInt(10) == 0 ? 4 : 2;
        grid[lastAddedPos.row][lastAddedPos.col] = new Tile(value);
        return true;
    }

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

    public boolean move(Direction direction) {
        boolean movedOrMerged = false;
        boolean[][] merged = new boolean[SIZE][SIZE];
        int rowStart = 0, rowEnd = SIZE, rowInc = 1;
        int colStart = 0, colEnd = SIZE, colInc = 1;
        if (direction == Direction.DOWN) { rowStart = SIZE - 1; rowEnd = -1; rowInc = -1; }
        if (direction == Direction.RIGHT) { colStart = SIZE - 1; colEnd = -1; colInc = -1; }
        for (int r = rowStart; r != rowEnd; r += rowInc) {
            for (int c = colStart; c != colEnd; c += colInc) {
                if (grid[r][c] == null) continue;
                int currentR = r;
                int currentC = c;
                Tile currentTile = grid[r][c];
                int nextR = currentR + direction.getDeltaRow();
                int nextC = currentC + direction.getDeltaCol();
                while (isValid(nextR, nextC)) {
                    Tile nextTile = grid[nextR][nextC];
                    if (nextTile == null) {
                        grid[nextR][nextC] = currentTile;
                        grid[currentR][currentC] = null;
                        currentR = nextR;
                        currentC = nextC;
                        nextR += direction.getDeltaRow();
                        nextC += direction.getDeltaCol();
                        movedOrMerged = true;
                    } else if (nextTile.getValue() == currentTile.getValue() && !merged[nextR][nextC]) {
                        int newValue = nextTile.getValue() * 2;
                        nextTile.setValue(newValue);
                        score += newValue;
                        grid[currentR][currentC] = null;
                        merged[nextR][nextC] = true;
                        movedOrMerged = true;
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        return movedOrMerged;
    }

    private boolean isValid(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    public boolean isGameOver() {
        if (!getEmptyPositions().isEmpty()) {
            return false;
        }
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Tile current = grid[r][c];
                if (current == null) continue;
                if (isValid(r, c + 1) && grid[r][c + 1] != null && current.getValue() == grid[r][c + 1].getValue()) {
                    return false;
                }
                if (isValid(r + 1, c) && grid[r + 1][c] != null && current.getValue() == grid[r + 1][c].getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getScore() { return score; }
    public Tile[][] getGrid() { return grid; }

    public GameState getState() {
        Tile[][] gridCopy = new Tile[SIZE][SIZE];
        for(int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] != null) {
                    gridCopy[r][c] = new Tile(grid[r][c].getValue());
                } else {
                    gridCopy[r][c] = null;
                }
            }
        }
        return new GameState(gridCopy, score);
    }

    public void setState(GameState state) {
        if (state == null) return;
        Tile[][] stateGrid = state.getGrid();
        if (stateGrid == null || stateGrid.length != SIZE || stateGrid[0].length != SIZE) {
             System.err.println("Warning: Invalid GameState grid provided to setState.");
             return;
         }
        for(int r = 0; r < SIZE; r++) {
             for (int c = 0; c < SIZE; c++) {
                 if (stateGrid[r][c] != null) {
                     this.grid[r][c] = new Tile(stateGrid[r][c].getValue());
                 } else {
                     this.grid[r][c] = null;
                 }
             }
         }
        this.score = state.getScore();
    }

    public static class Position {
        public final int row;
        public final int col;
        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
    
    public Position getLastAddedPos() {
        return lastAddedPos;
    }
}