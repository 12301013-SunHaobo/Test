package modules.at.stg;

import java.util.ArrayList;
import java.util.List;

import modules.at.formula.Indicators;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.FixedLengthQueue;
import modules.at.model.visual.VMarker;
import modules.at.model.visual.VSeries;
import modules.at.model.visual.VXY;
import modules.at.model.visual.VXYsMarker;
import modules.at.pattern.Pattern;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.regression.SimpleRegression;

public class StrategyMA implements Strategy {

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
				if(false
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
    	
    	//Low-Low2 diff
    	public double maxSMALow2Diff = Double.MIN_VALUE;//max SMA Low-Low2 diff
    	public double minSMALow2Diff = Double.MAX_VALUE;//min SMA Low-Low2 diff

    	//calculate MA50 slope
    	public double slopeMASlow = 0; //slope of MA50;
    	public int MA_SLOPE_LENGTH = 5;
    	private FixedLengthQueue<MAPoint> fixedLengthQ = new FixedLengthQueue<MAPoint>(MA_SLOPE_LENGTH); //
    	
    	
    	public IndicatorsMA() {
    		super();
    		this.ds4MAHL = new DescriptiveStatistics(AlgoSetting.maHLLength);
    		this.ds4MAHigh2 = new DescriptiveStatistics(AlgoSetting.maHigh2Length);
    		this.ds4MAHigh = new DescriptiveStatistics(AlgoSetting.maHighLength);
    		this.ds4MALow = new DescriptiveStatistics(AlgoSetting.maLowLength);
    		this.ds4MALow2 = new DescriptiveStatistics(AlgoSetting.maLow2Length);
    	}
    	
    	public void addBar(Bar bar){
    		super.addBar(bar);
    		this.ds4MAHL.addValue((bar.getHigh()+bar.getLow())/2);
    		this.ds4MAHigh2.addValue((3*bar.getHigh()-bar.getLow())/2);
    		this.ds4MAHigh.addValue(bar.getHigh());
    		this.ds4MALow.addValue(bar.getLow());
    		this.ds4MALow2.addValue((3*bar.getLow()-bar.getHigh())/2);
    		
    		double tmpSMALow2 = getSMALow2();
    		if(!Double.isNaN(tmpSMALow2)){
    			this.fixedLengthQ.add(new MAPoint(bar.getDate().getTime(), tmpSMALow2));
    		}
    		
    		getSMALow2Diff();//testing
    	}
    	
    	public double getSMAHigh2(){
    		if(this.barAdded<AlgoSetting.maHigh2Length){
    			return Double.NaN;
    		}
    		return ds4MAHigh2.getSum()/AlgoSetting.maHigh2Length;
    	}
    	public double getSMAHigh(){
    		if(this.barAdded<AlgoSetting.maHighLength){
    			return Double.NaN;
    		}
    		return ds4MAHigh.getSum()/AlgoSetting.maHighLength;
    	}
    	public double getSMAHL(){
    		if(this.barAdded<AlgoSetting.maHLLength){
    			return Double.NaN;
    		}
    		return ds4MAHL.getSum()/AlgoSetting.maHLLength;
    	}
    	public double getSMALow(){
    		if(this.barAdded<AlgoSetting.maLowLength){
    			return Double.NaN;
    		}
    		return ds4MALow.getSum()/AlgoSetting.maLowLength;
    	}
    	public double getSMALow2(){
    		if(this.barAdded<AlgoSetting.maLow2Length){
    			return Double.NaN;
    		}
    		return ds4MALow2.getSum()/AlgoSetting.maLow2Length;
    	}
    	public double getSMALow2Diff(){
    		double low = getSMALow();
    		double low2 = getSMALow2();
    		if(Double.isNaN(low) || Double.isNaN(low2)){
    			return Double.NaN;
    		}
    		double result = low-low2;
    		this.maxSMALow2Diff = Math.max(result, this.maxSMALow2Diff);
    		this.minSMALow2Diff = Math.min(result, this.minSMALow2Diff);
    		return result;
    	}
    	public double getSMALow2Slope(){
    		if(this.fixedLengthQ.size()<MA_SLOPE_LENGTH){
    			return Double.NaN;
    		}
    		SimpleRegression r = new SimpleRegression();
    		for(int i=0;i<this.fixedLengthQ.size();i++){
    			MAPoint tmpMAPoint = (MAPoint)this.fixedLengthQ.get(i);
    			r.addData(tmpMAPoint.getTime(), tmpMAPoint.getMa());
    		}
    		double fullWidth = (5*60+30)*60*1000D;
    		double fullHeight = 0.55D;
    		
    		return r.getSlope()*(fullWidth/fullHeight); 
    	}
    	

    	/**
    	 *Utility for indicator VXY lists
    	 */
    	public enum SeriesType {
    		MAHigh2, MAHigh, MAHL, MALow, MALow2, MALow2Diff, 
    		MALow2Slope//lagging, worse than MALow2Diff
    	}
    	
    	public List<VXY> getVXYList(SeriesType seriesType, List<Bar> barList){

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
    				case MALow2Diff: indicatorVal =  indicator.getSMALow2Diff(); break;
    				case MALow2Slope: indicatorVal =  indicator.getSMALow2Slope(); break;
    				default:break;
    			}

    			if(!Double.isNaN(indicatorVal)){
    				vxyList.add(new VXY(bar.getDate().getTime(), indicatorVal));
    			}
    		}
    		return vxyList;
    	}	    
    	//PlotBar
    	@Override
    	public List<VSeries> getPlotBarVSeriesList(List<Bar> barList){
    		List<VSeries> vseriesList = new ArrayList<VSeries>();
    		//vseriesList.addAll(super.getPlotBarVSeriesList(barList));
    		vseriesList.add(new VSeries("MAHigh2("+AlgoSetting.maHigh2Length+")", getVXYList(SeriesType.MAHigh2, barList), null, java.awt.Color.blue));
    		vseriesList.add(new VSeries("MAHigh("+AlgoSetting.maHighLength+")", getVXYList(SeriesType.MAHigh, barList), null, java.awt.Color.red));
    		vseriesList.add(new VSeries("MAHL("+AlgoSetting.maHLLength+")", getVXYList(SeriesType.MAHL, barList), null, java.awt.Color.cyan));
    		vseriesList.add(new VSeries("MALow("+AlgoSetting.maLowLength+")", getVXYList(SeriesType.MALow, barList), null, java.awt.Color.red));
    		vseriesList.add(new VSeries("MALow2("+AlgoSetting.maLow2Length+")", getVXYList(SeriesType.MALow2, barList), null, java.awt.Color.blue));
    		return vseriesList;
    	}
    	//Plots
    	@Override
    	public List<List<VSeries>> getPlotsVSeriesLists(List<Bar> barList){
    		List<List<VSeries>> vSeriesLists = new ArrayList<List<VSeries>>();
    		
    		//plot 1
    		List<VSeries> vseriesList = new ArrayList<VSeries>();
    		//vseriesList.addAll(super.getPlot1VSeriesList(barList));
    		//vseriesList.add(new VSeries("MADiff(High2-HL)", getVXYList(SeriesType.MAHigh2Diff, barList), null, java.awt.Color.gray));
    		vseriesList.add(new VSeries("MALow2Diff", getVXYList(SeriesType.MALow2Diff, barList), null, java.awt.Color.black));
    		//vseriesList.add(new VSeries("MALow2Slope", getVXYList(SeriesType.MALow2Slope, barList), null, java.awt.Color.gray));
    		vSeriesLists.add(vseriesList);
    		
    		/*
    		//plot 2
    		List<VSeries> vseriesList2 = new ArrayList<VSeries>();
    		vseriesList2.add(new VSeries("MALow2Slope", getVXYList(SeriesType.MALow2Slope, barList), null, java.awt.Color.gray));
    		vSeriesLists.add(vseriesList2);
    		*/
    		
    		return vSeriesLists;
    	}
    
    }


	@Override
	public Indicators getIndicators() {
		return this.indicators;
	}
    
    private static class MAPoint {
    	private double time;
    	private double ma;
		public MAPoint(double time, double ma) {
			super();
			this.time = time;
			this.ma = ma;
		}
		public double getMa() {
			return ma;
		}
		public void setMa(double ma) {
			this.ma = ma;
		}
		public double getTime() {
			return time;
		}
		public void setTime(double time) {
			this.time = time;
		}
    }

}
