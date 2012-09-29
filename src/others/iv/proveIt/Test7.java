package others.iv.proveIt;

import java.util.ArrayList;
import java.util.Iterator;

public class Test7 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ArrayList myList = new ArrayList();
		myList.add("One");
		myList.add("Two");
		myList.add("Three");

		Iterator iter  =myList.iterator();
		
		while(iter.hasNext()){
			System.out.println(iter.next());
		}
		
	}

}
