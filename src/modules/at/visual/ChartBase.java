package modules.at.visual;

import java.awt.Dimension;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import utils.GlobalSetting;

public class ChartBase extends ApplicationFrame {


	private static final long serialVersionUID = 1L;
	
	public ChartBase(ChartData chartData) {
		super("Chart");
		JFreeChart jfreechart = createChart(chartData);
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



	private JFreeChart createChart(ChartData chartData) {
		CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(new DateAxis("Date/Time"));
		combineddomainxyplot.setDomainPannable(true);
		
		//loop through chartData plot list
		List<XYPlot> plotList = chartData.getPlotList();
		List<Integer> plotWeightList = chartData.getPlotWeightList();
		for(int i=0;i<plotList.size();i++){
			combineddomainxyplot.add(plotList.get(i),plotWeightList.get(i));//weight is relative size of panel height
		}
		JFreeChart jfreechart = new JFreeChart(chartData.getChartTitle(), JFreeChart.DEFAULT_TITLE_FONT, combineddomainxyplot, true);
		//jfreechart.setBackgroundPaint(Color.white);
		//ChartUtilities.applyCurrentTheme(jfreechart); //gray background
		return jfreechart;
	}




}
