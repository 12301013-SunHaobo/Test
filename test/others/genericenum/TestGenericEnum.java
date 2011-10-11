package others.genericenum;

public class TestGenericEnum {

    
    /**
     * @param args
     */
    public static void main(String[] args) {
        testEnum(OibFeature.ASSESSMENT_MANAGEMENT);
        
    }

    
    private static<T extends Enum> void testEnum(Enum e){
        
    }
}
