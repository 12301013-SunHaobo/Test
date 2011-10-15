package modules.at.analyze;

import java.util.ArrayList;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicator;
import modules.at.model.Bar;
import modules.at.model.Position;
import modules.at.model.Tick;
import modules.at.model.Trade;
import modules.at.visual.BarChartBase;
import utils.Formatter;

public class TestAuto {

	// change begin
	static String STOCK_CODE = "qqq";
	static String DATE_STR = "20110923";
	static String TIME_STR = "223948";
	static String TICK_FILENAME = DATE_STR + "-" + "223948" + ".txt";
	// change end

	static int length = 14;
	public static double rsiUpper = 70;
	public static double rsiLower = 30;
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

	static Position position = new Position(); //qty
	static double CUT_LOSS = - 0.05; //absolute loss, not %
	
	private static List<Trade> auto() throws Exception {
		String nazTickOutputDateStr = DATE_STR;// change for new date
		List<Tick> tickList = HistoryLoader.getNazHistTicks(STOCK_CODE, TICK_FILENAME, nazTickOutputDateStr);
		// change end -> for new date
		List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);

		Indicator indicator = new Indicator(14);
		
		List<Trade> tradeList = new ArrayList<Trade>();
		for (Bar bar : barList) {
			indicator.addValue(bar.getClose());
			double rsi = indicator.getRsi();
			double price = bar.getClose();
			long time = bar.getDate().getTime();
			
			Trade trade = decide(rsi, price, time, position);
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
	private static Trade decide(double rsi, double price, long time, Position position) throws Exception{
		int pQty = position.getQty();
		Trade trade = null;
		TradeTimeLot tradeLot = getTradeLot(time);
		switch (tradeLot){
			case PreTrade :
				break;
			case InTrade :
				//cut loss checking, including short|long
				if(pQty!=0){
					//double profitPer = (price-position.getPrice())*(pQty>0?1:-1)/position.getPrice();
					double tmpPnL = (price - position.getPrice())*pQty;
					if(tmpPnL < CUT_LOSS){
						trade = new Trade(price, -1 * pQty, time, Trade.Type.CutLoss);
						position.setPosition(0, price);
						System.out.println(trade+":"+tmpPnL);
						break;
					}
				}
				
				
				if(rsi>rsiUpper){
					if (pQty == 0){//short
						trade = new Trade(price, -1, time, Trade.Type.Short);
						position.setPosition(pQty - 1, price);
					} else if(pQty>0){//sell
						trade = new Trade(price, -1, time, Trade.Type.Sell);
						position.setPosition(pQty - 1, price);
					} else {//pQty<0?
						//keep short position
					}
				} else if(rsi<rsiLower) {
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
