package others.iv;

public class ReverseString {

	public static void main(String[] args) {
		String s = "abcdefghijklmn";
		String result = reverse(s);
		System.out.println(result);
	}

	
	private static String reverse(String s){
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
			sb.append(reverse(s.substring(midIndex+1)));
			sb.append(reverse(s.substring(0, midIndex+1)));
			return sb.toString();
		}
	}
}
