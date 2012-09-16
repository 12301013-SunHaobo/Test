package others.iv;

/**
 * Q: program to test whether an input long integer’s binary representation is
 * the same left-to-right or right-to-left.
 */
public class ShiftTest {
	public static void main(String[] args) {
		//long input = 0xff00ff0180ff00ffL;
		long input = 8L;
		char[] bitsForDisplay = populateArray(input);
		dumpArray(bitsForDisplay);
		for (short start = 0, end = 63; start < end; start++, end--) {
			if (bitsForDisplay[start] != bitsForDisplay[end]) {
				System.out.format("\nBad - Mismatch between Positions #%d and #%d. (1-based)", ++start, ++end);
				return;
			}
		}
		System.out.println("\nGood - Binary representation reads identical from left or right");
	}

	private static void dumpArray(char[] array64) {
		int subscript = 0;
		for (char element : array64) {
			System.out.print(element);
			if (++subscript % 8 == 0)
				System.out.print(' ');
		}
	}

	private static char[] populateArray(long input) {
		char[] bitsForDisplay = new char[64];
		for (int subscript = 0; subscript < 64; subscript++, input <<= 1) {
			// if the shifted num is negative, then leading bit must be 1
			bitsForDisplay[subscript] = ((input < 0) ? '1' : '0');
		}
		return bitsForDisplay;
	}
}