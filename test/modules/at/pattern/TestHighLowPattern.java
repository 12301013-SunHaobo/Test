package modules.at.pattern;

public class TestHighLowPattern {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		testAllPatterns();
		
	}
	
	private static void testAllPatterns(){
		//HighHighLowHigh, //up
		printOut(25, 27, 15, 17);
		//HighHighLowFlat, //open up triangle
		printOut(25, 27, 15, 15);
		//HighHighLowLow, //open triangle
		printOut(25, 27, 15, 13);
		//
		//HighFlatLowHigh, //shrink up triangle
		printOut(25, 25, 15, 17);
		//HighFlatLowFlat, //range
		printOut(25, 25, 15, 15);
		//HighFlatLowLow, //open down triangle
		printOut(25, 25, 15, 13);

		//HighLowLowHigh, //shrink triangle
		printOut(25, 23, 15, 17);
		//HighLowLowFlat, //shrink down triangle
		printOut(25, 23, 15, 15);
		//HighLowLowLow, //down
		printOut(25, 23, 15, 13);

	}
	
	private static void printOut(double high1, double high2, double low1, double low2){
		System.out.println("high1="+high1+", high2="+high2+", low1="+low1+", low2="+low2+", type="+HighLowPattern.getType(high1, high2, low1, low2));
	}

}
