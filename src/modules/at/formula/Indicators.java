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
	
	protected DescriptiveStatistics ds4MAFast;//for MA fast
	protected DescriptiveStatistics ds4MASlow;//for MA slow
	protected DescriptiveStatistics ds4MA3;//for MA 3
	
	protected DescriptiveStatistics ds4BB;//DescriptiveStatistics for BB
	protected RsiEmaSelfImpl rsi;
	
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
		this.ds4MAFast = new DescriptiveStatistics(AlgoSetting.MA_FAST_LENGTH);
		this.ds4MASlow = new DescriptiveStatistics(AlgoSetting.MA_SLOW_LENGTH);
		this.ds4MA3 = new DescriptiveStatistics(AlgoSetting.MA_3_LENGTH);

		this.ds4BB = new DescriptiveStatistics(AlgoSetting.BB_LENGTH);
		this.rsi = new RsiEmaSelfImpl(AlgoSetting.RSI_LENGTH);
		this.ds4StoKHigh = new DescriptiveStatistics(AlgoSetting.STOCHASTIC_K_LENGTH);
		this.ds4StoKLow = new DescriptiveStatistics(AlgoSetting.STOCHASTIC_K_LENGTH);
		this.ds4StoD = new DescriptiveStatistics(AlgoSetting.STOCHASTIC_D_LENGTH);
		//my invented
		this.ds4MAUpperShadow = new DescriptiveStatistics(AlgoSetting.MA_UPPER_SHADOW_LENGTH);
	}
	
	public void addBar(Bar bar){
		this.barAdded++;
		this.curBar = bar;
		
		this.ds4MAFast.addValue(bar.getClose());
		this.ds4MASlow.addValue(bar.getClose());
		this.ds4MA3.addValue(bar.getLow());
		this.ds4BB.addValue(bar.getClose());
		this.rsi.addValue(bar.getClose());
		//stochastic
		this.ds4StoKHigh.addValue(bar.getHigh());
		this.ds4StoKLow.addValue(bar.getLow());
		this.stoK = (bar.getClose() - ds4StoKLow.getMin())/(ds4StoKHigh.getMax() - ds4StoKLow.getMin()) * 100;
		this.ds4StoD.addValue(this.stoK);
		//highLow pattern
		
		//my invented
		this.ds4MAUpperShadow.addValue(bar.getHigh()-Math.max(bar.getOpen(), bar.getClose()));
	}
	
	//MA
	public double getSMAFast(){
		if(this.barAdded<AlgoSetting.MA_FAST_LENGTH){
			return Double.NaN;
		}
		return ds4MAFast.getSum()/AlgoSetting.MA_FAST_LENGTH;
	}
	public double getSMASlow(){
		if(this.barAdded<AlgoSetting.MA_SLOW_LENGTH){
			return Double.NaN;
		}
		return ds4MASlow.getSum()/AlgoSetting.MA_SLOW_LENGTH;
	}
	public double getSMA3(){
		if(this.barAdded<AlgoSetting.MA_3_LENGTH){
			return Double.NaN;
		}
		return ds4MA3.getSum()/AlgoSetting.MA_3_LENGTH;
	}
	
	//BB
	public double getBBUpper(){
		if(this.barAdded<AlgoSetting.BB_LENGTH){
			return Double.NaN;
		}
		return getBBMiddle()+getStdDev()*2;
	}
	public double getBBMiddle(){
		if(this.barAdded<AlgoSetting.BB_LENGTH){
			return Double.NaN;
		}
		return ds4BB.getSum()/AlgoSetting.BB_LENGTH;
	}
	public double getBBLower(){
		if(this.barAdded<AlgoSetting.BB_LENGTH){
			return Double.NaN;
		}
		return getBBMiddle()-getStdDev()*2;
	}
	
	//RSI
	public double getRsi(){
		if(this.barAdded<AlgoSetting.RSI_LENGTH){
			return Double.NaN;
		}
		return rsi.getValue();
	}
	
	//value is not the same as in TOS, but has same direction, same time at high/low
	private double getStdDev(){
		return ds4BB.getStandardDeviation();
	}
	
	//Stochastic K
	public double getStochasticK(){
		return this.stoK; 
	}
	public double getStochasticD(){
		if(this.ds4StoD.getN()<AlgoSetting.STOCHASTIC_D_LENGTH){
			return Double.NaN;
		}
		return this.ds4StoD.getSum()/AlgoSetting.STOCHASTIC_D_LENGTH;
	}
	
	//my invented
	public double getMAUpperShadow() {
		if(this.ds4MAUpperShadow.getN()<AlgoSetting.MA_UPPER_SHADOW_LENGTH){
			return Double.NaN;
		}
		return this.ds4MAUpperShadow.getSum()/AlgoSetting.MA_UPPER_SHADOW_LENGTH;
	}
	
	public Bar getCurBar() {
		return curBar;
	}

	/**
	 *Utility for indicator VXY lists
	 */
	public enum SeriesType {
		RsiUpper, Rsi, RsiLower,
		BBUpper, BBMiddle, BBLower,
		MAFast, MASlow, MA3, 
		MAUpperShadow,
		StoK, StoD, StoUpper, StoLower
	}	
	public static List<VXY> getVXYList(SeriesType seriesType, List<Bar> barList){

		List<VXY> vxyList = new ArrayList<VXY>();
		Indicators indicator = new Indicators();
		
		for(Bar bar : barList){
			indicator.addBar(bar);
			double indicatorVal = Double.NaN;

			switch (seriesType) {
				case RsiUpper: indicatorVal = AlgoSetting.RSI_UPPER; break;
				case Rsi: indicatorVal = indicator.getRsi(); break;
				case RsiLower: indicatorVal = AlgoSetting.RSI_LOWER; break;
				case BBUpper: indicatorVal = indicator.getBBUpper(); break;
				case BBMiddle: indicatorVal = indicator.getBBMiddle(); break;
				case BBLower: indicatorVal = indicator.getBBLower(); break;
				case MAFast: indicatorVal = indicator.getSMAFast(); break;
				case MASlow: indicatorVal =  indicator.getSMASlow(); break;
				case MA3: indicatorVal =  indicator.getSMA3(); break;
				case MAUpperShadow: indicatorVal =  indicator.getMAUpperShadow(); break;
				case StoK: indicatorVal = indicator.getStochasticK(); break;
				case StoD: indicatorVal = indicator.getStochasticD(); break;
				case StoUpper: indicatorVal = AlgoSetting.STOCHASTIC_UPPER; break;
				case StoLower: indicatorVal = AlgoSetting.STOCHASTIC_LOWER; break;
				default:break;
			}

			if(!Double.isNaN(indicatorVal)){
				vxyList.add(new VXY(bar.getDate().getTime(), indicatorVal));
			}
		}
		return vxyList;
	}		
	
	//PlotBar
	public static List<VSeries> getPlotBarVSeriesList(List<Bar> barList){
		List<VSeries> vseriesList = new ArrayList<VSeries>();
		vseriesList.add(new VSeries("MAFast("+AlgoSetting.MA_FAST_LENGTH+")",Indicators.getVXYList(Indicators.SeriesType.MAFast, barList), null, java.awt.Color.magenta));
		vseriesList.add(new VSeries("MASlow("+AlgoSetting.MA_SLOW_LENGTH+")", Indicators.getVXYList(Indicators.SeriesType.MASlow, barList), null, java.awt.Color.cyan));
		vseriesList.add(new VSeries("MA3Low("+AlgoSetting.MA_3_LENGTH+")", Indicators.getVXYList(Indicators.SeriesType.MA3, barList), null, java.awt.Color.blue));
		
		vseriesList.add(new VSeries("BBUpper",Indicators.getVXYList(Indicators.SeriesType.BBUpper, barList), null, java.awt.Color.gray));
		vseriesList.add(new VSeries("BB("+AlgoSetting.BB_LENGTH+")",Indicators.getVXYList(Indicators.SeriesType.BBMiddle, barList), null, java.awt.Color.gray));
		vseriesList.add(new VSeries("BBLower",Indicators.getVXYList(Indicators.SeriesType.BBLower, barList), null, java.awt.Color.gray));

		return vseriesList;
	}
	//Plot1
	public static List<VSeries> getPlot1VSeriesList(List<Bar> barList){
		List<VSeries> vseriesList = new ArrayList<VSeries>();
		vseriesList.add(new VSeries("RsiUpper", Indicators.getVXYList(Indicators.SeriesType.RsiUpper, barList), null, java.awt.Color.red));
		vseriesList.add(new VSeries("Rsi("+AlgoSetting.RSI_LENGTH+")", Indicators.getVXYList(Indicators.SeriesType.Rsi, barList), null, java.awt.Color.red));
		vseriesList.add(new VSeries("RsiLower", Indicators.getVXYList(Indicators.SeriesType.RsiLower, barList), null, java.awt.Color.red));
		return vseriesList;
	}
	
	
}
