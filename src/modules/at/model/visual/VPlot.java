package modules.at.model.visual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;

/**
 * 
 * A group of VSeries, e.g one panel
 *
 */
public class VPlot {
    
    private int weight = 1;//relative height in proportion in panel
    
    private List<VSeries> vseriesList = new ArrayList<VSeries>();
    private List<XYAnnotation> annotationList = new ArrayList<XYAnnotation>();
    
    public VPlot(int weight) {
		super();
		this.weight = weight;
	}

	public XYPlot toXYPlot(){
        XYPlot xyplot = new XYPlot();
        
        NumberAxis numberAxis = new NumberAxis("Value");
        numberAxis.setLowerMargin(0.02);
        numberAxis.setUpperMargin(0.02);
        numberAxis.setAutoRangeIncludesZero(false);
        xyplot.setRangeAxis(numberAxis);

        /** below is set by @link ChartBase.createChart(VChart vChart) for all plots
        ValueAxis timeAxis = new DateAxis("Time");
        timeAxis.setLowerMargin(0.0);
        timeAxis.setUpperMargin(0.0);
        xyplot.setDomainAxis(timeAxis);
        */

        for(int i=0;i<vseriesList.size();i++){
            VSeries vseries = vseriesList.get(i);
            xyplot.setDataset(i, vseries.toXYDataset());
            xyplot.setRenderer(i, vseries.getRenderer());
        }
        
        for(int i=0;i<annotationList.size();i++){
        	xyplot.addAnnotation(annotationList.get(i));
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
    
    public void addSeries(VSeries vseries){
    	this.vseriesList.add(vseries);
    }
    public void addAllSeries(List<VSeries> vseriesList){
    	this.vseriesList.addAll(vseriesList);
    }
    
    public void addAnnotation(XYAnnotation anno){
    	this.annotationList.add(anno);
    }
    
    public void addAnnotations(Collection<XYAnnotation> annos){
    	this.annotationList.addAll(annos);
    }
    
}
