package others.iv;

import java.util.HashMap;
import java.util.Map;

public class Permutation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		permutationsRoland();
		//permutationsIterative("abc");
		//permutationRecursive("", "abc");
	}

	//using random number to get all permutations
	private static void permutationsRoland() {
		char[] str = { 'a', 'b', 'c', 'd'};
		String x = new String(str);

		Map<Integer, Character> m = new HashMap<Integer, Character>();
		Integer i = 0;
		String numStr = "";

		for (Character c : str) {
			m.put(i, x.charAt(i));
			numStr += i;
			i++;
		}

		int len = str.length;
		int Min = 0;
		int Max = len - 1;

		int permMax = 1;
		for (int a = 1; a <= len; a++) {
			permMax *= a;
		}

		String newNumStr = "";
		Map<Integer, String> m2 = new HashMap<Integer, String>();
		m2.put(0, numStr);
		int z = 1;
		while (true) {
			newNumStr = "";
			while (newNumStr.length() != len) {
				int rand = Min + (int) (Math.random() * ((Max - Min) + 1));
				String randStr = rand + "";
				if (newNumStr.indexOf(randStr) < 0) {
					newNumStr += randStr;
				}
			}

			if (!m2.containsValue(newNumStr)) {
				m2.put(z, newNumStr);
			}
			z++;

			if (m2.size() == permMax) {

				break;
			}

		}

		StringBuilder sb = new StringBuilder();

		for (String v : m2.values()) {
			sb.append(v + " | ");
		}

		String result = sb.toString();
		for (Map.Entry<Integer, Character> entry : m.entrySet()) {
			String keyTemp = entry.getKey() + "";
			String valueTemp = entry.getValue() + "";
			result = result.replaceAll(keyTemp, valueTemp);
		}

		System.out.println(result);
	}

	private static void permutationsIterative(String string) {
		int[] factorialArr = new int[string.length() + 1];
		factorialArr[0] = 1;
		for (int i = 1; i <= string.length(); i++) {
			factorialArr[i] = factorialArr[i - 1] * i;
		}
		int maxFactorial = factorialArr[string.length()];

		for (int i = 0; i < maxFactorial; i++) {
			String onePermutation = "";
			String temp = string;
			int positionCode = i;
			for (int position = string.length(); position > 0; position--) {
				int selected = positionCode / factorialArr[position - 1];
				onePermutation += temp.charAt(selected);
				positionCode = positionCode % factorialArr[position - 1];
				temp = temp.substring(0, selected) + temp.substring(selected + 1);
			}
			System.out.println(onePermutation);
		}
	}

	/**
	 * [abc], defghij
	 * 
	 * permutationRecursive([abc]d, efghij) permutationRecursive([abc]e, dfghij)
	 * permutationRecursive([abc]f, defghij)
	 * 
	 * 
	 * @param prefix
	 *            [abc]
	 * @param s
	 *            [defghij]
	 */
	private static void permutationRecursive(String prefix, String s) {
		int n = s.length();
		if (n == 0) {
			System.out.println(prefix);
		} else {
			for (int i = 0; i < n; i++) {
				permutationRecursive(prefix + s.charAt(i), s.substring(0, i) + s.substring(i + 1, n));
			}
		}
	}
}
