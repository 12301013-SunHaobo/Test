package others.inherit;

public class Child extends Parent {

	{
		System.out.println("dynamic block Child called 001");
	}

	static {
		System.out.println("static block Child called 001");
	}
	

	public static String conStr = "child const str";
	//protected String varStr = "inChild"; 
    @Override
    public void printName(){
        System.out.println("child instance");
        //printName();//infinite loop
        System.out.println(this.varStr);
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        Child c = new Child();
        //c.printName();
        System.out.println(Child.conStr);
    }

}
