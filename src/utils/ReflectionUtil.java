package utils;

public class ReflectionUtil {

    
    public static void printCallerStackTrace(){
        printCallerStackTrace(true);
    }

    public static void printCallerStackTrace(boolean onlyWgen){
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        for(int i=2;i<stackTraces.length;i++){
            String className = stackTraces[i].getClassName();
            if(!onlyWgen || className.contains("wgen")){
                System.out.println("OneObject.method1():"+i+":"+className+"."+stackTraces[i].getMethodName()+"(...)");
            }
        }
    }

}
