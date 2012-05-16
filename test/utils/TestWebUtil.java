package utils;

public class TestWebUtil {

    /**
     * @param args
     */
    public static void main(String[] args) {

        testGetUrl();
    }
    
    private static void testGetUrl(){
        String pageContent = WebUtil.getPageSource("http://www.nasdaq.com/symbol/qqq/time-sales?time=1","utf-8");
        System.out.println(pageContent);
    }

}
