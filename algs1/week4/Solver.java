import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final State goalState;
    private final LinkedList<Board> solutionList;

    public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
        goalState = runAStar(initial);
        solutionList = new LinkedList<Board>();
    }

    private State runAStar(Board initial) {
        if (initial == null)
            throw new java.lang.IllegalArgumentException();
        MinPQ<State> pq = new MinPQ<State>(new StateComparator());        
        boolean found = false;
        State tmpState = null;

        pq.insert(new State(initial, 0, null, false));
        pq.insert(new State(initial.twin(), 0, null, true)); 
        while (!found) {
            State curState = pq.delMin();
            if (curState.board.isGoal()) {
                tmpState = curState;
                break;
            }
           
            boolean meetEqual = false; 
            for (Board nextBoard : curState.board.neighbors()) {
                if (curState.prevState != null && !meetEqual)
                    if (nextBoard.equals(curState.prevState.board)) {
                        meetEqual = true;
                        continue;
                    }
                
                pq.insert(new State(nextBoard, curState.move +1, curState, curState.isTwin));
            }
        }

        return tmpState;
    }

    private class State {
        private final Board board;
        private final int move;
        private final State prevState;
        private final boolean isTwin;
        private final int manhattanP;

        public State(Board board, int move, State prevState, boolean isTwin) {
            this.board = board;
            this.move = move;
            this.prevState = prevState;
            this.isTwin = isTwin;
            this.manhattanP = board.manhattan() + move;
        }

        public int manhattanPriority() {
            return manhattanP;
        }
    }

    private class StateComparator implements Comparator<State> {
        public int compare(State arg0, State arg1) {
            if (arg0.manhattanPriority() < arg1.manhattanPriority())
                return -1;
            else if (arg0.manhattanPriority() > arg1.manhattanPriority())
                return 1;
            else {
                if (!arg0.isTwin && arg1.isTwin)
                    return -1;
                else if (arg0.isTwin && !arg1.isTwin)
                    return 1;
                else {
                    if (arg0.move > arg1.move)
                        return -1;
                    else if (arg0.move < arg1.move)
                        return 1;
                    else 
                        return 0;
                }
            }
        }
    }

    public boolean isSolvable() {            // is the initial board solvable?
        return !goalState.isTwin;
    }

    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        if (goalState.isTwin)
            return -1;
        else
            return goalState.move;
    }

    public Iterable<Board> solution() {      // sequence of boards in a shortest solution; null if unsolvable
        if (!isSolvable())
            return null;    
        if (solutionList.isEmpty())
            trackSolution();
        return new IterableSolution(solutionList);
    }

    private class IterableSolution implements Iterable<Board> {
        List<Board> tmpSolutionList;

        public IterableSolution(List<Board> tmpSolutionList) {
            this.tmpSolutionList = tmpSolutionList;
        }

        @Override
            public Iterator<Board> iterator() {
                return tmpSolutionList.iterator();
            }
    }

    private void trackSolution() {
        State curState = goalState;
        while (curState != null) {
            solutionList.addFirst(curState.board);
            curState = curState.prevState;
        }
    }

    public static void main(String[] args) { // solve a slider puzzle (given below)
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        } 
    }
}

