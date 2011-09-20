package utils;

public class TestWebUtil {

    /**
     * @param args
     */
    public static void main(String[] args) {

        testGetUrl();
    }
    
    private static void testGetUrl(){
        String pageContent = WebUtil.getPageSource("http://localhost:8000/oib/login","utf-8");
        System.out.println(pageContent);
    }

}
