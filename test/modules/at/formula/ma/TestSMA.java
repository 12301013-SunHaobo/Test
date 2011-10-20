package modules.at.formula.ma;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicators;
import modules.at.model.Bar;
import utils.Formatter;
import utils.TimeUtil;

public class TestSMA {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        long b0 = System.currentTimeMillis();
    	
        //testDailyMA();
        testDailyMAByMathLib();
        
        long e0 = System.currentTimeMillis();
        System.out.println("Total time : "+(e0-b0));
        
    }
    
    private static void testDailyMA() throws Exception{
    	List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");
        MASelfImpl ma = new MASelfImpl(13);
        for(Bar bar : barList){
        	ma.addPrice(bar.getClose());
        	System.out.println(bar+" "+Formatter.DECIMAL_FORMAT.format(ma.getValue()));
        	
        }
    	
    	
    }
    
    private static void testDailyMAByMathLib() throws Exception{
    	List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");
    	int length = 14;
    	Indicators indicator = new Indicators();
        for(Bar bar : barList){
        	indicator.addBar(bar);
        	System.out.println(bar+" SMA("+length+")="+Formatter.DECIMAL_FORMAT.format(indicator.getSMAFast()));
        	
        }
    	
    	
    }
    
    

    
    
    

    
    private static void testMA(List<Bar> barList){
        MASelfImpl ma = new MASelfImpl(13);
        for(Bar bar : barList){
        	ma.addPrice(bar.getClose());
        	//System.out.println(bar+","+Formatter.DECIMAL_FORMAT.format(ma.getAvg()));
        	System.out.println(bar.getId()+","+Formatter.DECIMAL_FORMAT.format(ma.getValue()));
        	
        }
    	
    }

}
