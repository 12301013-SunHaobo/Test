package modules.at.formula.rsi;

import java.util.List;

import utils.Formatter;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import modules.at.model.Tick;

public class TestRsi {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

	    long b0 = System.currentTimeMillis();
	    List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");

        long b1 = System.currentTimeMillis();
        RsiSelfImpl rsi = new RsiSelfImpl(14);
        for(Bar bar : barList){
            rsi.addPrice(bar.getClose());
            System.out.println(bar+" "+Formatter.DECIMAL_FORMAT.format(rsi.calculate()));
        }
        
        long e0 = System.currentTimeMillis();
        
        System.out.println("Total used time: "+(e0-b1));
        
        
        
	}

	
}
