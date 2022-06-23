import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {

    public static void main(String[] args){

        RandomizedQueue<String> R = new RandomizedQueue<String>();

        while(!StdIn.isEmpty()){
            R.enqueue(StdIn.readString());
        }

        int count = 0;
        Iterator it = R.iterator();
        while(it.hasNext() && count<Integer.parseInt(args[0])){
            count++;
            StdOut.println(it.next());
        }
    }
}
