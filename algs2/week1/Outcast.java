import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }        // constructor takes a WordNet object
    
    public String outcast(String[] nouns) {
        int dt = 0;
        int xt = 0;
        for (int i = 0; i < nouns.length; ++i) {
            int di = 0;
            for (int j = 0; j < nouns.length; ++j)
                di += wordnet.distance(nouns[i], nouns[j]);
            
            if (di > dt) {
                dt = di;
                xt = i;
            }
        }

        return nouns[xt];

    }  // given an array of WordNet nouns, return an outcast
    
    public static void main(String[] args) {  // see test client below
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
