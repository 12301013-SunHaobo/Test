package others.reflection;

public class OneObject {
    
    
    
    public OneObject() {
        super();
        printCallerStackTrace();
    }

    public String method1(){
        return "";
    }
    
    
    public static void printCallerStackTrace(){
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        for(int i=2;i<stackTraces.length;i++){
            System.out.println("OneObject.method1():"+i+":"+stackTraces[i].getClassName()+"."+stackTraces[i].getMethodName()+"(...)");
        }
    }
}
