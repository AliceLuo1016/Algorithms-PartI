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
        range(rect, root, queue);
        return queue;
    }

    private void range(RectHV rect, Node x, Queue<Point2D> queue){
        if(x != null) {
            if (rect.contains(x.point)) {//search both subtrees
                queue.enqueue(x.point);
                range(rect, x.left, queue);
                range(rect, x.right, queue);
            } else {
                if (x.vertical) {
                    if(rect.xmax() > x.point.x() && rect.xmin() < x.point.x()){
                        range(rect, x.left, queue);
                        range(rect, x.right, queue);
                    }else if (rect.xmax() < x.point.x()) range(rect, x.left, queue);
                    else if (rect.xmin() >= x.point.x() || rect.xmax() == x.point.x()) range(rect, x.right, queue);
                } else {
                    if(rect.ymax() > x.point.y() && rect.ymin() < x.point.y()){
                        range(rect, x.left, queue);
                        range(rect, x.right, queue);
                    }
                    else if (rect.ymax() < x.point.y()) range(rect, x.left, queue);
                    else if (rect.ymin() >= x.point.y() || rect.ymax() == x.point.y()) range(rect, x.right, queue);
                }
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public           Point2D nearest(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        nearestP = null;
        RectHV rect = new RectHV(0, 0, 1, 1);
        nearest(p, root, Double.POSITIVE_INFINITY, rect);
        return nearestP;
    }

    private Double nearest(Point2D p, Node x, Double distance, RectHV rect){
        if(x != null) {
            if (p.distanceSquaredTo(x.point) < distance) {
                nearestP = x.point;
                distance = p.distanceSquaredTo(x.point);
            }


            if(x.vertical){
                RectHV right = new RectHV(x.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
                RectHV left = new RectHV(rect.xmin(), rect.ymin(), x.point.x(), rect.ymax());
                if(p.x() < x.point.x()){
                    //right subtree bounding box
                    Double rightMinDistance = right.distanceSquaredTo(p);
                    Double leftMinDistance = nearest(p, x.left, distance, left);
                    if(leftMinDistance <= rightMinDistance) return distance = leftMinDistance;
                    else return rightMinDistance = nearest(p, x.right, distance, right) < leftMinDistance ?
                            rightMinDistance : leftMinDistance;
                }else{
                    //left subtree bounding box
                    Double leftMinDistance = left.distanceSquaredTo(p);
                    Double rightMinDistance = nearest(p, x.right, distance, right);
                    if(rightMinDistance <= leftMinDistance) return distance = rightMinDistance;
                    else return leftMinDistance = nearest(p, x.left, distance, left) < rightMinDistance ?
                            leftMinDistance : rightMinDistance;
                }
            }else{
                RectHV down = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.point.y());
                RectHV up = new RectHV(rect.xmin(), x.point.y(), rect.xmax(), rect.ymax());
                if(p.y() < x.point.y()){
                    //down subtree bounding box
                    Double upMinDistance = up.distanceSquaredTo(p);
                    Double downMinDistance = nearest(p, x.left, distance, down);
                    if(downMinDistance <= upMinDistance) return distance = downMinDistance;
                    else return upMinDistance = nearest(p, x.right, distance, up) < downMinDistance ?
                            upMinDistance : downMinDistance;
                }else{
                    //up subtree bounding box
                    Double downMinDistance = down.distanceSquaredTo(p);
                    Double upMinDistance = nearest(p, x.left, distance, down);
                    if(upMinDistance <= downMinDistance) return distance = upMinDistance;
                    else return downMinDistance = nearest(p, x.right, distance, up) < upMinDistance ?
                            downMinDistance : upMinDistance;
                }
            }
        }

        return distance;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args){
        KdTree kd = new KdTree();
        kd.insert(new Point2D(0.7, 0.2));
        kd.insert(new Point2D(0.5, 0.4));
        kd.insert(new Point2D(0.2, 0.3));
        kd.insert(new Point2D(0.4, 0.7));
        kd.insert(new Point2D(0.9, 0.6));
        kd.insert(new Point2D(0.8, 0.6));
        kd.insert(new Point2D(0.1, 0.2));
        kd.draw();
        StdOut.println("set size: " + kd.size());
        StdOut.println("contains: " + kd.contains(new Point2D(0.1, 0.3)));

        RectHV rect = new RectHV(0.7,0.4,0.85,0.6);
        rect.draw();
        for(Point2D p: kd.range(rect)){
            StdOut.println("range: " + p.toString());
        }

        StdOut.println("nearest:" + kd.nearest(new Point2D(0.6, 0.6)));

    }
}
