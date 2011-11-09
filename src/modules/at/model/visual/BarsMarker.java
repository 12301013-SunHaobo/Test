package modules.at.model.visual;

import java.util.ArrayList;
import java.util.List;

import modules.at.model.Bar;

public class BarsMarker extends VMarker{

	private List<Bar> barList = new ArrayList<Bar>();

	@Override
	protected double[] getVertexes() {
		if (barList.size() > 0) {
			Bar tmpBar = barList.get(0);
			double left = tmpBar.getDate().getTime()-50*1000;
			double top = tmpBar.getHigh();
			double bottom = tmpBar.getLow();
			for (Bar bar : barList) {
				top = Math.max(top, bar.getHigh());
				bottom = Math.min(bottom, bar.getLow());
			}
			top += 0.01;
			bottom -= 0.01;
			tmpBar = barList.get(barList.size()-1);
			double right = tmpBar.getDate().getTime()+50*1000;
			
			double[] vertexes = new double[]{
					left,top,
					right, top,
					right, bottom,
					left, bottom
			};
			return vertexes;
		}
		return null;
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
