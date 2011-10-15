// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package modules.at.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.at.analyze.TestAuto;
import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicator;
import modules.at.model.Bar;
import modules.at.model.Tick;
import modules.at.model.Trade;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class BarChartBase extends ApplicationFrame {

	//change begin
	static String STOCK_CODE = "qqq";
	static String DATE_STR = "20110923";
	static String TIME_STR = "223948";
	
	public static double RSI_UPPER = TestAuto.rsiUpper; //70;//to pass in
	public static double RSI_LOWER = TestAuto.rsiLower; //30;//to pass in
	//change end

	private String stockCode;
	private String tickFileName;

	private static final long serialVersionUID = 1L;
	
	private List<Bar> barList;
	private List<XYPointerAnnotation> annotationList;
	
	public BarChartBase(String stockCode, String dateStr, String timeStr, List<Trade> tradeList) {
		super(stockCode+":"+dateStr + "-" + timeStr+".txt");

		this.stockCode = stockCode;
		this.tickFileName = dateStr + "-" + timeStr+".txt";
		//init barList
		this.barList = getBarList();
		this.annotationList = BarChartUtil.getTradeAnnotationList(tradeList);
		
		JFreeChart jfreechart = createChart();
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		chartpanel.setPreferredSize(new Dimension(1500, 700));//window (width, height)
		setContentPane(chartpanel);
	}

	public static void main(String args[]) {
		List<Trade> tradeList = new ArrayList<Trade>();
		BarChartBase barchartBase = new BarChartBase(STOCK_CODE,DATE_STR, TIME_STR, tradeList);
		barchartBase.pack();
		RefineryUtilities.centerFrameOnScreen(barchartBase);
		barchartBase.setVisible(true);
	}

	private JFreeChart createChart() {
		CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(new DateAxis("Date/Time"));
		combineddomainxyplot.setDomainPannable(true);
		//upper height/lower height = 4/1
		combineddomainxyplot.add(createCandlestickPlot(),4);//weight 4 for upper candlestick chart, 
		combineddomainxyplot.add(createLowerIndicatorPlot(),1);//weight 1 for lower indicator chart
		//combineddomainxyplot.add(createSubplot2(createDataset2()));
		JFreeChart jfreechart = new JFreeChart(this.stockCode+":"+tickFileName, JFreeChart.DEFAULT_TITLE_FONT, combineddomainxyplot, true);
		//jfreechart.setBackgroundPaint(Color.white);
		//ChartUtilities.applyCurrentTheme(jfreechart); //gray background
		
		return jfreechart;
	}

	//create candlestick chart with BB
	private XYPlot createCandlestickPlot(){
        ValueAxis timeAxis = new DateAxis("Time");
        NumberAxis valueAxis = new NumberAxis("Value");
        XYDataset dataset = createCandlestickDataset();
		XYPlot xyplot = new XYPlot(dataset, timeAxis, valueAxis, null);
		xyplot.setRenderer(new CandlestickRenderer());
		 
		xyplot.setDomainPannable(true);
		
		//add annotations
		for (XYPointerAnnotation anno : this.annotationList){
			xyplot.addAnnotation(anno);
		}
		//BB indicator
		XYDataset bbDataset = createUpperIndicatorXYDataset();
		xyplot.setDataset(1, bbDataset);
		StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
		xyItemRenderer.setSeriesPaint(0, Color.blue);//bb upper band
		xyItemRenderer.setSeriesPaint(1, Color.gray);//bb middle
		xyItemRenderer.setSeriesPaint(2, Color.blue);//bb lower band
		xyplot.setRenderer(1, xyItemRenderer);
		
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		
		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		numberaxis.setAutoRangeIncludesZero(false);
		numberaxis.setUpperMargin(0.1);//upper margin
		numberaxis.setLowerMargin(0.1);//lower margin
		
		return xyplot;
	}
	
	//lower indicator plot : RSI_EMA
	private XYPlot createLowerIndicatorPlot(){
		ValueAxis timeAxis = new DateAxis("Time");
        NumberAxis valueAxis = new NumberAxis("Value");
        XYDataset dataset = createLowerRSIEMAIndicatorXYDataset();
        XYPlot xyplot = new XYPlot(dataset, timeAxis, valueAxis, null);
        
		StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
		xyItemRenderer.setSeriesPaint(0, Color.blue);//RSI upper band
		xyItemRenderer.setSeriesPaint(1, Color.gray);//RSI
		xyItemRenderer.setSeriesPaint(2, Color.blue);//RSI lower band
		xyplot.setRenderer(xyItemRenderer);
		
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		
		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		numberaxis.setAutoRangeIncludesZero(false);
		 
		return xyplot;
	}
	
	//get bar list
	private List<Bar> getBarList() {
		List<Bar> barList = new ArrayList<Bar>();
		try {
			// change begin -> for new date
			String nazTickOutputDateStr = DATE_STR;// change for new date
			List<Tick> tickList = HistoryLoader.getNazHistTicks(this.stockCode, this.tickFileName, nazTickOutputDateStr); 
			// change end -> for new date
			barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return barList;
	}
	
	//create OHLC dataset
	public OHLCDataset createCandlestickDataset() {
			int dataSize = this.barList.size();
			Date dateArr[] = new Date[dataSize];
			double highArr[] = new double[dataSize];
			double lowArr[] = new double[dataSize];
			double openArr[] = new double[dataSize];
			double closeArr[] = new double[dataSize];
			double volumeArr[] = new double[dataSize];

			Bar tmpBar = null;
			for (int i = 0; i < dataSize; i++) {
				tmpBar = this.barList.get(i);
				dateArr[i] = tmpBar.getDate();
				highArr[i] = tmpBar.getHigh();
				lowArr[i] = tmpBar.getLow();
				openArr[i] = tmpBar.getOpen();
				closeArr[i] = tmpBar.getClose();
				volumeArr[i] = tmpBar.getVolume();
			}
			return new DefaultHighLowDataset("Series 1", dateArr, highArr, lowArr, openArr, closeArr, volumeArr);
	}
	
	//create BB dataset
	private XYDataset createUpperIndicatorXYDataset() {
		
		XYSeries bbUpperSeries = new XYSeries("BB Upper Line");
		XYSeries bbMiddleSeries = new XYSeries("BB Middle Line");
		XYSeries bbLowerSeries = new XYSeries("BB Lower Line");
		Indicator indicator = new Indicator(14);

		for(Bar bar : this.barList){
			indicator.addValue(bar.getClose());
			
			if(Double.NaN != indicator.getBBUpper()
					&& Double.NaN != indicator.getBBLower()
					&& Double.NaN != indicator.getSMAFast()){
			
				bbUpperSeries.add(bar.getDate().getTime(), indicator.getBBUpper());
				bbMiddleSeries.add(bar.getDate().getTime(), indicator.getSMAFast());
				bbLowerSeries.add(bar.getDate().getTime(), indicator.getBBLower());
			}
		}
		
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(bbUpperSeries);
		xyseriescollection.addSeries(bbMiddleSeries);
		xyseriescollection.addSeries(bbLowerSeries);
		
		return xyseriescollection;
	}
	
	//create lower dataset RSI_EMA
	private XYDataset createLowerRSIEMAIndicatorXYDataset() {
		
		XYSeries rsiEmaUpperSeries = new XYSeries("RSI_EMA_UPPER");
		XYSeries rsiEmaSeries = new XYSeries("RSI_EMA");
		XYSeries rsiEmaLowerSeries = new XYSeries("RSI_EMA_LOWER");
		Indicator indicator = new Indicator(14);

		for(Bar bar : this.barList){
			indicator.addValue(bar.getClose());
			
			if(Double.NaN != indicator.getRsi()){
				rsiEmaUpperSeries.add(bar.getDate().getTime(), RSI_UPPER);
				rsiEmaSeries.add(bar.getDate().getTime(), indicator.getRsi());
				rsiEmaLowerSeries.add(bar.getDate().getTime(), RSI_LOWER);
			}
		}
		
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(rsiEmaUpperSeries);
		xyseriescollection.addSeries(rsiEmaSeries);
		xyseriescollection.addSeries(rsiEmaLowerSeries);
		
		return xyseriescollection;
	}
	

	



}
