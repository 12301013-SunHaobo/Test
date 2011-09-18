package modules.at.analyze;

import java.util.List;

import modules.at.model.Point;

public class HighLowResult {
	
	public static enum HLPattern {
		HighHighLowHigh, //up 
		HighLowLowLow, //down
	}
	
	private List<Point> highPointsList;
	private List<Point> lowPointsList;
	
	public HighLowResult(List<Point> highPointsList, List<Point> lowPointsList) {
		super();
		this.highPointsList = highPointsList;
		this.lowPointsList = lowPointsList;
	}
	
	public List<Point> getHighPointsList() {
		return highPointsList;
	}
	public void setHighPointsList(List<Point> highPointsList) {
		this.highPointsList = highPointsList;
	}
	public List<Point> getLowPointsList() {
		return lowPointsList;
	}
	public void setLowPointsList(List<Point> lowPointsList) {
		this.lowPointsList = lowPointsList;
	}
	

	
}
