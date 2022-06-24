import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree {

    private class Node{

        private final Point2D point;
        private Node left;
        private Node right;
        //using x coordinate means true, using y coordinate means false
        private final boolean vertical;

        private Node(Point2D point, Node left, Node right, boolean vertical){
            this.point = point;
            this.left = left;
            this.right = right;
            this.vertical = vertical;
        }

    }

    private Node root;
    private int count;
    private Point2D nearestP;
    private Double distance;

    // construct an empty set of points
    public         KdTree(){
        root = null;
        count = 0;
    }

    // is the set empty?
    public           boolean isEmpty(){
        return size() == 0;
    }

    // number of points in the set
    public               int size(){
        return count;
    }

    // add the point to the set (if it is not already in the set)
    public              void insert(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        root = insert(root, p, true);
    }

    private Node insert(Node x, Point2D p, boolean vertical){
        if(x == null) {
            count++;
            return new Node(p, null, null, vertical);
        }else if(x.point.equals(p)){ //ignore duplicates
            return x;
        }
        //compare x coordinate
        else if(vertical){
            if(p.x() < x.point.x()) x.left = insert(x.left, p, !vertical);
            else x.right = insert(x.right, p, !vertical);
        }else{//compare y coordinate
            if(p.y() < x.point.y()) x.left = insert(x.left, p, !vertical);
            else x.right = insert(x.right, p, !vertical);
        }
        return x;
    }

    // does the set contain point p?
    public           boolean contains(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p){
        if(x != null) {
            if (x.point.equals(p)) return true;
            if (x.vertical) {
                if (p.x() < x.point.x()) return contains(x.left, p);
                else return contains(x.right, p);
            } else {
                if (p.y() < x.point.y()) return contains(x.left, p);
                else return contains(x.right, p);
            }
        }
        return false;
    }

    // draw all points to standard draw
    public              void draw(){
        Queue<Node> q1 = new Queue<Node>();
        //keep a record of the line boundary and if it is a left or right  child
        Queue<Point2D> q2 = new Queue<Point2D>();
        Queue<Boolean> left = new Queue<Boolean>();
        boolean first = true;
        q1.enqueue(root);
        while(!q1.isEmpty()){
            Node x = q1.dequeue();
            if(x.left != null) {
                q1.enqueue(x.left);
                q2.enqueue(x.point);
                left.enqueue(true);
            }
            if(x.right != null) {
                q1.enqueue(x.right);
                q2.enqueue(x.point);
                left.enqueue(false);
            }
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            x.point.draw();
            StdDraw.setPenRadius();
            if(x.vertical){
                StdDraw.setPenColor(StdDraw.RED);
                if(!first) {
                    Double bound = q2.dequeue().y();
                    if (left.dequeue()) (new Point2D(x.point.x(), 0)).drawTo(new Point2D(x.point.x(), bound));
                    else (new Point2D(x.point.x(), bound)).drawTo(new Point2D(x.point.x(), 1));
                }else (new Point2D(x.point.x(), 0)).drawTo(new Point2D(x.point.x(), 1));
            }else {
                StdDraw.setPenColor(StdDraw.BLUE);
                if (!first) {
                    Double bound = q2.dequeue().x();
                    if (left.dequeue()) (new Point2D(0, x.point.y())).drawTo(new Point2D(bound, x.point.y()));
                    else (new Point2D(bound, x.point.y())).drawTo(new Point2D(1, x.point.y()));
                } else (new Point2D(0, x.point.y())).drawTo(new Point2D(1, x.point.y()));
            }
            first = false;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect){
        if(rect == null) throw new IllegalArgumentException();
        Queue<Point2D> queue = new Queue<Point2D>();
        RectHV range = new RectHV(0, 0, 1, 1);
        range(rect, root, queue, range);
        return queue;
    }

    private void range(RectHV rect, Node x, Queue<Point2D> queue, RectHV range){
        if(x == null) return;
        //when x is a leaf node
        if(x.left == null && x.right == null && rect.contains(x.point)){
            queue.enqueue(x.point);
            return;
        }
        if(rect.contains(x.point)) queue.enqueue(x.point);
        if(x.vertical){
            RectHV left = new RectHV(range.xmin(), range.ymin(), x.point.x(), range.ymax());
            RectHV right = new RectHV(x.point.x(), range.ymin(), range.xmax(), range.ymax());
            if(rect.intersects(left) && rect.intersects(right)){
                range(rect, x.left, queue, left);
                range(rect, x.right, queue, right);
            }else if(rect.intersects(left)){
                range(rect, x.left, queue, left);
            }else if(rect.intersects(right)){
                range(rect, x.right, queue, right);
            }else return;

        }else{
            RectHV down = new RectHV(range.xmin(), range.ymin(), range.xmax(), x.point.y());
            RectHV up = new RectHV(range.xmin(), x.point.y(), range.xmax(), range.ymax());
            if(rect.intersects(down) && rect.intersects(up)){
                range(rect, x.left, queue, down);
                range(rect, x.right, queue, up);
            }else if(rect.intersects(down)){
                range(rect, x.left, queue, down);
            }else if(rect.intersects(up)){
                range(rect, x.right, queue, up);
            }else return;
        }



    }



    // a nearest neighbor in the set to point p; null if the set is empty
    public           Point2D nearest(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        nearestP = null;
        distance = Double.POSITIVE_INFINITY;
        RectHV rect = new RectHV(0, 0, 1, 1);
        nearest(p, root, rect);
        return nearestP;
    }

    private void nearest(Point2D p, Node x, RectHV rect){
        if(x == null || rect.distanceSquaredTo(p) > distance) return;
        if (p.distanceSquaredTo(x.point) < distance) {
            nearestP = x.point;
            distance = p.distanceSquaredTo(x.point);
        }
        if(x.vertical){
            RectHV left = new RectHV(rect.xmin(), rect.ymin(), x.point.x(), rect.ymax());
            RectHV right = new RectHV(x.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            if(p.x() < x.point.x()) {
                nearest(p, x.left, left);
                nearest(p, x.right, right);
            }else{
                nearest(p, x.right, right);
                nearest(p, x.left, left);
            }
        }else{
            RectHV down = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.point.y());
            RectHV up = new RectHV(rect.xmin(), x.point.y(), rect.xmax(), rect.ymax());
            if(p.y() < x.point.y()) {
                nearest(p, x.left, down);
                nearest(p, x.right, up);
            }else{
                nearest(p, x.right, up);
                nearest(p, x.left, down);
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args){
        KdTree kd = new KdTree();
        kd.insert(new Point2D(0.7, 0.2));
        kd.insert(new Point2D(0.5, 0.4));
        kd.insert(new Point2D(0.2, 0.3));
        kd.insert(new Point2D(0.4, 0.7));
        kd.insert(new Point2D(0.9, 0.6));
        //kd.insert(new Point2D(0.8, 0.6));
        //kd.insert(new Point2D(0.1, 0.2));
        kd.draw();
        StdOut.println("set size: " + kd.size());
        StdOut.println("contains: " + kd.contains(new Point2D(0.1, 0.3)));

        RectHV rect = new RectHV(0.45,0.4,0.9,0.6);
        rect.draw();
        for(Point2D p: kd.range(rect)){
            StdOut.println("range: " + p.toString());
        }

        StdDraw.setPenRadius(0.01);
        Point2D queryP = new Point2D(0.188, 0.898);
        queryP.draw();
        StdDraw.setPenRadius();

        StdOut.println("nearest:" + kd.nearest(queryP));

    }
}
