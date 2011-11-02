package modules.at.analyze;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.at.TradeUtil;
import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicators;
import modules.at.formula.IndicatorsRule;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.Position;
import modules.at.model.Tick;
import modules.at.model.Trade;
import modules.at.model.visual.PatternMarker;
import modules.at.model.visual.VChart;
import modules.at.model.visual.VPlot;
import modules.at.pattern.Pattern;
import modules.at.pattern.PatternEngulfing;
import modules.at.pattern.PatternMACross;
import modules.at.visual.BarChartUtil;
import modules.at.visual.ChartBase;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.ui.Layer;

import utils.Formatter;

public class TestAuto {

	static double LOCK_PROFIT = Double.NaN;//keeps changing, and LOCK_PROFIT always > CUT_LOSS
	static List<Pattern> patternList = new ArrayList<Pattern>();
	
	public static void main(String[] args) throws Exception {
		initPatternList();
		testOneDay();
		//testAllDays();
	}

	private static void testOneDay() throws Exception{
		String stockCode = "qqq";//qqq, tna, tza 
		//String[] dateTimeArr = new String[] {"20111020", "200115"};
		//String[] dateTimeArr = new String[] {"20111028", "200140"};
		//String[] dateTimeArr = new String[] {"20111019", "200101"};
		//String[] dateTimeArr = new String[] {"20111020", "200135"};
		String[] dateTimeArr = new String[] {"20111101", "200252"};
		
		//get barList
		String tickFileName = dateTimeArr[0] + "-" + dateTimeArr[1] + ".txt";
		List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, dateTimeArr[0]);
		List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);
		//get tradeList
		List<Trade> tradeList = auto(dateTimeArr[0], barList);
		//System.out.println(stockCode + ":" + dateTimeArr[0] + "-" + dateTimeArr[1]);
		TradeUtil.printTrades(tradeList, true);
		
		//display chart with trade and mark info
		VChart vchart = createMarkedChart(barList, tradeList, patternList);
	    new ChartBase(vchart);
	    
	}
	
	private static void initPatternList(){
		patternList.add(new PatternMACross());
		//patternList.add(new PatternRsi());
		//patternList.add(new PatternSto());
		//patternList.add(new PatternHighLow());
		//patternList.add(new PatternEngulfing());
	}
	
	private static List<Trade> auto(String dateStr, List<Bar> barList) throws Exception {

		Indicators indicators = new Indicators();
		for(Pattern pattern : patternList){
			indicators.addObserver(pattern);
		}
		IndicatorsRule indicatorsRule = new IndicatorsRule();
		indicatorsRule.setPatternList(patternList);
		
		
		List<Trade> tradeList = new ArrayList<Trade>();
		for (Bar bar : barList) {
			indicators.addBar(bar);//update indicators 
			//System.out.println("TestAuto:time="+Formatter.DEFAULT_DATETIME_FORMAT.format(bar.getDate())+", price="+Formatter.DECIMAL_FORMAT.format(bar.getClose()));
			double price = bar.getClose();
			long time = bar.getDate().getTime();
			
			Trade trade = decide(indicatorsRule, price, time, dateStr);
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
	private static Trade decide(IndicatorsRule indicatorsRule, double price, long time, String dateStr) throws Exception{
		String tmpTimeStr = Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(time));
		if("20111028-10:41:46".equals(tmpTimeStr)){
			//System.out.println();
		}
		
		Position position = Position.getInstance();
		int pQty = position.getQty();
		double stopPrice = position.getStopLossPrice();
//		System.out.println(
//				Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(time))+
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

				Pattern.Trend trend = indicatorsRule.predictTrend();
				if(Pattern.Trend.Down.equals(trend)){
					if (pQty >= 0){//short
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
			trade.setPnl(0);//TODO: is this necessary?
		}
		position.updateStopPrice(price);
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
		
		for(String[] dateTimeArr : dateTimeArrList){
			String tickFileName = dateTimeArr[0] + "-" + dateTimeArr[1] + ".txt";
			List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, dateTimeArr[0]);
			List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);
			
			List<Trade> tradeList = auto(dateTimeArr[0], barList);
			System.out.print(stockCode + ":" + dateTimeArr[0] + "-" + dateTimeArr[1]);
			TradeUtil.printTrades(tradeList, false);
			//break;
		}
	}
	

	private static VChart createMarkedChart(List<Bar> barList, List<Trade> tradeList, List<Pattern> patternList){
	    VChart vchart = BarChartUtil.createBasicChart(barList);
        VPlot vplotBar = vchart.getPlotList().get(0);	
        //add trade annotations
	    List<XYAnnotation> tradeAnnoList = BarChartUtil.trade2AnnotationList(tradeList);
	    vplotBar.addAnnotations(tradeAnnoList);
	    CandlestickRenderer barRenderer = (CandlestickRenderer)vplotBar.getVseriesList().get(0).getRenderer();//bar renderer, first plot, first series
		//add pattern markers to plotBar
	    for(Pattern p : patternList){
	    	if(p instanceof PatternEngulfing){
			    PatternEngulfing pe = (PatternEngulfing)p;
		        for(PatternMarker pm : pe.getPatternMarkerList()){
		        	barRenderer.addAnnotation(pm.toAnno(),Layer.BACKGROUND);
		        }
	    	}
	    }
        return vchart;
	}
	

	
}
