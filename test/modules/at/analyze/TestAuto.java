package modules.at.analyze;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicators;
import modules.at.formula.IndicatorsRule;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.Position;
import modules.at.model.Tick;
import modules.at.model.Trade;
import modules.at.pattern.Pattern;
import modules.at.pattern.PatternSto;
import modules.at.visual.BarChartBase;

import org.jfree.ui.RefineryUtilities;

import utils.FileUtil;
import utils.Formatter;
import utils.GlobalSetting;

public class TestAuto {

	static double LOCK_PROFIT = Double.NaN;//keeps changing, and LOCK_PROFIT always > CUT_LOSS
	
	public static void main(String[] args) throws Exception {
		testOneDay();
		//testAllDays();
	}

	private static void testOneDay() throws Exception{
		String stockCode = "qqq";//qqq, tna, tza 
		String[] dateTimeArr = new String[] {"20111018", "200248"};
		
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
	
	private static void testAllDays() throws Exception{
		String stockCode = "tza";//qqq, tna, tza 
		List<String[]> dateTimeArrList = getInputParams(stockCode);
		
		for(String[] dateTimeArr : dateTimeArrList){
			List<Trade> tradeList = auto(stockCode, dateTimeArr[0], dateTimeArr[1]);
			System.out.print(stockCode + ":" + dateTimeArr[0] + "-" + dateTimeArr[1]);
			printTrades(tradeList, true);
			//break;
		}
	}
	
	private static List<Pattern> getPatternList(){
		List<Pattern> patternList = new ArrayList<Pattern>();
		//patternList.add(new PatternMA());
		//patternList.add(new PatternRsi());
		patternList.add(new PatternSto());
		return patternList;
		
	}
	
	private static List<Trade> auto(String stockCode, String dateStr, String timeStr) throws Exception {
		String tickFileName = dateStr + "-" + timeStr + ".txt";
		List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, dateStr);
		List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);

		Indicators indicators = new Indicators();
		List<Pattern> patternList = getPatternList();
		for(Pattern pattern : patternList){
			indicators.addObserver(pattern);
		}
		IndicatorsRule indicatorsRule = new IndicatorsRule();
		indicatorsRule.setPatternList(patternList);
		
		
		List<Trade> tradeList = new ArrayList<Trade>();
		for (Bar bar : barList) {
			indicators.addBar(bar);//update indicators 
			System.out.println("time="+Formatter.DEFAULT_DATETIME_FORMAT.format(bar.getDate())+", price="+Formatter.DECIMAL_FORMAT.format(bar.getClose()));
			double price = bar.getClose();
			long time = bar.getDate().getTime();
			
			Trade trade = decide(indicatorsRule, price, time, dateStr);
			if(trade != null){
				tradeList.add(trade);
			}
			System.out.println("-------------------------------------------");
		}
		return tradeList;
	}


	
	/**
	 * position.qty==0: long, short, 
	 * position.qty!=0: take profit, cut loss
	 * 
	 * @param rsi
	 * @param price
	 * @param time
	 * @param position
	 * @return
	 * @throws Exception
	 */
	private static Trade decide(IndicatorsRule indicatorsRule, double price, long time, String dateStr) throws Exception{
		String tmpTimeStr = Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(time));
		if("20111018-10:03:59".equals(tmpTimeStr)){
			System.out.println();
		}
		
		Position position = Position.getInstance();
		int pQty = position.getQty();
		double tmpPnL = (price - position.getPrice())*pQty;

		Trade trade = null;
		TradeTimeLot tradeLot = getTradeLot(time, dateStr);
		switch (tradeLot){
			case PreTrade :
				break;
			case InTrade :
				//lock profit & cut loss checking, including short|long
				if(pQty!=0){
					
					//lock profit checking
					if(!Double.isNaN(LOCK_PROFIT)){
						if(tmpPnL < LOCK_PROFIT){
							trade = new Trade(price, -1 * pQty, time, Trade.Type.LockProfit);
							position.setPosition(0, price);
							LOCK_PROFIT = Double.NaN;
							break;
						} else {//increase LOCK_PROFIT
							LOCK_PROFIT = Math.max(LOCK_PROFIT, tmpPnL+AlgoSetting.PROFIT_LOSS);
						}
					} else {
						if(tmpPnL > 0){
							LOCK_PROFIT = Math.max(0, tmpPnL+AlgoSetting.PROFIT_LOSS);
						}
					}
					//cut loss checking
					if(tmpPnL < AlgoSetting.CUT_LOSS){
						trade = new Trade(price, -1 * pQty, time, Trade.Type.CutLoss);
						position.setPosition(0, price);
						break;
					}
				}
				
				Pattern.Trend trend = indicatorsRule.predictTrend();
				if(Pattern.Trend.Down.equals(trend)){
					if (pQty >= 0){//short
						trade = new Trade(price, (-1*pQty)-1, time, Trade.Type.Short);
						position.setPosition(pQty+(-1*pQty)-1, price);
					} else {//pQty<0?
						//keep short position
					}
				} else if(Pattern.Trend.Up.equals(trend)) {
					if (pQty <= 0){//long
						trade = new Trade(price, (-1*pQty)+1, time, Trade.Type.Long);
						position.setPosition(pQty + (-1*pQty)+1, price);
					} else{//pQty>0?
						//keep long position
					}
				}
				break;
			case WrapUp :
				if(pQty != 0){
					trade = new Trade(price, -1 * pQty, time, Trade.Type.WrapUp);
					position.setPosition(0, price);
				}
				break;
			case AfterTrade:
				break;
			default: 
				break;
		}
		if(trade!=null){
			trade.setPnl(tmpPnL);
		}
		return trade;
	}
	
	
	
	
	// time range of trade
	enum TradeTimeLot {
		PreTrade, InTrade, WrapUp, AfterTrade
	}
	private static TradeTimeLot getTradeLot(long time, String dateStr) throws Exception{

		long inTradeBegin = Formatter.DEFAULT_DATETIME_FORMAT.parse(dateStr+"-09:45:00").getTime();
		long inTradeEnd = Formatter.DEFAULT_DATETIME_FORMAT.parse(dateStr+"-15:45:00").getTime();
		long marketClose = Formatter.DEFAULT_DATETIME_FORMAT.parse(dateStr+"-16:00:00").getTime();
		
		if(time<inTradeBegin){
			return TradeTimeLot.PreTrade;
		}else if(time<inTradeEnd){
			return TradeTimeLot.InTrade;
		}else if(time<marketClose){
			return TradeTimeLot.WrapUp;
		}
		return TradeTimeLot.AfterTrade;
	}
	

	
	private static void printTrades(List<Trade> tradeList, boolean printDetail){
		double pnL = 0;
		for(Trade trade : tradeList){
		    if(printDetail){
		        System.out.println(trade);
		    }
			pnL = pnL + (trade.getPrice() * trade.getQty()*(-1));
		}
		System.out.println("Total pnL : "+Formatter.DECIMAL_FORMAT.format(pnL));
	}
	
	//return List of array like: {20111014,200153}
	private static List<String[]> getInputParams(String stockCode){
		List<String[]> inputParams = new ArrayList<String[]>();
		String dir = GlobalSetting.TEST_HOME+"/data/naz/tick/output/"+stockCode;
		List<String> fileNames = FileUtil.getAllFileNames(dir);
		for(String fileName : fileNames) {
			String[] dateTimeArr = fileName.split("-|\\.|txt");
			inputParams.add(dateTimeArr);
		}
		return inputParams;
	}

}
