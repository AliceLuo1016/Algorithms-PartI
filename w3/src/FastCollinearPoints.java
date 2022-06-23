import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private List<LineSegment> lineSegments = new ArrayList<LineSegment>();
    private List<Point> lineChecker = new ArrayList<Point>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points){

        if (points == null) throw new IllegalArgumentException();

        //create a new arr of points
        Point[] copy = new Point[points.length];
        copy = points.clone();
        Arrays.sort(copy);

        if(check(copy)) throw new IllegalArgumentException();

        for(int i = 0; i < points.length; i++){

            Arrays.sort(copy, points[i].slopeOrder());

            int count = 0;
            for(int k = 1; k < copy.length; k++){
                if(points[i].slopeTo(copy[k]) == points[i].slopeTo(copy[k-1])){
                    count++;
                }else{
                    if(count >= 2){

                        Point[] line = new Point[count+2];
                        int lineIndex = 0;
                        line[lineIndex++] = points[i];

                        for(int a = k-count-1; a < k; a++){
                            line[lineIndex++] = copy[a];
                        }

                        Arrays.sort(line);
                        boolean exists = false;
                        for(int j = 1; j < lineChecker.size(); j+=2){
                            if(line[0] == lineChecker.get(j-1) && line[line.length - 1] == lineChecker.get(j)){
                                //line exists
                                exists = true;
                            }
                        }
                        if (!exists) {
                            LineSegment lineSegment = new LineSegment(line[0], line[line.length - 1]);
                            lineSegments.add(lineSegment);
                            lineChecker.add(line[0]);
                            lineChecker.add(line[line.length - 1]);
                        }


                    }
                    count = 0;
                }


            }

            if(count >= 2){

                Point[] line = new Point[count+2];
                int lineIndex = 0;
                line[lineIndex++] = points[i];

                for(int a = copy.length-count-1; a < copy.length; a++){
                    line[lineIndex++] = copy[a];
                }

                Arrays.sort(line);
                boolean exists = false;
                for(int j = 1; j < lineChecker.size(); j+=2){
                    if(line[0] == lineChecker.get(j-1) && line[line.length - 1] == lineChecker.get(j)){
                        //line exists
                        exists = true;
                    }
                }
                if (!exists) {
                    LineSegment lineSegment = new LineSegment(line[0], line[line.length - 1]);
                    lineSegments.add(lineSegment);
                    lineChecker.add(line[0]);
                    lineChecker.add(line[line.length - 1]);
                }


            }


        }
    }

    private boolean check(Point[] ps){
        for(int i = 0; i < ps.length; i++){
            //check if contains null point
            if(ps[i] == null) return true;
            if(i >= 1 && ps[i].compareTo(ps[i-1]) == 0) return true;
        }
        return false;
    }


    // the number of line segments
    public int numberOfSegments(){
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }

}
