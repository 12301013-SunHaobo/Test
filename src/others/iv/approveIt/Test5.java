package others.iv.approveIt;

public class Test5 {

	static String theString = "Initialized";
	
	
	public static void main(String[] args) {
		System.out.println(theString);

	}
	static {
		System.out.println(theString);
		theString = "Hello World";
		
	}

}
