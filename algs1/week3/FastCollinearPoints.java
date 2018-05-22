import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {
    private static final double EPS = 1e-10;

    private final int numOfSegments;
    private final ArrayList<LineSegment> res;
   
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new java.lang.IllegalArgumentException("");
        res = new ArrayList<LineSegment>();

        Point[] ppoints = points.clone();

        Point cornerP = ppoints[0];
        int n = points.length;
        for (int i = 0; i < n; ++i) {
            if (ppoints[i] == null)
                throw new java.lang.IllegalArgumentException("");

            if (ppoints[i].compareTo(cornerP) < 0)
                cornerP = ppoints[i];
        }

        for (int i = 0; i < n; ++i) {
            Arrays.sort(ppoints, 0, n, cornerP.slopeOrder());
             
            if (i > 0 && ppoints[i].compareTo(ppoints[i -1]) == 0)
                throw new java.lang.IllegalArgumentException("");
            Point tmp = ppoints[i];
            ppoints[i] = ppoints[0];
            ppoints[0] = tmp;
          
            Arrays.sort(ppoints, 1, n, ppoints[0].slopeOrder());

            int j = 1;
            for (int k = 2; k <= n; ++k) {
                if (k == n || !isSameSlope(ppoints[0], ppoints[j], ppoints[k])) {
                    if (k - j >= 3) {   // at least 4 points
                        Point minP = ppoints[0];
                        Point maxP = ppoints[0];
                        boolean legal = true;
                        for (int a = j; a < k && legal; ++a) {
                            if (a > j && ppoints[a].compareTo(ppoints[a -1]) == 0)
                                legal = false;
                            minP = minP.compareTo(ppoints[a]) < 0 ? minP : ppoints[a];
                            maxP = maxP.compareTo(ppoints[a]) > 0 ? maxP : ppoints[a];
                        }
                       
                        if (legal && minP.compareTo(ppoints[0]) == 0 
                                && maxP.compareTo(ppoints[0]) != 0) {
                            res.add(new LineSegment(minP, maxP));
                        }
                    }

                    j = k;
                }           
            }
        }

        numOfSegments = res.size();
    }

    public int numberOfSegments() {
        return numOfSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] tmp = new LineSegment[numOfSegments];
        tmp = res.toArray(tmp);
        return tmp;
    }

    private boolean isSameSlope(Point p, Point q, Point r) {
        if (p.slopeTo(q) == Double.POSITIVE_INFINITY 
                && p.slopeTo(r) == Double.POSITIVE_INFINITY)
            return true;

        return Math.abs(p.slopeTo(q) - p.slopeTo(r)) < EPS;
    }
}
