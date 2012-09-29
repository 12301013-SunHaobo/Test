package others.iv.proveIt.abstract1;

import java.io.IOException;

public class Tester24 extends TestAbstract24 {

	//A
	@Override
	public void printIt(String it) {
		System.out.println(it);
	}
	
	/*
	//B //cannot reduce visibility
	private void printIt(String it){
		System.out.println(it);
	}

	//C //Exception IOException is not compatible with throws clause in TestAbstract.printIt(String)
	public void printIt(String it) throws IOException {
		System.out.println(it);
	}
	
	//D  //The return type is incompatible with TestAbstract.printIt(String)
	public String printIt(String it) {
		System.out.println(it);
		return it;
	}
	*/
	
	
}
