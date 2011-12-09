package modules.at.visual;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import modules.at.model.visual.VChart;
import modules.at.model.visual.VPlot;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import utils.GlobalSetting;

public class ChartBase extends ApplicationFrame {

    private static final long serialVersionUID = 1L;

    //default:(1800,900); (1500, 700),(2000, 933),
	private int width = 1810;//
	private int height = 1000;//
	
    private JFreeChart chart = null;
    
    public ChartBase(VChart vChart){
    	this(vChart, true);
    }
    
    public ChartBase(VChart vChart, boolean visible) {
        super("Chart");
        JFreeChart jfreechart = createChart(vChart);
        this.chart = jfreechart;
        
        //jfreechart.setPadding(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setMouseWheelEnabled(true);
        
        //window (width,height)
        chartpanel.setPreferredSize(new Dimension(1810, 900));//(2200,900)
        
        JScrollPane scrollPane = new JScrollPane(chartpanel, 
        		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
        		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(width, height));
        setContentPane(scrollPane);
        // only display at home
        if (visible && GlobalSetting.isAtHome()) {
            this.pack();
            //RefineryUtilities.centerFrameOnScreen(this);//center on screen
            RefineryUtilities.positionFrameOnScreen(this, 0.01, 0.01);//a little top and left
            this.setVisible(true);
        }

        
    }

    private JFreeChart createChart(VChart vChart) {
    	DateAxis timeAxis = new DateAxis("Date-Time : "+vChart.getTitle());
        timeAxis.setLowerMargin(0.0);
        timeAxis.setUpperMargin(0.0);
        CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(timeAxis);
        combineddomainxyplot.setDomainPannable(true);

        // loop through chartData plot list
        List<VPlot> plotList = vChart.getPlotList();
        for (VPlot vPlot : plotList) {
            combineddomainxyplot.add(vPlot.toXYPlot(), vPlot.getWeight());
        }
        JFreeChart jfreechart = new JFreeChart(vChart.getTitle(), JFreeChart.DEFAULT_TITLE_FONT, combineddomainxyplot,true);
        //jfreechart.setBackgroundPaint(Color.white);
        //ChartUtilities.applyCurrentTheme(jfreechart); //gray background
        return jfreechart;
    }

    
    public void saveToFile(String fileName){
    	if(this.chart==null){
    		System.err.println("chart is null, not saved to " + fileName);
    		return;
    	}
		try {
			ChartUtilities.saveChartAsPNG(new File(fileName), chart, this.width, this.height);//width-14, height-78
		} catch (IOException e) {
			System.err.println("Problem occurred creating chart to "+fileName);
		}
    }
}
