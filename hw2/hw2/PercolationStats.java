package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

/**
 * Estimate the percolation threshold through a Monte Carlo simulation.
 *
 * @author Liang Wei
 */

public class PercolationStats {
    private Percolation p;
    private int[] openNum;
    private int T;

    /**
     * Performs T independent experiments on an N-by-N grid.
      * @param N The number of grids in each direction.
     * @param T The number of independent computer experiments.
     * @param pf An object used to create an Percolation Object.
     */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        p = pf.make(N);
        this.T = T;
        openNum = new int[T];

        for (int i = 0; i < T; i += 1) {
            while (!p.percolates()) {
                p.open(StdRandom.uniform(N), StdRandom.uniform(N));
            }
            openNum[i] = p.numberOfOpenSites();
        }
    }

    /**
     * Sample mean of percolation threshold
     * @return
     */
    public double mean() {
        return StdStats.mean(openNum);
    }

    /**
     * Sample standard deviation of percolation threshold
     * @return
     */

    public double stddev() {
        return StdStats.stddev(openNum);
    }


    /**
     * Low endpoint of 95% confidence interval
      * @return
     */
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    /**
     * High endpoint of 95% confidence interval
      * @return
     */
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }


}
