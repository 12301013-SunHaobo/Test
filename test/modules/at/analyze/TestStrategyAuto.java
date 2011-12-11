package modules.at.analyze;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.at.TradeUtil;
import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
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
		System.out.println("total time used: "+ (e0-b0));
	}

	private static void testOneDay() throws Exception{
		String stockCode = "qqq";//qqq, tna, tza 
		
		//get barList
		for(int i=0;i<dateTimeArr.length;i++){
			String tickFileName = dateTimeArr[i][0] + "-" + dateTimeArr[i][1] + ".txt";
			List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, dateTimeArr[i][0]);
			List<List<Bar>> barLists = new ArrayList<List<Bar>>();
			List<Bar> barList = TickToBarConverter.convert(tickList, AlgoSetting.BAR_TIME_PERIOD);
			List<Bar> barList2 = TickToBarConverter.convert(tickList, 5*AlgoSetting.BAR_TIME_PERIOD);
			barLists.add(barList);
			barLists.add(barList2);
			
			//get tradeList
			List<Trade> tradeList = auto(dateTimeArr[i][0], barList);
			System.out.print(stockCode + ":" + dateTimeArr[i][0] + "-" + dateTimeArr[i][1]+", ");
			TradeUtil.printTrades(tradeList, true);
			
			//display chart with trade and mark info
			VChart vchart = createMarkedChart(barLists, tradeList, strategy.getDecisionMarkerList());
			vchart.setTitle(tickFileName);
			
			boolean saveToFile = false;//save to file | display
		    ChartBase cb = new ChartBase(vchart, !saveToFile && dateTimeArr.length==1);
		    if(saveToFile){
		    	cb.saveToFile("D:/user/stock/us/screen-snapshot/MAStrategy/"+i+"_"+dateTimeArr[i][0]+".png");
		    }	
		}
	    
	}
	

	
	private static List<Trade> auto(String dateStr, List<Bar> barList) throws Exception {

		List<Trade> tradeList = new ArrayList<Trade>();
		for (Bar bar : barList) {
			//System.out.println("TestAuto:time="+Formatter.DEFAULT_DATETIME_FORMAT.format(bar.getDate())+", price="+Formatter.DECIMAL_FORMAT.format(bar.getClose()));
			Trade trade = decide(bar, dateStr);
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
	private static Trade decide(Bar bar, String dateStr) throws Exception{
		strategy.update(bar);//update strategy
		double price = bar.getClose();
		long time = bar.getDate().getTime();
		String tmpTimeStr = Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(time));
		if("20111028-10:41:46".equals(tmpTimeStr)){
			//System.out.println();
		}
		
		Position position = Position.getInstance();
		int pQty = position.getQty();
		double stopPrice = position.getStopLossPrice();
//		System.out.println(Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(time))+", stopPrice="+Formatter.DECIMAL_FORMAT4.format(stopPrice));

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
	
	//test all days in data/naz/tick/output/qqq
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
	

	private static VChart createMarkedChart(List<List<Bar>> barLists, List<Trade> tradeList, List<VMarker> decisionMarkerList){
	    VChart vchart = BarChartUtil.createBasicChart(barLists);
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
//				{"20110928", "220751"},
//				{"20111004", "195804"},
//				{"20110930", "205736"},
//				{"20110922", "200738"},
//				{"20111205", "200105"},
//				{"20111025", "200332"},
//				{"20111011", "200137"},
//				{"20111123", "200120"},
//				{"20111118", "200223"},
//				{"20111102", "200117"},
//				{"20111114", "200132"},
//				{"20110916", "195420"},
//				{"20111117", "200308"},
//				{"20111031", "200117"},
//				{"20111121", "200136"},
//				{"20110920", "212519"},
//				{"20111017", "200114"},
//				{"20111115", "200120"},
//				{"20111202", "200058"},
//				{"20110923", "223948"},
				{"20111128", "200121"},
//				{"20111021", "200115"},
//				{"20111007", "001654"},
//				{"20111014", "200153"},
//				{"20111028", "200140"},
//				{"20111130", "200141"},
//				{"20110915", "194819"},
//				{"20111020", "200135"},
//				{"20111122", "200102"},
//				{"20111012", "200149"},
//				{"20111013", "200134"},
//				{"20111104", "200117"},
//				{"20111109", "200435"},
//				{"20110927", "200425"},
//				{"20111111", "200109"},
//				{"20110926", "200301"},
//				{"20111026", "200246"},
//				{"20111201", "200119"},
//				{"20111024", "200203"},
//				{"20111206", "200048"},
//				{"20111107", "200117"},
//				{"20111116", "213410"},
//				{"20111027", "200154"},
//				{"20110929", "202100"},
//				{"20111108", "200141"},
//				{"20111003", "224038"},
//				{"20111129", "200135"},
//				{"20111101", "200252"},
//				{"20111103", "200218"},
//				{"20111018", "200248"},
//				{"20111110", "200140"},
//				{"20110919", "205230"},
//				{"20111207", "200059"},



				};

	}
}
