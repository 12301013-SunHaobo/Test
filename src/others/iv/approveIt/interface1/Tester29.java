package others.iv.approveIt.interface1;

public class Tester29 implements Interface1, Interface2{

	public void method1(){
		System.out.println("method1");
	}
	public void method2(){
		System.out.println("method2");
	}
	
	
	public static void main(String[] args) {
		Tester29 tester = new Tester29();
		
		tester.method1();
		tester.method2();
	}

}
