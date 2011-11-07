package modules.at.model.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import modules.at.pattern.Pattern.Trend;
import modules.at.visual.BarChartUtil;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYPolygonAnnotation;

public class VMarker {
	// the pattern trend at this marker
	private Trend trend = Trend.NA;

	//{time1, price1, time2, price2,...}
	private List<Double> pointsList = new ArrayList<Double>();
	
	private double[] vertexes = null;
	
	public XYAnnotation toAnno() {

		double[] vertexes = getVertexes();
		if (vertexes!=null && vertexes.length >= 3) {//at least 3 vertexes to form a polygon
			Color color = Color.green;
			if (Trend.Up.equals(this.trend)) {
				color = Color.black;
			} else if (Trend.Down.equals(this.trend)) {
				color = Color.red;
			}
			return new XYPolygonAnnotation(vertexes, BarChartUtil.MARKER_STROKE, color, null);
		}
		return null;

	}

	// to be overridden by subclasses [time1, price1, time2, price2, ...]
	protected double[] getVertexes() {
		if(vertexes!=null){
			return vertexes;
		}
		if(pointsList.size()>0){
			vertexes = new double[pointsList.size()];
			for(int i=0;i<pointsList.size();i++){
				vertexes[i] = pointsList.get(i);
			}
		}
		return vertexes;
	}
	
	public Trend getTrend() {
		return trend;
	}

	public void setTrend(Trend trend) {
		this.trend = trend;
	}

	

}
