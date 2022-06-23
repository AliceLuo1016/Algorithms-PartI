import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class PointSET {
    private SET<Point2D> SET;

    // construct an empty set of points
    public         PointSET(){
        SET = new SET<Point2D>();
    }

    // is the set empty?
    public           boolean isEmpty(){
        return SET.isEmpty();
    }

    // number of points in the set
    public               int size(){
        return SET.size();
    }

    // add the point to the set (if it is not already in the set)
    public              void insert(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        SET.add(p);
    }

    // does the set contain point p?
    public           boolean contains(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        return SET.contains(p);
    }

    // draw all points to standard draw
    public              void draw(){
        for(Point2D p : SET){
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect){
        if(rect == null) throw new IllegalArgumentException();
        Queue<Point2D> range = new Queue<Point2D>();
        for(Point2D p : SET){
            if(rect.contains(p)){
                range.enqueue(p);
            }
        }
        return range;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public           Point2D nearest(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        Double distance = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for(Point2D q : SET){
            if(nearest == null || q.distanceSquaredTo(p) < distance){
                distance = q.distanceSquaredTo(p);
                nearest = q;
            }
        }
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args){
        PointSET set = new PointSET();

        set.insert(new Point2D(0.1, 0.1));
        set.insert(new Point2D(0.2, 0.3));
        set.insert(new Point2D(0.5, 0.3));

        RectHV rect = new RectHV(0.1,0.1,0.4,0.4);
        for(Point2D p: set.range(rect)){
            StdOut.println(p.toString());
        }

        StdDraw.enableDoubleBuffering();
        rect.draw();
        set.draw();
        StdDraw.show();

    }
}