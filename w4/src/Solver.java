import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private boolean isSolvable;
    private Stack<Board> manhattanSolution = new Stack<Board>();

    private final class SearchNode implements Comparable<SearchNode>{

        private final Board board;
        private final SearchNode prev;
        private final int moves;

        private final int mahattan;

        private SearchNode(Board board, SearchNode prev){
            this.board = board;
            this.prev = prev;
            if(prev == null) moves = 0;
            else moves = prev.moves + 1;

            mahattan = board.manhattan();
        }

        private int priority() {
            return mahattan + moves;
        }

        public int compareTo(SearchNode that) {
            return Integer.signum(this.priority() - that.priority());
        }

    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        if(initial == null) throw new IllegalArgumentException();

        MinPQ<SearchNode> PQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();

        PQ.insert(new SearchNode(initial, null));
        twinPQ.insert(new SearchNode(initial.twin(), null));

        MinPQ<SearchNode> queue = PQ;
        SearchNode cur;

        while(true){
            //initial solution
            if(!queue.isEmpty()) {
                cur = queue.delMin();

                if (cur.board.isGoal()) {
                    isSolvable = queue == PQ;

                    while (cur != null && isSolvable) {
                        manhattanSolution.push(cur.board);
                        cur = cur.prev;
                    }

                    return;
                }

                for (Board neighbor : cur.board.neighbors()) {
                    //do not enqueue if neighbor is the same as prev board
                    if (cur.prev == null || !neighbor.equals(cur.prev.board)) {
                        queue.insert(new SearchNode(neighbor, cur));
                    }
                }

            }
            if (queue == PQ) queue = twinPQ;
            else queue = PQ;

        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
        if(isSolvable()) return manhattanSolution.size()-1;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution(){
        if(!isSolvable()) return null;
        else return manhattanSolution;
    }

    // test client (see below)
    public static void main(String[] args){
// create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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

    }
}
