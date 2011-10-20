package modules.at.model;

public class AlgoSetting {

	//RSI
	public static final int RSI_LENGTH = 14;
	public static final double RSI_UPPER = 70;
	public static final double RSI_LOWER = 30;
	
	//BB
	public static final int BB_LENGTH = 14;
	
	//MA
	public static final int MA_FAST_LENGTH = 5;
	public static final int MA_SLOW_LENGTH = 13;
	
	//Cut loss & lock profit
	public static final double CUT_LOSS = - 0.05; //absolute price loss, not %
	public static final double PROFIT_LOSS = - 0.05; //absolute price loss from previous profit  	
	
	//Pattern weight
	public static final int PATTERN_WEIGHT_MA = 1;
	public static final int PATTERN_WEIGHT_RSI = 1;
}
