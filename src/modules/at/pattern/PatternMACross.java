package modules.at.pattern;

import java.util.ArrayList;
import java.util.List;

import modules.at.model.visual.VMarker;
import modules.at.stg.Indicator;
import modules.at.stg.Setting;

/**
 * Observes Indicators 
 *
 */
public class PatternMACross extends AbstractPattern{
	
    public static enum CrossType {
        FastCrossUp, FastCrossDown, NoCross
    }

    //diff = maFast - maSlow
    private double preDiff;
    private double curDiff;
    
    private Trend trend;
    
    private List<Double> maFastList;
    private List<Double> maSlowList;
    
    public PatternMACross(Setting as) {
        super(as);
        this.preDiff = Double.NaN;
        this.curDiff = Double.NaN;
        this.trend = Trend.NA;
        this.maFastList = new ArrayList<Double>();
        this.maSlowList = new ArrayList<Double>();
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

    public void update(Indicator o, Object arg) {
    	Indicator indicators = (Indicator)o;
        double maFast = indicators.getSMAFast();
        double maSlow = indicators.getSMASlow();
        this.maFastList.add(maFast);
        this.maSlowList.add(maSlow);;
        preDiff = curDiff;
        curDiff = maFast - maSlow;
    	//System.out.println("preDiff:"+preDiff+", curDiff:"+curDiff);
        
    }

	@Override
	public Trend getTrend() {
		CrossType ct = getCrossType();
		switch (ct){
			case FastCrossUp : this.trend = Trend.Up; break;
			case FastCrossDown : this.trend = Trend.Down; break;
			case NoCross : this.trend = Trend.NA; break;
		}
		return this.trend;
	}

	@Override
	public int getWeight() {
		return this.as.getPatternWeightMA();
	}

	@Override
	public List<VMarker> getPatternMarkerList() {
		// TODO Auto-generated method stub
		return null;
	}


    
}
