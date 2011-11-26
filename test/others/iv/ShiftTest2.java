package others.iv;

/**
 * Q: program to test whether an input long integer’s binary representation is
 * the same left-to-right or right-to-left.
 */
public class ShiftTest2 {
	public static final long NOT_MATCHED_INPUT = Long.MAX_VALUE;
	public static final long MATCHED_INPUT = 0xff00ff0180ff00ffL;
	public static final long MINUS_INPUT = 0xef00ff0180ff00ffL;
	
	public static void main(String[] args) {
		long input = MINUS_INPUT;
		System.out.println(toBinaryString(input));
		
		for (short start = 0, end = 63; start < end; start++, end--) {
			if(isZero(input, start)!=isZero(input, end)){
				System.out.format("\nBad - Mismatch between Positions #%d and #%d. (0-based)", start, end);
				return;
			}
		}
		System.out.println("\nGood - Binary representation reads identical from left or right");
	}

	/**
	 * check if bit value at idx position is zero for specified input long 
	 * @param input long
	 * @param idx 
	 * @return
	 */
	private static boolean isZero(long input, int idx){
		return (input & (1L << idx)) == 0;
	}

	
	/**
	 * Convert input long to a binary String
	 * @param input long
	 * @return signed binary String
	 */
	private static String toBinaryString(long input){
		StringBuffer sb = new StringBuffer();
		for (short i = 63; i >=0; i--) {
			sb.append(isZero(input, i)?"0":"1");
		}
		return sb.toString();
	}
	

}