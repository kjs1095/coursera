import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private static final double INFINITY = Double.POSITIVE_INFINITY;
    private static final double FLOATING_POINT_EPSILON = 1E-11;

    private final int n;
    private final Map<String, Integer> table;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;
    private FordFulkerson flowAlgo;
    private FlowNetwork flownetwork;

    public BaseballElimination(String filename) {
        if (filename == null)
            throw new java.lang.IllegalArgumentException("filename is null");

        In in = new In(filename);
        n = in.readInt();

        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
        table = new HashMap<String, Integer>();

        for (int i = 0; i < n; ++i) {
            String team = in.readString();
            table.put(team, i);

            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();

            for (int j = 0; j < n; ++j) 
                g[i][j] = in.readInt();
        }
        flowAlgo = null;
        flownetwork = null;
    }

    public int numberOfTeams() {
        return n;
    }

    public Iterable<String> teams() {
        return new TeamIterable(table.keySet());
    }

    private class TeamIterable implements Iterable<String> {
        private final Set<String> set;
        public TeamIterable(Set<String> set) {
            this.set = set;
        }
        @Override
        public Iterator<String> iterator() {
            return set.iterator();
        }
    }

    public int wins(String team) {
        validateTeam(team);

        return w[table.get(team)];
    }

    public int losses(String team) {
        validateTeam(team);

        return l[table.get(team)];
    }

    public int remaining(String team) {
        validateTeam(team);

        return r[table.get(team)];
    }

    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);

        return g[table.get(team1)][table.get(team2)];
    }

    public boolean isEliminated(String team) {
        validateTeam(team);

        int x = table.get(team);
        buildGraph(x);
        flowAlgo = new FordFulkerson(flownetwork, 0, flownetwork.V() -1);

        int curMax = 0;
        for (int i = 0; i < n; ++i)
            curMax = Math.max(curMax, w[i]);
        
        int remainAgainst = 0;
        for (int i = 0; i < n; ++i)
            for (int j = i +1; j < n; ++j)
                if (i != x && j != x)
                    remainAgainst += g[i][j];
    
        return curMax > w[x] + r[x] 
                || Math.abs(flowAlgo.value() - remainAgainst) > FLOATING_POINT_EPSILON;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (isEliminated(team)) {
            Set<String> set = new HashSet<String>();
            int x = table.get(team);
            for (String teamname : table.keySet()) {
                int teamNodeId = flownetwork.V() -1 - n + table.get(teamname);
                if (flowAlgo.inCut(teamNodeId))
                    set.add(teamname);
                else if (w[table.get(teamname)] > w[x] + r[x])
                    set.add(teamname);
            }
            return new TeamIterable(set);
        } else 
            return null;
    }

    private void buildGraph(int x) {
        int numOfGameNode = (n * (n -1)) /2; 
        flownetwork = new FlowNetwork(2 + n + (n * (n -1))/2);
        // 0, 1 ~ numOfAgainst, numOfAgainst +1 ~ numOfAgainst + n, n + numOfAgainst +1
        int s = 0;
        int t = 2 + n + numOfGameNode -1;

        int gameNodeId = 1;
        for (int i = 0; i < n; ++i)
            for (int j = i +1; j < n; ++j) {
                if (i != x && j != x && g[i][j] != 0) {
                    int teamNode1 = numOfGameNode + i +1;
                    int teamNode2 = numOfGameNode + j +1;
                    flownetwork.addEdge(new FlowEdge(s, gameNodeId, g[i][j]));
                    flownetwork.addEdge(new FlowEdge(gameNodeId, teamNode1, INFINITY));
                    flownetwork.addEdge(new FlowEdge(gameNodeId, teamNode2, INFINITY));
                }
                ++gameNodeId;
            }

        for (int i = 0; i < n; ++i) {
            if (i == x)
                continue;

            int teamNode = numOfGameNode + i +1;
            flownetwork.addEdge(new FlowEdge(teamNode, t, Math.max(0, w[x] + r[x] - w[i])));
        }
    }

    private void validateTeam(String team) {
        if (team == null)
            throw new java.lang.IllegalArgumentException("team is null");
        else if (!table.containsKey(team))
            throw new java.lang.IllegalArgumentException("team do not exist");
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    } 
}
