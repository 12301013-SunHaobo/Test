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
import modules.at.model.visual.VChart;
import modules.at.model.visual.VMarker;
import modules.at.model.visual.VPlot;
import modules.at.pattern.Pattern;
import modules.at.pattern.PatternEngulfing;
import modules.at.pattern.PatternHighLow;
import modules.at.pattern.PatternMACross;
import modules.at.pattern.PatternS1;
import modules.at.visual.BarChartUtil;
import modules.at.visual.ChartBase;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.ui.Layer;

import utils.Formatter;

public class TestPatternAuto {
	
	static String[] dateTimeArr = initDateTimeArr();

	static double LOCK_PROFIT = Double.NaN;//keeps changing, and LOCK_PROFIT always > CUT_LOSS
	static List<Pattern> patternList = new ArrayList<Pattern>();
	
	public static void main(String[] args) throws Exception {
		initPatternList();
		testOneDay();
		//testAllDays();
	}

	private static void testOneDay() throws Exception{
		String stockCode = "qqq";//qqq, tna, tza 
		
		//get barList
		String tickFileName = dateTimeArr[0] + "-" + dateTimeArr[1] + ".txt";
		List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, dateTimeArr[0]);
		List<Bar> barList = TickToBarConverter.convert(tickList, AlgoSetting.BAR_TIME_PERIOD);
		//get tradeList
		List<Trade> tradeList = auto(dateTimeArr[0], barList);
		//System.out.println(stockCode + ":" + dateTimeArr[0] + "-" + dateTimeArr[1]);
		TradeUtil.printTrades(tradeList, true);
		
		//display chart with trade and mark info
		VChart vchart = createMarkedChart(barList, tradeList, patternList);
		vchart.setTitle(tickFileName);
	    new ChartBase(vchart);
	    
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
	    	if(p instanceof PatternEngulfing 
    			|| p instanceof PatternHighLow
    			|| p instanceof PatternS1){
		        for(VMarker m : p.getPatternMarkerList()){
		        	barRenderer.addAnnotation(m.toAnno(),Layer.BACKGROUND);
		        }
	    	}
	    }
        return vchart;
	}
	
	private static void initPatternList(){
		patternList.add(new PatternMACross());
		//patternList.add(new PatternRsi());
		//patternList.add(new PatternSto());
		//patternList.add(new PatternHighLow());
		//patternList.add(new PatternEngulfing());
		//patternList.add(new PatternS1());
	}
	
	private static String[] initDateTimeArr(){
		return new String[] {
				//"20110915", "194819"
				"20110916", "195420"
				//"20110919", "205230"
				//"20110920", "212519"
				//"20110922", "200738"
				//"20110923", "223948"
				//"20110926", "200301"
				//"20110927", "200425"
				//"20110928", "220751"
				//"20110929", "202100"
				//"20110930", "205736"
				//"20111003", "224038"
				//"20111004", "195804"
				//"20111007", "001654"
				//"20111011", "200137"
				//"20111012", "200149"
				//"20111013", "200134"
				//"20111014", "200153"
				//"20111017", "200114"
				//"20111018", "200248"
				//"20111020", "200135"
				//"20111021", "200115"
				//"20111024", "200203"
				//"20111025", "200332"
				//"20111026", "200246"
				//"20111027", "200154"
				//"20111028", "200140"
				//"20111031", "200117"
				//"20111101", "200252"
				//"20111102", "200117"
				//"20111103", "200218"
				//"20111104", "200117"
				//"20111107", "200117"
				//"20111108", "200141"
				};

	}
}
