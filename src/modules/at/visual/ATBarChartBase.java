// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package modules.at.visual;

import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import modules.at.model.Tick;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class ATBarChartBase extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	
	public ATBarChartBase(String s) {
		super(s);
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(1000, 540));
		setContentPane(jpanel);
	}

	public static JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart(createDataset2());
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}

	public static void main(String args[]) {
		ATBarChartBase candlestickchartdemo1 = new ATBarChartBase("JFreeChart : CandlestickChartDemo1.java");
		candlestickchartdemo1.pack();
		RefineryUtilities.centerFrameOnScreen(candlestickchartdemo1);
		candlestickchartdemo1.setVisible(true);
	}

	private static JFreeChart createChart(OHLCDataset ohlcdataset) {
		JFreeChart jfreechart = ChartFactory.createCandlestickChart("Candlestick Demo 1", "Time", "Value", ohlcdataset, true);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setDomainPannable(true);
		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		numberaxis.setAutoRangeIncludesZero(false);
		numberaxis.setUpperMargin(0.0);
		numberaxis.setLowerMargin(0.0);
		return jfreechart;
	}

	public static OHLCDataset createDataset2() {
		try {
			// change begin -> for new date
			String nazTickOutputDateStr = "20110919";// change for new date
			List<Tick> tickList = HistoryLoader.getNazHistTicks("qqq", "20110919-205230.txt", nazTickOutputDateStr); // change for new file
			// change end -> for new date
			List<Bar> barList = TickToBarConverter.convert(tickList, TickToBarConverter.MINUTE);

			int dataSize = barList.size();
			Date dateArr[] = new Date[dataSize];
			double highArr[] = new double[dataSize];
			double lowArr[] = new double[dataSize];
			double openArr[] = new double[dataSize];
			double closeArr[] = new double[dataSize];
			double volumeArr[] = new double[dataSize];

			Bar tmpBar = null;
			for (int i = 0; i < dataSize; i++) {
				tmpBar = barList.get(i);
				dateArr[i] = tmpBar.getDate();
				highArr[i] = tmpBar.getHigh();
				lowArr[i] = tmpBar.getLow();
				openArr[i] = tmpBar.getOpen();
				closeArr[i] = tmpBar.getClose();
				volumeArr[i] = tmpBar.getVolume();
			}
			return new DefaultHighLowDataset("Series 1", dateArr, highArr, lowArr, openArr, closeArr, volumeArr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
