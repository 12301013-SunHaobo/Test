package others;

public class TestString {

    /**
     * @param args
     */
    public static void main(String[] args) {
        testSplitLine();
        //testSplit();
        //testRemovePackageStr();

    }
    
    private static void testSplitLine(){
        String s = "WASHINGTON (CNN) -- A state law mandating \"humane treatment\" of downed livestock headed for the slaughterhouse was unanimously overturned Monday by the Supreme Court.";
        String[] strArr = s.split("\\.|\\s");
        System.out.println(strArr.length);
        
    }

    private static void testSplit(){
        String s = "abc, def , ehi j , ";
        String[] split = s.split(",");
        
        System.out.println(s);
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
