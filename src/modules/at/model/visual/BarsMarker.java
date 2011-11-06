package modules.at.model.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import modules.at.model.Bar;
import modules.at.pattern.Pattern.Trend;
import modules.at.visual.BarChartUtil;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYPolygonAnnotation;

public class PatternMarker {
	
	//the bars that formed this pattern
	Trend trend = Trend.NA;
	private List<Bar> barList = new ArrayList<Bar>();

	public XYAnnotation toAnno() {

		if (barList.size() > 0) {
			Bar tmpBar = barList.get(0);
			double left = tmpBar.getDate().getTime()-5*1000;
			double top = tmpBar.getHigh();
			double bottom = tmpBar.getLow();
			for (Bar bar : barList) {
				top = Math.max(top, bar.getHigh());
				bottom = Math.min(bottom, bar.getLow());
			}
			top += 0.01;
			bottom -= 0.01;
			tmpBar = barList.get(barList.size()-1);
			double right = tmpBar.getDate().getTime()+5*1000;
			
			double[] vertexes = new double[]{
					left,top,
					right, top,
					right, bottom,
					left, bottom
			};

			Color color = Color.green;
			if(Trend.Up.equals(this.trend)){
				color = Color.black;
			}else if(Trend.Down.equals(this.trend)){
				color = Color.red;
			}
			return new XYPolygonAnnotation(vertexes, BarChartUtil.MARKER_STROKE, color, null);
		}
		return null;
	}

	public Trend getTrend() {
		return trend;
	}

	public void setTrend(Trend trend) {
		this.trend = trend;
	}

	public List<Bar> getBarList() {
		return barList;
	}

	public void setBarList(List<Bar> barList) {
		this.barList = barList;
	}
	
	public void addBar(Bar bar){
		this.barList.add(bar);
	}
}
