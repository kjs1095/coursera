/*----------------------------------------------------------------
 *  Author:        Jyun-Sheng Kao
 *  Written:       5/18/2018
 *  Last updated:  5/19/2018
 *
 *  Compilation:   javac-coursera Percolation.java
 *  Execution:     java-coursera Percolation < input.txt
 *  Dependencies:  StdIn.java StdOut.java WeightedQuickUnionUF.java
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Percolation {
    private final short[][] dir = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private boolean[][] opened; // true for site open
    private boolean[][] top;    // true for connected to top row
    private boolean[][] bottom; // true for connected to bottom row

    private final int n;
    private final WeightedQuickUnionUF gridUF;  // connectivity of open sites
    private int numOfOpenSites;
    private boolean isPercolates;

    /**
     * Initializes n-by-n grid, with all sites blocked.
     *
     * @param  n the length of grid
     * @throws IllegalArgumentException if {@code n <= 0}
     */
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("row/col index out of bounds");
        this.n = n;
        numOfOpenSites = 0;
        gridUF = new WeightedQuickUnionUF(n * n);
        opened = new boolean[n][n];
        top = new boolean[n][n];
        bottom = new boolean[n][n];
        isPercolates = false;
    }

    /**
     * Open a site with given row and col if the site is blocked.
     * Then, update connectivity of its open neighbors in four directions.
     *
     * @param row the row index of site
     * @param col the column index of site
     */
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            opened[row -1][col -1] = true;
            
            top[row -1][col -1] = (row == 1);
            bottom[row -1][col -1] = (row == n);
            
            int id = gridUF.find(xyTo1D(row, col));
            for (short[] d : dir) {
                int nextRow = row + d[0];
                int nextCol = col + d[1];
                if (outBound(nextRow) || outBound(nextCol))   continue;
                if (!isOpen(nextRow, nextCol))                continue;
                    
                int nextId = gridUF.find(xyTo1D(nextRow, nextCol));

                updateFilled(id /n +1, id % n +1, nextId /n +1, nextId % n +1);
                gridUF.union(xyTo1D(id /n +1, id % n +1), 
                                xyTo1D(nextId /n +1, nextId % n +1));
                id = gridUF.find(xyTo1D(row, col));
            }

            ++numOfOpenSites;
            isPercolates |= (top[id /n][id % n] && bottom[id /n][id % n]);
        }
    }

    /**
     * Identify site (row, col) is open
     *
     * @param  row the row index of site
     * @param  col the column index of site
     * @return if the site is open or not
     * @throws IllegalArgumentException unless
     *         both {@code 1 <= row <= n} and {@code 1 <= col <= n}
     */
    public boolean isOpen(int row, int col) {
        if (outBound(row) || outBound(col)) 
            throw new IllegalArgumentException("row/col index out of bounds");
        return opened[row -1][col -1];
    }

    /**
     * Identify site (row, col) is full
     *
     * @param row the row index of site
     * @param col the column index of site
     * @return if the site is full or not
     * @throws IllegalArgumentException unless
     *         both {@code 1 <= row <= n} and {@code 1 <= col <= n}
     */
    public boolean isFull(int row, int col) {
        if (outBound(row) || outBound(col))
            throw new IllegalArgumentException("row/col index out of bounds");
        int pr = gridUF.find(xyTo1D(row, col));
        return top[pr / n][pr % n];
    }

    /**
     * Returns the number of open sites.
     *
     * @return the number of open sites (between {@code 1} and {@code n x n})
     */
    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    /**
     * Returns the system percolates or not
     *
     * @return if the site is percolates or not
     */
    public boolean percolates() {
        return isPercolates;
    }

    // identify the index is out of bound
    private boolean outBound(int index) {
        return 0 >= index || index > n;
    }
    
    // convert 2 dimension position to 1 dimension
    private int xyTo1D(int x, int y) {
        return (x -1) * n + y -1;
    }

    // update connectivity of site (r1, c1) and (r2, c2)
    private void updateFilled(int r1, int c1, int r2, int c2) {
        top[r1 -1][c1 -1] |= top[r2 -1][c2 -1];
        top[r2 -1][c2 -1] |= top[r1 -1][c1 -1];
        bottom[r1 -1][c1 -1] |= bottom[r2 -1][c2 -1];
        bottom[r2 -1][c2 -1] |= bottom[r1 -1][c1 -1];
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation p = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            p.open(row, col);
            StdOut.println("isFull(" + row + "," + col + ") = " + p.isFull(row, col));
        }
        StdOut.println(p.percolates());
    }
}
