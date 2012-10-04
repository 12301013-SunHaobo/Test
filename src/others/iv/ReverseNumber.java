package others.iv;

public class ReverseNumber {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int n = 123456789;

		System.out.println(reverseIteratively(n));

	}
	
	
	private static int reverseIteratively(int n){
		int reversed = 0;
		int remainder = 0;
		while(n>0){
			remainder = n%10;
			reversed = reversed*10 +remainder;
			n = n/10;
		}
		return reversed;
	}

}
