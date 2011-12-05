package others;

public class TestString {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        testRemovePackageStr();

    }

    
    private static void testRemovePackageStr(){
        String fullClassName = "net.wgen.threetwelve.outcomes.ui.web.service.internal.RPTServiceController2Test";
        System.out.println(fullClassName.replaceAll(".*?\\.", ""));
    }
    
    private static void test1(){
        //String featureSettingStr = "setLearningMapEnabled";
        String testStr = "setEnabledabcEnabled";
        String resultStr = testStr.replaceAll("^set|Enabled$", "");
        System.out.println(resultStr);
    }
}
