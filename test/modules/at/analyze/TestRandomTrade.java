package modules.at.analyze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modules.at.TradeUtil;
import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import modules.at.model.Tick;
import modules.at.model.Trade;
import utils.MathUtil;

public class TestRandomTrade {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		testRandom();

	}

	//testing
	/**
	 * loop through all dates, test 10 tiems / day, 
	 * avg pnl = e.g: 0.036592727, 0.011640455, -0.025676818, 0.0146508, 0.019521932
	 * 
	 */
	private static void testRandom() throws Exception {
	    
        String stockCode = "qqq";// qqq, tna, tza
        List<String[]> dateTimeArrList = TradeUtil.getInputParams(stockCode);

        for(int n4all=0; n4all<50; n4all++){
            double tmpPnL4AllDays = 0;
            int testTimes4OneDay = 20;
            for (String[] dateTimeArr : dateTimeArrList) {
                double totalPnL = 0;
                String dateStr = dateTimeArr[0];
                String timeStr = dateTimeArr[1];
                String tickFileName = dateStr + "-" + timeStr + ".txt";
    
                // loop testTimes for one date
                for (int j = 0; j < testTimes4OneDay; j++) {
                    List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, dateStr);
                    List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);
    
                    List<Integer> selectedIdxs = MathUtil.getUniqueRandomIntSet(0, barList.size()-1, new Random(), 102);
                    List<Trade> tradeList = new ArrayList<Trade>();
                    for (int i = 0; i < selectedIdxs.size(); i++) {
                        Bar bar = barList.get(selectedIdxs.get(i));
                        if (i % 2 == 0) {
                            tradeList.add(new Trade(bar.getClose(), 1, bar.getDate().getTime(), Trade.Type.LongEntry));
                        } else {
                            tradeList.add(new Trade(bar.getClose(), -1, bar.getDate().getTime(), Trade.Type.LongExit));
                        }
                    }
                    double pnL = TradeUtil.printTrades(tradeList, false);
                    totalPnL = totalPnL + pnL;
                }
                double avgPnL4OneDay = (totalPnL / testTimes4OneDay);
                //System.out.println(tickFileName + " X " + testTimes4OneDay + ", avg pnl=" + avgPnL4OneDay);
                tmpPnL4AllDays+=avgPnL4OneDay;
            }
            System.out.println("--------------- > 4AllDays avg pnl="+(tmpPnL4AllDays/dateTimeArrList.size()));
        }       
	}
}
