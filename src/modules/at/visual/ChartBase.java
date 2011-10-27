package modules.at.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;
import java.util.List;

import modules.at.model.Bar;
import modules.at.model.visual.VChart;
import modules.at.model.visual.VPlot;
import modules.at.model.visual.VSeries;
import modules.at.model.visual.VXY;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import utils.GlobalSetting;

public class ChartBase extends ApplicationFrame {

    private static final long serialVersionUID = 1L;

    public ChartBase(VChart vChart) {
        super("Chart");
        JFreeChart jfreechart = createChart(vChart);
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setMouseWheelEnabled(true);
        
        //window (width,height)
        chartpanel.setPreferredSize(new Dimension(1500, 700));
        setContentPane(chartpanel);
        // only display at home
        if (GlobalSetting.isAtHome()) {
            this.pack();
            RefineryUtilities.centerFrameOnScreen(this);
            this.setVisible(true);
        }
    }

    private JFreeChart createChart(VChart vChart) {
        CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(new DateAxis("Date/Time"));
        combineddomainxyplot.setDomainPannable(true);

        // loop through chartData plot list
        List<VPlot> plotList = vChart.getPlotList();
        for (VPlot vPlot : plotList) {
            combineddomainxyplot.add(vPlot.toXYPlot(), vPlot.getWeight());
        }
        JFreeChart jfreechart = new JFreeChart(vChart.getTitle(), JFreeChart.DEFAULT_TITLE_FONT, combineddomainxyplot,true);
        // jfreechart.setBackgroundPaint(Color.white);
        // ChartUtilities.applyCurrentTheme(jfreechart); //gray background
        return jfreechart;
    }



    
    
    
    private XYSeries vxyList2XYSeries(List<VXY> vxyList) {
        XYSeries xySeries = new XYSeries("series1");
        for(VXY vxy : vxyList){
            xySeries.add(vxy.getX(), vxy.getY());
        }
        return xySeries;
        
    }
    
    private XYDataset barList2Dataset(List<Bar> barList) {
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
    }
}
