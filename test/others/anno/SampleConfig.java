package others.anno;

import modules.at.model.IntRange;

public class SampleConfig {

    public double d1 = 1.11;
    
    @IntRange(start=10, end=20, intervals=3)
    public int i1 = 2;

    //trade unit, how many shares in one trade unit
    public static final int TRADE_UNIT = 1;

    @Override
    public String toString() {
        return "SampleConfig [d1=" + d1 + ", i1=" + i1 + ", TRADE_UNIT=" + TRADE_UNIT + "]";
    }




    
    
    
}
