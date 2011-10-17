package modules.at.formula.rsi;

import java.util.List;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import utils.Formatter;

public class TestRsi {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

	    long b0 = System.currentTimeMillis();
	    List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");

        long b1 = System.currentTimeMillis();
        Rsi rsi = new RsiSelfImpl(14);
        for(Bar bar : barList){
            rsi.addPrice(bar.getClose());
            System.out.println(bar+" "+Formatter.DECIMAL_FORMAT.format(rsi.getValue()));
        }
        
        long e0 = System.currentTimeMillis();
        
        System.out.println("Total used time: "+(e0-b1));
        
        
        
	}

	
}
