import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    //    private int[] id;
    private final int[] status;
    //    private int[] weight;
    private final int size;
    private final int upper_root;
    private final int lower_root;
    private int num_opensites = 0;
    private final WeightedQuickUnionUF wqu;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        sanitySize(n);
//        id = new int[n*n+2];
        status = new int[n * n + 2];
//        weight = new int[n*n+2];
        for (int i = 0; i < n * n + 2; i++) {
            //id[i] = i;
            status[i] = 0;
//            weight[i] = 1;
        }
        size = n;
        wqu = new WeightedQuickUnionUF(size * size + 2);

        upper_root = n * n + 1;
        lower_root = n * n;
        status[upper_root] = 1;
        status[lower_root] = 1;

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        sanityLoc(row, col);
        int loc = findLoc(row, col);
        if (status[loc] == 0) {
            status[loc] = 1;
            // first row or last row
            if (row == 1){
                wqu.union(loc, upper_root);
            }
//            if(row == size){
//                wqu.union(loc, lower_root);
//            }
            //left
            if (col > 1) {
                if (isOpen(row, col - 1)) {
                    int left_loc = findLoc(row, col - 1);
                    if (wqu.find(left_loc) != wqu.find(loc)) {
                        wqu.union(left_loc, loc);
                    }
                }
            }
            //right
            if (col < size) {
                if (isOpen(row, col + 1)) {
                    int right_loc = findLoc(row, col + 1);
                    if (wqu.find(right_loc) != wqu.find(loc)) {
                        wqu.union(right_loc, loc);
                    }
                }
            }
            //top
            if (row > 1) {
                if (isOpen(row - 1, col)) {
                    int top_loc = findLoc(row - 1, col);
                    if (wqu.find(top_loc) != wqu.find(loc)) {
                        wqu.union(top_loc, loc);
                    }
                }
            }
            //bottom
            if (row < size) {
                if (isOpen(row + 1, col)) {
                    int bottom_loc = findLoc(row + 1, col);
                    if (wqu.find(bottom_loc) != wqu.find(loc)) {
                        wqu.union(bottom_loc, loc);
                    }
                }
            }
            num_opensites++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        sanityLoc(row, col);
        int loc = (row - 1) * size + (col - 1);
        return status[loc] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        sanityLoc(row, col);
        if (!isOpen(row, col)) return false;
        int loc = findLoc(row, col);

        return wqu.find(loc) == wqu.find(upper_root);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return num_opensites;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int i = size*(size -1); i<size*size; i++){
            if (status[i] == 1){
                if (wqu.find(upper_root) == wqu.find(i)) return true;
            }
        }
        return false;
    }

/*
    private int root(int i){
        while(id[i] != i){
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }
/*
    private void union(int p, int q){
        int i = root(p);
        int j = root(q);
        if (i >= lower_root || j >= lower_root){
            if (i>j) {
                id[j] = i;
                weight[i] += weight[j];
            }
            else {
                id[i] = j;
                weight[j] += weight[i];
            }
        }
        else{
            if  (weight[i] > weight[j]){
                id[j] = i;
                weight[i] += weight[j];
            }
            else{
                id[i] = j;
                weight[j] += weight[i];
            }
        }
    }

 */

    private int findLoc(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    private void sanityLoc(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException();
        }
    }

    private void sanitySize(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(2);
        StdOut.println(p.isOpen(2, 3));
        p.open(1, 1);
        p.open(2, 3);
        StdOut.println(p.isOpen(2, 3));
        StdOut.println("num of open sites: " + p.numberOfOpenSites());
        StdOut.println("is full? " + p.isFull(2, 3));
        StdOut.println("percolate? " + p.percolates());
//        p.open(3, 3);
//        p.open(4, 3);
//        p.open(4, 4);
//        p.open(5, 4);
//        StdOut.println("percolate? " + p.percolates());
    }
}