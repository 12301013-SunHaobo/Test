package others.iv.proveIt;


public class Tester2 {
	String myString = "Tester";
	
	public void printIt(){
		String myString = "printIt";
		System.out.println(myString);
	}
	
	public static void main(String[] args){
		Tester2 tester = new Tester2();
		tester.printIt();
	}
}