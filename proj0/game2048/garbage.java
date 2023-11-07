/**package game2048;

public class garbage {
    public boolean[][] iterateabove(Tile tile, boolean[][] mergedlist){
    int i = tile.row();
    int j = tile.col();
    int size = size();
    for (int k = i + 1; k < size; k++) {
        if (k == size - 1){
            return mergedlist;
        }
        else {
            if (board.tile(j, k) == null) { //empty above
                board.move(j, k, tile);
                mergedlist[j][k] = mergedlist[j][i]; // move in case moving already merged tile
                mergedlist[j][i] = false; //now empty
                iterateabove(tile, mergedlist);
            } else if (board.tile(j, k).value() == tile.value() && !mergedlist[j][k] && !mergedlist[j][i]) { //can merge
                score += tile.value() * 2;
                board.move(j, k, tile);
                mergedlist[j][k] = true; //new tile is merged
                mergedlist[j][i] = false; //old tile (null) not merged
                iterateabove(tile, mergedlist);
            }
        }
    }
    return mergedlist;
}

    public boolean[][]iterateabove2(Tile tile, int j, int i, boolean[][] mergedlist){
        Tile tile = board.tile(j, i);
        int size = size();
        if (i == size - 1){ //if at top, stop
            return mergedlist;
        }
        else { //if not, proceed
            Tile tile2 = board.tile(j, i+1); //get tile above
            if (tile2 == null){
                System.out.print("empty tile above. moving up\n");
                board.move(j, i+1, tile);
                //mergedlist[j][i+1] = mergedlist[j][i];
                //mergedlist[j][i] = false;
                iterateabove2(j, i+1, mergedlist);//if above is empty, move it there then check it again
            }
            else if (tile2.value() == tile.value() && !mergedlist[j][i+1] && !mergedlist[j][i]) { // if tile above has same value and both not merged
                System.out.print("mergable tile above. merging\n");
                score += tile.value()*2;
                board.move(j, i+1, tile); //merge
                mergedlist[j][i+1] = true;
                iterateabove2(j, i+1, mergedlist);
            }
            else{
                return mergedlist;
            }
        }
        return mergedlist;
    }
    public boolean checkabove(){
        int size = size();
        boolean[][] mergedlist = new boolean[size][size];
        boolean x = false; //changed bool
        for (int i = size - 1; i > -1; i--){ //for each row (starting at top of board)
            for (int j = 0; j < size; j++){ //for each column in row
                System.out.print("checked col " + j + ", row " +i+"\n");
                System.out.print(board + "\n");
                Tile tile = board.tile(j,i);
                if (tile != null) { //only for non-empty tiles
                    //if (i == size - 1){
                    //return mergedlist; //do nothing
                    System.out.print(tile.value());
                    if (i == size - 2){ //top row
                        Tile tabove = board.tile(j, i + 1);
                        if (tabove == null){
                            board.move(j, i+1, tile);
                            System.out.print(board);
                            x = true;
                        }
                        else if ((tile.value() == tabove.value()) && !mergedlist[j][i + 1] && !mergedlist[j][i]){ //since top row, none will be merged
                            board.move(j, i + 1, tile);
                            System.out.print(board);
                            //score += tile.value() * 2;
                            mergedlist[j][i+1] = true;
                            x = true;
                        }
                    }
                    else {
                        for (int k = i + 1; k < size; k++) { //one problem is if square above is full but two above is empty or the same
                            if (board.tile(j, k) == null) { //empty above
                                x = true;
                                board.move(j, k, tile);
                                System.out.print(board);
                                mergedlist[j][k] = mergedlist[j][i]; // move in case moving already merged tile
                                mergedlist[j][i] = false; //now empty
                                mergedlist = iterateabove2(j, k, mergedlist);
                            }
                            else if (board.tile(j, k).value() == tile.value() && !mergedlist[j][k] && !mergedlist[j][i]){ //can merge
                                x = true;
                                score += tile.value() * 2;
                                board.move(j, k, tile);
                                System.out.print(board);
                                mergedlist[j][k] = true; //new tile is merged
                                mergedlist[j][i] = false; //old tile (null) not merged
                                mergedlist = iterateabove2(j, k, mergedlist);
                            }
                        }
                        //now on a tile now in first or second row
                        //want to check above, if its null or same then move. then update mergedlist
                    }
                }
            }
        }
        return x;
    }
    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    /**public boolean tilt(Side side) {
     boolean changed;
     changed = false;


     // TODO: Modify this.board (and perhaps this.score) to account
     // for the tilt to the Side SIDE. If the board changed, set the
     // changed local variable to true.
     //score changes if >0 tiles combine. its incremented by the sum of all combined tiles (so if 2+2 and 4+4 merge, increase score by 4+8 = 12
     //start with only up direction
     int size = size();
     boolean[][] mergedlist = new boolean[0][];
     for (int i = 0; i < size; i++){ //rows
     for (int j = 0; j < size; j++){ //columns
     Tile tile = board.tile(j,i);
     if (tile != null) {
     checkabove(tile, mergedlist);
     }
     }
     }
     checkGameOver();
     if (changed) {
     setChanged();
     }
     return changed;
     }

}*/
