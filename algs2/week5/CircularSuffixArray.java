import java.util.Comparator;
import java.util.Arrays;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private final int len;
    private final Integer[] index;
    private final char[] arr;
    public CircularSuffixArray(String s) {   // circular suffix array of s
        if (s == null)
            throw new java.lang.IllegalArgumentException("string is null");

        len = s.length();
        index = new Integer[len];

        arr = s.toCharArray();
        for (int i = 0; i < len; ++i)
            index[i] = i;
        
        Arrays.sort(index, new SuffixComparator());         
    }

    private class SuffixComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            for (int i = 0; i < len; ++i) {
                int pa = (i + a.intValue()) % len;
                int pb = (i + b.intValue()) % len;
                if (arr[pa] > arr[pb])  return 1;
                else if (arr[pa] < arr[pb]) return -1;
            }
            return 0;
        }
    }

    public int length() {                    // length of s
        return len;
    }

    public int index(int i) {                // returns index of ith sorted suffix
        return index[i];
    }

    public static void main(String[] args) {  // unit testing (required)
        CircularSuffixArray csa = null;
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            csa = new CircularSuffixArray(str);
            for (int i = 0; i < csa.length(); ++i)
                StdOut.println(csa.index(i));
        }
    }
}
