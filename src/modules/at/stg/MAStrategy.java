package modules.at.stg;

import java.util.ArrayList;
import java.util.List;

import modules.at.formula.Indicators;
import modules.at.model.Bar;
import modules.at.model.visual.VMarker;
import modules.at.model.visual.VXY;
import modules.at.model.visual.VXYsMarker;
import modules.at.pattern.Pattern;

public class MAStrategy implements Strategy {

	private List<VMarker> decisionMarkerList = new ArrayList<VMarker>();
	
	//ma1->ma2->ma3 form a turning point
    private double ma1 = Double.NaN;
    private double ma2 = Double.NaN;
    
    private Bar bar1 = null;
    private Bar bar2 = null;
    
    private Decision decision;
    
	@Override
	public Decision getDecision() {
		//return this.decision;
		return Decision.NA;
	}

	@Override
	public List<VMarker> getDecisionMarkerList() {
		return this.decisionMarkerList;
	}

	@Override
	public void update(Indicators indicators) {
        double curMA = indicators.getSMALow();
		Bar curBar = indicators.getCurBar();
        //make decision
		TurningType maTrend = getMATrendType(this.ma1, this.ma2, curMA); 
			//getMATrendType(this.ma1, this.ma2, curMA);
		switch (maTrend){
			case Up : this.decision = Decision.LongEntry; break;
			case Down : this.decision = Decision.LongExit; break;
			case NA : this.decision = Decision.NA; break;
		}

		if(!TurningType.NA.equals(maTrend)){
			VXYsMarker m = new VXYsMarker();
			if(Decision.LongEntry.equals(this.decision) && this.ma2 < indicators.getSMALow2()){
				m.setTrend(Pattern.Trend.Up);
			} else if(Decision.LongExit.equals(this.decision)){
				//m.setTrend(Pattern.Trend.Down);//TODO: to find LongExit
			}
			
			if(!Pattern.Trend.NA.equals(m.getTrend())){
				m.addVxy(new VXY(this.bar2.getDate().getTime(), this.ma2)); 
				this.decisionMarkerList.add(m);
			}
		}

		//
		this.ma1 = this.ma2;
		this.ma2 = curMA;
		this.bar1 = this.bar2;
		this.bar2 = curBar;
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
    	
    	if(ma1>=ma2 && ma2<curMA){
    		return TurningType.Up;
    	} else if(ma1<=ma2 && ma2>curMA){
    		return TurningType.Down;
    	}
    	return TurningType.NA;
    }
}
