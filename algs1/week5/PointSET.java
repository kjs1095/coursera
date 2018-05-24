import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    private final Set<Point2D> tree;

    /**
     * construct an empty set of points
     */
    public PointSET() {
        tree = new TreeSet<Point2D>();
    }

    /**
     * is the set empty?
     */
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    /**
     * number of points in the set
     */
    public int size() {
        return tree.size();
    }

    /**
     *  add the point to the set (if it is not already in the set
     */
    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("p is null");
        tree.add(p);
    }

    /**
     * does the set contain point p?
     */
    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("p is null");
        return tree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-0.1, 1.1);
        StdDraw.setYscale(-0.1, 1.1);

        rect.draw();


        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        Iterator<Point2D> it = tree.iterator();
        while (it.hasNext())        
            it.next().draw();

        StdDraw.show();
        StdDraw.pause(20);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.IllegalArgumentException("rectangle is null");
        List<Point2D> pointList = new LinkedList<Point2D>();

        if (!isEmpty()) {
            Iterator<Point2D> it = tree.iterator();
            
            while (it.hasNext()) {
                Point2D nextP = it.next();
                if (rect.contains(nextP))
                    pointList.add(nextP);
            }
        }

        return new Point2DIterable(pointList);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("p is null");
        Point2D nearestP = null;
        double minDist = Double.POSITIVE_INFINITY;

        if (!isEmpty()) {
            Iterator<Point2D> it = tree.iterator();
            while (it.hasNext()) {
                Point2D nextP = it.next();
                double tmpDist = p.distanceSquaredTo(nextP);
                if (tmpDist < minDist) {
                    nearestP = nextP;
                    minDist = tmpDist;
                }
            }
        }

        return nearestP;
    }

    private class Point2DIterable implements Iterable<Point2D> {
        List<Point2D> pointList;

        public Point2DIterable(List<Point2D> pointList) {
            this.pointList = pointList;
        }

        @Override
            public Iterator<Point2D> iterator() {
                return pointList.iterator();
            }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET tree = new PointSET();
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            tree.insert(new Point2D(x, y));
        }

        Point2D p = new Point2D(0.81, 0.30);
        StdOut.println(tree.nearest(p));

        RectHV rect = new RectHV(0.7,0.2,0.7,0.2);
        for (Point2D point: tree.range(rect))
            StdOut.println(point);
        tree.draw();
    }
}

