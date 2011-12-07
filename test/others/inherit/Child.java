package others.inherit;

public class Child extends Parent {

    @Override
    public void printName(){
        System.out.println("child instance");
        printName();//infinite loop
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        Child c = new Child();
        c.printName();
    }

}
