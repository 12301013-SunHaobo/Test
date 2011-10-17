package modules.at.formula;

import modules.at.formula.rsi.RsiEmaSelfImpl;
import modules.at.model.AlgoSetting;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * SMA fast/slow
 * BB
 * 
 * 
 * @author r
 *
 */
public class Indicators {

	private DescriptiveStatistics ds4MAFast;//for MA fast
	private DescriptiveStatistics ds4MASlow;//for MA slow
	private DescriptiveStatistics ds4BB;//DescriptiveStatistics for BB
	private RsiEmaSelfImpl rsi;
	
	//for internal calculation only
	private int barAdded = 0;
	
	public Indicators() {
		super();
		this.ds4MAFast = new DescriptiveStatistics(AlgoSetting.MA_FAST_LENGTH);
		this.ds4MASlow = new DescriptiveStatistics(AlgoSetting.MA_SLOW_LENGTH);
		this.ds4BB = new DescriptiveStatistics(AlgoSetting.BB_LENGTH);
		this.rsi = new RsiEmaSelfImpl(AlgoSetting.RSI_LENGTH);
	}
	
	public void addValue(double price){
		this.barAdded++;
		this.ds4MAFast.addValue(price);
		this.ds4MASlow.addValue(price);
		this.ds4BB.addValue(price);
		this.rsi.addPrice(price);
	}
	
	//MA
	public double getSMASlow(){
		if(this.barAdded<AlgoSetting.MA_SLOW_LENGTH){
			return Double.NaN;
		}
		return ds4MASlow.getSum()/AlgoSetting.MA_SLOW_LENGTH;
	}

	public double getSMAFast(){
		if(this.barAdded<AlgoSetting.MA_FAST_LENGTH){
			return Double.NaN;
		}
		return ds4MAFast.getSum()/AlgoSetting.MA_FAST_LENGTH;
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
	public double getStdDev(){
		return ds4BB.getStandardDeviation();
	}
	


}
