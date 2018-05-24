import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private static final double LOWER = 0.0;
    private static final double UPPER = 1.0;

    private int n;
        private Node root;
            private Point2D nearestP;

    // helper class
    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            lb = null;
            rt = null;
        }
    }

    /**
     * construct an empty KD tree
     */
    public KdTree() {
        root = null;
        n = 0;
        nearestP = null;
    }

    /**
     * Is the KD tree empty?
     * @return true if the KD tree is empty; false otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Returns number of points in the KD tree.
     * @return number of points in the KD tree
     */
    public int size() {
        return n;
    }

    /**
     * Adds the point to the KD tree (if it is not already in the KD tree).
     * @param p the point to add
     * @throws java.lang.IllegalArgumentException if the point is null
     */
    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("p is null");
        if (!contains(p)) {
            root = insert(root, p, true, LOWER, LOWER, UPPER, UPPER);
            ++n;
        }
    }

    private Node insert(Node root, Point2D p, boolean isOdd, 
            double xmin, double ymin, double xmax, double ymax) {
        if (root == null) {
            RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
            Node leaf = new Node(p, rect);
            return leaf;
        } else {

            if (isOdd) {
                if (p.x() < root.p.x()) {
                    root.lb = insert(root.lb, p, !isOdd, xmin, ymin, root.p.x(), ymax);
                } else {
                    root.rt = insert(root.rt, p, !isOdd, root.p.x(), ymin, xmax, ymax);
                }
            } else {
                if (p.y() < root.p.y()) {
                    root.lb = insert(root.lb, p, !isOdd, xmin, ymin, xmax, root.p.y());
                } else {
                    root.rt = insert(root.rt, p, !isOdd, xmin, root.p.y(), xmax, ymax);
                }
            }

            return root;
        }
    }

    /**
     * Does the KD tree contain point p?
     * @param p the point to verify
     * @return true if the KD tree contains point p; false otherwise
     * @throws java.lang.IllegalArgumentException if the point is null
     */
    public boolean contains(Point2D p) {
        if (p == null)    
            throw new java.lang.IllegalArgumentException("p is null");
        return contains(root, p, true);
    }

    private boolean contains(Node root, Point2D p, boolean isOdd) {
        if (root == null) {
            return false;
        } else if (root.p.equals(p)) {
            return true;
        } else {
            if (isOdd) {
                if (p.x() < root.p.x())
                    return contains(root.lb, p, !isOdd);
                else
                    return contains(root.rt, p, !isOdd);
            } else {
                if (p.y() < root.p.y())
                    return contains(root.lb, p, !isOdd);
                else 
                    return contains(root.rt, p, !isOdd);
            }
        }

    }

    /**
     * Draw all points to standard draw.
     */
    public void draw() {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-0.1, 1.1);
        StdDraw.setYscale(-0.1, 1.1);

        rect.draw();
        draw(root, true);

        StdDraw.show();
        StdDraw.pause(20);
    }

    private void draw(Node root, boolean isOdd) {
        if (root != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            root.p.draw();

            StdDraw.setPenRadius();
            if (isOdd) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(root.p.x(), root.rect.ymin(), root.p.x(), root.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(root.rect.xmin(), root.p.y(), root.rect.xmax(), root.p.y());
            }

            draw(root.lb, !isOdd);
            draw(root.rt, !isOdd);
        }
    }

    /**
     * Returns an iterable to go through all points that are inside the 
     * rectangle (or on the boundary)
     * @param rect the rectangle
     * @return 
     * @throws java.lang.IllegalArgumentException if the rectangle is null
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.IllegalArgumentException("rectangle is null");
        List<Point2D> pointList = new LinkedList<Point2D>();

        range(root, rect, pointList); 
        return new IterablePoint2D(pointList);
    }

    private void range(Node root, RectHV rect, List<Point2D> pointList) {
        if (root != null) {
            if (rect.contains(root.p))
                pointList.add(root.p);

            Node lbTree = root.lb;
            Node rtTree = root.rt;
            if (lbTree != null && rect.intersects(lbTree.rect))
                range(root.lb, rect, pointList);
            if (rtTree != null && rect.intersects(rtTree.rect))
                range(root.rt, rect, pointList);;
        }
    }

    private class IterablePoint2D implements Iterable<Point2D> {
        List<Point2D> pointList;

        public IterablePoint2D(List<Point2D> pointList) {
            this.pointList = pointList;
        }

        @Override
            public Iterator<Point2D> iterator() {
                return pointList.iterator();
            } 
    }

    /**
     * Returns a nearest neighbor in the set to point p
     * @param p the point to verify
     * @return the nearest neighbor in the set to point p; 
     *         null if the set is empty
     * @throws java.lang.IllegalArgumentException if the point is null 
     */
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("p is null");
        if (root != null)
            nearestP = root.p;

        nearest(root, p, true);

        return nearestP;
    }

    private void nearest(Node root, Point2D p, boolean isOdd) {
        if (root != null) {
            Node lb = root.lb;
            Node rt = root.rt;

            // StdOut.println(root.p);
            if (root.p.distanceSquaredTo(p) < nearestP.distanceSquaredTo(p))
                nearestP = root.p;

            double minDistSquare = nearestP.distanceSquaredTo(p);
            if (lb != null && rt != null) {
                Node first = lb;
                Node second = rt;

                if (isOdd && !(p.x() < root.p.x())) {
                    first = rt;
                    second = lb;
                } else if (!isOdd && !(p.y() < root.p.y())) {
                    first = rt;
                    second = lb;
                }

                if (first.rect.distanceSquaredTo(p) < minDistSquare)
                    nearest(first, p, !isOdd);

                minDistSquare = nearestP.distanceSquaredTo(p);
                if (second.rect.distanceSquaredTo(p) < minDistSquare)
                    nearest(second, p, !isOdd);
            } else if (lb != null 
                    && !(minDistSquare < lb.rect.distanceSquaredTo(p))) {
                nearest(lb, p, !isOdd);
            } else if (rt != null 
                    && !(minDistSquare < rt.rect.distanceSquaredTo(p))) {
                nearest(rt, p, !isOdd);
            }
        }
    } 

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            tree.insert(new Point2D(x, y));
        } 

        // Point2D p = new Point2D(0.81, 0.93);
        Point2D p = new Point2D(0.7, 0.05);
        StdOut.println(tree.nearest(p));

        RectHV rect = new RectHV(0.50872802724375,5.035399390625E-4,0.50872802744375,5.035401390625E-4);
        for (Point2D point: tree.range(rect))
            StdOut.println(point);
        tree.draw();
    }
}
