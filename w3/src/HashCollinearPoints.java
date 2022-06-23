import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class HashCollinearPoints {
    //all lines
    private HashMap<Line, HashSet<Point>> lines = new HashMap<>();
    //slopes from one single point
    private HashMap<Double, HashSet<Point>> slopes = new HashMap<>();
    //slopes need to be skipped because lines have been discovered
    private HashSet<Line> skipSlopes = new HashSet<>();
    //used to check if there are duplicate points
    private HashSet<Point> checkDuplicate = new HashSet<>();

    private class Line{
        private Point p;
        private Double s;
        private Line(Point p, Double s){
            this.p = p;
            this.s = s;
        }

        @Override
        public boolean equals(Object o){
            if (o == this) return true;
            if (!(o instanceof Line)) {
                return false;
            }
            Line line = (Line) o;
            return line.p.equals(p) && line.s.equals(s);
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + p.hashCode() + s.hashCode();
            return result;
        }
    }

    // finds all line segments containing 4 points
    public HashCollinearPoints(Point[] points){

        if (points == null) throw new IllegalArgumentException();

        //Arrays.sort(points);

        for(int i = 0; i < points.length; i++){

            if(checkDuplicate.contains(points[i])) throw new IllegalArgumentException();
            checkDuplicate.add(points[i]);

            for(int j = i+1; j < points.length; j++){
                if (points[i] == null || points[j] == null) throw new IllegalArgumentException();

                Double s = points[i].slopeTo(points[j]);
                Line tmp1 = new Line(points[i], s);
                Line tmp2 = new Line(points[j], s);


                if(slopes.containsKey(s) && !skipSlopes.contains(tmp2)){
                    if(!slopes.get(s).contains(points[j])){
                        slopes.get(s).add(points[j]);
                        if(slopes.get(s).size() == 4){
                            Line l = new Line(points[i], s);
                            lines.put(l, slopes.get(s));
                        }
                    }

                }else if(!skipSlopes.contains(tmp2)){
                    //Add slope and points pair
                    HashSet<Point> ps = new HashSet<>();
                    ps.add(points[i]);
                    ps.add(points[j]);
                    slopes.put(s, ps);
                }
                skipSlopes.add(tmp1);
                skipSlopes.add(tmp2);
            }

            slopes.clear();

        }

        //1. points with the same slope but not in one line
        //2. points with the same slope but have multiple lines

    }

    // the number of line segments
    public int numberOfSegments(){
        return lines.size();
    }

    // the line segments

    public LineSegment[] segments(){
        LineSegment[] ling_segments = new LineSegment[numberOfSegments()];
        int index = 0;

        for(Line l : lines.keySet()){
            HashSet<Point> hsp = lines.get(l);

            Iterator<Point> itp = hsp.iterator();
            Point[] arr = new Point[hsp.size()];
            int count = 0;

            while(itp.hasNext()) {
                arr[count++] = itp.next();
            }

            Arrays.sort(arr);
            Point first = arr[0];
            Point last = arr[arr.length-1];

//            if(l.s == 0.0){//horizontal line
//
//            }else if(l.s == Double.POSITIVE_INFINITY){//vertical line
//
//            }

            LineSegment line_segment = new LineSegment(first, last);
            ling_segments[index++] = line_segment;
        }
        return ling_segments;
    }


    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        HashCollinearPoints collinear = new HashCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
