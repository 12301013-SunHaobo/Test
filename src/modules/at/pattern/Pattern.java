package modules.at.pattern;

import java.util.List;
import java.util.Observer;

import modules.at.model.visual.BarsMarker;

public interface Pattern extends Observer{
	
	enum Trend {
		Up, Down, NA
	}
	
	public Trend getTrend(); 
	public int getWeightedTrend();
	public int getWeight();
	public List<BarsMarker> getPatternMarkerList();
}
