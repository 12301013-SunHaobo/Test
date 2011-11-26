package modules.at.stg;

import java.util.List;

import modules.at.formula.Indicators;
import modules.at.model.visual.VMarker;

public class SampleStrategy implements Strategy {

	@Override
	public Decision getDecision() {
		
		return Decision.NA;
	}

	@Override
	public List<VMarker> getDecisionMarkerList() {
		
		return null;
	}

	@Override
	public void update(Indicators indicators) {
		
	}

}
