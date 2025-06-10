package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the game board for 2048.
 * Manages the grid of tiles, score, and game logic such as moving tiles,
 * adding random tiles, and checking for game over conditions.
 * It also supports saving and loading game states.
 */
public class Board {
    /**
     * The size of the grid (e.g., 4 for a 4x4 grid).
     */
    public static final int SIZE = 4;
    /**
     * The 2D array representing the grid of tiles.
     */
    private Tile[][] grid;
    /**
     * The current score of the game.
     */
    private int score;
    /**
     * Random number generator for adding new tiles.
     */
    private Random random = new Random();
    /**
     * Stores the position (row and column) of the most recently added random tile.
     * This is used by the view to animate the new tile.
     */
    private Position lastAddedPos; // Håller koll på den nya brickan

    /**
     * Constructs a new Board and initializes the game by resetting the grid
     * and adding two random tiles.
     */
    public Board() {
        grid = new Tile[SIZE][SIZE];
        reset();
    }

    /**
     * Constructs a new Board as a deep copy of another Board.
     * This is useful for operations like undo or AI simulations that require
     * exploring future states without modifying the current board.
     * @param other The Board to copy.
     */
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

    /**
     * Resets the board to its initial state: an empty grid, zero score,
     * and two new random tiles.
     */
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

    /**
     * Adds a random tile (either 2 or 4, with 4 appearing less frequently)
     * to an empty position on the board.
     * Updates the {@link #lastAddedPos} field with the position of the new tile.
     * @return true if a tile was successfully added, false if the board is full
     *         and no tile could be added.
     */
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

    /**
     * Gets a list of all currently empty positions (cells with no tiles) on the board.
     * @return A list of {@link Position} objects representing empty cells.
     *         Returns an empty list if the board is full.
     */
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
     * Moves tiles on the board in the specified direction and merges adjacent tiles
     * of the same value.
     * A move consists of shifting all tiles as far as possible in the given direction.
     * If two tiles of the same value collide during a move, they merge into a single
     * tile with double the value, and the score is updated. A tile can only merge
     * once per move.
     * @param direction The {@link Direction} in which to move the tiles (UP, DOWN, LEFT, RIGHT).
     * @return true if any tiles were moved or merged, false otherwise (i.e., the board
     *         state did not change).
     */
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

    /**
     * Checks if a given row and column index are within the valid bounds of the grid.
     * @param r The row index.
     * @param c The column index.
     * @return true if the position (r, c) is valid (i.e., 0 <= r < SIZE and 0 <= c < SIZE),
     *         false otherwise.
     */
    private boolean isValid(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    /**
     * Checks if the game is over.
     * The game is over if there are no empty cells on the board and no adjacent
     * tiles (horizontally or vertically) have the same value (meaning no more
     * merges are possible).
     * @return true if the game is over, false otherwise.
     */
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

    /**
     * Gets the current score of the game.
     * The score increases when tiles are merged.
     * @return The current score.
     */
    public int getScore() { return score; }

    /**
     * Gets the current grid of tiles.
     * Returns a direct reference to the internal 2D array of {@link Tile} objects.
     * Be cautious when modifying this array directly, as it can affect the game state.
     * Consider using {@link #getState()} for a safe copy if modifications are planned.
     * @return A 2D array of {@link Tile} objects representing the grid.
     */
    public Tile[][] getGrid() { return grid; }

    /**
     * Gets the current state of the game, including a deep copy of the grid and the current score.
     * This is useful for saving the game state or for undo functionality, as it provides
     * a snapshot of the board that is independent of the current game board.
     * @return A {@link GameState} object representing the current state of the game.
     */
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

    /**
     * Sets the state of the game (grid and score) from a given {@link GameState} object.
     * This performs a deep copy of the provided state's grid into the current board.
     * If the provided state or its grid is null or has incorrect dimensions,
     * a warning is printed to standard error, and the board remains unchanged.
     * @param state The {@link GameState} object to restore. If null, the method returns early.
     */
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

    /**
     * Represents a position (row and column) on the game board.
     * This is an immutable value object.
     */
    public static class Position {
        /**
         * The row index of the position (0-indexed).
         */
        public final int row;
        /**
         * The column index of the position (0-indexed).
         */
        public final int col;

        /**
         * Constructs a new Position with the specified row and column.
         * @param row The row index.
         * @param col The column index.
         */
        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
    
    /**
     * Gets the position where the last random tile was added.
     * This can be used by the view to highlight or animate the newly appeared tile.
     * @return The {@link Position} of the last added tile. Returns null if no tile
     *         has been added yet via {@link #addRandomTile()} since the last reset or board creation.
     */
    public Position getLastAddedPos() {
        return lastAddedPos;
    }
}