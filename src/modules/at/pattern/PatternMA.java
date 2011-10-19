package modules.at.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import modules.at.formula.Indicators;

public class PatternMA implements Observer{
    
    public static enum CrossType {
        FastCrossUp, FastCrossDown, NoCross
    }

    private double preDiff;
    private double curDiff;
    
    private List<Double> maFastList;
    private List<Double> maSlowList;
    
    public PatternMA() {
        super();
        this.preDiff = Double.NaN;
        this.curDiff = Double.NaN;
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

    @Override
    public void update(Observable o, Object arg) {
        Indicators indicators = (Indicators)o;
        double maFast = indicators.getSMAFast();
        double maSlow = indicators.getSMASlow();
        this.maFastList.add(maFast);
        this.maSlowList.add(maSlow);;
        preDiff = curDiff;
        curDiff = maFast - maSlow;
    }
    
}
