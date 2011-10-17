package others;

public class TestMath {

	static double LOCK_PROFIT = Double.NaN; 
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//boolean flag = (Double.NaN==Double.NaN);//wrong, Double.Nan doesn't equal to itself. use Double.isNaN() instead
		boolean flag = Double.isNaN(LOCK_PROFIT);
		System.out.println("flag:"+flag);

	}

}
