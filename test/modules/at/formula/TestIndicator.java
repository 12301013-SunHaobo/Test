package modules.at.formula;

import java.util.List;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import utils.Formatter;
import utils.TimeUtil;

public class TestIndicator {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		test1();
	}
	
	private static void test1() throws Exception{
    	List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");
    	int length = 14;
        Indicator indicator = new Indicator(length);
        for(Bar bar : barList){
        	indicator.addValue(bar.getClose());
        	//System.out.println(bar+" "+Formatter.DECIMAL_FORMAT.format(indicator.getVolatilityStdDev()));
        	System.out.println(
        			Formatter.DISPLAY_DEFAULT_DATE_FORMAT.format(bar.getDate())+","+bar.getClose()+","+
        			"SMA("+length+")="+Formatter.DECIMAL_FORMAT.format(indicator.getSMAFast())+","+
        			"StdDev("+length+")="+Formatter.DECIMAL_FORMAT.format(indicator.getStdDev())+","+
        			"BB("+length+")=("+Formatter.DECIMAL_FORMAT.format(indicator.getSMAFast())+","+
        					Formatter.DECIMAL_FORMAT.format(indicator.getBBLower())+","+
        					Formatter.DECIMAL_FORMAT.format(indicator.getBBUpper())+")"
        			
        		);
        	
        }

	}

}
