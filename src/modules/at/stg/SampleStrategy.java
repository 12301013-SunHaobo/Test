package modules.at.stg;

import java.util.ArrayList;
import java.util.List;

import modules.at.formula.Indicators;
import modules.at.model.visual.VMarker;
import modules.at.pattern.Pattern.Trend;
import modules.at.pattern.PatternMACross.CrossType;

public class SampleStrategy implements Strategy {

    public static enum CrossType {
        FastCrossUp, FastCrossDown, NoCross
    }
    
	private List<VMarker> decisionMarkerList = new ArrayList<VMarker>();
	
    //diff = maFast - maSlow
    private double preDiff;
    private double curDiff;
    
    private Decision decision;
    
	@Override
	public Decision getDecision() {
		CrossType ct = getCrossType();
		switch (ct){
			case FastCrossUp : this.decision = Decision.LongEntry; break;
			case FastCrossDown : this.decision = Decision.LongExit; break;
			case NoCross : this.decision = Decision.NA; break;
		}
		return this.decision;
	}

	@Override
	public List<VMarker> getDecisionMarkerList() {
		return this.decisionMarkerList;
	}

	@Override
	public void update(Indicators indicators) {
        double maFast = indicators.getSMAFast();
        double maSlow = indicators.getSMASlow();
        preDiff = curDiff;
        curDiff = maFast - maSlow;
	}

    public CrossType getCrossType(){
        if(preDiff>0 && curDiff<0){
            return CrossType.FastCrossDown;
        } else if(preDiff<0 && curDiff>0){
            return CrossType.FastCrossUp;
        } else {
            return CrossType.NoCross;
        }
    }
}
