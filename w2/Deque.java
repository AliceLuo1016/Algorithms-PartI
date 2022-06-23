import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size = 0;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque(){
        first = new Node();
        last = first;
    }

    // is the deque empty?
    public boolean isEmpty(){
        return first.item == null;
    }

    // return the number of items on the deque
    public int size(){
        return size;
    }

    // add the item to the front
    public void addFirst(Item item){
        sanityCheck(item);
        if (first.item != null) {
            Node old_first = first;
            first = new Node();
            first.item = item;
            first.next = old_first;
            old_first.prev = first;
        }else{
            first.item = item;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item){
        sanityCheck(item);
        if (last.item != null) {
            Node old_last = last;
            last = new Node();
            last.item = item;
            last.prev = old_last;
            old_last.next = last;
        }else{
            last.item = item;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        sanityCheckDeque();
        Item i = first.item;
        if(first.next != null){
            first = first.next;
            first.prev = null;
        }else{
            first.item = null;
        }
        size--;
        return i;
    }

    // remove and return the item from the back
    public Item removeLast() {
        sanityCheckDeque();
        Item i = last.item;
        if (last.prev != null) {
            last = last.prev;
            last.next = null;
        }else{
            last.item = null;
        }
        size--;
        return i;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new DequeIterator();
    }
    private class DequeIterator implements Iterator<Item>{
        private Node current = first;
        public boolean hasNext(){
            if (current != null && current.item != null){
                return true;
            }
            else{
                return false;
            }
        }

        public void remove(){
            throw new UnsupportedOperationException();
        }

        public Item next(){
            if (current != null && current.item != null) {
                throw new java.util.NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    private void sanityCheck(Item item){
        if (item == null){
            throw new IllegalArgumentException();
        }
    }

    private void sanityCheckDeque(){
        if (first.item == null){
            throw new java.util.NoSuchElementException();
        }
    }

    // unit testing (required)
    public static void main(String[] args){
        Deque<String> d = new Deque<String>();
        StdOut.println(d.isEmpty());
        StdOut.println(d.size());
        d.addFirst("Mellow");
        StdOut.println(d.isEmpty());
        StdOut.println(d.size());
        d.addFirst("Ginger");
        StdOut.println(d.size());
        d.addLast("Brownie");
        StdOut.println(d.size());

        Iterator i = d.iterator();
        while(i.hasNext()){
            StdOut.println(i.next());
        }
        StdOut.println("\n");

        StdOut.println(d.removeFirst());
        StdOut.println(d.removeLast());
        StdOut.println(d.removeLast());

        i = d.iterator();
        while(i.hasNext()){
            StdOut.println(i.next());
        }


    }

}