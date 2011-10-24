package modules.at.model;

public class AlgoSetting {

    //trade unit, how many shares in one trade unit
    public static final int TRADE_UNIT = 1;

	//RSI
	public static final int RSI_LENGTH = 14;
	public static final double RSI_UPPER = 70;
	public static final double RSI_LOWER = 30;
	
	//BB
	public static final int BB_LENGTH = 14;
	
	//MA
	public static final int MA_FAST_LENGTH = 5;
	public static final int MA_SLOW_LENGTH = 13;
	
	//Stochastic
	public static final int STOCHASTIC_K_LENGTH = 10;
	public static final int STOCHASTIC_D_LENGTH = 3;
	public static final double STOCHASTIC_UPPER = 80;
	public static final double STOCHASTIC_LOWER = 20;
	
	//Cut loss & lock profit
	public static final double INIT_CUT_WIN_LOSS_TOTAL = - 0.05; //absolute total cut win/loss, priceDiff * Qty 
	//public static final double PROFIT_LOSS = - 0.05 * TRADE_UNIT; //absolute price loss from previous profit  	
	
	//Pattern weight
	public static final int PATTERN_WEIGHT_MA = 1;
	public static final int PATTERN_WEIGHT_RSI = 1;
	public static final int PATTERN_WEIGHT_STO = 1;
	public static final int PATTERN_WEIGHT_HL = 1;
	
	//PatternHighLow
	public static final int HIGH_LOW_LIST_LENGTH =2000;//2 is the min useful size
	public static final boolean ALTERNATIVE_HIGH_LOW_POINTS = true; //high low alternatively
	
}
