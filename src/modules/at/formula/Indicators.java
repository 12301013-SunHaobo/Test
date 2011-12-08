package modules.at.formula;

import java.util.Observable;

import modules.at.formula.rsi.RsiEmaSelfImpl;
import modules.at.model.AlgoSetting;
import modules.at.model.Bar;

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

	private DescriptiveStatistics ds4MAFast;//for MA fast
	private DescriptiveStatistics ds4MASlow;//for MA slow
	private DescriptiveStatistics ds4MA3;//for MA 3
	private DescriptiveStatistics ds4MAHL;//for MA (H+L)/2
	private DescriptiveStatistics ds4MAHigh2;//for (H+L)/2 + 2(H-(H+L)/2) =(3H-L)/2
	private DescriptiveStatistics ds4MAHigh;//for MA of High
	private DescriptiveStatistics ds4MALow;//for MA of Low
	private DescriptiveStatistics ds4MALow2;//for (H+L)/2 - 2((H+L)/2-L)=(3L-H)/2
	
	private DescriptiveStatistics ds4BB;//DescriptiveStatistics for BB
	private RsiEmaSelfImpl rsi;
	
	private DescriptiveStatistics ds4MAUpperShadow;//MA of upper shadow
	
	/**
	 * for stochastic
	 * %K = (Current Close - Lowest Low)/(Highest High - Lowest Low) * 100
	 * %D = 3-day SMA of %K
	 * Lowest Low = lowest low for the look-back period
	 * Highest High = highest high for the look-back period
	 * %K is multiplied by 100 to move the decimal point two places
	 */
	private DescriptiveStatistics ds4StoKHigh;//for stochastic K high
	private DescriptiveStatistics ds4StoKLow;//for stochastic K low
	private DescriptiveStatistics ds4StoD;//for stochastic D, saves K
	private double stoK = Double.NaN;

	//current bar
	private Bar curBar;
	
	//for internal calculation only, tracks how many bars are added
	private int barAdded = 0;
	
	public Indicators() {
		super();
		this.ds4MAFast = new DescriptiveStatistics(AlgoSetting.MA_FAST_LENGTH);
		this.ds4MASlow = new DescriptiveStatistics(AlgoSetting.MA_SLOW_LENGTH);
		this.ds4MA3 = new DescriptiveStatistics(AlgoSetting.MA_3_LENGTH);
		this.ds4MAHL = new DescriptiveStatistics(AlgoSetting.MA_HL_LENGTH);
		this.ds4MAHigh2 = new DescriptiveStatistics(AlgoSetting.MA_HIGH2_LENGTH);
		this.ds4MAHigh = new DescriptiveStatistics(AlgoSetting.MA_HIGH_LENGTH);
		this.ds4MALow = new DescriptiveStatistics(AlgoSetting.MA_LOW_LENGTH);
		this.ds4MALow2 = new DescriptiveStatistics(AlgoSetting.MA_LOW2_LENGTH);
		this.ds4BB = new DescriptiveStatistics(AlgoSetting.BB_LENGTH);
		this.rsi = new RsiEmaSelfImpl(AlgoSetting.RSI_LENGTH);
		this.ds4StoKHigh = new DescriptiveStatistics(AlgoSetting.STOCHASTIC_K_LENGTH);
		this.ds4StoKLow = new DescriptiveStatistics(AlgoSetting.STOCHASTIC_K_LENGTH);
		this.ds4StoD = new DescriptiveStatistics(AlgoSetting.STOCHASTIC_D_LENGTH);
		//my invented
		this.ds4MAUpperShadow = new DescriptiveStatistics(AlgoSetting.MA_UPPER_SHADOW_LENGTH);
	}
	
	public void addBar(Bar bar){
		this.curBar = bar;
		this.ds4MAFast.addValue(bar.getClose());
		this.ds4MASlow.addValue(bar.getClose());
		this.ds4MA3.addValue(bar.getLow());
		this.ds4MAHL.addValue((bar.getHigh()+bar.getLow())/2);
		this.ds4MAHigh2.addValue((3*bar.getHigh()-bar.getLow())/2);
		this.ds4MAHigh.addValue(bar.getHigh());
		this.ds4MALow.addValue(bar.getLow());
		this.ds4MALow2.addValue((3*bar.getLow()-bar.getHigh())/2);
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
		
		this.barAdded++;
		//notify observers: PatternMA, PatternRsi ...
        setChanged();
        notifyObservers();
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
		return (high2-hl);
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

}
