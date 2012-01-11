package modules.at.formula;

import java.util.List;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import modules.at.stg.Setting;
import modules.at.stg.other.IndicatorOther;
import utils.Formatter;

public class TestIndicator {
	private Setting as = new Setting();
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		TestIndicator ti = new TestIndicator();
		ti.testBB();
	}
	
	private void testBB() throws Exception{
    	List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");
        IndicatorOther indicator = new IndicatorOther(this.as);
        for(Bar bar : barList){
        	indicator.addBar(bar);
        	//System.out.println(bar+" "+Formatter.DECIMAL_FORMAT.format(indicator.getVolatilityStdDev()));
        	System.out.println(
        			Formatter.DISPLAY_DEFAULT_DATE_FORMAT.format(bar.getDate())+","+bar.getClose()+","+
        			"SMA("+this.as.getMaFastLength()+")="+Formatter.DECIMAL_FORMAT.format(indicator.getSMAFast())+","+
        			"BB("+this.as.getBbLength()+")=("+Formatter.DECIMAL_FORMAT.format(indicator.getSMAFast())+","+
        					Formatter.DECIMAL_FORMAT.format(indicator.getBBLower())+","+
        					Formatter.DECIMAL_FORMAT.format(indicator.getBBUpper())+")"
        			
        		);
        	
        }

	}

}
