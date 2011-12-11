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

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class MAStrategy implements Strategy {

	private List<VMarker> decisionMarkerList = new ArrayList<VMarker>();
	
	//ma1->ma2->ma3 form a turning point
    private double maLow1 = Double.NaN;
    private double maLow2 = Double.NaN;
    
    private double maHigh1 = Double.NaN;
    private double maHigh2 = Double.NaN;

    private Bar preBar = null;
    
    private Decision decision;
    
    private IndicatorsMA indicators = new IndicatorsMA(); 
    
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
        double curMAHigh = indicators.getSMAHigh();
        double curMALow = indicators.getSMALow();
		Bar curBar = indicators.getCurBar();
        //make decision
		TurningType maHighTrend = getMAHighTrendType(this.maHigh1, this.maHigh2, curMAHigh);
		TurningType maLowTrend = getMALowTrendType(this.maLow1, this.maLow2, curMALow); 
		
		switch (maLowTrend){
			case Up : 
				if(this.maLow2 < indicators.getSMALow2()
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
			case Down : 
				if(this.maHigh2 > indicators.getSMAHigh2()){
					VXYsMarker m = new VXYsMarker();
					m.setTrend(Pattern.Trend.Down);
					//m.addVxy(new VXY(this.preBar.getDate().getTime(), this.maHigh2));//turning point 
					m.addVxy(new VXY(curBar.getDate().getTime(), curBar.getClose()));//curBar close
					//this.decisionMarkerList.add(m);
					//this.decision = Decision.LongExit; 
				}
				break;
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
    
    
    public static class IndicatorsMA extends Indicators{
    	private DescriptiveStatistics ds4MAHL;//for MA (H+L)/2
    	private DescriptiveStatistics ds4MAHigh2;//for (H+L)/2 + 2(H-(H+L)/2) =(3H-L)/2
    	private DescriptiveStatistics ds4MAHigh;//for MA of High
    	private DescriptiveStatistics ds4MALow;//for MA of Low
    	private DescriptiveStatistics ds4MALow2;//for (H+L)/2 - 2((H+L)/2-L)=(3L-H)/2

    	public IndicatorsMA() {
    		super();
    		this.ds4MAHL = new DescriptiveStatistics(AlgoSetting.MA_HL_LENGTH);
    		this.ds4MAHigh2 = new DescriptiveStatistics(AlgoSetting.MA_HIGH2_LENGTH);
    		this.ds4MAHigh = new DescriptiveStatistics(AlgoSetting.MA_HIGH_LENGTH);
    		this.ds4MALow = new DescriptiveStatistics(AlgoSetting.MA_LOW_LENGTH);
    		this.ds4MALow2 = new DescriptiveStatistics(AlgoSetting.MA_LOW2_LENGTH);
    	}
    	
    	public void addBar(Bar bar){
    		super.addBar(bar);
    		this.ds4MAHL.addValue((bar.getHigh()+bar.getLow())/2);
    		this.ds4MAHigh2.addValue((3*bar.getHigh()-bar.getLow())/2);
    		this.ds4MAHigh.addValue(bar.getHigh());
    		this.ds4MALow.addValue(bar.getLow());
    		this.ds4MALow2.addValue((3*bar.getLow()-bar.getHigh())/2);
    	}
    	
    	public double getSMAHigh2(){
    		if(this.barAdded<AlgoSetting.MA_HIGH2_LENGTH){
    			return Double.NaN;
    		}
    		return ds4MAHigh2.getSum()/AlgoSetting.MA_HIGH2_LENGTH;
    	}
    	public double getSMAHigh(){
    		if(this.barAdded<AlgoSetting.MA_HIGH_LENGTH){
    			return Double.NaN;
    		}
    		return ds4MAHigh.getSum()/AlgoSetting.MA_HIGH_LENGTH;
    	}
    	public double getSMAHL(){
    		if(this.barAdded<AlgoSetting.MA_HL_LENGTH){
    			return Double.NaN;
    		}
    		return ds4MAHL.getSum()/AlgoSetting.MA_HL_LENGTH;
    	}
    	public double getSMALow(){
    		if(this.barAdded<AlgoSetting.MA_LOW_LENGTH){
    			return Double.NaN;
    		}
    		return ds4MALow.getSum()/AlgoSetting.MA_LOW_LENGTH;
    	}
    	public double getSMALow2(){
    		if(this.barAdded<AlgoSetting.MA_LOW2_LENGTH){
    			return Double.NaN;
    		}
    		return ds4MALow2.getSum()/AlgoSetting.MA_LOW2_LENGTH;
    	}
    	public double getSMAHigh2Diff(){
    		double high2 = this.curBar.getHigh();
    		double hl = getSMAHL();
    		if(Double.isNaN(high2) || Double.isNaN(hl)){
    			return Double.NaN;
    		}
    		//return (high2-hl);
    		return (curBar.getHigh()-curBar.getLow())*((curBar.getClose()-curBar.getOpen())>0?1:-1);
    	}

    	/**
    	 *Utility for indicator VXY lists
    	 */
    	public enum SeriesType {
    		MAHigh2, MAHigh, MAHL, MALow, MALow2, MAHigh2Diff, MALow2Diff,
    	}
    	
    	public static List<VXY> getVXYList(SeriesType seriesType, List<Bar> barList){

    		List<VXY> vxyList = new ArrayList<VXY>();
    		IndicatorsMA indicator = new IndicatorsMA();
    		
    		for(Bar bar : barList){
    			indicator.addBar(bar);
    			double indicatorVal = Double.NaN;

    			switch (seriesType) {
    				case MAHigh2: indicatorVal =  indicator.getSMAHigh2(); break;
    				case MAHigh: indicatorVal =  indicator.getSMAHigh(); break;
    				case MAHL: indicatorVal =  indicator.getSMAHL(); break;
    				case MALow: indicatorVal =  indicator.getSMALow(); break;
    				case MALow2: indicatorVal =  indicator.getSMALow2(); break;
    				case MAHigh2Diff: indicatorVal =  indicator.getSMAHigh2Diff(); break;
    				default:break;
    			}

    			if(!Double.isNaN(indicatorVal)){
    				vxyList.add(new VXY(bar.getDate().getTime(), indicatorVal));
    			}
    		}
    		return vxyList;
    	}	    
    	//Plot0
    	public static List<VSeries> getPlotBarVSeriesList(List<Bar> barList){
    		List<VSeries> vseriesList = new ArrayList<VSeries>();
    		vseriesList.add(new VSeries("MAHigh2("+AlgoSetting.MA_HIGH2_LENGTH+")", MAStrategy.IndicatorsMA.getVXYList(MAStrategy.IndicatorsMA.SeriesType.MAHigh2, barList), null, java.awt.Color.blue));
    		vseriesList.add(new VSeries("MAHigh("+AlgoSetting.MA_HIGH_LENGTH+")", MAStrategy.IndicatorsMA.getVXYList(MAStrategy.IndicatorsMA.SeriesType.MAHigh, barList), null, java.awt.Color.red));
    		vseriesList.add(new VSeries("MAHL("+AlgoSetting.MA_HL_LENGTH+")", MAStrategy.IndicatorsMA.getVXYList(MAStrategy.IndicatorsMA.SeriesType.MAHL, barList), null, java.awt.Color.cyan));
    		vseriesList.add(new VSeries("MALow("+AlgoSetting.MA_LOW_LENGTH+")", MAStrategy.IndicatorsMA.getVXYList(MAStrategy.IndicatorsMA.SeriesType.MALow, barList), null, java.awt.Color.red));
    		vseriesList.add(new VSeries("MALow2("+AlgoSetting.MA_LOW2_LENGTH+")", MAStrategy.IndicatorsMA.getVXYList(MAStrategy.IndicatorsMA.SeriesType.MALow2, barList), null, java.awt.Color.blue));
    		return vseriesList;
    	}
    	//Plot1
    	public static List<VSeries> getPlot1VSeriesList(List<Bar> barList){
    		List<VSeries> vseriesList = new ArrayList<VSeries>();
    		vseriesList.add(new VSeries("MADiff(High2-HL)", MAStrategy.IndicatorsMA.getVXYList(MAStrategy.IndicatorsMA.SeriesType.MAHigh2Diff, barList), null, java.awt.Color.gray));
    		return vseriesList;
    	}
    
    }
    

}
