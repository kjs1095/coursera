import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final char[] status;
    private final int n;
    private final int hammingCode;
    private final int manhattanDist;
    private final ArrayList<Board> neighborList;
    private Board twinBoard;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        status = new char[n * n];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j) 
                status[i * n + j] = (char) blocks[i][j];

        manhattanDist = countManhattanDist(blocks);
        hammingCode = countHammingCode(blocks);

        neighborList = new ArrayList<Board>();
        twinBoard = null;
    }

    private int countManhattanDist(int[][] blocks) {
        int tmp = 0;
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                if (blocks[i][j] != 0) 
                    tmp += Math.abs(i - (blocks[i][j] -1) / n) 
                        + Math.abs(j - (blocks[i][j] -1) % n);
        return tmp;        
    }

    private int countHammingCode(int[][] blocks) {
        int tmp = 0;
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                if (blocks[i][j] != 0
                        && blocks[i][j] != i * n + j +1)
                    tmp++;
        return tmp;
    }

    private Board genTwinBoard() {
        int[][] tmpBoard = new int[n][];
        for (int i = 0; i < n; ++i)
            tmpBoard[i] = new int[n];

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                tmpBoard[i][j] = status[i * n + j];

        int x1, x2, y1, y2;
        x1 = -1;
        x2 = -1;
        y1 = -1;
        y2 = -1; 
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                if (tmpBoard[i][j] != 0) {
                    if (x1 == -1 && y1 == -1) {
                        x1 = i;
                        y1 = j;
                    } else if (x2 == -1 && y2 == -1) {
                        x2 = i;
                        y2 = j;
                    } else 
                        break;
                }
        
        int tmp = tmpBoard[x1][y1];
        tmpBoard[x1][y1] = tmpBoard[x2][y2];
        tmpBoard[x2][y2] = tmp;
        
        return new Board(tmpBoard);
    }

    private void genNeighbor() {
        int sx = 0;
        int sy = 0;

        for (int i = 0; i < n * n; ++i)
            if (status[i] == 0) {
                sx = i / n;
                sy = i % n;
                break;
            }

        int[][] tmpBoard = new int[n][];
        for (int i = 0; i < n; ++i)
            tmpBoard[i] = new int[n];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                tmpBoard[i][j] = status[i * n + j];

        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : dirs) {
            int ntx = sx + dir[0];
            int nty = sy + dir[1];

            if (ntx < 0 || ntx >= n || nty < 0 || nty >= n)
                continue;

            int tmp = tmpBoard[ntx][nty];
            tmpBoard[sx][sy] = tmp;
            tmpBoard[ntx][nty] = 0;

            neighborList.add(new Board(tmpBoard));

            tmpBoard[ntx][nty] = tmp;
            tmpBoard[sx][sy] = 0;
        }
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        return hammingCode;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattanDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hammingCode == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (twinBoard == null)
            twinBoard = genTwinBoard();
        return twinBoard;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this)  return true;
        if (y == null)  return false;
        if (y.getClass() != this.getClass())    return false;
        Board that = (Board) y;
        if (this.n != that.n)  return false;

        for (int i = 0; i < n * n; ++i)
            if (that.status[i] != this.status[i])
                return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (neighborList.isEmpty())
            genNeighbor();
        return new IterableBoard(neighborList);
    }

    private class IterableBoard implements Iterable<Board> {
        List<Board> neighborList;

        public IterableBoard(List<Board> neighborList) {
            this.neighborList = neighborList;
        }

        @Override
            public Iterator<Board> iterator() {
                return neighborList.iterator();
            }
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n * n; ++i) {
            s.append(String.format("%2d ", (int) status[i]));
            if ((i +1) % n == 0)
                s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);    
    
        StdOut.println(initial);
        
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
        StdOut.println(initial.isGoal());
        for (Board neighbor : initial.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println(neighbor.hamming());
            StdOut.println(neighbor.manhattan());
        }
  
        StdOut.println(initial.twin());      
    }
}
