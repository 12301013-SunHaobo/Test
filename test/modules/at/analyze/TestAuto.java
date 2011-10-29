package modules.at.analyze;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
import modules.at.model.visual.VPlot;
import modules.at.model.visual.VSeries;
import modules.at.pattern.Pattern;
import modules.at.pattern.PatternHighLow;
import modules.at.pattern.PatternSto;
import modules.at.visual.BarChartUtil;
import modules.at.visual.ChartBase;

import org.jfree.chart.annotations.XYAnnotation;

import utils.FileUtil;
import utils.Formatter;
import utils.GlobalSetting;
import utils.MathUtil;

public class TestAuto {

	static double LOCK_PROFIT = Double.NaN;//keeps changing, and LOCK_PROFIT always > CUT_LOSS
	
	public static void main(String[] args) throws Exception {
		testOneDay();
		//testAllDays();
	    //testRandom();
	}

	private static void testOneDay() throws Exception{
		String stockCode = "qqq";//qqq, tna, tza 
		String[] dateTimeArr = new String[] {"20111021", "200115"};
		
		String tickFileName = dateTimeArr[0] + "-" + dateTimeArr[1] + ".txt";
		List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, dateTimeArr[0]);
		List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);

		List<Trade> tradeList = auto(dateTimeArr[0], barList);
		System.out.println(stockCode + ":" + dateTimeArr[0] + "-" + dateTimeArr[1]);
		printTrades(tradeList, true);
		
		
		//show chart
	    VChart vchart = new VChart(tickFileName);
	    
	    /**
	     * bar plot0
	     */
	    VPlot vplotBar = new VPlot(4);
	    vplotBar.addSeries(new VSeries("Bar", null, barList, java.awt.Color.red));
	    vplotBar.addSeries(new VSeries("MAFast",BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAFast, barList), null, java.awt.Color.magenta));
	    List<XYAnnotation> tradeAnnoList = BarChartUtil.trade2AnnotationList(tradeList);
	    vplotBar.addAnnotations(tradeAnnoList);
	    vchart.addPlot(vplotBar);	    
	    
//	    /**
//	     * indicators plot1
//	     */
//	    //MA plot
//	    VPlot vplotIndicator = new VPlot(1);
//	    vplotIndicator.addSeries(new VSeries("MAFast",BarChartUtil.getVXYList(BarChartUtil.SeriesType.MAFast, barList), null, java.awt.Color.red));
//	    vplotIndicator.addSeries(new VSeries("MASlow", BarChartUtil.getVXYList(BarChartUtil.SeriesType.MASlow, barList), null, java.awt.Color.blue));
//	    vchart.addPlot(vplotIndicator);

	    //RSI plot
	    VPlot vplotRsi = new VPlot(1);
	    vplotRsi.addSeries(new VSeries("RsiUpper", BarChartUtil.getVXYList(BarChartUtil.SeriesType.RsiUpper, barList), null, java.awt.Color.red));
	    vplotRsi.addSeries(new VSeries("Rsi", BarChartUtil.getVXYList(BarChartUtil.SeriesType.Rsi, barList), null, java.awt.Color.red));
	    vplotRsi.addSeries(new VSeries("RsiLower", BarChartUtil.getVXYList(BarChartUtil.SeriesType.RsiLower, barList), null, java.awt.Color.red));
	    vchart.addPlot(vplotRsi);
	    
	    new ChartBase(vchart);
	    
	}
	
	private static void testAllDays() throws Exception{
		String stockCode = "tza";//qqq, tna, tza 
		List<String[]> dateTimeArrList = getInputParams(stockCode);
		
		for(String[] dateTimeArr : dateTimeArrList){
			String tickFileName = dateTimeArr[0] + "-" + dateTimeArr[1] + ".txt";
			List<Tick> tickList = HistoryLoader.getNazHistTicks(stockCode, tickFileName, dateTimeArr[0]);
			List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);
			
			List<Trade> tradeList = auto(dateTimeArr[0], barList);
			System.out.print(stockCode + ":" + dateTimeArr[0] + "-" + dateTimeArr[1]);
			printTrades(tradeList, true);
			//break;
		}
	}
	
	/**
	 * loop through all dates, test 10 tiems / day, 
	 * avg pnl = e.g: 0.036592727, 0.011640455, -0.025676818, 0.0146508, 0.019521932
	 * 
	 */
	private static void testRandom() throws Exception {
	    
        String stockCode = "qqq";// qqq, tna, tza
        List<String[]> dateTimeArrList = getInputParams(stockCode);

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
                            tradeList.add(new Trade(bar.getClose(), 1, bar.getDate().getTime(), Trade.Type.Long));
                        } else {
                            tradeList.add(new Trade(bar.getClose(), -1, bar.getDate().getTime(), Trade.Type.Sell));
                        }
                    }
                    double pnL = printTrades(tradeList, false);
                    totalPnL = totalPnL + pnL;
                }
                double avgPnL4OneDay = (totalPnL / testTimes4OneDay);
                //System.out.println(tickFileName + " X " + testTimes4OneDay + ", avg pnl=" + avgPnL4OneDay);
                tmpPnL4AllDays+=avgPnL4OneDay;
            }
            System.out.println("--------------- > 4AllDays avg pnl="+(tmpPnL4AllDays/dateTimeArrList.size()));
        }       
	}
	
	private static List<Pattern> getPatternList(){
		List<Pattern> patternList = new ArrayList<Pattern>();
		//patternList.add(new PatternMA());
		//patternList.add(new PatternRsi());
		patternList.add(new PatternSto());
		patternList.add(new PatternHighLow());
		return patternList;
		
	}
	
	private static List<Trade> auto(String dateStr, List<Bar> barList) throws Exception {

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
		if("20111018-09:51:58".equals(tmpTimeStr)){
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
					
					//cut win/loss
					if(tmpPnL < position.getCutWinLossTotal()){
						trade = new Trade(price, -1 * pQty, time, Trade.Type.CutLoss);
						position.setPosition(0, price);
						position.setCutWinLossTotal(AlgoSetting.INIT_CUT_WIN_LOSS_TOTAL);
						break;
					} else {
						//increase cut win/loss level
						position.setCutWinLossTotal(tmpPnL+AlgoSetting.INIT_CUT_WIN_LOSS_TOTAL);
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
