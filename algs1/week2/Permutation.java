/*----------------------------------------------------------------
 *  Author:        Jyun-Sheng Kao
 *  Written:       5/19/2018
 *  Last updated:  5/23/2018
 *
 *  Compilation:   javac-coursera Permutaion.java
 *  Execution:     java-coursera Permutaion {n} < input.txt
 *  Dependencies:  StdIn.java StdOut.java
 *
 *  % more input.txt
 *  A B C D E
 *
 *  % java-coursera Permutaion 3 < input.txt
 *  D
 *  B
 *  A
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        int k = 0;
        if (args.length == 1)   k = Integer.parseInt(args[0]);
        
        int count = 1;  // For reservoir sampling
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            if (rq.size() == k) {
                if (StdRandom.uniform(count) < k) {
                    rq.dequeue();
                    rq.enqueue(str);
                }
            } else {
                rq.enqueue(str);
            }
            ++count;
        }

        for (int i = 0; i < k; ++i)
            StdOut.println(rq.dequeue());
    }
}
