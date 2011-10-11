package others;

public class TestString {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        //String featureSettingStr = "setLearningMapEnabled";
        String testStr = "setEnabledabcEnabled";
        
        String resultStr = testStr.replaceAll("^set|Enabled$", "");
        
        System.out.println(resultStr);
        

    }

}
