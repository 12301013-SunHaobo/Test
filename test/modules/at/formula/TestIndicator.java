package modules.at.formula;

import java.util.List;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import utils.Formatter;

public class TestIndicator {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		testBB();
	}
	
	private static void testBB() throws Exception{
    	List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");
        Indicators indicator = new Indicators();
        for(Bar bar : barList){
        	indicator.addBar(bar);
        	//System.out.println(bar+" "+Formatter.DECIMAL_FORMAT.format(indicator.getVolatilityStdDev()));
        	System.out.println(
        			Formatter.DISPLAY_DEFAULT_DATE_FORMAT.format(bar.getDate())+","+bar.getClose()+","+
        			"SMA("+AlgoSetting.MA_FAST_LENGTH+")="+Formatter.DECIMAL_FORMAT.format(indicator.getSMAFast())+","+
        			"BB("+AlgoSetting.BB_LENGTH+")=("+Formatter.DECIMAL_FORMAT.format(indicator.getSMAFast())+","+
        					Formatter.DECIMAL_FORMAT.format(indicator.getBBLower())+","+
        					Formatter.DECIMAL_FORMAT.format(indicator.getBBUpper())+")"
        			
        		);
        	
        }

	}

}
