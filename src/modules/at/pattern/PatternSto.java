package modules.at.pattern;

import java.util.List;
import java.util.Observable;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.visual.VMarker;

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
    
    public PatternSto() {
        super();
        this.preK = Double.NaN;
        this.curK = Double.NaN;
        this.trend = Trend.NA;
    }
    
    public CrossType getCrossType(){
        if(preK<AlgoSetting.stochasticUpper && curK>AlgoSetting.stochasticUpper){
            return CrossType.UpperCrossUp;
        } else if(preK>AlgoSetting.stochasticUpper && curK<AlgoSetting.stochasticUpper){
            return CrossType.UpperCrossDown;
        } else if(preK<AlgoSetting.stochasticLower && curK>AlgoSetting.stochasticLower){
            return CrossType.LowerCrossUp;
        } else if(preK>AlgoSetting.stochasticLower && curK<AlgoSetting.stochasticLower){
        	return CrossType.LowerCrossDown;
        } 
        return CrossType.NoCross;
    }

    @Override
    public void update(Observable o, Object arg) {
        Indicators indicators = (Indicators)o;
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
		return AlgoSetting.patternWeightSTO;
	}

	@Override
	public List<VMarker> getPatternMarkerList() {
		// TODO Auto-generated method stub
		return null;
	}


    
}
