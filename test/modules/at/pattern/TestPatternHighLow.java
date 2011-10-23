package modules.at.pattern;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicators;
import modules.at.model.Bar;
import modules.at.model.Point;
import modules.at.model.Tick;
import modules.at.model.Trade;
import modules.at.visual.BarChartBase;

import org.jfree.ui.RefineryUtilities;

import utils.Formatter;
import utils.GlobalSetting;

public class TestPatternHighLow {

	static double LOCK_PROFIT = Double.NaN;//keeps changing, and LOCK_PROFIT always > CUT_LOSS
	
	private static PatternHighLow patternHighLow = new PatternHighLow(3);
	
	public static void main(String[] args) throws Exception {
		testOneDay();
	}

	private static void testOneDay() throws Exception{
		String stockCode = "qqq";//qqq, tna, tza 
		String[] dateTimeArr = new String[] {"20111021", "200115"};
		
		List<Trade> tradeList = auto(stockCode, dateTimeArr[0], dateTimeArr[1]);
		System.out.println(stockCode + ":" + dateTimeArr[0] + "-" + dateTimeArr[1]);
		printTrades(tradeList, true);
		
		if(GlobalSetting.isAtHome()){
			BarChartBase barchartBase = new BarChartBase(stockCode, dateTimeArr[0], dateTimeArr[1], tradeList);
			barchartBase.pack();
			RefineryUtilities.centerFrameOnScreen(barchartBase);
			barchartBase.setVisible(true);
		}
	}
	
	private static List<Trade> auto(String stockCode, String dateStr, String timeStr) throws Exception {
		String tickFileName = dateStr + "-" + timeStr + ".txt";
		List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, dateStr);
		List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);

		Indicators indicators = new Indicators();
		indicators.addObserver(patternHighLow);
		
		List<Trade> tradeList = new ArrayList<Trade>();
		for (Bar bar : barList) {
			indicators.addBar(bar);//update indicators 
		}
		//generate all high/low trades
		LinkedList<Point> highList = patternHighLow.getHighList();
		LinkedList<Point> lowList = patternHighLow.getLowList();
		for(Point point : highList){
			tradeList.add(new Trade(point.getPrice(), -1, point.getDateTime().getTime(), Trade.Type.Short));
		}
		for(Point point : lowList){
			tradeList.add(new Trade(point.getPrice(), 1, point.getDateTime().getTime(), Trade.Type.Long));
		}
		
		return tradeList;
	}
	
	


	
	private static double printTrades(List<Trade> tradeList, boolean printDetail){
		double pnL = 0;
		for(Trade trade : tradeList){
		    if(printDetail){
		        System.out.println(trade);
		    }
			pnL = pnL + (trade.getPrice() * trade.getQty()*(-1));
		}
		System.out.println("Total pnL : "+Formatter.DECIMAL_FORMAT.format(pnL));
		return pnL;
	}


}
