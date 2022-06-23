import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item>{

    private Item[] arr;
    private int[] forward;
    private int[] backward;
    private int size;
    private int occupy;
    // location for adding new item
    private int loc;

    // construct an empty randomized queue
    public RandomizedQueue(){
        arr = (Item[]) new Object[1];
        size = 1;
        occupy = 0;
        loc = 0;

        forward = new int[1];
        backward = new int[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty(){
        return occupy == 0;
    }

    // return the number of items on the randomized queue
    public int size(){
        return occupy;
    }

    // add the item
    public void enqueue(Item item){
        if (item == null) throw new IllegalArgumentException();
        if (loc < size) {
            arr[loc] = item;
            loc++;
            occupy++;
        }else if (loc == size){
            //increase arr size when no space
            Item[] new_arr = (Item[]) new Object[size*2];
            loc=0;
            Iterator I = iterator();
            while(I.hasNext()){
                new_arr[loc] = (Item) I.next();
                loc++;
            }
            arr = new_arr;
            int[] new_forward = new int[size*2];
            int[] new_backward = new int[size*2];
            forward = new_forward;
            backward = new_backward;
            size = size*2;

            arr[loc] = item;
            loc++;
            occupy++;
        }

    }

    // remove and return a random item
    public Item dequeue(){
        if(isEmpty()) throw new java.util.NoSuchElementException();
        int random = StdRandom.uniform(loc);
        while(arr[random] == null){
            random = StdRandom.uniform(loc);
        }
        Item out = arr[random];
        arr[random] = null;

        //maintain step
        forward[random] = 1;
        backward[random] = 1;

        //both forward and backward units are non-empty
        if(random > 0 && random+1<size && forward[random-1]>0 && forward[random+1]>0){
            //total distance
            int distance = forward[random+1] + backward[random-1] + 1;
            forward[random - backward[random-1]] = distance;
            backward[random + forward[random+1]] = distance;
        }
        //backward unit is non-empty
        else if (random+1<size && forward[random+1]>0) {
            forward[random] = forward[random+1]+1;
            backward[random + forward[random]-1] = forward[random];
        }
        //forward unit is non-empty
        else if(random > 0 && forward[random-1]>0){
            backward[random] = backward[random-1]+1;
            forward[random-backward[random]+1] = backward[random];
        }
        occupy--;

        //shrink size when occupy<1/4size
        if(occupy<size/4){
            Item[] new_arr = (Item[]) new Object[size/2];
            loc=0;
            Iterator I = iterator();
            while(I.hasNext()){
                new_arr[loc] = (Item) I.next();
                loc++;
            }
            arr = new_arr;
            int[] new_forward = new int[size/2];
            int[] new_backward = new int[size/2];
            forward = new_forward;
            backward = new_backward;
            size = size/2;
        }

//        for(int i=0; i<size; i++){
//            StdOut.println(forward[i]);
//        }
//        StdOut.println("\n");
//
//        for(int i=0; i<size; i++){
//            StdOut.println(backward[i]);
//        }
//        StdOut.println("\n");

        return out;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if(isEmpty()) throw new java.util.NoSuchElementException();
        int random = StdRandom.uniform(loc);
        while(arr[random] == null){
            random = StdRandom.uniform(loc);
        }
        return arr[random];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){return new RandomizedQueueIterator();}

    private class RandomizedQueueIterator implements Iterator<Item>{
        private int count =0;
        private RandomizedQueue<Item> temp_arr;

        public RandomizedQueueIterator(){
            temp_arr = new RandomizedQueue<Item>();
            for(int i=0; i<size&&count<occupy; i++){
                i = forward[i]+i;
                temp_arr.enqueue(arr[i]);
                count++;
            }
        }

        public boolean hasNext() {
            return count>0;
        }

        public Item next() {
            if(count<=0) throw new java.util.NoSuchElementException();
            count--;
            return(temp_arr.dequeue());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }


    }
    // unit testing (required)
    public static void main(String[] args){
        RandomizedQueue<String> R = new RandomizedQueue<String>();
        R.enqueue("a");
        R.enqueue("b");
        R.enqueue("c");
        R.enqueue("d");
        R.enqueue("e");
        R.enqueue("f");
        R.enqueue("g");
        R.enqueue("h");


        Iterator i = R.iterator();
        while(i.hasNext()){
            StdOut.println(i.next());
        }
        StdOut.println("\n");

        R.dequeue();

        i = R.iterator();
        while(i.hasNext()){
            StdOut.println(i.next());
        }

        StdOut.println("\n");

        R.dequeue();
        i = R.iterator();
        while(i.hasNext()){
            StdOut.println(i.next());
        }

        StdOut.println("\n");

        R.dequeue();
        i = R.iterator();
        while(i.hasNext()){
            StdOut.println(i.next());
        }

        StdOut.println("\n");

        R.dequeue();
        i = R.iterator();
        while(i.hasNext()){
            StdOut.println(i.next());
        }

        StdOut.println("\n");

        R.dequeue();
        i = R.iterator();
        while(i.hasNext()){
            StdOut.println(i.next());
        }


    }

}
