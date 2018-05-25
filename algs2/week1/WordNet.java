import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;

public class WordNet {
    private final Map<String, List<Integer> > treeMap;
    private final List<String> nounList;
    private SAP sap;
    private Digraph G;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        treeMap = new TreeMap<String, List<Integer>>();
        nounList = new ArrayList<String>();
        parseSynset(synsets);
        parseHypernyms(hypernyms);
    }

    private void parseSynset(String synsets) {
        if (synsets == null)
            throw new IllegalArgumentException("synsets file is null");

        In in = new In(synsets);
        while (!in.isEmpty()) {
            String[] elements = in.readLine().split(",");
            int id = Integer.parseInt(elements[0]);
            nounList.add(elements[1]);
            for (String noun : elements[1].split(" ")) {
                if (!treeMap.containsKey(noun))
                    treeMap.put(noun, new LinkedList<Integer>());
                treeMap.get(noun).add(id);
            }
        }
    }

    private void parseHypernyms(String hypernyms) {
        if (hypernyms == null)
            throw new IllegalArgumentException("hypernyms file is null");

        G = new Digraph(treeMap.size());
        In in = new In(hypernyms);
        while (!in.isEmpty()) {
            String[] elements = in.readLine().split(",");
            int u = Integer.parseInt(elements[0]);
            for (int i = 1; i < elements.length; ++i) {
                int v = Integer.parseInt(elements[i]);
                G.addEdge(u, v);
            }
        }
        
        Topological topological = new Topological(G);
        if (!topological.hasOrder())
            throw new IllegalArgumentException("graph is not DAG");
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return treeMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("word is null");
        return treeMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("either nounA or nounB is not noun");
        return sap.length(treeMap.get(nounA), treeMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("either nounA or nounB is not noun");
        return nounList.get(sap.ancestor(treeMap.get(nounA), treeMap.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);

        StdOut.println(wordnet.distance("worm", "bird"));
        StdOut.println(wordnet.distance("municipality", "region"));        
        StdOut.println(wordnet.distance("white_marlin", "mileage"));
        StdOut.println(wordnet.distance("Black_Plague", "black_marlin"));
        StdOut.println(wordnet.distance("American_water_spaniel", "histology"));
        StdOut.println(wordnet.distance("Brown_Swiss", "barrel_roll"));
    }
}
