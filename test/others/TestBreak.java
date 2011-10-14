package others;

public class TestBreak {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int i = 0;

		switch (i) {
			case 0:
				System.out.println(" is 0 1");
				
				if(i>-1){
					System.out.println("i>-1");
					break;//break from switch
				}
				
				System.out.println(" is 0 2");
				
				break;
			case 1:
				System.out.println(" is 1");
				break;
			case 2:
				System.out.println(" is 2");
				break;
			default:
				break;
		}

	}

}
