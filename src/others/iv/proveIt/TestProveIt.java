package others.iv.proveIt;

import java.util.ArrayList;

public class TestProveIt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 //test1();
		 //test2();
		 //test3();
		 //test4();
		 //test5();
		 //test8();
		 test21();
	}
	
	private static void test1(){
		int val = 2;
		switch(val){
		case 1: 
			System.out.println("1");
		case 2: 
			System.out.println("2");
		case 3: 
			System.out.println("3");
		default: 
			System.out.println("de");
		}
	}

	
	private static void test_x2(){
		StringBuffer ms = new StringBuffer("Test123");
		ms.reverse();
		System.out.println(ms);
	}
	
	private static void test3()  {
		System.out.println("Test123".substring(4));
	}
	
	private static void test4(){
		int a = 1;
		int b =2;
		int c = 3;
		if((a==1) || (++b>1)) {
			c +=b;
		}
		System.out.println(c);
	}
	
	private static void test5(){
		int a =1;
		int b =2;
		int c = ++a +b--;
		
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
	}
	
	private static void test6(){
		short s1 = 1;
		short s2 = 2;
//		short s3 = s1+s2;
	}

	
/*	
	private static void test7(){
		File file = new File("C:\\MyFile.txt");
	    FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    DataInputStream dis = null;

	    try {
	      fis = new FileInputStream(file);

	      // Here BufferedInputStream is added for fast reading.
	      bis = new BufferedInputStream(fis);
	      dis = new DataInputStream(bis);

	      // dis.available() returns 0 if the file does not have more lines.
	      while (dis.available() != 0) {

	      // this statement reads the line from the file and print it to
	        // the console.
	        System.out.println(dis.readLine());
	      }

	      // dispose all the resources after using them.
	      fis.close();
	      bis.close();
	      dis.close();

	    } catch (Exception e) {
		  e.printStackTrace();
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    }
	  }
	
	}
*/
	private static void test8(){
		int a = 312;
		int b = 4;
		int c = a>>b;
		System.out.println(c);
	}
	
	private static void test9(){
		ArrayList myList = new ArrayList();
		myList.add("One");
		myList.add("One");
		myList.add("One");
		
	}
	
	private static void test21(){
		int val = 0;
		for(;;){
			System.out.println(val);
			val = val +1;
			if(val >3) {
				break;
			}
		}
	}
	
	private static void test10(){
		
	}
	
	private static void test11(){
		
	}
	
	private static void test12(){
		
	}
	
	private static void test13(){
		
	}
	
	private static void test14(){
		
	}
	
	private static void test15(){
		
	}
	
	private static void test16(){
		
	}
	
	private static void test17(){
		
	}
	
	private static void test18(){
		
	}
	
	private static void test19(){
		
	}
	
	private static void test20(){
		
	}
	

	
	private static void test22(){
		
	}
	
	private static void test23(){
		
	}
	
	private static void test24(){
		
	}
	
	private static void test25(){
		
	}
	
	private static void test26(){
		
	}
	
	private static void test27(){
		
	}
	
	private static void test28(){
		
	}
	
	private static void test29(){
		
	}
	
	private static void test30(){
		
	}
	
	private static void test31(){
		
	}
	
	private static void test32(){
		
	}
	
	private static void test33(){
		
	}
	
	private static void test34(){
		
	}
	
	private static void test35(){
		
	}
	
	private static void test36(){
		
	}
	
	private static void test37(){
		
	}
	
	private static void test38(){
		
	}
	
	private static void test39(){
		
	}
	
	private static void test40(){
		
	}
	
	private static void test41(){
		
	}

	private static void test42(){
		
	}

	private static void test43(){
		
	}

	private static void test44(){
		
	}

	private static void test45(){
		
	}

	private static void test46(){
		
	}

	private static void test47(){
		
	}

	private static void test48(){
		
	}

	private static void test49(){
		
	}

	private static void test50(){
		
	}

	private static void test51(){
		
	}

	private static void test52(){
		
	}

	private static void test53(){
		
	}

	private static void test54(){
		
	}

	private static void test55(){
		
	}

	private static void test56(){
		
	}

	private static void test57(){
		
	}

	private static void test58(){
		
	}
}
