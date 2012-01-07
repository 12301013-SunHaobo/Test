package modules.at.model;

public class AlgoSetting {

	public static final int barTimePeriod = 1 * 60 * 1000 ; //milliseconds
	
    //trade unit, how many shares in one trade unit
    public static final int tradeUnit = 1;

	//MA ------------------------------------------------------
	public static final int maFastLength = 5;//default=5
	public static final int maSlowLength = 14;//default=14
	public static final int ma3Length = 25; 
	public static final int maHLLength = 50;
	public static final int maHigh2Length = maHLLength;
	public static final int maHighLength = 5;//5
	public static final int maLowLength = 5; //5
	public static final int maLow2Length = maHLLength;//5
	
	//BB ------------------------------------------------------
	public static final int bbLength = 14;
	
	//RSI ------------------------------------------------------
	public static final int rsiLength = 14;
	public static final double rsiUpper = 70;
	public static final double rsiLower = 30;
	
	//Stochastic ------------------------------------------------------
	public static final int stochasticKLength = 10;
	public static final int stochastickDLength = 3;
	public static final double stochasticUpper = 80;
	public static final double stochasticLower = 20;
	
	//Macd ------------------------------------------------------
	public static final int macdMAFastLength = 50; //12
	public static final int macdMASlowLength = 75; //26
	public static final int macdLength = 9; //9
	
	//my invented ------------------------------------------------------
	public static final int maUpperShadowLength = 3;
	
	//Cut loss & lock profit
	//public static final double CUT_WIN = 0.001; //increase percentage % to cut win
	public static final double cutLoss = 0.001; //drop percentage % to cut loss
	
	//public static final double PROFIT_LOSS = - 0.05 * TRADE_UNIT; //absolute price loss from previous profit  	
	
	//Pattern weight
	public static final int patternWeightMA = 1;
	public static final int patternWeightRSI = 1;
	public static final int patternWeightSTO = 1;
	public static final int patternWeightHL = 1;
	
	//PatternHighLow
	public static final int highLowListLength =2000;//2 is the min useful size
	public static final boolean alternativeHighLowPoints = true; //high low alternatively
	
	//PatternEngulf
	public static final int engulfListLength =500;//saves all engulfing bars
	
	
	//trade direction
	public static final TradeDirection tradeDirection = TradeDirection.LongOnly;
	public enum TradeDirection {
		LongOnly, ShortOnly, Both
	}
	
	
	
}
