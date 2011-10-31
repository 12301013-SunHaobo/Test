package modules.at.model.visual;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import modules.at.model.Bar;
import modules.at.visual.BarChartUtil;

import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * 
 * One series of VXY
 * 
 */
public class VSeries {

    private String legendTitle;
    private List<VXY> vxyList;
    private List<Bar> barList;
    private Color color;
    
    public VSeries(
    		String legendTitle, 
    		List<VXY> vxyList, 
    		List<Bar> barList, 
    		Color color) {
        super();
        this.legendTitle = legendTitle;
        this.vxyList = vxyList;
        this.barList = barList;
        this.color = color;
    }

    public XYDataset toXYDataset() {
        if(vxyList!=null && barList!=null){
            System.err.println("VSeries.VSeries(List<VXY> vxyList, List<Bar> barList) accept either vxyList or barList, not both.");
            return null;
        }
        if (this.barList != null) {
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
            return new DefaultHighLowDataset(this.legendTitle, dateArr, highArr, lowArr, openArr, closeArr, volumeArr);
        } else if (this.vxyList != null) {
            XYSeriesCollection xyseriescollection = new XYSeriesCollection();
            XYSeries series = new XYSeries(this.legendTitle);
            for(VXY vxy : this.vxyList){
                if(!Double.isNaN(vxy.getY())){
                    series.add(vxy.getX(), vxy.getY());
                }
            }
            xyseriescollection.addSeries(series);
            return xyseriescollection;
        }
        return null;
    }

    public XYItemRenderer getRenderer(){
    	XYItemRenderer renderer;
    	if(this.barList!=null){
    		CandlestickRenderer c = new CandlestickRenderer();
    		c.setUpPaint(Color.green);
    		c.setDownPaint(Color.red);
    		c.setUseOutlinePaint(true);
    		c.setSeriesOutlinePaint(0, Color.black);
    		c.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);
    		renderer = c; 
    	} else {
        	renderer = new StandardXYItemRenderer();
    	}
        renderer.setSeriesPaint(0, this.color);
        renderer.setSeriesStroke(0, BarChartUtil.BASIC_STOKE);
        return renderer;
    }
    
    // auto generated
    public List<VXY> getVxyList() {
        return vxyList;
    }

    public void setVxyList(List<VXY> vxyList) {
        this.vxyList = vxyList;
    }

    public List<Bar> getBarList() {
        return barList;
    }

    public void setBarList(List<Bar> barList) {
        this.barList = barList;
    }

}
