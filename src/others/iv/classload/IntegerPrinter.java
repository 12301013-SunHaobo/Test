package others.iv.classload;

public class IntegerPrinter {
	
	static {
		System.out.println("123");
	}
    /**
     * Creates an instance of Integer class and prints it.
     */
    public void runMe() {
        System.out.println(new Integer(4));
    }
}
