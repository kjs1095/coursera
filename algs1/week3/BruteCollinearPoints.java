import java.util.ArrayList;

public class BruteCollinearPoints {
    private static final double EPS = 1e-10;

    private final int numOfSegments;
    private final ArrayList<LineSegment> res;

    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new java.lang.IllegalArgumentException("");

        res = new ArrayList<LineSegment>();
        int n = points.length;
        for (int pi = 0; pi < n; ++pi) {
            Point p = points[pi];
            if (p == null)  
                throw new java.lang.IllegalArgumentException("");
            for (int qi = pi +1; qi < n; ++qi) {
                Point q = points[qi];
                if (q == null)
                    throw new java.lang.IllegalArgumentException("");
                if (p.compareTo(q) == 0)
                    throw new java.lang.IllegalArgumentException("");

                for (int ri = qi +1; ri < n; ++ri) {
                    Point r = points[ri];
                    if (r == null)
                        throw new java.lang.IllegalArgumentException("");
                    if (p.compareTo(r) == 0 || q.compareTo(r) == 0)
                        throw new java.lang.IllegalArgumentException("");
                    
                    for (int si = ri +1; si < n; ++si) {
                        Point s = points[si];
                        if (s == null)
                            throw new java.lang.IllegalArgumentException("");
                        if (p.compareTo(s) == 0)
                            throw new java.lang.IllegalArgumentException("");
                        if (q.compareTo(s) == 0)
                            throw new java.lang.IllegalArgumentException("");
                        if (r.compareTo(s) == 0)
                            throw new java.lang.IllegalArgumentException("");

                        if (sameLine(p, q, r, s)) 
                            res.add(buildLineSegment(p, q, r, s));
                    }
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

    private boolean sameLine(Point p, Point q, Point r, Point s) {
        if (p.slopeTo(q) == Double.POSITIVE_INFINITY
                && q.slopeTo(r) == Double.POSITIVE_INFINITY
                && r.slopeTo(s) == Double.POSITIVE_INFINITY)
            return true;

        else
            return Math.abs(p.slopeTo(q) - q.slopeTo(r)) < EPS 
                    && Math.abs(q.slopeTo(r) - r.slopeTo(s)) < EPS;
    }

    private LineSegment buildLineSegment(Point p, Point q, Point r, Point s) {
        Point minP = p;
        Point maxP = p;
        if (q.compareTo(minP) < 0)  minP = q;
        if (r.compareTo(minP) < 0)  minP = r;
        if (s.compareTo(minP) < 0)  minP = s;

        if (q.compareTo(maxP) > 0)  maxP = q;
        if (r.compareTo(maxP) > 0)  maxP = r;
        if (s.compareTo(maxP) > 0)  maxP = s;

        return new LineSegment(minP, maxP);
    }
}
