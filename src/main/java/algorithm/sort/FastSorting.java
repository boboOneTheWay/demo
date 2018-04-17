package algorithm.sort;

public class FastSorting {

	public static int partition(int []array,int lo,int hi){
        int key=array[lo];
        
        while(lo<hi){
            while(array[hi]>=key&&hi>lo){
                hi--;
            }
            array[lo]=array[hi];
            while(array[lo]<=key&&hi>lo){
                lo++;
            }
            array[hi]=array[lo];
        }
        array[hi]=key;
        return hi;
    }
    
    public static void sort(int[] array,int lo ,int hi){
        if(lo>=hi){
            return ;
        }
        int index=partition(array,lo,hi);
        sort(array,lo,index-1);
        sort(array,index+1,hi);
    }
    
    public static void main(String args[]) {
    	
    		int [] a = {1,2,5,6,9,3,5,8};
    		sort(a,0,a.length - 1);
    		for(int j= 0; j<a.length; j++) {
    			System.out.println(a[j]);
    		}
    }
}
