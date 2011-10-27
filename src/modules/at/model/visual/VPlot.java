package modules.at.model.visual;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

/**
 * 
 * A group of VSeries, e.g one panel
 *
 */
public class VPlot {
    
    private int weight;//relative height in proportion in panel
    
    private List<VSeries> vseriesList = new ArrayList<VSeries>();

    
    public XYPlot toXYPlot(){
        XYPlot xyplot = new XYPlot();
        ValueAxis timeAxis = new DateAxis("Time");
        NumberAxis valueAxis = new NumberAxis("Value");
        xyplot.setDomainAxis(timeAxis);
        xyplot.setRangeAxis(valueAxis);

        for(int i=0;i<vseriesList.size();i++){
            VSeries vseries = vseriesList.get(i);
            xyplot.setDataset(i, vseries.toXYDataset());
            xyplot.setRenderer(i, vseries.getRenderer());
        }
        return xyplot;
    }
    
    
	public List<VSeries> getVseriesList() {
		return vseriesList;
	}

	public void setVseriesList(List<VSeries> vseriesList) {
		this.vseriesList = vseriesList;
	}

	public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
