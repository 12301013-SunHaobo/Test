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
		CutLossForLong, CutLossForShort,
		NA
	}
	
	public void update(Bar bar);
	public Decision getPreBarDecision(); 
	public void setPreBarDecision(Decision preBarDecision);
	public List<VMarker> getDecisionMarkerList();
	public Indicators getIndicators();
}
