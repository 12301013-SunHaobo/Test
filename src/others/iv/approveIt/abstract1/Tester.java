package others.iv.approveIt.abstract1;


public class Tester extends Abstract2 {
	public void method1() {
		super.method1();
		System.out.println("Tester");
	}
	public static void main(String[] args){
		Tester tester = new Tester();
		tester.method1();
	}
}
