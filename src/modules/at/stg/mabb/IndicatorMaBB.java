package modules.at.stg.mabb;

import java.util.ArrayList;
import java.util.List;

import modules.at.formula.Indicator;
import modules.at.model.Setting;
import modules.at.model.Bar;
import modules.at.model.FixedLengthQueue;
import modules.at.model.visual.VSeries;
import modules.at.model.visual.VXY;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.regression.SimpleRegression;

public class IndicatorMaBB extends Indicator{
	private DescriptiveStatistics ds4MAHL;//for MA (H+L)/2
	private DescriptiveStatistics ds4MAHigh2;//for (H+L)/2 + 2(H-(H+L)/2) =(3H-L)/2
	private DescriptiveStatistics ds4MAHigh;//for MA of High
	private DescriptiveStatistics ds4MALow;//for MA of Low
	private DescriptiveStatistics ds4MALow2;//for (H+L)/2 - 2((H+L)/2-L)=(3L-H)/2
	protected DescriptiveStatistics ds4MAHigh2BB;//DescriptiveStatistics for MAHigh2BB
	protected DescriptiveStatistics ds4MALow2BB;//DescriptiveStatistics for MALow2BB
	
	
	//Low-Low2 diff
	public double maxSMALow2Diff = Double.MIN_VALUE;//max SMA Low-Low2 diff
	public double minSMALow2Diff = Double.MAX_VALUE;//min SMA Low-Low2 diff

	//calculate MA50 slope
	public double slopeMASlow = 0; //slope of MA50;
	public int MA_SLOPE_LENGTH = 5;
	private FixedLengthQueue<MAPoint> fixedLengthQ = new FixedLengthQueue<MAPoint>(MA_SLOPE_LENGTH); //
	
	
	public IndicatorMaBB(Setting as) {
		super(as);
		this.ds4MAHL = new DescriptiveStatistics(this.as.getMaHLLength());
		this.ds4MAHigh2 = new DescriptiveStatistics(this.as.getMaHigh2Length());
		this.ds4MAHigh = new DescriptiveStatistics(this.as.getMaHighLength());
		this.ds4MALow = new DescriptiveStatistics(this.as.getMaLowLength());
		this.ds4MALow2 = new DescriptiveStatistics(this.as.getMaLow2Length());
		this.ds4MAHigh2BB = new DescriptiveStatistics(this.as.getMaLow2Length());
		this.ds4MALow2BB = new DescriptiveStatistics(this.as.getMaLow2Length());
	}
	
	public void addBar(Bar bar){
		super.addBar(bar);
		this.ds4MAHL.addValue((bar.getHigh()+bar.getLow())/2);
		this.ds4MAHigh2.addValue((3*bar.getHigh()-bar.getLow())/2);
		this.ds4MAHigh.addValue(bar.getHigh());
		this.ds4MALow.addValue(bar.getLow());
		this.ds4MALow2.addValue((3*bar.getLow()-bar.getHigh())/2);
		this.ds4MAHigh2BB.addValue(bar.getHigh());
		this.ds4MALow2BB.addValue(bar.getLow());
		
		double tmpSMALow2 = getSMALow2();
		if(!Double.isNaN(tmpSMALow2)){
			this.fixedLengthQ.add(new MAPoint(bar.getDate().getTime(), tmpSMALow2));
		}
		
		getSMALow2Diff();//testing
	}
	
	public double getSMAHigh2(){
		if(this.barAdded<this.as.getMaHigh2Length()){
			return Double.NaN;
		}
		return ds4MAHigh2.getSum()/this.as.getMaHigh2Length();
	}
	public double getSMAHigh(){
		if(this.barAdded<this.as.getMaHighLength()){
			return Double.NaN;
		}
		return ds4MAHigh.getSum()/this.as.getMaHighLength();
	}
	public double getSMAHL(){
		if(this.barAdded<this.as.getMaHLLength()){
			return Double.NaN;
		}
		return ds4MAHL.getSum()/this.as.getMaHLLength();
	}
	public double getSMALow(){
		if(this.barAdded<this.as.getMaLowLength()){
			return Double.NaN;
		}
		return ds4MALow.getSum()/this.as.getMaLowLength();
	}
	public double getSMALow2(){
		if(this.barAdded<this.as.getMaLow2Length()){
			return Double.NaN;
		}
		return ds4MALow2.getSum()/this.as.getMaLow2Length();
	}
	public double getSMAHigh2BB() {
		if(this.barAdded<this.as.getMaHigh2Length()){
			return Double.NaN;
		}
		return ds4MAHigh2BB.getSum()/this.as.getMaHigh2Length() + this.as.getMaHigh2BBTimes()*ds4MAHigh2BB.getStandardDeviation();
	}
	public double getSMALow2BB() {
		if(this.barAdded<this.as.getMaLow2Length()){
			return Double.NaN;
		}
		return ds4MALow2BB.getSum()/this.as.getMaLow2Length() - this.as.getMaLow2BBTimes()*ds4MALow2BB.getStandardDeviation();
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
		MAHigh2BB, MALow2BB,
		MALow2Slope//lagging, worse than MALow2Diff
	}
	
	public List<VXY> getVXYList(SeriesType seriesType, List<Bar> barList){

		List<VXY> vxyList = new ArrayList<VXY>();
		IndicatorMaBB tmpIndicator = new IndicatorMaBB(this.as);
		
		for(Bar bar : barList){
			tmpIndicator.addBar(bar);
			double indicatorVal = Double.NaN;

			double rate = this.as.getTmp();//
			switch (seriesType) {
				//case MAHigh2: indicatorVal =  tmpIndicator.getSMAHigh2(); break;
				case MAHigh2: indicatorVal =  rate*(tmpIndicator.getSMAHigh2()-tmpIndicator.getSMAHL())+tmpIndicator.getSMAHL(); break;
				case MAHigh: indicatorVal =  tmpIndicator.getSMAHigh(); break;
				case MAHL: indicatorVal =  tmpIndicator.getSMAHL(); break;
				case MALow: indicatorVal =  tmpIndicator.getSMALow(); break;
				//case MALow2: indicatorVal =  tmpIndicator.getSMALow2(); break;
				case MALow2: indicatorVal =  tmpIndicator.getSMAHL()-rate*(tmpIndicator.getSMAHL()-tmpIndicator.getSMALow2()); break;
				case MAHigh2BB: indicatorVal =  tmpIndicator.getSMAHigh2BB(); break;
				case MALow2BB: indicatorVal =  tmpIndicator.getSMALow2BB(); break;
				
				case MALow2Diff: indicatorVal =  tmpIndicator.getSMALow2Diff(); break;
				case MALow2Slope: indicatorVal =  tmpIndicator.getSMALow2Slope(); break;
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
		//High2+Low2 band
		//vseriesList.add(new VSeries("MAHigh2("+this.as.getMaHigh2Length()+")", getVXYList(SeriesType.MAHigh2, barList), null, java.awt.Color.blue));
		//vseriesList.add(new VSeries("MAHigh("+this.as.getMaHighLength()+")", getVXYList(SeriesType.MAHigh, barList), null, java.awt.Color.red));
		vseriesList.add(new VSeries("MAHL("+this.as.getMaHLLength()+")", getVXYList(SeriesType.MAHL, barList), null, java.awt.Color.cyan));
		//vseriesList.add(new VSeries("MALow("+this.as.getMaLowLength()+")", getVXYList(SeriesType.MALow, barList), null, java.awt.Color.red));
		//vseriesList.add(new VSeries("MALow2("+this.as.getMaLow2Length()+")", getVXYList(SeriesType.MALow2, barList), null, java.awt.Color.blue));
		
		vseriesList.add(new VSeries("MAHigh2BB("+this.as.getMaHigh2Length()+")", getVXYList(SeriesType.MAHigh2BB, barList), null, java.awt.Color.gray));
		vseriesList.add(new VSeries("MALow2BB("+this.as.getMaLow2Length()+")", getVXYList(SeriesType.MALow2BB, barList), null, java.awt.Color.gray));
		
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
