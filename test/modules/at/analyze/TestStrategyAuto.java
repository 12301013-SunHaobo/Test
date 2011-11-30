package modules.at.analyze;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.at.TradeUtil;
import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.AlgoSetting.TradeDirection;
import modules.at.model.Bar;
import modules.at.model.Position;
import modules.at.model.Tick;
import modules.at.model.Trade;
import modules.at.model.visual.VChart;
import modules.at.model.visual.VMarker;
import modules.at.model.visual.VPlot;
import modules.at.stg.MAStrategy;
import modules.at.stg.Strategy;
import modules.at.stg.Strategy.Decision;
import modules.at.visual.BarChartUtil;
import modules.at.visual.ChartBase;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.ui.Layer;

import utils.Formatter;

public class TestStrategyAuto {
	
	static String[][] dateTimeArr = initDateTimeArr();

	static double LOCK_PROFIT = Double.NaN;//keeps changing, and LOCK_PROFIT always > CUT_LOSS
	
	static Strategy strategy = new MAStrategy();
	//static Strategy strategy = new MACrossStrategy();
	
	public static void main(String[] args) throws Exception {
		long b0 = System.currentTimeMillis();
		testOneDay();
		//testAllDays();
		long e0 = System.currentTimeMillis();
		System.out.println("total time used: "+ (b0-e0));
	}

	private static void testOneDay() throws Exception{
		String stockCode = "qqq";//qqq, tna, tza 
		
		//get barList
		for(int i=0;i<dateTimeArr.length;i++){
			String tickFileName = dateTimeArr[i][0] + "-" + dateTimeArr[i][1] + ".txt";
			List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, dateTimeArr[i][0]);
			List<Bar> barList = TickToBarConverter.convert(tickList, AlgoSetting.BAR_TIME_PERIOD);
			//get tradeList
			List<Trade> tradeList = auto(dateTimeArr[i][0], barList);
			System.out.print(stockCode + ":" + dateTimeArr[i][0] + "-" + dateTimeArr[i][1]+", ");
			TradeUtil.printTrades(tradeList, true);
			
			//display chart with trade and mark info
			VChart vchart = createMarkedChart(barList, tradeList, strategy.getDecisionMarkerList());
			vchart.setTitle(tickFileName);
			
			boolean saveToFile = false;//save to file | display
		    ChartBase cb = new ChartBase(vchart, !saveToFile && dateTimeArr.length==1);
		    if(saveToFile){
		    	cb.saveToFile("D:/user/stock/us/screen-snapshot/MAStrategy/"+i+"_"+dateTimeArr[i][0]+".png");
		    }	
		}
	    
	}
	

	
	private static List<Trade> auto(String dateStr, List<Bar> barList) throws Exception {

		Indicators indicators = new Indicators();
		
		List<Trade> tradeList = new ArrayList<Trade>();
		for (Bar bar : barList) {
			indicators.addBar(bar);//update indicators 
			//System.out.println("TestAuto:time="+Formatter.DEFAULT_DATETIME_FORMAT.format(bar.getDate())+", price="+Formatter.DECIMAL_FORMAT.format(bar.getClose()));
			
			Trade trade = decide(indicators, dateStr);
			if(trade != null){
				tradeList.add(trade);
			}
			//System.out.println("-------------------------------------------");
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
	private static Trade decide(Indicators indicators, String dateStr) throws Exception{
		strategy.update(indicators);//update strategy
		Bar bar = indicators.getCurBar();
		double price = bar.getClose();
		long time = bar.getDate().getTime();
		String tmpTimeStr = Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(time));
		if("20111028-10:41:46".equals(tmpTimeStr)){
			//System.out.println();
		}
		
		Position position = Position.getInstance();
		int pQty = position.getQty();
		double stopPrice = position.getStopLossPrice();
//		System.out.println(Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(time))+
//				", stopPrice="+Formatter.DECIMAL_FORMAT4.format(stopPrice));

		Trade trade = null;
		TradeTimeLot tradeLot = getTradeLot(time, dateStr);
		switch (tradeLot){
			case PreTrade :
				break;
			case InTrade :
				//lock profit & cut loss checking, including short|long
				if(pQty>0){//cut loss for long
					if(price<stopPrice){
						trade = new Trade(price, -1 * pQty, time, Trade.Type.CutLoss);
						position.setPosition(0, price);
						break;
					}
				}else if(pQty<0){//cut loss for short
					if(price>stopPrice){
						trade = new Trade(price, -1 * pQty, time, Trade.Type.CutLoss);
						position.setPosition(0, price);
						break;
					}
				}

				Strategy.Decision decision = strategy.getDecision();
				trade = processDecision(position, decision, bar);
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
			trade.setPnl(0);//TODO: is this necessary?
		}
		position.updateStopPrice(price);
		return trade;
	}
	
	/**
	 * return null means don't do anything
	 * 
	 */
	private static Trade processDecision(Position position, Strategy.Decision decision, Bar bar){
		Trade trade = null;
		
		int pQty = position.getQty();
	
		if(AlgoSetting.TRADE_DIRECTION.equals(TradeDirection.LongOnly)){
			if(pQty==0 && Decision.LongEntry.equals(decision)){
				trade = new Trade(bar.getClose(), AlgoSetting.TRADE_UNIT, bar.getDate().getTime(), Trade.Type.LongEntry);
				position.setPosition(pQty+1*AlgoSetting.TRADE_UNIT, bar.getClose());
			} else if(pQty>0 && Decision.LongExit.equals(decision)){
				trade = new Trade(bar.getClose(), -1*pQty*AlgoSetting.TRADE_UNIT, bar.getDate().getTime(), Trade.Type.LongExit);
				position.setPosition(0, bar.getClose());
			}
		}else if(AlgoSetting.TRADE_DIRECTION.equals(TradeDirection.ShortOnly)){
			if(pQty==0 && Decision.ShortEntry.equals(decision)){
				trade = new Trade(bar.getClose(), -1*AlgoSetting.TRADE_UNIT, bar.getDate().getTime(), Trade.Type.ShortEntry);
			} else if(pQty<0 && Decision.ShortExit.equals(decision)){
				trade = new Trade(bar.getClose(), -1*pQty*AlgoSetting.TRADE_UNIT, bar.getDate().getTime(), Trade.Type.ShortExit);
			}
		}else if(AlgoSetting.TRADE_DIRECTION.equals(TradeDirection.Both)){
			
		}
		
		/*
			if (pQty >= 0){//short
				if(Decision.LongEntry.equals(trend)){
				trade = new Trade(price, (-1*pQty)-AlgoSetting.TRADE_UNIT, time, Trade.Type.Short);
				position.setPosition(pQty+(-1*pQty)-AlgoSetting.TRADE_UNIT, price);
				
			} else {//pQty<0?
				//keep short position
			}
		} else if(Pattern.Trend.Up.equals(trend)) {
			if (pQty <= 0){//long
				trade = new Trade(price, (-1*pQty)+AlgoSetting.TRADE_UNIT, time, Trade.Type.Long);
				position.setPosition(pQty + (-1*pQty)+AlgoSetting.TRADE_UNIT, price);
			} else{//pQty>0?
				//keep long position
			}
		}*/
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
	
	private static void testAllDays() throws Exception{
		String stockCode = "qqq";//qqq, tna, tza 
		List<String[]> dateTimeArrList = TradeUtil.getInputParams(stockCode);
		
		double totalDaysPnL = 0;
		for(String[] dateTimeArr : dateTimeArrList){
			String tickFileName = dateTimeArr[0] + "-" + dateTimeArr[1] + ".txt";
			List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, dateTimeArr[0]);
			List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);
			
			List<Trade> tradeList = auto(dateTimeArr[0], barList);
			System.out.print(stockCode + ":" + dateTimeArr[0] + "-" + dateTimeArr[1]+"; ");
			totalDaysPnL += TradeUtil.printTrades(tradeList, false);
			//break;
		}
		System.out.println("totalDaysPnL = "+Formatter.DECIMAL_FORMAT.format(totalDaysPnL));
	}
	

	private static VChart createMarkedChart(List<Bar> barList, List<Trade> tradeList, List<VMarker> decisionMarkerList){
	    VChart vchart = BarChartUtil.createBasicChart(barList);
        VPlot vplotBar = vchart.getPlotList().get(0);	
        //add trade annotations
	    List<XYAnnotation> tradeAnnoList = BarChartUtil.trade2AnnotationList(tradeList);
	    vplotBar.addAnnotations(tradeAnnoList);
	    CandlestickRenderer barRenderer = (CandlestickRenderer)vplotBar.getVseriesList().get(0).getRenderer();//bar renderer, first plot, first series
		//add pattern markers to plotBar
        for(VMarker m : decisionMarkerList){
        	barRenderer.addAnnotation(m.toAnno(),Layer.BACKGROUND);
        }
        return vchart;
	}
	

	
	private static String[][] initDateTimeArr(){
		//sanple format: "20110915", "194819"
		return new String[][] {
				{"20110928", "220751"},
				{"20111118", "200223"},
				{"20111017", "200114"},
				{"20110922", "200738"},
				{"20111125", "200037"},
				{"20111117", "200308"},
				{"20111025", "200332"},
				{"20111128", "200121"},
				{"20110930", "205736"},
				{"20111020", "200135"},
				{"20110927", "200425"},
				{"20111014", "200153"},
				{"20111123", "200120"},
				{"20111124", "200133"},
				{"20111109", "200435"},
				{"20110929", "202100"},
				{"20110923", "223948"},
				{"20111122", "200102"},
				{"20111031", "200117"},
				{"20111102", "200117"},
				{"20110920", "212519"},
				{"20111028", "200140"},
				{"20111114", "200132"},
				{"20111012", "200149"},
				{"20110915", "194819"},
				{"20111101", "200252"}

				};

	}
}
