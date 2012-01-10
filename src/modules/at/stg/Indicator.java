package modules.at.stg;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import modules.at.formula.rsi.RsiEmaSelfImpl;
import modules.at.model.Bar;
import modules.at.model.visual.VSeries;
import modules.at.model.visual.VXY;
import modules.at.stg.mabb.StrategyMaBB;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * SMA fast/slow
 * BB
 * 
 * 
 * @author r
 *
 */
public class Indicator extends Observable {
	protected Setting as = null;
	
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
	
	public Indicator(Setting as) {
		super();
		this.as = as;
		this.ds4MAFast = new DescriptiveStatistics(this.as.getMaFastLength());
		this.ds4MASlow = new DescriptiveStatistics(this.as.getMaSlowLength());
		this.ds4MA3 = new DescriptiveStatistics(this.as.getMa3Length());

		this.ds4BB = new DescriptiveStatistics(this.as.getBbLength());
		this.rsi = new RsiEmaSelfImpl(this.as.getRsiLength());
		
		this.ds4MacdMAFast = new DescriptiveStatistics(this.as.getMacdMAFastLength());
		this.ds4MacdMASlow = new DescriptiveStatistics(this.as.getMacdMASlowLength());
		this.ds4Macd = new DescriptiveStatistics(this.as.getMacdLength());
		
		this.ds4StoKHigh = new DescriptiveStatistics(this.as.getStochasticKLength());
		this.ds4StoKLow = new DescriptiveStatistics(this.as.getStochasticKLength());
		this.ds4StoD = new DescriptiveStatistics(this.as.getStochastickDLength());
		//my invented
		this.ds4MAUpperShadow = new DescriptiveStatistics(this.as.getMaUpperShadowLength());
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
		if(this.barAdded<this.as.getMaFastLength()){
			return Double.NaN;
		}
		return ds4MAFast.getSum()/this.as.getMaFastLength();
	}
	public double getSMASlow(){
		if(this.barAdded<this.as.getMaSlowLength()){
			return Double.NaN;
		}
		return ds4MASlow.getSum()/this.as.getMaSlowLength();
	}
	public double getSMA3(){
		if(this.barAdded<this.as.getMa3Length()){
			return Double.NaN;
		}
		return ds4MA3.getSum()/this.as.getMa3Length();
	}
	
	//BB
	public double getBBUpper(){
		if(this.barAdded<this.as.getBbLength()){
			return Double.NaN;
		}
		return getBBMiddle()+getStdDev()*2;
	}
	public double getBBMiddle(){
		if(this.barAdded<this.as.getBbLength()){
			return Double.NaN;
		}
		return ds4BB.getSum()/this.as.getBbLength();
	}
	public double getBBLower(){
		if(this.barAdded<this.as.getBbLength()){
			return Double.NaN;
		}
		return getBBMiddle()-getStdDev()*2;
	}
	
	//RSI
	public double getRsi(){
		if(this.barAdded<this.as.getRsiLength()){
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
		if(this.barAdded<this.as.getMacdMASlowLength()
				|| this.barAdded<this.as.getMacdMAFastLength()){
			return Double.NaN;
		}
		double macdFastSMA = this.ds4MacdMAFast.getSum()/this.as.getMacdMAFastLength();
		double macdSlowSMA = this.ds4MacdMASlow.getSum()/this.as.getMacdMASlowLength();
		return (macdFastSMA - macdSlowSMA);
	}
	public double getMacdSignal(){
		if(this.ds4Macd.getN()<this.as.getMacdLength()){
			return Double.NaN;
		}
		return this.ds4Macd.getSum()/this.as.getMacdLength();
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
		if(this.ds4StoD.getN()<this.as.getStochastickDLength()){
			return Double.NaN;
		}
		return this.ds4StoD.getSum()/this.as.getStochastickDLength();
	}
	
	//my invented
	public double getMAUpperShadow() {
		if(this.ds4MAUpperShadow.getN()<this.as.getMaUpperShadowLength()){
			return Double.NaN;
		}
		return this.ds4MAUpperShadow.getSum()/this.as.getMaUpperShadowLength();
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
		Indicator tmpIndicators = new Indicator(this.as);
		
		for(Bar bar : barList){
			tmpIndicators.addBar(bar);
			double indicatorVal = Double.NaN;

			switch (seriesType) {
				case MAFast: indicatorVal = tmpIndicators.getSMAFast(); break;
				case MASlow: indicatorVal =  tmpIndicators.getSMASlow(); break;
				case MA3: indicatorVal =  tmpIndicators.getSMA3(); break;
				case MAUpperShadow: indicatorVal =  tmpIndicators.getMAUpperShadow(); break;
				case BBUpper: indicatorVal = tmpIndicators.getBBUpper(); break;
				case BBMiddle: indicatorVal = tmpIndicators.getBBMiddle(); break;
				case BBLower: indicatorVal = tmpIndicators.getBBLower(); break;
				case RsiUpper: indicatorVal = this.as.getRsiUpper(); break;
				case Rsi: indicatorVal = tmpIndicators.getRsi(); break;
				case RsiLower: indicatorVal = this.as.getRsiLower(); break;
				case Macd: indicatorVal = tmpIndicators.getMacd(); break;
				case MacdSignal: indicatorVal = tmpIndicators.getMacdSignal(); break;
				case MacdHistogram: indicatorVal = tmpIndicators.getMacdHistogram(); break;
				case MacdZero: indicatorVal = 0; break;
				case StoK: indicatorVal = tmpIndicators.getStochasticK(); break;
				case StoD: indicatorVal = tmpIndicators.getStochasticD(); break;
				case StoUpper: indicatorVal = this.as.getStochasticUpper(); break;
				case StoLower: indicatorVal = this.as.getStochasticLower(); break;
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
//		vseriesList.add(new VSeries("MAFast("+this.as.getMaFastLength()+")",getVXYList(SeriesType.MAFast, barList), null, java.awt.Color.magenta));
//		vseriesList.add(new VSeries("MASlow("+this.as.getMaSlowLength()+")", getVXYList(SeriesType.MASlow, barList), null, java.awt.Color.cyan));
//		vseriesList.add(new VSeries("MA3Low("+this.as.getMa3Length()+")", getVXYList(SeriesType.MA3, barList), null, java.awt.Color.blue));
		
		vseriesList.add(new VSeries("BBUpper",getVXYList(SeriesType.BBUpper, barList), null, java.awt.Color.gray));
		vseriesList.add(new VSeries("BB("+this.as.getBbLength()+")",getVXYList(SeriesType.BBMiddle, barList), null, java.awt.Color.gray));
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
		
		vseriesList.add(new VSeries("Macd("+this.as.getMacdMAFastLength()+","+this.as.getMacdMASlowLength()+")", getVXYList(SeriesType.Macd, barList), null, java.awt.Color.black));
		vseriesList.add(new VSeries("MacdSignal("+this.as.getMacdLength()+")", getVXYList(SeriesType.MacdSignal, barList), null, java.awt.Color.red));
		vseriesList.add(new VSeries("MacdHistogram", getVXYList(SeriesType.MacdHistogram, barList), null, java.awt.Color.cyan));
		vseriesList.add(new VSeries("0", getVXYList(SeriesType.MacdZero, barList), null, java.awt.Color.blue));
		
		vSeriesLists.add(vseriesList);

		return vSeriesLists;
	}
	
	
}
