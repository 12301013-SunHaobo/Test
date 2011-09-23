package modules.at.visual;

import java.awt.Color;
import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

public class ATTickChartBase extends ApplicationFrame {

	private static final long serialVersionUID = -9050631696899168131L;

	public ATTickChartBase(String s) {
		super(s);
		JFreeChart jfreechart = createChart();
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);// use mouse to zoom in/out
		chartpanel.setPreferredSize(new Dimension(1000, 540));

		setContentPane(chartpanel);
	}

	public static void main(String args[]) {
		ATTickChartBase timeseriesdemo3 = new ATTickChartBase("Time Series Demo 3");
		timeseriesdemo3.pack();
		RefineryUtilities.centerFrameOnScreen(timeseriesdemo3);
		timeseriesdemo3.setVisible(true);
	} 

	private static XYDataset createXYDataset() {
		XYSeries xyseries = new XYSeries("Tick");
		xyseries.add(1.0D, 181.80d);
		xyseries.add(2D, 167.30);
		xyseries.add(3D, 153.80);
		xyseries.add(4D, 167.59);
		xyseries.add(5D, 158.80);
		xyseries.add(6D, 148.30);
		xyseries.add(7D, 153.90);
		xyseries.add(8D, 142.69);
		xyseries.add(9D, 123.2);
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(xyseries);
		return xyseriescollection;
	}

	private static JFreeChart createChart() {
		XYDataset xydataset = createXYDataset();
		JFreeChart jfreechart = ChartFactory.createXYLineChart("Time Series Demo 3", "Time", "Value", xydataset, PlotOrientation.VERTICAL,false, true, false);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setDomainPannable(false);
		xyplot.setRangePannable(false);
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);

		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		numberaxis.setAutoRangeIncludesZero(false);

		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
		xylineandshaperenderer.setBaseShapesVisible(true);
		xylineandshaperenderer.setSeriesFillPaint(0, Color.white);
		xylineandshaperenderer.setUseFillPaint(true);
		xylineandshaperenderer.setLegendItemToolTipGenerator(new StandardXYSeriesLabelGenerator("Tooltip {0}"));

		//<< add one annotation begin
		XYPointerAnnotation xypointerannotation = new XYPointerAnnotation("Annotation 1 (2.0, 167.3)", 2D, 160, -0.78);
		xypointerannotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
		xypointerannotation.setPaint(Color.red);
		xypointerannotation.setArrowPaint(Color.red);
		xylineandshaperenderer.addAnnotation(xypointerannotation);
		// add one annotation end >>
		
		ChartUtilities.applyCurrentTheme(jfreechart);
		return jfreechart;
	}

}
