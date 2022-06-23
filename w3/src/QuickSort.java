import edu.princeton.cs.algs4.StdRandom;

public class QuickSort {

    public QuickSort(int[] a){
        StdRandom.shuffle(a);
        sort(a, 0, a.length-1);
    }

    public int select(int[]a, int k){
        StdRandom.shuffle(a);
        return select(a, 0, a.length-1, k);
    }

    private int select(int[] a, int lo, int hi, int k){
        if(lo >= hi) return a[k];
        int j = partition(a, lo, hi);

        if(j > k) hi = j - 1;
        else if(j < k) lo = j + 1;
        else return a[k];

        select(a, lo, hi, k);
        return a[k];
    }

    private void sort(int[] a, int lo, int hi){
        if(lo >= hi) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j-1);
        sort(a, j+1, hi);
    }

    private int partition(int[] a, int lo, int hi){
        int i = lo, j = hi+1;
        while(true){
            while(a[++i] < a[lo]) if(i == hi) break;
            while(a[--j] > a[lo]) if(j == lo) break;
            if(i >= j) break;
            swap(a, i, j);
        }
        swap(a, lo, j);
        return j;
    }

    private void swap(int[] a, int i, int j){
       int tmp = a[i];
       a[i] = a[j];
       a[j] = tmp;
    }
}
