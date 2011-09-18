package modules.at.formula;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class Indicator {

	
	private int length; //number of bars to calculate 

	private DescriptiveStatistics dsDefault;//for SMA fast and others
	private DescriptiveStatistics dsSlow;//for SMA slow only
	
	
	//for internal calculation only
	private int barAdded = 0;
	
	public Indicator(int length) {
		super();
		this.length = length;
		this.dsDefault = new DescriptiveStatistics(length);
	}
	
	public void addValue(double price){
		this.barAdded++;
		this.dsDefault.addValue(price);
	}
	
	public double getSMAFast(){
		if(this.barAdded<this.length){
			return Double.NaN;
		}
		return dsDefault.getSum()/this.length;
	}
	
	public double getBBUpper(){
		if(this.barAdded<this.length){
			return Double.NaN;
		}
		return getSMAFast()+getStdDev()*2;
	}
	public double getBBLower(){
		if(this.barAdded<this.length){
			return Double.NaN;
		}
		return getSMAFast()-getStdDev()*2;
	}
	
	public double getRsi(){
		return 0;
	}
	

	
	//value is not the same as in TOS, but has same direction, same time at high/low
	public double getStdDev(){
		return dsDefault.getStandardDeviation();
	}
	
	//using longer different length
	public double getSMASlow(){
		return Double.NaN;
	}

}
