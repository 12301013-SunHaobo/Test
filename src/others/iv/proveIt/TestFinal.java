package others.iv.proveIt;

public final class TestFinal {

	//abstract void test() ;
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String result = testFinallyMethod();
		System.out.println(result);
		
		
		
	}

	
	private static String testFinallyMethod() {
		try {
			System.out.println("try");
			if(false){
				throw new Exception("exception");
			}
			return "001";
			
		} catch (Exception e){
			System.out.println("catch");
		} finally {
			System.out.println("finally");
			return "002";
		}
	}
	
}
