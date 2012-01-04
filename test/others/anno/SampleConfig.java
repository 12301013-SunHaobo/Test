package others.anno;

public class SampleConfig {
    public static String str1 = "str1";
    
    public static String str2 = "str2";
    
    @DoubleRange(start=0.1, end=3, intervals=100)
    public static double d1 = 1.11;
    
    @IntRange(start=1, end=20, intervals=100)
    public static double d2 = 2.22;
    
    @IntRange(start=10, end=20, intervals=10)
    public static int i1 = 11;
    
    @DoubleRange(start=0.1, end=3, intervals=100)
    public static int i2 = 22;

    //trade unit, how many shares in one trade unit
    public static final int TRADE_UNIT = 1;
}
