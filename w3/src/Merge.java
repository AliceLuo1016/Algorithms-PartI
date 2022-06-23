public class Merge {
    private static void Merge(int[] a, int[] aux, int lo, int mid, int hi){
        for (int i = lo; i<=hi; i++){
            aux[i] = a[i];
        }
        int j = lo, k = mid+1;
        for (int i =lo; i <= hi; i++){
            if      (j>mid)     a[i] = aux[k++];
            else if (k>hi)      a[i] = aux[j++];
            else if (aux[k]<aux[j]) a[i] = aux[k++];
            else a[i] = a[j++];
        }
    }

    private static void Sort(int[] a, int[] aux, int lo, int hi){
        if(lo>=hi) return;
        int mid = lo+(hi-lo)/2;
        Sort(a, aux, lo, mid);
        Sort(a, aux, mid+1, hi);
        if(a[mid+1] >= a[mid]) return;
        Merge(a, aux, lo, mid, hi);
    }

    private static void Sort(int[] a){
        int[] aux = new int[a.length];
        Sort(a, aux, 0, a.length-1);
    }

    public static void main(String[] args) {
        int[] a = new int[]{7,6,5,4,3,2,1,0};
        Merge.Sort(a);
        for(int i = 0; i < a.length; i++){
            System.out.println(a[i]);
        }
    }

}




