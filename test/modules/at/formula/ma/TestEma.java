package modules.at.formula.ma;

import java.util.List;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import utils.Formatter;

public class TestEma {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

        long b0 = System.currentTimeMillis();
    	
        testDailyEMA();
        //testStaticData();
        
        long e0 = System.currentTimeMillis();
        System.out.println("Total time : "+(e0-b0));
	}


	
    private static void testDailyEMA() throws Exception{
    	List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");
        EMA ema = new EMA(13);
        for(Bar bar : barList){
        	ema.addPrice(bar.getClose());
        	System.out.println(bar+" "+Formatter.DECIMAL_FORMAT.format(ema.getValue()));
        	
        }
    }
    
	private static void testStaticData()throws Exception{
		EMA ema = new EMA(10);
		double[] dArr = new double[]{
				64.75,
				63.79,
				63.73,
				63.73,
				63.55,
				63.19,
				63.91,
				63.85,
				62.95,
				63.37,
				61.33,
				61.51,
				61.87,
				60.25,
				59.35,
				59.95,
				58.93,
				57.68,
				58.82,
				58.87
		};
		
		for(double d: dArr){
			ema.addPrice(d);
			System.out.println(d+" "+ema.getValue());
		}
		
	}    
}
