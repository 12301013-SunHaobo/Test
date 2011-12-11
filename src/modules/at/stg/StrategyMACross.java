package modules.at.stg;

import java.util.ArrayList;
import java.util.List;

import modules.at.formula.Indicators;
import modules.at.model.Bar;
import modules.at.model.visual.BarsMarker;
import modules.at.model.visual.VMarker;
import modules.at.pattern.Pattern.Trend;
import modules.at.stg.StrategyMA.IndicatorsMA;

public class StrategyMACross implements Strategy {

    public static enum CrossType {
        FastCrossUp, FastCrossDown, NoCross
    }
    
	private List<VMarker> decisionMarkerList = new ArrayList<VMarker>();
	
    //diff = maFast - maSlow
    private double preDiff;
    private double curDiff;
    
    private Decision decision;
    private Indicators indicators = new Indicators(); 
    private Bar preBar;//previous bar
    
	@Override
	public Decision getDecision() {
		return this.decision;
	}

	@Override
	public List<VMarker> getDecisionMarkerList() {
		return this.decisionMarkerList;
	}

	@Override
	public void update(Bar bar) {
		this.indicators.addBar(bar);
        double maFast = indicators.getSMAFast();
        double maSlow = indicators.getSMASlow();
        preDiff = curDiff;
        curDiff = maFast - maSlow;
        //make decision
		CrossType ct = getCrossType();
		switch (ct){
			case FastCrossUp : this.decision = Decision.LongEntry; break;
			case FastCrossDown : this.decision = Decision.LongExit; break;
			case NoCross : this.decision = Decision.NA; break;
		}
		//
		Bar curBar = indicators.getCurBar();
		if(this.preBar!=null){
			if(Decision.LongEntry.equals(this.decision)||Decision.LongExit.equals(this.decision)){
				BarsMarker pm = new BarsMarker();
				pm.addBar(this.preBar);
				pm.addBar(curBar);
				if(Decision.LongEntry.equals(this.decision)){
					pm.setTrend(Trend.Up);
				}else if(Decision.LongExit.equals(this.decision)){
					pm.setTrend(Trend.Down);
				}
				this.decisionMarkerList.add(pm);
			}
		}
		this.preBar = curBar;
	}

    public CrossType getCrossType(){
        if(preDiff>0 && curDiff<=0){
            return CrossType.FastCrossDown;
        } else if(preDiff<0 && curDiff>=0){
            return CrossType.FastCrossUp;
        } else {
            return CrossType.NoCross;
        }
    }
}
