package modules.at.stg;

import java.util.List;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.visual.VMarker;

public interface Strategy {
	
	public enum Decision {
		LongEntry, LongExit,
		ShortEntry, ShortExit,
		NA
	}
	
	public void update(Bar bar);
	public Decision getDecision(); 
	public List<VMarker> getDecisionMarkerList();
	public Indicators getIndicators();
}
