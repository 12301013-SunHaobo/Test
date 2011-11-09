package modules.at.pattern;

import java.util.List;
import java.util.Observable;

import modules.at.model.visual.VMarker;

public class PatternS1 extends AbstractPattern {

	@Override
	public void update(Observable o, Object arg) {
		
	}

	
	@Override
	public Trend getTrend() {
		return null;
	}

	@Override
	public int getWeight() {
		return 1;
	}

	@Override
	public List<VMarker> getPatternMarkerList() {
		return null;
	}




}
