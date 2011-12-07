package modules.at.stg;

import java.util.ArrayList;
import java.util.List;

import modules.at.formula.Indicators;
import modules.at.model.Bar;
import modules.at.model.visual.VMarker;
import modules.at.model.visual.VXY;
import modules.at.model.visual.VXYsMarker;
import modules.at.pattern.Pattern;
import modules.at.pattern.Pattern.Trend;

public class MAStrategy implements Strategy {

	private List<VMarker> decisionMarkerList = new ArrayList<VMarker>();
	
	//ma1->ma2->ma3 form a turning point
    private double maLow1 = Double.NaN;
    private double maLow2 = Double.NaN;
    
    private double maHigh1 = Double.NaN;
    private double maHigh2 = Double.NaN;

    private Bar preBar = null;
    
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
        double curMAHigh = indicators.getSMAHigh();
        double curMALow = indicators.getSMALow();
		Bar curBar = indicators.getCurBar();
        //make decision
		TurningType maHighTrend = getMAHighTrendType(this.maHigh1, this.maHigh2, curMAHigh);
		TurningType maLowTrend = getMALowTrendType(this.maLow1, this.maLow2, curMALow); 
		
		switch (maLowTrend){
			case Up : this.decision = Decision.LongEntry; break;
			case Down : this.decision = Decision.LongExit; break;
			case NA : this.decision = Decision.NA; break;
		}

		if(!TurningType.NA.equals(maLowTrend) || !TurningType.NA.equals(maHighTrend)){
			
			if(TurningType.Down.equals(maHighTrend) && this.maHigh2 > indicators.getSMAHigh2()){
				VXYsMarker m = new VXYsMarker();
				m.setTrend(Pattern.Trend.Down);
				//m.addVxy(new VXY(this.preBar.getDate().getTime(), this.maHigh2));//turning point 
				m.addVxy(new VXY(curBar.getDate().getTime(), curBar.getClose()));//curBar close
				//this.decisionMarkerList.add(m);
			}
			if(TurningType.Up.equals(maLowTrend) && this.maLow2 < indicators.getSMALow2()){
				VXYsMarker m = new VXYsMarker();
				m.setTrend(Pattern.Trend.Up);
				//m.addVxy(new VXY(this.preBar.getDate().getTime(), this.maLow2));//turning point
				m.addVxy(new VXY(curBar.getDate().getTime(), curBar.getClose()));//curBar close
				this.decisionMarkerList.add(m);
			}
		}

		//
		this.maHigh1 = this.maHigh2;
		this.maHigh2 = curMAHigh;
		this.maLow1 = this.maLow2;
		this.maLow2 = curMALow;
		this.preBar = curBar;
	}

    public static enum TurningType {
        Up, Down, NA
    }
    public TurningType getMAHighTrendType(double maHigh1, double maHigh2, double curMAHigh){
    	if(maHigh1==Double.NaN){
    		this.maHigh1 = curMAHigh;
    		return TurningType.NA;
    	}
    	if(maHigh2==Double.NaN){
    		this.maHigh2 = curMAHigh;
    		return TurningType.NA;
    	}
    	
    	if(maHigh2==curMAHigh){
    		return TurningType.NA;
    	} 
    	
    	if(maHigh1>=maHigh2 && maHigh2<curMAHigh){
    		return TurningType.Up;
    	} else if(maHigh1<=maHigh2 && maHigh2>curMAHigh){
    		return TurningType.Down;
    	}
    	return TurningType.NA;
    }    
    public TurningType getMALowTrendType(double maLow1, double maLow2, double curMALow){
    	if(maLow1==Double.NaN){
    		this.maLow1 = curMALow;
    		return TurningType.NA;
    	}
    	if(maLow2==Double.NaN){
    		this.maLow2 = curMALow;
    		return TurningType.NA;
    	}
    	
    	if(maLow2==curMALow){
    		return TurningType.NA;
    	} 
    	
    	if(maLow1>=maLow2 && maLow2<curMALow){
    		return TurningType.Up;
    	} else if(maLow1<=maLow2 && maLow2>curMALow){
    		return TurningType.Down;
    	}
    	return TurningType.NA;
    }
}
