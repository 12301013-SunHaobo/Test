package others.iv.proveIt;

public class Tester3 {
	 public int calculate(int n){
		 if (n<=1){
			 return 1;
		 }else {
			 return calculate(n-1)+n;
		 }
	 }
	 public static void main (String[] args){
		 Tester3 tester = new Tester3();
		 System.out.println(tester.calculate(5));
	 }
}