package modules.at.stg.mabb;

import java.util.ArrayList;
import java.util.List;

import modules.at.formula.Indicator;
import modules.at.model.Setting;
import modules.at.model.Bar;
import modules.at.model.FixedLengthQueue;
import modules.at.model.Position;
import modules.at.model.visual.VMarker;
import modules.at.model.visual.VXY;
import modules.at.model.visual.VXYsMarker;
import modules.at.pattern.Pattern;
import modules.at.stg.Strategy;

public class StrategyMaBB implements Strategy {

	private List<VMarker> decisionMarkerList = new ArrayList<VMarker>();
	
	private int TOTAL_BARS_UNDER_LOW2BB = 3;
    private FixedLengthQueue<Bar> preBars = new FixedLengthQueue<Bar>(TOTAL_BARS_UNDER_LOW2BB);
    private FixedLengthQueue<Double> preMALow2BB = new FixedLengthQueue<Double>(TOTAL_BARS_UNDER_LOW2BB);
    
    private Decision preBarDecision;
    
    private IndicatorMaBB indicators = null; 
    private SettingMaBB setting = null;
    
    
	public StrategyMaBB(Setting as) {
		super();
		this.setting = (SettingMaBB)as;
		this.indicators = new IndicatorMaBB(as);
	}

	@Override
	public Decision getPreBarDecision() {
		return this.preBarDecision;
		//return Decision.NA;
	}
	@Override
	public void setPreBarDecision(Decision preBarDecision) {
		this.preBarDecision = preBarDecision;
	}

	@Override
	public List<VMarker> getDecisionMarkerList() {
		return this.decisionMarkerList;
	}

	@Override
	public void update(Bar bar) {
		this.indicators.addBar(bar);
		this.preBarDecision = Decision.NA;//reset decision 
		Bar curBar = indicators.getCurBar();
        //make decision
		//check if need to cutloss
		Position position = Position.getInstance(this.setting);
		Decision cutLossDecision = position.getCutLossDecision(curBar);
		
		if(Decision.CutLossForLong.equals(cutLossDecision) 
				||Decision.CutLossForShort.equals(cutLossDecision)) {
			this.preBarDecision = cutLossDecision;
			return;
		}
		if(isExit()){
			
		}
		
		//strategy decision
		if(isLowCrossUp()){
			VXYsMarker m = new VXYsMarker();
			m.setTrend(Pattern.Trend.Up);
			//m.addVxy(new VXY(this.preBar.getDate().getTime(), this.maLow2));//turning point
			m.addVxy(new VXY(curBar.getDate().getTime(), curBar.getClose()));//curBar close
			this.decisionMarkerList.add(m);
			this.preBarDecision = Decision.LongEntry; 
		}

		//prepare for later calculation
		this.preBars.add(curBar);
		this.preMALow2BB.add(this.indicators.getSMALow2BB());
	}

    public static enum CrossType {
        Up, Down, NA
    }
    
    /**
     * Condition:
     * previous 3 bar.getLow < preMALow2BB
     * and curBar.getLow > MALow2BB
     * and curBar.getClose < (MAHL+MALow2BB)/2
     */
    private boolean isLowCrossUp() {
    	//previous 3 bar.getLow < preMALow2BB
    	Bar curBar = this.indicators.getCurBar();
    	for(int i=0; i<TOTAL_BARS_UNDER_LOW2BB; i++){
    		Bar preBar = (Bar)this.preBars.get(i);
    		Double d = (Double)this.preMALow2BB.get(i);
    		if(d == null || Double.isNaN(d)){
    			return false;
    		}
    		if(preBar.getLow()>d){
    			return false;
    		}
    	}
    	//and curBar.getLow > MALow2BB
    	boolean lowCrossUp = curBar.getLow()>this.indicators.getSMALow2BB();
    	//and curBar.getClose < (MAHL+MALow2BB)/2
    	lowCrossUp = lowCrossUp && (curBar.getClose()<(this.indicators.getSMAHL()+this.indicators.getSMALow2BB())/2);
    	return lowCrossUp;
    }
   
    /**
     * 
     * 
     */
    private boolean isExit() {
    	//previous 3 bar.getLow < preMALow2BB
    	Bar curBar = this.indicators.getCurBar();
    	for(int i=0; i<TOTAL_BARS_UNDER_LOW2BB; i++){
    		Bar preBar = (Bar)this.preBars.get(i);
    		Double d = (Double)this.preMALow2BB.get(i);
    		if(d == null || Double.isNaN(d)){
    			return false;
    		}
    		if(preBar.getLow()>d){
    			return false;
    		}
    	}
    	//and curBar.getLow > MALow2BB
    	boolean lowCrossUp = curBar.getLow()>this.indicators.getSMALow2BB();
    	//and curBar.getClose < (MAHL+MALow2BB)/2
    	lowCrossUp = lowCrossUp && (curBar.getClose()<(this.indicators.getSMAHL()+this.indicators.getSMALow2BB())/2);
    	return lowCrossUp;
    }
    

	@Override
	public Indicator getIndicators() {
		return this.indicators;
	}

	@Override
	public SettingMaBB getSetting() {
		return this.setting;
	}
    


}
