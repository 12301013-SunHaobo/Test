package modules.at.pattern;

import java.util.List;
import java.util.Observable;

import modules.at.model.visual.VMarker;
import modules.at.stg.Indicator;
import modules.at.stg.Setting;

/**
 * Observes Indicators 
 *
 */
public class PatternSto extends AbstractPattern{
	
    public static enum CrossType {
        UpperCrossUp, UpperCrossDown, 
        LowerCrossUp, LowerCrossDown,
        NoCross
    }

    //diff = maFast - maSlow
    private double preK;
    private double curK;
    
    private Trend trend;
    
    public PatternSto(Setting as) {
        super(as);
        this.preK = Double.NaN;
        this.curK = Double.NaN;
        this.trend = Trend.NA;
    }
    
    public CrossType getCrossType(){
        if(preK<this.as.getStochasticUpper() && curK> this.as.getStochasticUpper()){
            return CrossType.UpperCrossUp;
        } else if(preK>this.as.getStochasticUpper() && curK< this.as.getStochasticUpper()){
            return CrossType.UpperCrossDown;
        } else if(preK<this.as.getStochasticLower() && curK> this.as.getStochasticLower()){
            return CrossType.LowerCrossUp;
        } else if(preK>this.as.getStochasticLower() && curK< this.as.getStochasticLower()){
        	return CrossType.LowerCrossDown;
        } 
        return CrossType.NoCross;
    }

    @Override
    public void update(Observable o, Object arg) {
        Indicator indicators = (Indicator)o;
        preK = curK;
        curK = indicators.getStochasticK();
    	//System.out.println("preDiff:"+preDiff+", curDiff:"+curDiff);
        
    }

	@Override
	public Trend getTrend() {
		CrossType ct = getCrossType();
		switch (ct){
			case UpperCrossUp : this.trend = Trend.Up; break;
			case UpperCrossDown : this.trend = Trend.Down; break;
			case LowerCrossDown : this.trend = Trend.Down; break;
			case LowerCrossUp : this.trend = Trend.Up; break;
			case NoCross : this.trend = Trend.NA; break;
		}
		return this.trend;
	}

	@Override
	public int getWeight() {
		return this.as.getPatternWeightSTO();
	}

	@Override
	public List<VMarker> getPatternMarkerList() {
		// TODO Auto-generated method stub
		return null;
	}


    
}
