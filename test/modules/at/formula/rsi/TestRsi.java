package modules.at.formula.rsi;

import java.util.List;

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
        List<Tick> tickList = HistoryLoader.getHistTciks();
        List<Bar> barList = TickToBarConverter.convert(tickList);

        long b1 = System.currentTimeMillis();
        RRecal rsi = new RRecal(13);
        for(Bar bar : barList){
            rsi.addPrice(bar.getClose());
            System.out.println(bar.getId()+":"+rsi.calculate());
        }
        
        long e0 = System.currentTimeMillis();
        
        System.out.println("Total used time: "+(e0-b1));
        
        
        
	}

	
}
