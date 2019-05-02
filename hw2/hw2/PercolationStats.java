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
    private double[] openNumRatio;
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

        this.T = T;
        openNumRatio = new double[T];

        for (int i = 0; i < T; i += 1) {
            p = pf.make(N);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(N), StdRandom.uniform(N));
            }
            openNumRatio[i] = p.numberOfOpenSites() / (double) (N * N);
        }
    }

    /**
     * Sample mean of percolation threshold
     * @return
     */
    public double mean() {
        return StdStats.mean(openNumRatio);
    }

    /**
     * Sample standard deviation of percolation threshold
     * @return
     */

    public double stddev() {
        return StdStats.stddev(openNumRatio);
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
