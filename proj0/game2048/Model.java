package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
@SuppressWarnings("SpellCheckingInspection")
public class Model extends Observable {
    /**
     * Current contents of the board.
     */
    private Board board;
    /**
     * Current score.
     */
    private int score;
    /**
     * Maximum score so far.  Updated when game ends.
     */
    private int maxScore;
    /**
     * True iff game is ended.
     */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /**
     * Largest piece value.
     */
    public static final int MAX_PIECE = 2048;

    /**
     * A new 2048 game on a board of size SIZE with no pieces
     * and score 0.
     */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /**
     * A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes.
     */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /**
     * Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     * 0 <= COL < size(). Returns null if there is no tile there.
     * Used for testing. Should be deprecated and removed.
     */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /**
     * Return the number of squares on one side of the board.
     * Used for testing. Should be deprecated and removed.
     */
    public int size() {
        return board.size();
    }

    /**
     * Return true iff the game is over (there are no moves, or
     * there is a tile with value 2048 on the board).
     */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /**
     * Return the current score.
     */
    public int score() {
        return score;
    }

    /**
     * Return the current maximum game score (updated at end of game).
     */
    public int maxScore() {
        return maxScore;
    }


    /**
     * Clear the board to empty and reset the score.
     */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /**
     * Add TILE to the board. There must be no Tile currently at the
     * same position.
     */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    public void mainloop() {
        int size = size();
        boolean[][] mergedlist = new boolean[size][size];
        int moves = 0;
        for (int i = size - 2; i > -1; i--) { // only need to iterate on second row and below
            for (int j = 0; j < size; j++) {
                Tile tile = board.tile(j, i);
                if (tile != null) {
                    mergedlist = movetohighestfreetile(tile, mergedlist, i, j);
                }
            }
        }
    }

    public boolean mergable(Tile tile, Tile tileabove, boolean[][] mergedlist, int k, int j) {
        if (tileabove.value() == tile.value() && !mergedlist[j][k]) { //tile above not merged
            return true;
        } else {
            return false;
        }
    }

    public boolean[][] movetohighestfreetile(Tile tile, boolean[][] mergedlist, int i, int j) {
        int size = size();
        int highest = i;
        //boolean movable = false;
        for (int k = i + 1; k < size; k++) { //for each tile above current, want to see if its empty or mergable. once you hit one thats not (or the top),
            // set highest to be the last valid square and move there
            Tile tileabove = board.tile(j, k); //some tile above
            if (tileabove == null) {
                highest = k;
            }
            else if (mergable(tile, tileabove, mergedlist, k, j)) {
                highest = k;
                mergedlist[j][k] = true; //set mergedlist value to true ie dont merge
                score += tile.value() * 2;
            }
            else {
                board.move(j, highest, tile);
                return mergedlist;
            }
        }
        board.move(j, highest, tile);
        return mergedlist;
    }

    public int[][] boardarray(){
        int size = size();
        int[][] board_arr = new int[size][size];
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                Tile tile = board.tile(j,i);
                if (tile == null){
                    board_arr[j][i] = 0;
                }
                else{
                    board_arr[j][i] = tile(j, i).value();
                }
            }
        }
        return board_arr;
    }
    public boolean tilt(Side side) {
        boolean changed = false;
        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        //score changes if >0 tiles combine. its incremented by the sum of all combined tiles (so if 2+2 and 4+4 merge, increase score by 4+8 = 12
        int[][] old_board = boardarray();
        board.setViewingPerspective(side);

        int size = size();
        mainloop();
        board.setViewingPerspective(Side.NORTH);
        int[][] new_board = boardarray();
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (old_board[j][i] != new_board[j][i]){
                    changed = true;
                }
            }
        }
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        int size = b.size();
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (b.tile(i, j) == null){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int size = b.size();
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                Tile t = b.tile(i, j);
                if (t != null) {
                    if (t.value() == MAX_PIECE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // two ways a move exists: a tile is empty, or adjacent tiles with same values
        int size = b.size();
        if (emptySpaceExists(b) == true){
            return true;
        }
        else {
            //now check for adjacent same numbers
            for (int i = 0; i < size; i++){
                for (int j = 0; j < size; j++){
                    Tile t = b.tile(i, j);
                    int val = t.value();
                    //only need to test (max) four squares: up, down, left right
                    int moves[][] = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                    for (int[] x: moves){
                        if (i + x[0] >= 0 && i + x[0] < size && j + x[1] >= 0 && j + x[1] < size){ //validates index
                            Tile adjtile = b.tile(i+x[0], j +x[1]);
                            if (adjtile.value() == t.value()){
                                return true;
                            }
                        }

                    }
                }
            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
