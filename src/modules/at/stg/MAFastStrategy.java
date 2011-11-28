package modules.at.stg;

import java.util.ArrayList;
import java.util.List;

import modules.at.formula.Indicators;
import modules.at.model.visual.VMarker;

public class MAFastStrategy implements Strategy {

	private List<VMarker> decisionMarkerList = new ArrayList<VMarker>();
	
	//ma1->ma2->ma3 form a turning point
    private double ma1 = Double.NaN;
    private double ma2 = Double.NaN;
    
    private Decision decision;
    
	@Override
	public Decision getDecision() {
		return this.decision;
		//return Decision.NA;
	}

	@Override
	public List<VMarker> getDecisionMarkerList() {
		return this.decisionMarkerList;
	}

	@Override
	public void update(Indicators indicators) {
        double curMA = indicators.getSMAFast();
        //make decision
		TurningType maTrend = getMATrendType(this.ma1, this.ma2, curMA);
		switch (maTrend){
			case Up : this.decision = Decision.LongEntry; break;
			case Down : this.decision = Decision.LongExit; break;
			case NA : this.decision = Decision.NA; break;
		}
		/*
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
		*/
		//
		this.ma1 = this.ma2;
		this.ma2 = curMA;
	}

    public static enum TurningType {
        Up, Down, NA
    }
    public TurningType getMATrendType(double ma1, double ma2, double curMA){
    	if(ma1==Double.NaN){
    		this.ma1 = curMA;
    		return TurningType.NA;
    	}
    	if(ma2==Double.NaN){
    		this.ma2 = curMA;
    		return TurningType.NA;
    	}
    	if(ma2==curMA){
    		return TurningType.NA;
    	} 
    	
    	if(ma1>ma2 && ma2<curMA){
    		return TurningType.Up;
    	} else if(ma1<ma2 && ma2>curMA){
    		return TurningType.Down;
    	}
    	return TurningType.NA;
    }
}
