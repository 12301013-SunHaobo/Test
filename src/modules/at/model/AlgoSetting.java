package modules.at.model;

public class AlgoSetting {

	public static final int BAR_TIME_PERIOD = 1 * 60 * 1000 ; //milliseconds
	
    //trade unit, how many shares in one trade unit
    public static final int TRADE_UNIT = 1;

	//RSI
	public static final int RSI_LENGTH = 14;
	public static final double RSI_UPPER = 70;
	public static final double RSI_LOWER = 30;
	
	//BB
	public static final int BB_LENGTH = 14;
	
	//MA
	public static final int MA_FAST_LENGTH = 6;//default=5
	public static final int MA_SLOW_LENGTH = 14;//default=14
	public static final int MA_3_LENGTH = 30; 
	
	//Stochastic
	public static final int STOCHASTIC_K_LENGTH = 10;
	public static final int STOCHASTIC_D_LENGTH = 3;
	public static final double STOCHASTIC_UPPER = 80;
	public static final double STOCHASTIC_LOWER = 20;
	
	//my invented
	public static final int MA_UPPER_SHADOW_LENGTH = 3;
	
	//Cut loss & lock profit
	//public static final double CUT_WIN = 0.001; //increase percentage % to cut win
	public static final double CUT_LOSS = 0.005; //drop percentage % to cut loss
	
	//public static final double PROFIT_LOSS = - 0.05 * TRADE_UNIT; //absolute price loss from previous profit  	
	
	//Pattern weight
	public static final int PATTERN_WEIGHT_MA = 1;
	public static final int PATTERN_WEIGHT_RSI = 1;
	public static final int PATTERN_WEIGHT_STO = 1;
	public static final int PATTERN_WEIGHT_HL = 1;
	
	//PatternHighLow
	public static final int HIGH_LOW_LIST_LENGTH =2000;//2 is the min useful size
	public static final boolean ALTERNATIVE_HIGH_LOW_POINTS = true; //high low alternatively
	
	//PatternEngulf
	public static final int ENGULF_LIST_LENGTH =500;//saves all engulfing bars
	
	
	//trade direction
	public static final TradeDirection TRADE_DIRECTION = TradeDirection.LongOnly;
	public enum TradeDirection {
		LongOnly, ShortOnly, Both
	}
	
}
