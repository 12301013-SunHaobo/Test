package modules.at.analyze;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicator;
import modules.at.model.Bar;
import modules.at.model.Tick;
import utils.Formatter;

public class TestAuto {

	// change begin
	static String STOCK_CODE = "qqq";
	static String DATE_STR = "20110923";
	static String TICK_FILENAME = DATE_STR + "-" + "223948" + ".txt";
	// change end

	static int length = 14;
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		auto();
	}

	static Position position = new Position(); //qty
	static double CUT_LOSS = - 0.05; //absolute loss, not %
	static List<Trade> tradeList = new ArrayList<Trade>();
	
	private static void auto() throws Exception {
		String nazTickOutputDateStr = DATE_STR;// change for new date
		List<Tick> tickList = HistoryLoader.getNazHistTicks(STOCK_CODE, TICK_FILENAME, nazTickOutputDateStr);
		// change end -> for new date
		List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);

		Indicator indicator = new Indicator(14);
		
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
						trade = new Trade(price, -1 * pQty, time);
						position.setQty(0);
						break;
					}
				}
				
				
				if(rsi>70){
					if (pQty == 0){//short
						trade = new Trade(price, -1, time);
						position.setQty(pQty - 1);
					} else if(pQty>0){//sell
						trade = new Trade(price, -1, time);
						position.setQty(pQty - 1);
					} else {//pQty<0?
						//keep short position
					}
				} else if(rsi<30) {
					if (pQty == 0){//long
						trade = new Trade(price, 1, time);
						position.setQty(pQty + 1);
					} else if(pQty <0){//cover short
						trade = new Trade(price, 1, time);
						position.setQty(pQty + 1);
					} else{//pQty>0?
						//keep long position
					}
				}
				break;
			case WrapUp :
				if(pQty != 0){
					trade = new Trade(price, -1 * pQty, time);
					position.setQty(0);
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
	
	static class Position {
		int qty;
		double price;

		public Position() {
			super();
			this.qty = 0;
			this.price = 0;
		}
		public Position(int qty, double price) {
			super();
			this.qty = qty;
			this.price = price;
		}
		public int getQty() {
			return qty;
		}
		public void setQty(int qty) {
			this.qty = qty;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
	}
	
	static class Trade {
		private static int idSeq = 0; //sequence number to count how many trades are created
		
		int id;
		double price;
		int qty;
		long dateTime;

		public Trade(double price, int qty, long dateTime) {
			super();
			this.id = ++idSeq;
			this.price = price;
			this.qty = qty;
			this.dateTime = dateTime;
			
		}
		
		@Override
		public String toString() {
			return "Trade [id="+id+", price=" + price + ", qty=" + qty + ", dateTime=" + Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(dateTime)) + "]";
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public int getQty() {
			return qty;
		}
		public void setQty(int qty) {
			this.qty = qty;
		}
		public long getDateTime() {
			return dateTime;
		}
		public void setDateTime(long dateTime) {
			this.dateTime = dateTime;
		}
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
