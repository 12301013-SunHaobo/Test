package utils;

/**
 * 
 * usage: follow below order to track used time
 * 
 * MyWatch.start();
 * ...
 * MyWatch.printAndRestart();
 * MyWatch.printAndRestart();
 * ...
 * MyWatch.totalTime("finished");
 *
 */
public class MyWatch {

    public static long counter = 0;
    private static long startTime = 0;
    private static long subStartTime = 0;

    public static void start() {
        if(startTime==0){
            startTime = System.currentTimeMillis();
            subStartTime = startTime;
        }
    }

    public static void printAndRestart(String msg) {
        long curTime = System.currentTimeMillis();
        System.out.println(getCaller()+"|"+msg+"|ms = " + (curTime - subStartTime) );
        subStartTime = System.currentTimeMillis();
    }

    public static void totalTime(String msg) {
        System.out.println(getCaller()+"|"+msg+"|Total ms = " + (System.currentTimeMillis() - startTime));
    }
    
    private static String getCaller(){
        StackTraceElement s = new Throwable().fillInStackTrace().getStackTrace()[2];
        return s.getClassName().replaceAll(".*?\\.", "")+"."+s.getMethodName()+"()";
    }

}
