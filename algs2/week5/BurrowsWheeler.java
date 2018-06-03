import java.util.ArrayList;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        while (!BinaryStdIn.isEmpty()) {
            String str = BinaryStdIn.readString();
            CircularSuffixArray csa = new CircularSuffixArray(str);
            int first = -1;

            int n = csa.length();
            for (int i = 0; i < n && first == -1; ++i)
                if (csa.index(i) == 0)
                    first = i;

            BinaryStdOut.write(first);
            BinaryStdOut.flush();
            for (int i = 0; i < n; ++i) { 
                BinaryStdOut.write(str.charAt((csa.index(i) + n -1) % n));
                BinaryStdOut.flush();
            }
        }        
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        while (!BinaryStdIn.isEmpty()) {
            int first = BinaryStdIn.readInt();
            String str = BinaryStdIn.readString();
            int n = str.length();
            
            ArrayList<ArrayList<Integer>> index = new ArrayList<ArrayList<Integer>>(); 
            for (int i = 0; i < 256; ++i)
                index.add(new ArrayList<Integer>());
            for (int i = 0; i < n; ++i) {
                index.get((int) str.charAt(i)).add(i);
            }

            int[] next = new int[n];
            char[] arr = new char[n];
            int count = 0;
            for (int i = 0; i < 256; ++i)
                for (int j = 0; j < index.get(i).size(); ++j) {
                    next[count] = index.get(i).get(j);
                    arr[count] = (char) (i & 0xff);
                    ++count;
                }

            int cur = first;
            while (count > 1) {
                BinaryStdOut.write(arr[cur]);
                cur = next[cur];
                --count;
            }
            
            BinaryStdOut.write(arr[cur]);
            BinaryStdOut.flush();
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();        
    }
}
