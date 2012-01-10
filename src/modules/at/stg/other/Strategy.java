package modules.at.stg.other;

import java.util.List;

import modules.at.model.Bar;
import modules.at.model.visual.VMarker;
import modules.at.stg.Indicator;
import modules.at.stg.Setting;

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
	public Indicator getIndicators();
	public Setting getSetting();
	
}
