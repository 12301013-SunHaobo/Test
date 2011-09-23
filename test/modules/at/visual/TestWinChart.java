// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package modules.at.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Tick;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class TestWinChart extends ApplicationFrame {

	private static final long serialVersionUID = -9050631696899168131L;

	public TestWinChart(String s) throws Exception {
		super(s);
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(1500, 910));
		setContentPane(jpanel);
	}

	private static JFreeChart createChart(XYDataset xydataset) {
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("Time Series Demo 3", "Time", "Value",
				xydataset, true, true, false);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setDomainPannable(true);
		xyplot.setRangePannable(false);
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1, new SimpleDateFormat("MMM-yyyy")));
		dateaxis.setVerticalTickLabels(true);
		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
		xylineandshaperenderer.setBaseShapesVisible(true);
		xylineandshaperenderer.setSeriesFillPaint(0, Color.red);
		xylineandshaperenderer.setSeriesFillPaint(1, Color.white);
		xylineandshaperenderer.setUseFillPaint(true);
		xylineandshaperenderer.setLegendItemToolTipGenerator(new StandardXYSeriesLabelGenerator("Tooltip {0}"));
		return jfreechart;
	}

	private static XYDataset createDataset() throws Exception {
		TimeSeries timeseries = new TimeSeries("Series 1");
		
		//change begin -> for new date
		String nazTickOutputDateStr = "20110919";//change for new date 
		List<Tick> tickList = HistoryLoader.getNazHistTicks("qqq", "20110919-205230.txt", nazTickOutputDateStr); //change for new file
		//change end -> for new date
		
		Tick tmpTick = tickList.get(0);
		long firstSecond = tmpTick.getDate().getTime();
		for(int i=0; i< 100; i++){
			tmpTick = tickList.get(i);
			long tmpSecond = firstSecond+i*1000;
			timeseries.add(new Second(new Date(tmpSecond)), tmpTick.getPrice());
		}
		
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(timeseries);
		return timeseriescollection;
	}

	public static JPanel createDemoPanel() throws Exception {
		JFreeChart jfreechart = createChart(createDataset());
		return new ChartPanel(jfreechart);
	}

	public static void main(String args[]) throws Exception {
		TestWinChart timeseriesdemo3 = new TestWinChart("Time Series Demo 3");
		timeseriesdemo3.pack();
		RefineryUtilities.centerFrameOnScreen(timeseriesdemo3);
		timeseriesdemo3.setVisible(true);
	}
}
