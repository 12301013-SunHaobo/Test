// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package modules.at.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.Point;
import modules.at.model.Point.Type;
import modules.at.model.Tick;
import modules.at.pattern.highlow.HighLowUtil;

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
import org.jfree.ui.TextAnchor;

import utils.Formatter;
import utils.GlobalSetting;

public class TestHighLowBarChart extends ApplicationFrame {

	//change begin
	static String STOCK_CODE = "qqq";
	static String DATE_STR = "20110923";
	static String TICK_FILENAME = DATE_STR + "-" + "223948"+".txt";
	//change end
	
	private static final long serialVersionUID = 1L;
	
	private List<Bar> barList = null;
	
	private AlgoSetting as;
	
	public TestHighLowBarChart(String s) {
		super(s);
		//init barList
		this.barList = getBarList();
		this.as = new AlgoSetting();
		
		
		JFreeChart jfreechart = createChart();
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		chartpanel.setPreferredSize(new Dimension(1500, 700));//window (width, height)
		setContentPane(chartpanel);
	}

	public static void main(String args[]) {
	    if(GlobalSetting.isAtHome()){
    		TestHighLowBarChart candlestickchartdemo1 = new TestHighLowBarChart(STOCK_CODE+":"+TICK_FILENAME);
    		candlestickchartdemo1.pack();
    		RefineryUtilities.centerFrameOnScreen(candlestickchartdemo1);
    		candlestickchartdemo1.setVisible(true);
	    }
	}

	private JFreeChart createChart() {
		CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(new DateAxis("Date/Time"));
		combineddomainxyplot.setDomainPannable(true);
		combineddomainxyplot.add(createCandlestickPlot(),4);//weight 4 for upper candlestick chart
		combineddomainxyplot.add(createLowerIndicatorPlot(),1);//weight 1 for lower indicator chart
		//combineddomainxyplot.add(createSubplot2(createDataset2()));
		JFreeChart jfreechart = new JFreeChart(STOCK_CODE+":"+TICK_FILENAME, JFreeChart.DEFAULT_TITLE_FONT, combineddomainxyplot, true);
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
		List<XYPointerAnnotation> annoList = getAnnotationList();
		for (XYPointerAnnotation anno : annoList){
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
	
	private XYPlot createLowerIndicatorPlot(){
		ValueAxis timeAxis = new DateAxis("Time");
        NumberAxis valueAxis = new NumberAxis("Value");
        XYDataset dataset = createLowerRSIEMAIndicatorXYDataset();
        XYPlot xyplot = new XYPlot(dataset, timeAxis, valueAxis, null);
        
		StandardXYItemRenderer xyItemRenderer = new StandardXYItemRenderer();
		xyItemRenderer.setSeriesPaint(0, Color.blue);//bb upper band
		xyItemRenderer.setSeriesPaint(1, Color.gray);//bb middle
		xyItemRenderer.setSeriesPaint(2, Color.blue);//bb lower band
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
			List<Tick> tickList = HistoryLoader.getNazHistTicks(STOCK_CODE, TICK_FILENAME, nazTickOutputDateStr); 
			// change end -> for new date
			barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return barList;
	}
	
	//get high/low annotation list
	private List<XYPointerAnnotation> getAnnotationList(){
		List<XYPointerAnnotation> annoList = new ArrayList<XYPointerAnnotation>();
		List<Point> highLowPointList = null;
		try {
			highLowPointList = HighLowUtil.findHighLowPoints(this.barList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(highLowPointList!=null){
			for(Point point : highLowPointList)
			annoList.add(createAnnotation(point));
		}
		return annoList;
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
		Indicators indicator = new Indicators(this.as);

		for(Bar bar : this.barList){
			indicator.addBar(bar);
			
			if(!Double.isNaN(indicator.getBBUpper())
					&& !Double.isNaN(indicator.getBBLower())
					&& !Double.isNaN(indicator.getSMAFast())){
				
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
		Indicators indicator = new Indicators(this.as);

		for(Bar bar : this.barList){
			indicator.addBar(bar);
			
			if(!Double.isNaN(indicator.getRsi())){
				rsiEmaUpperSeries.add(bar.getDate().getTime(), 70D);
				rsiEmaSeries.add(bar.getDate().getTime(), indicator.getRsi());
				rsiEmaLowerSeries.add(bar.getDate().getTime(), 30D);
			}
		}
		
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(rsiEmaUpperSeries);
		xyseriescollection.addSeries(rsiEmaSeries);
		xyseriescollection.addSeries(rsiEmaLowerSeries);
		
		return xyseriescollection;
	}
	
	//create one annotation by a point
	private XYPointerAnnotation createAnnotation(Point point){
		//default is for high
		double angle = -Math.PI/2;
		Paint paint = Color.black;
		if(Point.Type.LOW.equals(point.getType())){
			angle = Math.PI/2;
			paint = Color.red;
		}
		
		/*
		if(isNoise(point)){
			if(Point.Type.HIGH.equals(point.getType())){
				paint = Color.green;
			} else {
				paint = Color.cyan;
			}
		}
		*/
		
		//System.out.println(Formatter.DEFAULT_TIME_FORMAT.format(point.getDateTime())+" "+paint.toString());
		
		XYPointerAnnotation xypointerannotation = new XYPointerAnnotation(
				//""+point.getPrice(),
				//"",
				Formatter.DEFAULT_TIME_FORMAT.format(point.getDateTime()),
				point.getDateTime().getTime(), point.getPrice(), angle);
		xypointerannotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
		xypointerannotation.setPaint(paint);
		xypointerannotation.setArrowPaint(paint);
		return xypointerannotation;
	}
	
	private boolean isNoise(Point point){
		Set<Noise> noiseTimeSet = new HashSet<Noise>();
		
		//high noise
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "09:42:58"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "10:10:58"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "11:37:59"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "12:30:54"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "12:57:59"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "13:18:53"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "13:22:56"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "13:31:59"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "14:32:57"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "15:02:59"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "15:09:59"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "15:23:59"));
		noiseTimeSet.add(new Noise(Point.Type.HIGH, "15:32:59"));

		
		//low noise
		noiseTimeSet.add(new Noise(Point.Type.LOW, "10:17:58"));
		noiseTimeSet.add(new Noise(Point.Type.LOW, "10:19:57"));
		noiseTimeSet.add(new Noise(Point.Type.LOW, "12:31:56"));
		noiseTimeSet.add(new Noise(Point.Type.LOW, "12:35:59"));
		noiseTimeSet.add(new Noise(Point.Type.LOW, "13:16:54"));
		noiseTimeSet.add(new Noise(Point.Type.LOW, "13:20:54"));
		noiseTimeSet.add(new Noise(Point.Type.LOW, "13:51:59"));
		noiseTimeSet.add(new Noise(Point.Type.LOW, "14:44:45"));
		noiseTimeSet.add(new Noise(Point.Type.LOW, "14:53:58"));
		
		if(noiseTimeSet.contains(new Noise(point))){
			return true;
		}
		return false;
		
	}

	class Noise {
		private Long time;
		private Point.Type pointType;
		
		public Noise(Point point){
			super();
			this.pointType = point.getType();
			this.time = point.getDateTime().getTime()/1000*1000;
		}
		
		public Noise(Type pointType, String timeStr) {
			super();
			this.pointType = pointType;
			try {
				this.time = Formatter.DEFAULT_DATETIME_FORMAT.parse(DATE_STR+"-"+timeStr).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		public Long getTime() {
			return time;
		}
		public void setTime(Long time) {
			this.time = time;
		}
		public Point.Type getPointType() {
			return pointType;
		}
		public void setPointType(Point.Type pointType) {
			this.pointType = pointType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((pointType == null) ? 0 : pointType.hashCode());
			result = prime * result + ((time == null) ? 0 : time.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Noise other = (Noise) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (pointType != other.pointType)
				return false;
			if (time == null) {
				if (other.time != null)
					return false;
			} else if (!time.equals(other.time))
				return false;
			return true;
		}

		private TestHighLowBarChart getOuterType() {
			return TestHighLowBarChart.this;
		}
		
		
		
	}
}
