package modules.at.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class ATChartBase extends ApplicationFrame {

	private static final long serialVersionUID = -9050631696899168131L;

	public ATChartBase(String s) {
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

	private static XYDataset createDataset() {
		TimeSeries timeseries = new TimeSeries("Series 1");
		timeseries.add(new Month(1, 2002), 500.19999999999999D);
		timeseries.add(new Month(2, 2002), 694.10000000000002D);
		timeseries.add(new Month(3, 2002), 734.39999999999998D);
		timeseries.add(new Month(4, 2002), 453.19999999999999D);
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(timeseries);
		return timeseriescollection;
	}

	public static JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart(createDataset());
		return new ChartPanel(jfreechart);
	}

	public static void main(String args[]) {
		ATChartBase timeseriesdemo3 = new ATChartBase("Time Series Demo 3");
		timeseriesdemo3.pack();
		RefineryUtilities.centerFrameOnScreen(timeseriesdemo3);
		timeseriesdemo3.setVisible(true);
	}
}
