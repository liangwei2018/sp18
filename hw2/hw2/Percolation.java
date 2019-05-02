package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Model a percolation system with N-by-N grid of sites. Each site
 * is either open or blocked. numOpen records the number of open sites.
 * uf contains two additional virtual sites: top and bottom, used to
 * reduce the time complexity for checking percolation from O(N) to O(1).
 * ufTop contains just one virtual sites: top, used to avoid bottom "backwash".
 * All methods have time complexity of O(1) except for the constructor (O(N^2)).
 *
 * @author Liang Wei
 */

public class Percolation {
    private boolean[][] site;
    private int N;
    private int numOpen;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufTop;
    private int top;
    private int bottom;

    /**
     * Creates N-by-N grid, with all sites initially blocked.
     * Initialize two disjoint sets uf and ufTop.
     * @param N the size of the grid
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;
        site = new boolean[N][N];
        for (int i = 0; i < N;  i += 1) {
            for (int j = 0; j < N; j += 1) {
                site[i][j] = false;
            }
        }
        numOpen = 0;
        top = N * N;
        bottom = top + 1;
        uf = new WeightedQuickUnionUF(top + 2);
        ufTop = new WeightedQuickUnionUF(top + 1);
    }
    private int xyTo1D(int r, int c) {
        return r * N + c;
    }

    /**
     * Opens the site (row, col) if it is not open already,
     * and Connects the neighboring open sites.
     * @param row The row of the site
     * @param col The column of the site
     */
    public void open(int row, int col) {
        if (row < 0 || col < 0 || row > N - 1 || col > N - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            site[row][col] = true;
            numOpen += 1;
            int pos1D = xyTo1D(row, col);

            if (row == 0) {
                uf.union(top, pos1D);
                ufTop.union(top, pos1D);
            }

            if (row == N - 1) {
                uf.union(bottom, pos1D);
            }

            if (row > 0 && isOpen(row - 1, col)) {
                uf.union(pos1D, pos1D - N);
                ufTop.union(pos1D, pos1D - N);
            }

            if (row < N - 1 && isOpen(row + 1, col)) {
                uf.union(pos1D, pos1D + N);
                ufTop.union(pos1D, pos1D + N);
            }

            if (col > 0 && isOpen(row, col - 1)) {
                uf.union(pos1D, pos1D - 1);
                ufTop.union(pos1D, pos1D - 1);
            }

            if (col < N - 1 && isOpen(row, col + 1)) {
                uf.union(pos1D, pos1D + 1);
                ufTop.union(pos1D, pos1D + 1);
            }

        }
    }

    /**
     *  Is the site (row, col) open?
     * @param row The row of the site
     * @param col The column of the site
     * @return true if open.
     */
    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row > N - 1 || col > N - 1) {
            throw new IndexOutOfBoundsException();
        }
        return site[row][col];
    }

    /**
     * Is the site (row, col) full?
     * @param row the row of the site
     * @param col the column of the site
     * @return true if full.
     */
    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0 || row > N - 1 || col > N - 1) {
            throw new IndexOutOfBoundsException();
        }
        return ufTop.connected(top, xyTo1D(row, col));
    }

    /**
     *
     * @return number of open sites
     */
    public int numberOfOpenSites() {
        return numOpen;
    }

    /**
     * Does the system percolate?
     * @return true if percolate.
     */
    public boolean percolates() {
        return uf.connected(top, bottom);
    }
    // use for unit testing (not required)
    public static void main(String[] args) {
        System.out.println("success!");

    }
}
