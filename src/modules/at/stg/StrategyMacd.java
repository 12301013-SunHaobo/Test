package modules.at.stg;

import java.util.ArrayList;
import java.util.List;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.visual.VMarker;
import modules.at.model.visual.VSeries;
import modules.at.model.visual.VXY;
import modules.at.model.visual.VXYsMarker;
import modules.at.pattern.Pattern;
import modules.at.stg.Strategy.Decision;
import modules.at.stg.StrategyMACross.CrossType;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * To find reversal bar
 * Not finished yet, just started.
 *
 */
public class StrategyMacd implements Strategy {

	private List<VMarker> decisionMarkerList = new ArrayList<VMarker>();
	
    private Bar preBar = null;
    double preMacd = Double.NaN;
    
    private Decision decision;
    
    private IndicatorsMacd indicators = new IndicatorsMacd(); 
    
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
	public void update(Bar bar) {
		this.indicators.addBar(bar);
		this.decision = Decision.NA;//reset decision 
        double curMacd = indicators.getMacd();
		Bar curBar = indicators.getCurBar();
        //make decision
		CrossType crossType = getCrossType(curMacd);
		switch (crossType){
			case CrossUp : 
				if(1==1
						//this.maLow2 < indicators.getSMALow2()
						//&& curBar.getClose() < indicators.getSMAHL() 
						){
					VXYsMarker m = new VXYsMarker();
					m.setTrend(Pattern.Trend.Up);
					//m.addVxy(new VXY(this.preBar.getDate().getTime(), this.maLow2));//turning point
					m.addVxy(new VXY(curBar.getDate().getTime(), curBar.getClose()));//curBar close
					this.decisionMarkerList.add(m);
					this.decision = Decision.LongEntry; 
				}
				break;
			case CrossDown : 
				if(1==1){
					VXYsMarker m = new VXYsMarker();
					m.setTrend(Pattern.Trend.Down);
					//m.addVxy(new VXY(this.preBar.getDate().getTime(), this.maHigh2));//turning point 
					m.addVxy(new VXY(curBar.getDate().getTime(), curBar.getClose()));//curBar close
					this.decisionMarkerList.add(m);
					//this.decision = Decision.LongExit; 
				}
				break;
		}

		//
		this.preBar = curBar;
		this.preMacd = curMacd;
	}

    public static enum CrossType {
        CrossUp, CrossDown, NoCross
    }
    public CrossType getCrossType(double curMacd){
    	if(Double.isNaN(curMacd) || Double.isNaN(this.preMacd)){
    		return CrossType.NoCross;
    	}
        if(preMacd>0 && curMacd<=0){
            return CrossType.CrossDown;
        } else if(preMacd<0 && curMacd>=0){
            return CrossType.CrossUp;
        } else {
            return CrossType.NoCross;
        }
    }  
    
    public static class IndicatorsMacd extends Indicators{

    	public IndicatorsMacd() {
    		super();
    	}
    	
    	@Override
    	public void addBar(Bar bar){
    		super.addBar(bar);
    	}

    	//Plot0
    	@Override
    	public List<VSeries> getPlotBarVSeriesList(List<Bar> barList){
    		List<VSeries> vseriesList = new ArrayList<VSeries>();
    		return vseriesList;
    	}
    	//Plot1
    	@Override
    	public List<List<VSeries>> getPlotsVSeriesLists(List<Bar> barList){
    		List<List<VSeries>> vSeriesLists = new ArrayList<List<VSeries>>();
    		
    		vSeriesLists.addAll(super.getPlotsVSeriesLists(barList));
    		
    		return vSeriesLists;
    	}
    
    }


	@Override
	public Indicators getIndicators() {
		return this.indicators;
	}
    

}
