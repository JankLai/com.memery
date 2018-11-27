import java.util.Scanner;

public class exp2_2 {

	  private static int[] array=new int[200];
	    public static void main(String[] args) {
	        Scanner in=new Scanner(System.in);
	        int n=in.nextInt();
	        array[1]=1;
	        System.out.println(Fun(n));
	    }

	    public static int Fun(int n) {
	        int result=1;
	        if(array[n]>0)  return array[n];
	        for ( int i = 1; i <= n/2 ; i++ ) {
	            result +=Fun(i);
	            if((i>10) && (2*(i/10)<=i%10))
	                result -=array[i/10];
	        }
	        array[n]=result;
	        return result;
	    }
	
}
