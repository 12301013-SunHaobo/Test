// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package modules.at.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.formula.Indicator;
import modules.at.model.Bar;
import modules.at.model.Point;
import modules.at.model.Tick;
import modules.at.pattern.highlow.HighLowPattern;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

public class ATBarChartBase extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	
	List<Bar> barList = null;
	
	
	public ATBarChartBase(String s) {
		super(s);
		
		//init barList
		this.barList = getBarList();
		
		
		JFreeChart jfreechart = createChart(createDataset());
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		chartpanel.setPreferredSize(new Dimension(1500, 700));//window (width, height)
		setContentPane(chartpanel);
	}

	public static void main(String args[]) {
		ATBarChartBase candlestickchartdemo1 = new ATBarChartBase("JFreeChart : CandlestickChartDemo1.java");
		candlestickchartdemo1.pack();
		RefineryUtilities.centerFrameOnScreen(candlestickchartdemo1);
		candlestickchartdemo1.setVisible(true);
	}

	private JFreeChart createChart(OHLCDataset ohlcdataset) {
		JFreeChart jfreechart = ChartFactory.createCandlestickChart("Candlestick Demo 1", "Time", "Value", ohlcdataset, true);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setDomainPannable(true);
		
		//add annotations
		List<XYPointerAnnotation> annoList = getAnnotationList();
		for (XYPointerAnnotation anno : annoList){
			xyplot.addAnnotation(anno);
		}
		
		XYDataset bbDataset = createIndicatorXYDataset();
		xyplot.setDataset(1, bbDataset);
		xyplot.setRenderer(1, new StandardXYItemRenderer());
		
		
		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		numberaxis.setAutoRangeIncludesZero(false);
		numberaxis.setUpperMargin(0.1);//upper margin
		numberaxis.setLowerMargin(0.1);//lower margin
		return jfreechart;
	}

	//get bar list
	private List<Bar> getBarList() {
		List<Bar> barList = new ArrayList<Bar>();
		try {
			// change begin -> for new date
			String nazTickOutputDateStr = "20110923";// change for new date
			List<Tick> tickList = HistoryLoader.getNazHistTicks("qqq", "20110923-223948.txt", nazTickOutputDateStr); 
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
			highLowPointList = HighLowPattern.findHighLowPoints(this.barList);
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
	public OHLCDataset createDataset() {
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
	private XYDataset createIndicatorXYDataset() {
		
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
	
	//create one annotation by a point
	private XYPointerAnnotation createAnnotation(Point point){
		//default is for high
		double angle = -Math.PI/2;
		Paint paint = Color.black;
		if(Point.Type.LOW.equals(point.getType())){
			angle = Math.PI/2;
			paint = Color.white;
		}
		XYPointerAnnotation xypointerannotation = new XYPointerAnnotation(
				//""+point.getPrice(),
				"",
				point.getDateTime().getTime(), point.getPrice(), angle);
		xypointerannotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
		xypointerannotation.setPaint(paint);
		xypointerannotation.setArrowPaint(paint);
		return xypointerannotation;
	}

}
