package modules.at.analyze;

import java.util.ArrayList;
import java.util.List;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicators;
import modules.at.formula.IndicatorsRule;
import modules.at.model.Bar;
import modules.at.model.Position;
import modules.at.model.Tick;
import modules.at.model.Trade;
import modules.at.visual.BarChartBase;

import org.jfree.ui.RefineryUtilities;

import utils.Formatter;

public class TestAuto {

	// change begin
	static String STOCK_CODE = "qqq";
	static String DATE_STR = "20111014";//"20110923";
	static String TIME_STR = "200153";//"223948";
	static String TICK_FILENAME = DATE_STR + "-" + TIME_STR + ".txt";
	// change end

	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		List<Trade> tradeList = auto();
		
		BarChartBase barchartBase = new BarChartBase(STOCK_CODE,DATE_STR, TIME_STR, tradeList);
		barchartBase.pack();
		RefineryUtilities.centerFrameOnScreen(barchartBase);
		barchartBase.setVisible(true);
	}

	static final double CUT_LOSS = - 0.05; //absolute loss, not %
	static final double PROFIT_LOSS = - 0.05; //absolute loss from previous profit  
	
	static double LOCK_PROFIT = Double.NaN;//keeps changing, and LOCK_PROFIT always > CUT_LOSS
	
	private static List<Trade> auto() throws Exception {
		String nazTickOutputDateStr = DATE_STR;// change for new date
		List<Tick> tickList = HistoryLoader.getNazHistTicks(STOCK_CODE, TICK_FILENAME, nazTickOutputDateStr);
		// change end -> for new date
		List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);

		Indicators indicators = new Indicators();
		
		List<Trade> tradeList = new ArrayList<Trade>();
		for (Bar bar : barList) {
			indicators.addValue(bar.getClose());//update indicators
			
			double price = bar.getClose();
			long time = bar.getDate().getTime();
			
			Trade trade = decide(indicators, price, time);
			if(trade != null){
				tradeList.add(trade);
			}
		}
		printTrades(tradeList);
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
	private static Trade decide(Indicators indicators, double price, long time) throws Exception{
		Position position = Position.getInstance();
		int pQty = position.getQty();
		Trade trade = null;
		TradeTimeLot tradeLot = getTradeLot(time);
		switch (tradeLot){
			case PreTrade :
				break;
			case InTrade :
				//lock profit & cut loss checking, including short|long
				if(pQty!=0){
					double tmpPnL = (price - position.getPrice())*pQty;
					
					//lock profit checking
					if(!Double.isNaN(LOCK_PROFIT)){
						if(tmpPnL < LOCK_PROFIT){
							trade = new Trade(price, -1 * pQty, time, Trade.Type.LockProfit);
							position.setPosition(0, price);
							LOCK_PROFIT = Double.NaN;
							break;
						} else {//increase LOCK_PROFIT
							LOCK_PROFIT = Math.max(LOCK_PROFIT, tmpPnL+PROFIT_LOSS);
						}
					} else {
						if(tmpPnL > 0){
							LOCK_PROFIT = Math.max(0, tmpPnL+PROFIT_LOSS);
						}
					}
					//cut loss checking
					if(tmpPnL < CUT_LOSS){
						trade = new Trade(price, -1 * pQty, time, Trade.Type.CutLoss);
						position.setPosition(0, price);
						System.out.println(trade+":"+tmpPnL);
						break;
					}
				}
				
				
				if(Rule.Trend.Down.equals(IndicatorsRule.predict(indicators))){
					if (pQty == 0){//short
						trade = new Trade(price, -1, time, Trade.Type.Short);
						position.setPosition(pQty - 1, price);
					} else if(pQty>0){//sell
						trade = new Trade(price, -1, time, Trade.Type.Sell);
						position.setPosition(pQty - 1, price);
					} else {//pQty<0?
						//keep short position
					}
				} else if(Rule.Trend.Up.equals(IndicatorsRule.predict(indicators))) {
					if (pQty == 0){//long
						trade = new Trade(price, 1, time, Trade.Type.Long);
						position.setPosition(pQty + 1, price);
					} else if(pQty <0){//cover short
						trade = new Trade(price, 1, time, Trade.Type.CoverShort);
						position.setPosition(pQty + 1, price);
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
		return trade;
	}
	
	
	
	
	// time range of trade
	enum TradeTimeLot {
		PreTrade, InTrade, WrapUp, AfterTrade
	}
	private static TradeTimeLot getTradeLot(long time) throws Exception{

		long inTradeBegin = Formatter.DEFAULT_DATETIME_FORMAT.parse(DATE_STR+"-09:45:00").getTime();
		long inTradeEnd = Formatter.DEFAULT_DATETIME_FORMAT.parse(DATE_STR+"-15:45:00").getTime();
		long marketClose = Formatter.DEFAULT_DATETIME_FORMAT.parse(DATE_STR+"-16:00:00").getTime();
		
		if(time<inTradeBegin){
			return TradeTimeLot.PreTrade;
		}else if(time<inTradeEnd){
			return TradeTimeLot.InTrade;
		}else if(time<marketClose){
			return TradeTimeLot.WrapUp;
		}
		return TradeTimeLot.AfterTrade;
	}
	

	
	private static void printTrades(List<Trade> tradeList){
		double pnL = 0;
		for(Trade trade : tradeList){
			System.out.println(trade);
			pnL = pnL + (trade.getPrice() * trade.getQty());
		}
		System.out.println("Total pnL : "+pnL);
	}

}
