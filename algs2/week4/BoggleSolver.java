import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private Set<String> res;
    private final TrieNode root;
    private boolean[][] visit;
    private BoggleBoard board;

    private class TrieNode {
        private final TrieNode[] child;
        private final boolean isWord;

        public TrieNode(boolean isWord) {
            child = new TrieNode[26];
            this.isWord = isWord;
        }

        public boolean isWord() {
            return isWord;
        }

        public void insert(char ch, boolean isWord) {
            child[ch - 'A'] = new TrieNode(isWord);
        }

        public TrieNode get(char ch) {
            return child[ch - 'A'];
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new TrieNode(false);
        for (String word: dictionary) {
            TrieNode ptr = root;
            char[] arr = word.toCharArray();
            for (int i = 0; i < arr.length; ++i) {
                if (ptr.get(arr[i]) == null)
                    ptr.insert(arr[i], i == arr.length -1);
                ptr = ptr.get(arr[i]);
            }
        }

        res = null;
        visit = null;
        board = null;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        res = new HashSet<String>();
        visit = new boolean[board.rows()][board.cols()]; 
        this.board = board;
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                StringBuilder sb = new StringBuilder();
                dfs(root, i, j, sb);
            }
        }

        return new WordIterable();        
    }

    private class WordIterable implements Iterable<String> {

        @Override
            public Iterator<String> iterator() {
                return res.iterator();
            }
    }

    private void dfs(TrieNode ptr, int x, int y, StringBuilder sb) {
        char ch = board.getLetter(x, y);
        if (ptr.get(ch) == null)
            return;
        visit[x][y] = true;
        sb.append(ch);

        if (ptr.get(ch).isWord() && scoreOf(sb.toString()) > 0)
            res.add(sb.toString());

        for (int i = -1; i <= 1; ++i)
            for (int j = -1; j <= 1; ++j) {
                int nx = x + i;
                int ny = y + j;

                if (nx < 0 || ny < 0 || nx >= board.rows() || ny >= board.cols())
                    continue;
                if (visit[nx][ny])
                    continue;

                if (ch == 'Q') {
                    sb.append('U');
                    dfs(ptr.get(ch).get('U'), nx, ny, sb);
                    sb.deleteCharAt(sb.length() -1);
                } else 
                    dfs(ptr.get(ch), nx, ny, sb);
            }
        sb.deleteCharAt(sb.length() -1);
        visit[x][y] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (find(word)) {
            int len = word.length();
            if (len <= 2)       return 0;
            else if (len <= 4)  return 1;
            else if (len == 5)  return 2;
            else if (len == 6)  return 3;
            else if (len == 7)  return 5;
            else                return 11;
        } else
            return 0;
    }

    private boolean find(String word) {
        char[] arr = word.toCharArray();
        TrieNode ptr = root;
        for (int i = 0; i < arr.length; ++i) {
            if (ptr.get(arr[i]) == null)
                return false;
            ptr = ptr.get(arr[i]);
        }

        return ptr.isWord();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
