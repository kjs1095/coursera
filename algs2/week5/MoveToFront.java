import java.util.LinkedList;
import java.util.ListIterator;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> seq = new LinkedList<Character>();
        for (int i = 0; i < R; ++i)
            seq.add((char) (i & 0xff));
        while (!BinaryStdIn.isEmpty()) {
            char input = BinaryStdIn.readChar();        
            ListIterator<Character> it = seq.listIterator(0);
            int index = 0;
            char curChar = 0;
            while (it.hasNext()) {
                curChar = it.next();
                if (curChar == input)
                    break;
                ++index;
            }
            seq.remove(index);
            seq.addFirst(curChar);

            BinaryStdOut.write((char) (index & 0xff));
            BinaryStdOut.flush();
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> seq = new LinkedList<Character>();
        for (int i = 0; i < R; ++i)
            seq.add((char) (i & 0xff));
        while (!BinaryStdIn.isEmpty()) {
            char input = BinaryStdIn.readChar();
            char curChar = seq.get((int) input);
          
            BinaryStdOut.write(curChar);
            BinaryStdOut.flush();

            seq.remove((int) input);
            seq.addFirst(curChar);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}
