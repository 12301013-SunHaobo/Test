package others.iv.approveIt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class TestProveItJava2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//test7();
		//test9();
		//test16();
		//test17();
		//test18();
		//test20();
		test21();
	}
	
	
	private static void test7(){
		ArrayList myList = new ArrayList();
		myList.add("One");
		myList.add("Two");
		myList.add("Three");

		Iterator iter  =myList.iterator();
		
		while(iter.hasNext()){
			System.out.println(iter.next());
		}
	}
	private static void test9(){
		ArrayList myList = new ArrayList();
		myList.add("One");
		myList.add("Two");
		myList.add("Three");

		for(Object elem : myList){
			System.out.println(elem);
		}
	}

	private static void test10(){
		//ArrayList items<String> = new ArrayList();//A
		//ArrayList items = new ArrayList(String);//B
		//ArrayList items = new ArrayList(<String>);//C
		ArrayList<String> items = new ArrayList<String>();//D
	}

	private static void test11(){
		String[] strings1 = new String[10];
		String strings2[] = new String[10];
		String[] strings = {"1","2"};
		
	}
	
	private static void test15(){
		/* doesn't compile
		try {
			throw new IOException("");
		} catch (Exception ex) {
			System.out.println("Caught Exception");
		} catch (IOException ex) {
			System.out.println("Caught IOException");
		}
		*/
	}

	private static void test16(){
		try {
			throw new IOException();
		} catch (IOException ex) {
			System.out.println("Caught IOException");
		} finally {
			System.out.println("Finally");
		}
	}

	private static void test17(){
		int val = 2;
		switch(val){
		case 1: 
			System.out.println("1");
		case 2: 
			System.out.println("2");
		case 3: 
			System.out.println("3");
		default: 
			System.out.println("default");
		}
	}

	private static void test18(){
		for(int i=0;i<3;i++){
			System.out.println(i);
		}
	}

	private static void test19(){
		/* doesn't compile
		int val =2;
		if(val==1){
			System.out.println("1");
		} elif (val == 2) {
			System.out.println("2");
		}
		*/
	}

	private static void test20(){
		int val = 0;
		while(val<10){
			val = val +1;
			if(val==4){
				continue;
			}
			
			if(val>4){
				break;
			}
			
			System.out.println(val);
		}
	}

	private static void test21(){
		int val = 0;
		for(;;){
			System.out.println(val);
			
			val = val +1;
			
			if(val>3) {
				break;
			}
		}
	}
	
	
	private static void test0(){

	}

}
