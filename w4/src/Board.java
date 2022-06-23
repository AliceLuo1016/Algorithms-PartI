import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Iterator;

public class Board {

    private final int[][] tiles;
    private final int N;
    private int zrow;
    private int zcol;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] blocks){
        N = blocks.length;
        tiles = new int[N][N];
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                tiles[row][col] = blocks[row][col];
                if(tiles[row][col] == 0){
                    zrow = row;
                    zcol = col;
                }
            }
        }
    }

    // string representation of this board
    public String toString(){
        String out = N + "\n";
        for (int row = 0; row < N; row++){
            for(int col = 0; col < N; col++){
                out = out + tiles[row][col] + " ";
            }
            out = out + "\n";
        }
        return out;
    }

    // board dimension n
    public int dimension(){
        return N;
    }

    // number of tiles out of place
    public int hamming(){
        int target = 1;
        int out = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                //last element does not count
                if(row == N-1 && col == N-1){
                    break;
                }
                if(tiles[row][col] != target){
                    out++;
                }
                target++;
            }
        }
        return out;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        int distance = 0;
        int target = 1;

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                //last element's target is 0
                if(row == N-1 && col == N-1) target = 0;
                if(tiles[row][col] != 0 && tiles[row][col] != target){
                    int targetRow = (tiles[row][col]-1)/N;
                    int targetCol = (tiles[row][col]-1)%N;
                    distance += Math.abs(row - targetRow) + Math.abs(col - targetCol);
                }
                target++;
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal(){
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y){
        if(y == null) return false;
        if(y.getClass() != getClass()) return false;
        Board other = (Board) y;
        if(this.N != other.N) return false;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if(this.tiles[row][col] != other.tiles[row][col]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors(){
        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                Iterator<Board> it = findNeighbors().iterator();
              return it;
            }
        };
    }

    private ArrayList<Board> findNeighbors(){
        ArrayList<Board> neighbors = new ArrayList<>();
        int[] offset = {-1, 1, 0, 0};
        for(int i = 0, j = offset.length - 1; i < offset.length && j >= 0; i++, j--){
            int row = zrow + offset[i];
            int col = zcol + offset[j];
            if(row < 0 || col < 0 || row > N-1 || col > N-1) continue;

            Board b = new Board(tiles);
            switchTiles(b.tiles, zrow, zcol, row, col);
            b.zrow = row;
            b.zcol = col;
            neighbors.add(b);
        }
        return neighbors;
    }

    //switch tiles location
    private void switchTiles(int[][] tmpTiles, int r1, int c1, int r2, int c2){
        int tmp = tmpTiles[r1][c1];
        tmpTiles[r1][c1] = tmpTiles[r2][c2];
        tmpTiles[r2][c2] = tmp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        Board twin = new Board(tiles);
        int zloc = zrow * twin.N + zcol + 1;
        int endloc = twin.N * twin.N;
        int loc = 0, loc1 = 0, loc2 = 0;

        while(loc1 == 0 || loc2 == 0){
            if(++loc == zloc) continue;
            if(loc > endloc) break;
            if(loc1 == 0) loc1 = loc;
            else loc2 = loc;
        }
        switchTiles(twin.tiles, (loc1-1)/N, (loc1-1)%N, (loc2-1)/N, (loc2-1)%N);
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args){
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        StdOut.println(initial.toString());
        StdOut.println("hamming: " + initial.hamming());
        StdOut.println("manhattan: " + initial.manhattan());

        Iterator<Board> it = initial.findNeighbors().iterator();
        while(it.hasNext()){
            Board b = it.next();
            StdOut.println("neighbor: " + b.toString() + "Equals?" + initial.equals(b));
        }

        StdOut.println("Twin: " + initial.twin().toString());
/*
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

 */
    }

}