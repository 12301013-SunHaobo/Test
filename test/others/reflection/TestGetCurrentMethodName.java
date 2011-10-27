package others.reflection;

public class TestGetCurrentMethodName {

    /**
     * @param args
     */
    public static void main(String[] args) {

        test2();
        
    }

    
    private static void test2(){
        OneObject oneObject = new OneObject();
        oneObject.method1();
    }
    
    
    private static void test1(){
        String curMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(curMethodName);
    }
}
