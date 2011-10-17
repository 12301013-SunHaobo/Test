// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package modules.at.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
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

	private static final boolean SHOW_BB = false;;
	private static final boolean SHOW_MA_FAST = true; 
	private static final boolean SHOW_MA_SLOW = true;
	
	private String stockCode;
	private String dateStr;
	private String tickFileName;

	private static final long serialVersionUID = 1L;
	
	private List<Bar> barList;
	private List<XYPointerAnnotation> annotationList;
	
	public BarChartBase(String stockCode, String dateStr, String timeStr, List<Trade> tradeList) {
		super(stockCode+":"+dateStr + "-" + timeStr+".txt");

		this.stockCode = stockCode;
		this.dateStr = dateStr;
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

	//Test BarCahrtBase.java
	public static void main(String args[]) {
		String stockCode = "qqq";
		String dateStr = "20111014";
		String timeStr = "200153";
		List<Trade> tradeList = new ArrayList<Trade>();
		BarChartBase barchartBase = new BarChartBase(stockCode,dateStr, timeStr, tradeList);
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
		
		int datasetIdx = 0;
		//BB indicator
		if(SHOW_BB){
			datasetIdx++;
			XYDataset bbDataset = createBBIndicatorXYDataset();
			xyplot.setDataset(datasetIdx, bbDataset);
			StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
			xyItemRenderer.setSeriesPaint(0, Color.blue);//bb upper band
			xyItemRenderer.setSeriesPaint(1, Color.gray);//bb middle
			xyItemRenderer.setSeriesPaint(2, Color.blue);//bb lower band
			xyplot.setRenderer(datasetIdx, xyItemRenderer);
		}
		//MA indicator
		if(SHOW_MA_FAST){
			datasetIdx++;
			XYDataset bbDataset = createMAFastIndicatorXYDataset();
			xyplot.setDataset(datasetIdx, bbDataset);
			StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
			xyItemRenderer.setSeriesPaint(0, Color.blue);//MA fast
			xyplot.setRenderer(datasetIdx, xyItemRenderer);
		}
		//MA indicator
		if(SHOW_MA_SLOW){
			datasetIdx++;
			XYDataset bbDataset = createMASlowIndicatorXYDataset();
			xyplot.setDataset(datasetIdx, bbDataset);
			StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
			xyItemRenderer.setSeriesPaint(0, Color.green);//MA slow
			xyplot.setRenderer(datasetIdx, xyItemRenderer);
		}
		
		
		
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
			String nazTickOutputDateStr = this.dateStr;// change for new date
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
	private XYDataset createBBIndicatorXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.BBUpper, barList));
		xyseriescollection.addSeries(getXYSeries(SeriesType.BBMiddle, barList));
		xyseriescollection.addSeries(getXYSeries(SeriesType.BBLower, barList));
		return xyseriescollection;
	}
	
	//create MA fast dataset
	private XYDataset createMAFastIndicatorXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.MAFast, barList));
		return xyseriescollection;
	}
	//create MA slow dataset
	private XYDataset createMASlowIndicatorXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.MASlow, barList));
		return xyseriescollection;
	}

	
	//create RSI_EMA dataset
	private XYDataset createLowerRSIEMAIndicatorXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.RsiUpper, barList));
		xyseriescollection.addSeries(getXYSeries(SeriesType.Rsi, barList));
		xyseriescollection.addSeries(getXYSeries(SeriesType.RsiLower, barList));
		return xyseriescollection;
	}
	

	enum SeriesType {
		RsiUpper, Rsi, RsiLower,
		BBUpper, BBMiddle, BBLower,
		MAFast, MASlow
	}
	private XYSeries getXYSeries(SeriesType seriesType, List<Bar> barList){
		XYSeries series = new XYSeries(seriesType.toString());
		Indicators indicator = new Indicators();
		
		for(Bar bar : this.barList){
			indicator.addValue(bar.getClose());
			switch (seriesType) {
				case RsiUpper:
					series.add(bar.getDate().getTime(), AlgoSetting.RSI_UPPER);
					break;
				case Rsi:
					if(!Double.isNaN(indicator.getRsi())){
						series.add(bar.getDate().getTime(), indicator.getRsi());
					}
					break;
				case RsiLower:
					series.add(bar.getDate().getTime(), AlgoSetting.RSI_LOWER);
					break;
	
				case BBUpper:
					if(!Double.isNaN(indicator.getBBUpper())){
						series.add(bar.getDate().getTime(), indicator.getBBUpper());
					}
					break;
				case BBMiddle:
					if(!Double.isNaN(indicator.getBBMiddle())){
						series.add(bar.getDate().getTime(), indicator.getBBMiddle());
					}	
					break;
				case BBLower:
					if(!Double.isNaN(indicator.getBBLower())){
						series.add(bar.getDate().getTime(), indicator.getBBLower());
					}
					break;
	
				case MAFast:
					if(!Double.isNaN(indicator.getSMAFast())){
						series.add(bar.getDate().getTime(), indicator.getSMAFast());
					}
					break;
				case MASlow:
					if(!Double.isNaN(indicator.getSMASlow())){
						series.add(bar.getDate().getTime(), indicator.getSMASlow());
					}
					break;
	
				default:
					break;
			}
		}
		return series;
	}



}
