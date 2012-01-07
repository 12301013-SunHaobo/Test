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
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.annotations.XYPolygonAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
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
import org.jfree.ui.TextAnchor;

import utils.GlobalSetting;

public class MySampleChartBase extends ApplicationFrame {

	private static final boolean SHOW_BB = false;;
	private static final boolean SHOW_MA_FAST = true; 
	private static final boolean SHOW_MA_SLOW = true;
	
	//RSI | STO in lower panel
	private static final boolean SHOW_RSI = false;
	
	private static final boolean SHOW_STO_K = true;
	private static final boolean SHOW_STO_D = false;
	
	private String stockCode;
	private String dateStr;
	private String tickFileName;

	private static final long serialVersionUID = 1L;
	
	private List<Bar> barList;
	private List<XYAnnotation> annotationList;
	
	public MySampleChartBase(String stockCode, String dateStr, String timeStr, List<Trade> tradeList) {
		super(stockCode+":"+dateStr + "-" + timeStr+".txt");

		this.stockCode = stockCode;
		this.dateStr = dateStr;
		this.tickFileName = dateStr + "-" + timeStr+".txt";
		//init barList
		this.barList = getBarList();
		this.annotationList = BarChartUtil.trade2AnnotationList(tradeList);
		
		JFreeChart jfreechart = createChart();
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		chartpanel.setPreferredSize(new Dimension(1500, 700));//window (width, height)
		setContentPane(chartpanel);
		//only display at home
		if(GlobalSetting.isAtHome()){
            this.pack();
            RefineryUtilities.centerFrameOnScreen(this);
            this.setVisible(true);
		}
	}

	//Test BarCahrtBase.java
	public static void main(String args[]) {
		String stockCode = "qqq";
		String dateStr = "20111014";
		String timeStr = "200153";
		List<Trade> tradeList = new ArrayList<Trade>();
		new MySampleChartBase(stockCode,dateStr, timeStr, tradeList);
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
		
		//add annotations begin
		//pointer annotation
		XYPointerAnnotation xypointerannotation = new XYPointerAnnotation(
				"XYPoint anno", 1318599419000D, 57.9917D, -Math.PI*3/4);
		xypointerannotation.setTextAnchor(TextAnchor.CENTER_RIGHT);
		xypointerannotation.setPaint(Color.black);
		xypointerannotation.setArrowPaint(Color.black);
		xypointerannotation.setBaseRadius(25);//the distance from point to arrow end
		xypointerannotation.setTipRadius(5);//the distance from point to arrow head
		xyplot.addAnnotation(xypointerannotation);
		
		//rectangle area annotation
		XYShapeAnnotation xyshapeannotation = new XYShapeAnnotation(
				new java.awt.geom.Rectangle2D.Double(1318599059000D, 57.886, 600*1000D, 0.09D), 
				BarChartUtil.DASH_STROKE, Color.blue);
		xyplot.addAnnotation(xyshapeannotation);
		
		//polygon area annotation(clockwise)
		XYPolygonAnnotation xypolygonannotation = new XYPolygonAnnotation(new double[] {
				1318599059000D, 57.82D, 
				1318599179000D, 57.92D, 
				1318599358000D, 57.94D, 
				1318599479000D, 57.81D,
				1318599598000D, 57.95D
			}, BarChartUtil.BASIC_STOKE, Color.blue, null);
		xyplot.addAnnotation(xypolygonannotation);
		
		//line annotation
		XYLineAnnotation xylineannotation = new XYLineAnnotation(1318599059000D, 57.986D, 1318599419000D, 57.786D);
		xyplot.addAnnotation(xylineannotation);

		//interval
		//xyplot.addDomainMarker(new IntervalMarker(1318599059000D, 1318599598000D), Layer.BACKGROUND);//domain=x
		//xyplot.addRangeMarker(new IntervalMarker(57.786D, 57.986D), Layer.BACKGROUND);//range=y; not working? answer: start<end
		
		//add annotations end
		
		return xyplot;
	}
	/*
	Bar [id=1, date=2011/10/14, o=57.83, h=57.89, l=57.82, c=57.886] time=1318599059000
	Bar [id=2, date=2011/10/14, o=57.89, h=57.92, l=57.87, c=57.89] time=1318599118000
	Bar [id=3, date=2011/10/14, o=57.89, h=57.94, l=57.8699, c=57.92] time=1318599179000
	Bar [id=4, date=2011/10/14, o=57.92, h=57.97, l=57.92, c=57.95] time=1318599239000
	Bar [id=5, date=2011/10/14, o=57.95, h=58.0, l=57.94, c=57.99] time=1318599299000
	Bar [id=6, date=2011/10/14, o=58.0, h=58.03, l=57.99, c=57.99] time=1318599358000
	Bar [id=7, date=2011/10/14, o=57.99, h=57.99, l=57.892, c=57.892] time=1318599419000
	Bar [id=8, date=2011/10/14, o=57.89, h=57.95, l=57.89, c=57.95] time=1318599479000
	Bar [id=9, date=2011/10/14, o=57.94, h=58.0, l=57.94, c=57.971] time=1318599538000
	Bar [id=10, date=2011/10/14, o=57.979, h=57.9917, l=57.97, c=57.9887] time=1318599598000	
	*/
	
	//lower indicator plot : RSI_EMA
	private XYPlot createLowerIndicatorPlot(){
		ValueAxis timeAxis = new DateAxis("Time");
        NumberAxis valueAxis = new NumberAxis("Value");
        XYDataset dataset = createRSIEMAIndicatorXYDataset();
        XYPlot xyplot = new XYPlot(dataset, timeAxis, valueAxis, null);
        
        int datasetIdx = 0;
        //RSI indicator
        if(SHOW_RSI){
			StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
			xyItemRenderer.setSeriesPaint(0, Color.blue);//RSI upper band
			xyItemRenderer.setSeriesPaint(1, Color.gray);//RSI
			xyItemRenderer.setSeriesPaint(2, Color.blue);//RSI lower band
			xyplot.setRenderer(xyItemRenderer);
        }else if(SHOW_STO_K){
        	datasetIdx++;
			XYDataset stoDataset = createStoKIndicatorXYDataset();
			xyplot.setDataset(datasetIdx, stoDataset);
			StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
			
			int seriesIdx = 0;
			xyItemRenderer.setSeriesPaint(seriesIdx++, Color.blue);//Sto upper band
			if(SHOW_STO_K){
				xyItemRenderer.setSeriesPaint(seriesIdx++, Color.red);//Sto K
			}
			if(SHOW_STO_D){
				xyItemRenderer.setSeriesPaint(seriesIdx++, Color.pink);//Sto D
			}
			xyItemRenderer.setSeriesPaint(seriesIdx++, Color.blue);//Sto lower band
			xyplot.setRenderer(datasetIdx, xyItemRenderer);
        	
        }
		
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
	private XYDataset createRSIEMAIndicatorXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.RsiUpper, barList));
		xyseriescollection.addSeries(getXYSeries(SeriesType.Rsi, barList));
		xyseriescollection.addSeries(getXYSeries(SeriesType.RsiLower, barList));
		return xyseriescollection;
	}
	
	//create StoK dataset
	private XYDataset createStoKIndicatorXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(getXYSeries(SeriesType.StoUpper, barList));
		if(SHOW_STO_K){
			xyseriescollection.addSeries(getXYSeries(SeriesType.StoK, barList));
		}
		if(SHOW_STO_D){
			xyseriescollection.addSeries(getXYSeries(SeriesType.StoD, barList));
		}
		xyseriescollection.addSeries(getXYSeries(SeriesType.StoLower, barList));
		return xyseriescollection;
	}

	enum SeriesType {
		RsiUpper, Rsi, RsiLower,
		BBUpper, BBMiddle, BBLower,
		MAFast, MASlow,
		StoK, StoD, StoUpper, StoLower
	}
	private XYSeries getXYSeries(SeriesType seriesType, List<Bar> barList){
		XYSeries series = new XYSeries(seriesType.toString());
		Indicators indicator = new Indicators();
		
		for(Bar bar : this.barList){
			indicator.addBar(bar);
			double indicatorVal = Double.NaN;
			switch (seriesType) {
				case RsiUpper: indicatorVal = AlgoSetting.rsiUpper; break;
				case Rsi: indicatorVal = indicator.getRsi(); break;
				case RsiLower: indicatorVal = AlgoSetting.rsiLower; break;
				case BBUpper: indicatorVal = indicator.getBBUpper(); break;
				case BBMiddle: indicatorVal = indicator.getBBMiddle(); break;
				case BBLower: indicatorVal = indicator.getBBLower(); break;
				case MAFast: indicatorVal = indicator.getSMAFast(); break;
				case MASlow: indicatorVal =  indicator.getSMASlow(); break;
				case StoK: indicatorVal = indicator.getStochasticK(); break;
				case StoD: indicatorVal = indicator.getStochasticD(); break;
				case StoUpper: indicatorVal = AlgoSetting.stochasticUpper; break;
				case StoLower: indicatorVal = AlgoSetting.stochasticLower; break;
				default:break;
			}
			if(!Double.isNaN(indicatorVal)){
				series.add(bar.getDate().getTime(), indicatorVal);
			}
		}
		return series;
	}



}
