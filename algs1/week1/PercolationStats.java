/*----------------------------------------------------------------
 *  Author:        Jyun-Sheng Kao
 *  Written:       5/18/2018
 *  Last updated:  5/19/2018
 *
 *  Compilation:   javac-coursera PercolationStats.java
 *  Execution:     java-coursera PercolationStats [#n #trials]
 *  Dependencies:  StdStats.java StdOut.java StdRandom.java 
 *                 Percolation.java
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private static final double FACTOR = 1.96;
    
    private final int trials;
    private final double pmean; // mean of trials
    private final double pstd;  // standard deviation of trials

    /**
     * Perform trials independent experiments on an n-by-n grid
     *
     * @param  n the length of grid
     * @param  trials the times of trials
     * @throws IllegalArgumentException if {@code n <= 0} or {@code trails <= 0}
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new java.lang.IllegalArgumentException("");   
        this.trials = trials;
        double[] experiment = new double[trials];
        for (int i = 0; i < trials; ++i) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) +1;
                int col = StdRandom.uniform(n) +1;
                percolation.open(row, col);
            }
            experiment[i] = (double) (percolation.numberOfOpenSites()) / (double) (n * n);
            percolation = null;
        }

        pmean = StdStats.mean(experiment);
        pstd = StdStats.stddev(experiment);

    }

    /**
     * Returns sample mean of percolation threshold
     *
     * @return sample mean of percolation threshold
     */
    public double mean() {
        return pmean;
    }
    
    /**
     * Returns sample standard deviation of percolation threshold
     *
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        return pstd;
    }
    
    /**
     * Returns low  endpoint of 95% confidence interval
     *
     * @return low  endpoint of 95% confidence interval
     */    
    public double confidenceLo() {
        return mean() - FACTOR * stddev() / Math.sqrt(trials);
    }

    /**
     * Returns high endpoint of 95% confidence interval
     *
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + FACTOR * stddev() / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println(ps.mean());
        StdOut.println(ps.stddev());
        StdOut.println("[" +  ps.confidenceLo() + " , " + ps.confidenceHi() + "]");
    }
}
