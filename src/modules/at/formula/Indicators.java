package modules.at.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import modules.at.formula.rsi.RsiEmaSelfImpl;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;
import modules.at.model.visual.VSeries;
import modules.at.model.visual.VXY;
import modules.at.stg.StrategyMA;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * SMA fast/slow
 * BB
 * 
 * 
 * @author r
 *
 */
public class Indicators extends Observable {
	//MA
	protected DescriptiveStatistics ds4MAFast;//for MA fast
	protected DescriptiveStatistics ds4MASlow;//for MA slow
	protected DescriptiveStatistics ds4MA3;//for MA 3
	//BB
	protected DescriptiveStatistics ds4BB;//DescriptiveStatistics for BB
	//RSI
	protected RsiEmaSelfImpl rsi;
	//MACD
	protected DescriptiveStatistics ds4MacdMAFast;//for MACD MA fast
	protected DescriptiveStatistics ds4MacdMASlow;//for MACD MA slow
	protected DescriptiveStatistics ds4Macd;//for MA 3
	
	
	//upper shadow
	protected DescriptiveStatistics ds4MAUpperShadow;//MA of upper shadow
	
	/**
	 * for stochastic
	 * %K = (Current Close - Lowest Low)/(Highest High - Lowest Low) * 100
	 * %D = 3-day SMA of %K
	 * Lowest Low = lowest low for the look-back period
	 * Highest High = highest high for the look-back period
	 * %K is multiplied by 100 to move the decimal point two places
	 */
	protected DescriptiveStatistics ds4StoKHigh;//for stochastic K high
	protected DescriptiveStatistics ds4StoKLow;//for stochastic K low
	protected DescriptiveStatistics ds4StoD;//for stochastic D, saves K
	protected double stoK = Double.NaN;

	//current bar
	protected Bar curBar;
	
	//for internal calculation only, tracks how many bars are added
	protected int barAdded = 0;
	
	public Indicators() {
		super();
		this.ds4MAFast = new DescriptiveStatistics(AlgoSetting.maFastLength);
		this.ds4MASlow = new DescriptiveStatistics(AlgoSetting.maSlowLength);
		this.ds4MA3 = new DescriptiveStatistics(AlgoSetting.ma3Length);

		this.ds4BB = new DescriptiveStatistics(AlgoSetting.bbLength);
		this.rsi = new RsiEmaSelfImpl(AlgoSetting.rsiLength);
		
		this.ds4MacdMAFast = new DescriptiveStatistics(AlgoSetting.macdMAFastLength);
		this.ds4MacdMASlow = new DescriptiveStatistics(AlgoSetting.macdMASlowLength);
		this.ds4Macd = new DescriptiveStatistics(AlgoSetting.macdLength);
		
		this.ds4StoKHigh = new DescriptiveStatistics(AlgoSetting.stochasticKLength);
		this.ds4StoKLow = new DescriptiveStatistics(AlgoSetting.stochasticKLength);
		this.ds4StoD = new DescriptiveStatistics(AlgoSetting.stochastickDLength);
		//my invented
		this.ds4MAUpperShadow = new DescriptiveStatistics(AlgoSetting.maUpperShadowLength);
	}
	
	public void addBar(Bar bar){
		this.barAdded++;
		this.curBar = bar;
		
		//MA
		this.ds4MAFast.addValue(bar.getClose());
		this.ds4MASlow.addValue(bar.getClose());
		this.ds4MA3.addValue(bar.getLow());
		//BB
		this.ds4BB.addValue(bar.getClose());
		this.rsi.addValue(bar.getClose());
		//macd
		this.ds4MacdMAFast.addValue(bar.getClose());
		this.ds4MacdMASlow.addValue(bar.getClose());
		//add MacdLine value
		double curMacdLine = getMacd();
		if(!Double.isNaN(curMacdLine)){
			this.ds4Macd.addValue(curMacdLine);
		}
		
		//stochastic
		this.ds4StoKHigh.addValue(bar.getHigh());
		this.ds4StoKLow.addValue(bar.getLow());
		this.stoK = (bar.getClose() - ds4StoKLow.getMin())/(ds4StoKHigh.getMax() - ds4StoKLow.getMin()) * 100;
		this.ds4StoD.addValue(this.stoK);
		
		//my invented
		this.ds4MAUpperShadow.addValue(bar.getHigh()-Math.max(bar.getOpen(), bar.getClose()));
	}
	
	//MA
	public double getSMAFast(){
		if(this.barAdded<AlgoSetting.maFastLength){
			return Double.NaN;
		}
		return ds4MAFast.getSum()/AlgoSetting.maFastLength;
	}
	public double getSMASlow(){
		if(this.barAdded<AlgoSetting.maSlowLength){
			return Double.NaN;
		}
		return ds4MASlow.getSum()/AlgoSetting.maSlowLength;
	}
	public double getSMA3(){
		if(this.barAdded<AlgoSetting.ma3Length){
			return Double.NaN;
		}
		return ds4MA3.getSum()/AlgoSetting.ma3Length;
	}
	
	//BB
	public double getBBUpper(){
		if(this.barAdded<AlgoSetting.bbLength){
			return Double.NaN;
		}
		return getBBMiddle()+getStdDev()*2;
	}
	public double getBBMiddle(){
		if(this.barAdded<AlgoSetting.bbLength){
			return Double.NaN;
		}
		return ds4BB.getSum()/AlgoSetting.bbLength;
	}
	public double getBBLower(){
		if(this.barAdded<AlgoSetting.bbLength){
			return Double.NaN;
		}
		return getBBMiddle()-getStdDev()*2;
	}
	
	//RSI
	public double getRsi(){
		if(this.barAdded<AlgoSetting.rsiLength){
			return Double.NaN;
		}
		return rsi.getValue();
	}
	
	//value is not the same as in TOS, but has same direction, same time at high/low
	private double getStdDev(){
		return ds4BB.getStandardDeviation();
	}
	
	//MACD
	public double getMacd(){
		if(this.barAdded<AlgoSetting.macdMASlowLength
				|| this.barAdded<AlgoSetting.macdMAFastLength){
			return Double.NaN;
		}
		double macdFastSMA = this.ds4MacdMAFast.getSum()/AlgoSetting.macdMAFastLength;
		double macdSlowSMA = this.ds4MacdMASlow.getSum()/AlgoSetting.macdMASlowLength;
		return (macdFastSMA - macdSlowSMA);
	}
	public double getMacdSignal(){
		if(this.ds4Macd.getN()<AlgoSetting.macdLength){
			return Double.NaN;
		}
		return this.ds4Macd.getSum()/AlgoSetting.macdLength;
	}
	public double getMacdHistogram(){
		double macd = getMacd();
		double signal = getMacdSignal();
		if(Double.isNaN(macd) || Double.isNaN(signal)){
			return Double.NaN;
		}
		return (macd - signal);
	}
	
	
	//Stochastic K
	public double getStochasticK(){
		return this.stoK; 
	}
	public double getStochasticD(){
		if(this.ds4StoD.getN()<AlgoSetting.stochastickDLength){
			return Double.NaN;
		}
		return this.ds4StoD.getSum()/AlgoSetting.stochastickDLength;
	}
	
	//my invented
	public double getMAUpperShadow() {
		if(this.ds4MAUpperShadow.getN()<AlgoSetting.maUpperShadowLength){
			return Double.NaN;
		}
		return this.ds4MAUpperShadow.getSum()/AlgoSetting.maUpperShadowLength;
	}
	
	public Bar getCurBar() {
		return curBar;
	}

	/**
	 *Utility for indicator VXY lists
	 */
	public enum SeriesType {
		MAFast, MASlow, MA3, 
		BBUpper, BBMiddle, BBLower,
		RsiUpper, Rsi, RsiLower,
		Macd, MacdSignal, MacdHistogram, MacdZero,
		MAUpperShadow,
		StoK, StoD, StoUpper, StoLower
	}	
	public List<VXY> getVXYList(SeriesType seriesType, List<Bar> barList){

		List<VXY> vxyList = new ArrayList<VXY>();
		Indicators indicator = new Indicators();
		
		for(Bar bar : barList){
			indicator.addBar(bar);
			double indicatorVal = Double.NaN;

			switch (seriesType) {
				case MAFast: indicatorVal = indicator.getSMAFast(); break;
				case MASlow: indicatorVal =  indicator.getSMASlow(); break;
				case MA3: indicatorVal =  indicator.getSMA3(); break;
				case MAUpperShadow: indicatorVal =  indicator.getMAUpperShadow(); break;
				case BBUpper: indicatorVal = indicator.getBBUpper(); break;
				case BBMiddle: indicatorVal = indicator.getBBMiddle(); break;
				case BBLower: indicatorVal = indicator.getBBLower(); break;
				case RsiUpper: indicatorVal = AlgoSetting.rsiUpper; break;
				case Rsi: indicatorVal = indicator.getRsi(); break;
				case RsiLower: indicatorVal = AlgoSetting.rsiLower; break;
				case Macd: indicatorVal = indicator.getMacd(); break;
				case MacdSignal: indicatorVal = indicator.getMacdSignal(); break;
				case MacdHistogram: indicatorVal = indicator.getMacdHistogram(); break;
				case MacdZero: indicatorVal = 0; break;
				case StoK: indicatorVal = indicator.getStochasticK(); break;
				case StoD: indicatorVal = indicator.getStochasticD(); break;
				case StoUpper: indicatorVal = AlgoSetting.stochasticUpper; break;
				case StoLower: indicatorVal = AlgoSetting.stochasticLower; break;
				default:break;
			}

			if(!Double.isNaN(indicatorVal)){
				vxyList.add(new VXY(bar.getDate().getTime(), indicatorVal));
			}
		}
		return vxyList;
	}		
	
	//PlotBar
	public List<VSeries> getPlotBarVSeriesList(List<Bar> barList){
		List<VSeries> vseriesList = new ArrayList<VSeries>();
		vseriesList.add(new VSeries("MAFast("+AlgoSetting.maFastLength+")",getVXYList(SeriesType.MAFast, barList), null, java.awt.Color.magenta));
		vseriesList.add(new VSeries("MASlow("+AlgoSetting.maSlowLength+")", getVXYList(SeriesType.MASlow, barList), null, java.awt.Color.cyan));
		vseriesList.add(new VSeries("MA3Low("+AlgoSetting.ma3Length+")", getVXYList(SeriesType.MA3, barList), null, java.awt.Color.blue));
		
		vseriesList.add(new VSeries("BBUpper",getVXYList(SeriesType.BBUpper, barList), null, java.awt.Color.gray));
		vseriesList.add(new VSeries("BB("+AlgoSetting.bbLength+")",getVXYList(SeriesType.BBMiddle, barList), null, java.awt.Color.gray));
		vseriesList.add(new VSeries("BBLower",getVXYList(SeriesType.BBLower, barList), null, java.awt.Color.gray));

		return vseriesList;
	}
	//Plot1
	public List<List<VSeries>> getPlotsVSeriesLists(List<Bar> barList){
		List<List<VSeries>> vSeriesLists = new ArrayList<List<VSeries>>();
		List<VSeries> vseriesList = new ArrayList<VSeries>();
//		vseriesList.add(new VSeries("RsiUpper", getVXYList(SeriesType.RsiUpper, barList), null, java.awt.Color.red));
//		vseriesList.add(new VSeries("Rsi("+AlgoSetting.RSI_LENGTH+")", getVXYList(SeriesType.Rsi, barList), null, java.awt.Color.red));
//		vseriesList.add(new VSeries("RsiLower", getVXYList(SeriesType.RsiLower, barList), null, java.awt.Color.red));
		
		vseriesList.add(new VSeries("Macd("+AlgoSetting.macdMAFastLength+","+AlgoSetting.macdMASlowLength+")", getVXYList(SeriesType.Macd, barList), null, java.awt.Color.black));
		vseriesList.add(new VSeries("MacdSignal("+AlgoSetting.macdLength+")", getVXYList(SeriesType.MacdSignal, barList), null, java.awt.Color.red));
		vseriesList.add(new VSeries("MacdHistogram", getVXYList(SeriesType.MacdHistogram, barList), null, java.awt.Color.cyan));
		vseriesList.add(new VSeries("0", getVXYList(SeriesType.MacdZero, barList), null, java.awt.Color.blue));
		
		vSeriesLists.add(vseriesList);

		return vSeriesLists;
	}
	
	
}
