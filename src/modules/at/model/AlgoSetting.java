package modules.at.model;

public class AlgoSetting {

	public int barTimePeriod = 1 * 60 * 1000 ; //milliseconds
	
    //trade unit, how many shares in one trade unit
    public int tradeUnit = 1;

	//MA ------------------------------------------------------
	public int maFastLength = 5;//default=5
	public int maSlowLength = 14;//default=14
	public int ma3Length = 25; 
	public int maHLLength = 50;
	public int maHigh2Length = maHLLength;
	public int maHighLength = 5;//5
	public int maLowLength = 5; //5
	public int maLow2Length = maHLLength;//5
	
	//BB ------------------------------------------------------
	public int bbLength = 14;
	
	//RSI ------------------------------------------------------
	public int rsiLength = 14;
	public double rsiUpper = 70;
	public double rsiLower = 30;
	
	//Stochastic ------------------------------------------------------
	public int stochasticKLength = 10;
	public int stochastickDLength = 3;
	public double stochasticUpper = 80;
	public double stochasticLower = 20;
	
	//Macd ------------------------------------------------------
	public int macdMAFastLength = 50; //12
	public int macdMASlowLength = 75; //26
	public int macdLength = 9; //9
	
	//my invented ------------------------------------------------------
	public int maUpperShadowLength = 3;
	
	//Cut loss & lock profit
	//public double CUT_WIN = 0.001; //increase percentage % to cut win
	public double cutLoss = 0.001; //drop percentage % to cut loss
	
	//public double PROFIT_LOSS = - 0.05 * TRADE_UNIT; //absolute price loss from previous profit  	
	
	//Pattern weight
	public int patternWeightMA = 1;
	public int patternWeightRSI = 1;
	public int patternWeightSTO = 1;
	public int patternWeightHL = 1;
	
	//PatternHighLow
	public int highLowListLength =2000;//2 is the min useful size
	public boolean alternativeHighLowPoints = true; //high low alternatively
	
	//PatternEngulf
	public int engulfListLength =500;//saves all engulfing bars
	
	
	//trade direction
	public TradeDirection tradeDirection = TradeDirection.LongOnly;
	public static enum TradeDirection {
		LongOnly, ShortOnly, Both
	}
	
	
	
	//setters & getters
	public int getBarTimePeriod() {
		return barTimePeriod;
	}
	public void setBarTimePeriod(int barTimePeriod) {
		this.barTimePeriod = barTimePeriod;
	}
	public int getTradeUnit() {
		return tradeUnit;
	}
	public void setTradeUnit(int tradeUnit) {
		this.tradeUnit = tradeUnit;
	}
	public int getMaFastLength() {
		return maFastLength;
	}
	public void setMaFastLength(int maFastLength) {
		this.maFastLength = maFastLength;
	}
	public int getMaSlowLength() {
		return maSlowLength;
	}
	public void setMaSlowLength(int maSlowLength) {
		this.maSlowLength = maSlowLength;
	}
	public int getMa3Length() {
		return ma3Length;
	}
	public void setMa3Length(int ma3Length) {
		this.ma3Length = ma3Length;
	}
	public int getMaHLLength() {
		return maHLLength;
	}
	public void setMaHLLength(int maHLLength) {
		this.maHLLength = maHLLength;
	}
	public int getMaHigh2Length() {
		return maHigh2Length;
	}
	public void setMaHigh2Length(int maHigh2Length) {
		this.maHigh2Length = maHigh2Length;
	}
	public int getMaHighLength() {
		return maHighLength;
	}
	public void setMaHighLength(int maHighLength) {
		this.maHighLength = maHighLength;
	}
	public int getMaLowLength() {
		return maLowLength;
	}
	public void setMaLowLength(int maLowLength) {
		this.maLowLength = maLowLength;
	}
	public int getMaLow2Length() {
		return maLow2Length;
	}
	public void setMaLow2Length(int maLow2Length) {
		this.maLow2Length = maLow2Length;
	}
	public int getBbLength() {
		return bbLength;
	}
	public void setBbLength(int bbLength) {
		this.bbLength = bbLength;
	}
	public int getRsiLength() {
		return rsiLength;
	}
	public void setRsiLength(int rsiLength) {
		this.rsiLength = rsiLength;
	}
	public double getRsiUpper() {
		return rsiUpper;
	}
	public void setRsiUpper(double rsiUpper) {
		this.rsiUpper = rsiUpper;
	}
	public double getRsiLower() {
		return rsiLower;
	}
	public void setRsiLower(double rsiLower) {
		this.rsiLower = rsiLower;
	}
	public int getStochasticKLength() {
		return stochasticKLength;
	}
	public void setStochasticKLength(int stochasticKLength) {
		this.stochasticKLength = stochasticKLength;
	}
	public int getStochastickDLength() {
		return stochastickDLength;
	}
	public void setStochastickDLength(int stochastickDLength) {
		this.stochastickDLength = stochastickDLength;
	}
	public double getStochasticUpper() {
		return stochasticUpper;
	}
	public void setStochasticUpper(double stochasticUpper) {
		this.stochasticUpper = stochasticUpper;
	}
	public double getStochasticLower() {
		return stochasticLower;
	}
	public void setStochasticLower(double stochasticLower) {
		this.stochasticLower = stochasticLower;
	}
	public int getMacdMAFastLength() {
		return macdMAFastLength;
	}
	public void setMacdMAFastLength(int macdMAFastLength) {
		this.macdMAFastLength = macdMAFastLength;
	}
	public int getMacdMASlowLength() {
		return macdMASlowLength;
	}
	public void setMacdMASlowLength(int macdMASlowLength) {
		this.macdMASlowLength = macdMASlowLength;
	}
	public int getMacdLength() {
		return macdLength;
	}
	public void setMacdLength(int macdLength) {
		this.macdLength = macdLength;
	}
	public int getMaUpperShadowLength() {
		return maUpperShadowLength;
	}
	public void setMaUpperShadowLength(int maUpperShadowLength) {
		this.maUpperShadowLength = maUpperShadowLength;
	}
	public double getCutLoss() {
		return cutLoss;
	}
	public void setCutLoss(double cutLoss) {
		this.cutLoss = cutLoss;
	}
	public int getPatternWeightMA() {
		return patternWeightMA;
	}
	public void setPatternWeightMA(int patternWeightMA) {
		this.patternWeightMA = patternWeightMA;
	}
	public int getPatternWeightRSI() {
		return patternWeightRSI;
	}
	public void setPatternWeightRSI(int patternWeightRSI) {
		this.patternWeightRSI = patternWeightRSI;
	}
	public int getPatternWeightSTO() {
		return patternWeightSTO;
	}
	public void setPatternWeightSTO(int patternWeightSTO) {
		this.patternWeightSTO = patternWeightSTO;
	}
	public int getPatternWeightHL() {
		return patternWeightHL;
	}
	public void setPatternWeightHL(int patternWeightHL) {
		this.patternWeightHL = patternWeightHL;
	}
	public int getHighLowListLength() {
		return highLowListLength;
	}
	public void setHighLowListLength(int highLowListLength) {
		this.highLowListLength = highLowListLength;
	}
	public boolean isAlternativeHighLowPoints() {
		return alternativeHighLowPoints;
	}
	public void setAlternativeHighLowPoints(boolean alternativeHighLowPoints) {
		this.alternativeHighLowPoints = alternativeHighLowPoints;
	}
	public int getEngulfListLength() {
		return engulfListLength;
	}
	public void setEngulfListLength(int engulfListLength) {
		this.engulfListLength = engulfListLength;
	}
	public TradeDirection getTradeDirection() {
		return tradeDirection;
	}
	public void setTradeDirection(TradeDirection tradeDirection) {
		this.tradeDirection = tradeDirection;
	}
	

	
}
