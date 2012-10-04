package others.iv;

/**
 * Recursively reverse a String 
 */
public class ReverseString {

	public static void main(String[] args) {
		String s = "abcdefghijklmn";
		String result = reverseRecursively(s);
		System.out.println(result);
	}

	private static String reverseRecursively(String s){
		if(s.length()<2){
			return s;
		} else {
			return reverseRecursively(s.substring(1))+s.charAt(0);
		}
	}

	@Deprecated
	//by Rong, 
	private static String reverseRecursive(String s){
		if(s.length()<=1){
			return s;
		}else if(s.length()==2){
			StringBuffer sb = new StringBuffer();
			sb.append(s.charAt(1));
			sb.append(s.charAt(0));
			return sb.toString();
		}else {//split to 2 sub strings
			int midIndex = s.length()/2;
			StringBuffer sb = new StringBuffer();
			sb.append(reverseRecursive(s.substring(midIndex+1)));
			sb.append(reverseRecursive(s.substring(0, midIndex+1)));
			return sb.toString();
		}
	}
	
	

}
