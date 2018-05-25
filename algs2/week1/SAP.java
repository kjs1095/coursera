import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph G;
    private int[] distTo;
    private int[] root;
    private int cachedPoint1;
    private int cachedPoint2;
    private int pathLength;
    private int commonAncestor;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) 
            throw new java.lang.IllegalArgumentException("Directed graph is null");

        this.G = new Digraph(G);
        distTo = new int[this.G.V()];
        root = new int[this.G.V()];
        cachedPoint1 = -1;
        cachedPoint2 = -1;
        pathLength = INFINITY;
        commonAncestor = -1;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        if (v == w)
            return 0;

        computeSAP(v, w);

        return pathLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        if (v == w)
            return v;

        computeSAP(v, w);

        return commonAncestor;
    }

    private void computeSAP(int v, int w) {
        if (v > w) {
            int tmp = v;
            v = w;
            w = tmp;
        }

        if (cachedPoint1 != v || cachedPoint2 != w) {
            for (int u = 0; u < G.V(); ++u)
                distTo[u] = INFINITY;
            for (int u = 0; u < G.V(); ++u)
                root[u] = -1;

            commonAncestor = -1;
            pathLength = -1;
            cachedPoint1 = v;
            cachedPoint2 = w;

            bfs1(G, v);
            bfs2(G, w);
        }
    }

    private void bfs1(Digraph G, int src) {
        Queue<Integer> q = new Queue<Integer>();
        distTo[src] = 0;
        root[src] = src;

        q.enqueue(src);
        while (!q.isEmpty()) {
            int u = q.dequeue();
            for (int v : G.adj(u)) {
                if (distTo[u] + 1 < distTo[v]) {
                    distTo[v] = distTo[u] +1;
                    q.enqueue(v);
                    root[v] = src;
                }
            }
        }
    }

    private void bfs2(Digraph G, int src) {
        Queue<Integer> q = new Queue<Integer>();
        if (distTo[src] != INFINITY) {
            commonAncestor = src;
            pathLength = distTo[src];
        }

        distTo[src] = 0;
        root[src] = src;
        q.enqueue(src);
        while (!q.isEmpty()) {
            int u = q.dequeue();
            for (int v : G.adj(u)) {
                if (distTo[v] != INFINITY && root[v] != src) {
                    if (pathLength == -1 || distTo[v] + distTo[u] +1 < pathLength) {
                        commonAncestor = v;
                        pathLength = distTo[v] + distTo[u] +1;
                    }
                }
                if (distTo[u] +1 <= distTo[v]) {
                    distTo[v] = distTo[u] +1;
                    q.enqueue(v);
                    root[v] = src;
                }
            }
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertex(v);
        validateVertex(w);

        computeSAP(v, w);

        return pathLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertex(v);
        validateVertex(w);

        computeSAP(v, w);

        return commonAncestor;
    }

    private void computeSAP(Iterable<Integer> v, Iterable<Integer> w) {
        for (int u = 0; u < G.V(); ++u)
            distTo[u] = INFINITY;
        commonAncestor = -1;
        pathLength = -1;
        cachedPoint1 = -1;
        cachedPoint2 = -1;
        bfs1(G, v);
        bfs2(G, w);
    }

    private void bfs1(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            distTo[s] = 0;
            q.enqueue(s);
        }

        while (!q.isEmpty()) {
            int u = q.dequeue();
            for (int v : G.adj(u)) {
                if (distTo[u] + 1 < distTo[v]) {
                    distTo[v] = distTo[u] +1;
                    q.enqueue(v);
                }
            }
        }
    }

    private void bfs2(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            if (distTo[s] != INFINITY) {
                if (pathLength == -1 || distTo[s] < pathLength) {
                    pathLength = distTo[s];
                    commonAncestor = s;
                }
            } else {
                distTo[s] = 0;
                q.enqueue(s);
            }
        }

        while (!q.isEmpty()) {
            int u = q.dequeue();
            for (int v : G.adj(u)) {
                if (distTo[v] != INFINITY) {
                    if (pathLength == -1 || distTo[v] + distTo[u] + 1 < pathLength) {
                        pathLength = distTo[v] + distTo[u] +1;
                        commonAncestor = v;
                    }
                } else {
                    distTo[v] = distTo[u] +1;
                    q.enqueue(v);
                }
            }
        }
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= G.V())
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (G.V()-1));
    }

    private void validateVertex(Iterable<Integer> v) {
        if (v == null)
            throw new IllegalArgumentException("vertex set is null");
        int count = 0;
        for (Integer u : v) {
            if (u == null)
                throw new IllegalArgumentException("vertex is null");
            validateVertex(u);
            ++count;
        }

        if (count == 0)
            throw new IllegalArgumentException("vertex set is empty");
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
