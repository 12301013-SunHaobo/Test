// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package demo;

import java.awt.Color;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.*;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.*;
import org.jfree.ui.*;

public class AnnotationDemo2 extends ApplicationFrame {

	public AnnotationDemo2(String s) {
		super(s);
		setContentPane(createDemoPanel());
	}

	private static XYDataset createDataset1() {
		XYSeries xyseries = new XYSeries("Random Data 1");
		xyseries.add(1.0D, 181.80000000000001D);
		xyseries.add(2D, 167.30000000000001D);
		xyseries.add(3D, 153.80000000000001D);
		xyseries.add(4D, 167.59999999999999D);
		xyseries.add(5D, 158.80000000000001D);
		xyseries.add(6D, 148.30000000000001D);
		xyseries.add(7D, 153.90000000000001D);
		xyseries.add(8D, 142.69999999999999D);
		xyseries.add(9D, 123.2D);
		xyseries.add(10D, 131.80000000000001D);
		xyseries.add(11D, 139.59999999999999D);
		xyseries.add(12D, 142.90000000000001D);
		xyseries.add(13D, 138.69999999999999D);
		xyseries.add(14D, 137.30000000000001D);
		xyseries.add(15D, 143.90000000000001D);
		xyseries.add(16D, 139.80000000000001D);
		xyseries.add(17D, 137D);
		xyseries.add(18D, 132.80000000000001D);
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(xyseries);
		return xyseriescollection;
	}

	private static JFreeChart createChart() {
		XYDataset xydataset = createDataset1();
		JFreeChart jfreechart = ChartFactory.createXYLineChart("Annotation Demo 2", "Date", "Price Per Unit", xydataset,
				PlotOrientation.VERTICAL, false, true, false);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setDomainPannable(true);
		xyplot.setRangePannable(true);
		
		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		numberaxis.setAutoRangeIncludesZero(false);
		
		NumberAxis numberaxis1 = new NumberAxis("Secondary");
		numberaxis1.setAutoRangeIncludesZero(false);
		xyplot.setRangeAxis(1, numberaxis1);
		
		xyplot.mapDatasetToRangeAxis(1, 1);
		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
		xylineandshaperenderer.setBaseToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
		xylineandshaperenderer.setBaseShapesVisible(true);
		xylineandshaperenderer.setBaseShapesFilled(true);
		XYPointerAnnotation xypointerannotation = new XYPointerAnnotation("Annotation 1 (2.0, 167.3)", 2D, 167.30000000000001D,
				-0.78539816339744828D);
		xypointerannotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
		xypointerannotation.setPaint(Color.red);
		xypointerannotation.setArrowPaint(Color.red);
		xylineandshaperenderer.addAnnotation(xypointerannotation);
		XYLineAndShapeRenderer xylineandshaperenderer1 = new XYLineAndShapeRenderer(true, true);
		xylineandshaperenderer1.setSeriesPaint(0, Color.black);
		xylineandshaperenderer.setBaseToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
		XYPointerAnnotation xypointerannotation1 = new XYPointerAnnotation("Annotation 2 (15.0, 613.2)", 15D, 613.20000000000005D,
				1.5707963267948966D);
		xypointerannotation1.setTextAnchor(TextAnchor.TOP_CENTER);
		xylineandshaperenderer1.addAnnotation(xypointerannotation1);
		xyplot.setRenderer(1, xylineandshaperenderer1);
		LegendTitle legendtitle = new LegendTitle(xylineandshaperenderer);
		LegendTitle legendtitle1 = new LegendTitle(xylineandshaperenderer1);
		BlockContainer blockcontainer = new BlockContainer(new BorderArrangement());
		blockcontainer.add(legendtitle, RectangleEdge.LEFT);
		blockcontainer.add(legendtitle1, RectangleEdge.RIGHT);
		blockcontainer.add(new EmptyBlock(2000D, 0.0D));
		CompositeTitle compositetitle = new CompositeTitle(blockcontainer);
		compositetitle.setFrame(new BlockBorder(Color.red));
		compositetitle.setBackgroundPaint(Color.yellow);
		compositetitle.setPosition(RectangleEdge.BOTTOM);
		jfreechart.addSubtitle(compositetitle);
		ChartUtilities.applyCurrentTheme(jfreechart);
		return jfreechart;
	}

	public static JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart();
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}

	public static void main(String args[]) {
		AnnotationDemo2 annotationdemo2 = new AnnotationDemo2("JFreeChart: AnnotationDemo2.java");
		annotationdemo2.pack();
		RefineryUtilities.centerFrameOnScreen(annotationdemo2);
		annotationdemo2.setVisible(true);
	}
}
