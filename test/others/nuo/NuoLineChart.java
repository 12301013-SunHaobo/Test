// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package others.nuo;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class NuoLineChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
    //default:(1800,900); (1500, 700),(2000, 933),
	private int width = 1800;//
	private int height = 900;//
	private JFreeChart chart = null;
	
	public NuoLineChart(String title, List<XYSeries> seriesList, boolean saveToFile) {
		super(title);
		JPanel jpanel = createDemoPanel(seriesList);
		jpanel.setPreferredSize(new Dimension(1800, 900));
		setContentPane(jpanel);
		
		if(!saveToFile){
			this.pack();
			RefineryUtilities.centerFrameOnScreen(this);
			this.setVisible(true);
		}
	}

	private JFreeChart createChart(XYDataset xydataset) {
		JFreeChart jfreechart = ChartFactory.createXYLineChart(super.getTitle(), "X", "Y", xydataset, PlotOrientation.VERTICAL, true,
				true, false);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setDomainPannable(true);
		xyplot.setRangePannable(true);
		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
		xylineandshaperenderer.setBaseShapesVisible(false);
		xylineandshaperenderer.setBaseShapesFilled(false);
		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		this.chart = jfreechart;
		return jfreechart;
	}

	public JPanel createDemoPanel(List<XYSeries> seriesList) {
		JFreeChart jfreechart = createChart(createDataset(seriesList));
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}

	private static XYDataset createDataset(List<XYSeries> seriesList) {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		for(XYSeries xySeries : seriesList) {
			xyseriescollection.addSeries(xySeries);
		}
		return xyseriescollection;
	}

	public void toFile(String fileName){
		if(this.chart!=null){
			try {
				ChartUtilities.saveChartAsPNG(new File(fileName), chart, this.width, this.height);//width-14, height-78
			} catch (IOException e) {
				System.err.println("Problem occurred creating chart to "+fileName);
			}
		}
	}
}
