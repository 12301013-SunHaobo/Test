package modules.at.stg;

import java.util.ArrayList;
import java.util.List;

import modules.at.formula.rsi.RsiEmaSelfImpl;
import modules.at.model.Bar;
import modules.at.model.visual.VSeries;
import modules.at.model.visual.VXY;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * SMA fast/slow
 * BB
 *
 *
 */
public class Indicator {
	
	protected Setting setting = null;

	//current bar
	protected Bar curBar;
	
	//for internal calculation only, tracks how many bars are added
	protected int barAdded = 0;
	
	//MA
	protected DescriptiveStatistics ds4MAFast;//for MA fast
	protected DescriptiveStatistics ds4MASlow;//for MA slow
	//BB
	protected DescriptiveStatistics ds4BB;//DescriptiveStatistics for BB
	//RSI
	protected RsiEmaSelfImpl rsi;
	//MACD
	protected DescriptiveStatistics ds4MacdMAFast;//for MACD MA fast
	protected DescriptiveStatistics ds4MacdMASlow;//for MACD MA slow
	protected DescriptiveStatistics ds4Macd;//for MA 3
	
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

	public Indicator(Setting setting) {
		super();
		this.setting = setting;
		this.ds4MAFast = new DescriptiveStatistics(this.setting.getMaFastLength());
		this.ds4MASlow = new DescriptiveStatistics(this.setting.getMaSlowLength());

		this.ds4BB = new DescriptiveStatistics(this.setting.getBbLength());
		this.rsi = new RsiEmaSelfImpl(this.setting.getRsiLength());
		
		this.ds4MacdMAFast = new DescriptiveStatistics(this.setting.getMacdMAFastLength());
		this.ds4MacdMASlow = new DescriptiveStatistics(this.setting.getMacdMASlowLength());
		this.ds4Macd = new DescriptiveStatistics(this.setting.getMacdLength());
		
		this.ds4StoKHigh = new DescriptiveStatistics(this.setting.getStochasticKLength());
		this.ds4StoKLow = new DescriptiveStatistics(this.setting.getStochasticKLength());
		this.ds4StoD = new DescriptiveStatistics(this.setting.getStochastickDLength());
	}
	
	public void addBar(Bar bar){
		this.barAdded++;
		this.curBar = bar;
		
		//MA
		this.ds4MAFast.addValue(bar.getClose());
		this.ds4MASlow.addValue(bar.getClose());
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
		
	}
	
	//MA
	public double getSMAFast(){
		if(this.barAdded<this.setting.getMaFastLength()){
			return Double.NaN;
		}
		return ds4MAFast.getSum()/this.setting.getMaFastLength();
	}
	public double getSMASlow(){
		if(this.barAdded<this.setting.getMaSlowLength()){
			return Double.NaN;
		}
		return ds4MASlow.getSum()/this.setting.getMaSlowLength();
	}
	
	//BB
	public double getBBUpper(){
		if(this.barAdded<this.setting.getBbLength()){
			return Double.NaN;
		}
		return getBBMiddle()+getStdDev()*2;
	}
	public double getBBMiddle(){
		if(this.barAdded<this.setting.getBbLength()){
			return Double.NaN;
		}
		return ds4BB.getSum()/this.setting.getBbLength();
	}
	public double getBBLower(){
		if(this.barAdded<this.setting.getBbLength()){
			return Double.NaN;
		}
		return getBBMiddle()-getStdDev()*2;
	}
	
	//RSI
	public double getRsi(){
		if(this.barAdded<this.setting.getRsiLength()){
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
		if(this.barAdded<this.setting.getMacdMASlowLength()
				|| this.barAdded<this.setting.getMacdMAFastLength()){
			return Double.NaN;
		}
		double macdFastSMA = this.ds4MacdMAFast.getSum()/this.setting.getMacdMAFastLength();
		double macdSlowSMA = this.ds4MacdMASlow.getSum()/this.setting.getMacdMASlowLength();
		return (macdFastSMA - macdSlowSMA);
	}
	public double getMacdSignal(){
		if(this.ds4Macd.getN()<this.setting.getMacdLength()){
			return Double.NaN;
		}
		return this.ds4Macd.getSum()/this.setting.getMacdLength();
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
		if(this.ds4StoD.getN()<this.setting.getStochastickDLength()){
			return Double.NaN;
		}
		return this.ds4StoD.getSum()/this.setting.getStochastickDLength();
	}
	
	public Bar getCurBar() {
		return curBar;
	}

	/**
	 *Utility for indicator VXY lists
	 */
	public static enum SeriesType {
		MAFast, MASlow, 
		BBUpper, BBMiddle, BBLower,
		RsiUpper, Rsi, RsiLower,
		Macd, MacdSignal, MacdHistogram, MacdZero,
		MAUpperShadow,
		StoK, StoD, StoUpper, StoLower
	}	
	
	public List<VXY> getVXYList(SeriesType seriesType, List<Bar> barList){

		List<VXY> vxyList = new ArrayList<VXY>();
		Indicator tmpIndicators = new Indicator(this.setting);
		
		for(Bar bar : barList){
			tmpIndicators.addBar(bar);
			double indicatorVal = Double.NaN;

			switch (seriesType) {
				case MAFast: indicatorVal = tmpIndicators.getSMAFast(); break;
				case MASlow: indicatorVal =  tmpIndicators.getSMASlow(); break;
				case BBUpper: indicatorVal = tmpIndicators.getBBUpper(); break;
				case BBMiddle: indicatorVal = tmpIndicators.getBBMiddle(); break;
				case BBLower: indicatorVal = tmpIndicators.getBBLower(); break;
				case RsiUpper: indicatorVal = this.setting.getRsiUpper(); break;
				case Rsi: indicatorVal = tmpIndicators.getRsi(); break;
				case RsiLower: indicatorVal = this.setting.getRsiLower(); break;
				case Macd: indicatorVal = tmpIndicators.getMacd(); break;
				case MacdSignal: indicatorVal = tmpIndicators.getMacdSignal(); break;
				case MacdHistogram: indicatorVal = tmpIndicators.getMacdHistogram(); break;
				case MacdZero: indicatorVal = 0; break;
				case StoK: indicatorVal = tmpIndicators.getStochasticK(); break;
				case StoD: indicatorVal = tmpIndicators.getStochasticD(); break;
				case StoUpper: indicatorVal = this.setting.getStochasticUpper(); break;
				case StoLower: indicatorVal = this.setting.getStochasticLower(); break;
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
		vseriesList.add(new VSeries("BB("+this.setting.getBbLength()+")",getVXYList(SeriesType.BBMiddle, barList), null, java.awt.Color.gray));
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
		
		vseriesList.add(new VSeries("Macd("+this.setting.getMacdMAFastLength()+","+this.setting.getMacdMASlowLength()+")", getVXYList(SeriesType.Macd, barList), null, java.awt.Color.black));
		vseriesList.add(new VSeries("MacdSignal("+this.setting.getMacdLength()+")", getVXYList(SeriesType.MacdSignal, barList), null, java.awt.Color.red));
		vseriesList.add(new VSeries("MacdHistogram", getVXYList(SeriesType.MacdHistogram, barList), null, java.awt.Color.cyan));
		vseriesList.add(new VSeries("0", getVXYList(SeriesType.MacdZero, barList), null, java.awt.Color.blue));
		
		vSeriesLists.add(vseriesList);

		return vSeriesLists;
	}
	
	
}
