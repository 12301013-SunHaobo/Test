package modules.at.model;

public class AlgoSetting {
	private static int idSeq = 0; //sequence number to count how many bars are created
	private int id;
	
	private int barTimePeriod = 1 * 60 * 1000 ; //milliseconds
	
	//for testing
	//@DoubleRange(start=1.0, end=2.0, intervals=10)
	private double tmp = 2;
	
    //trade unit, how many shares in one trade unit
    private int tradeUnit = 1;

	//MA ------------------------------------------------------
	private int maFastLength = 5;//default=5
	private int maSlowLength = 14;//default=14
	private int ma3Length = 25; 
	//@IntRange(start=45, end=55, intervals=5)
	private int maHLLength = 50;
	//private int maHigh2Length = -1;//-1 is dummy, getter uses maHLLength
	//@IntRange(start=5, end=15, intervals=3)
	private int maHighLength = 5;//5
	private int maLowLength = 5; //5
	//@DoubleRange(start=0.1, end=3.0, intervals=30)
	private double maHigh2BBTimes = 1.6;
	//@DoubleRange(start=0.1, end=3.0, intervals=30)
	private double maLow2BBTimes = 1.0;
	
	//BB ------------------------------------------------------
	private int bbLength = 50;
	
	//RSI ------------------------------------------------------
	private int rsiLength = 14;
	private double rsiUpper = 70;
	private double rsiLower = 30;
	
	//Stochastic ------------------------------------------------------
	private int stochasticKLength = 10;
	private int stochastickDLength = 3;
	private double stochasticUpper = 80;
	private double stochasticLower = 20;
	
	//Macd ------------------------------------------------------
	private int macdMAFastLength = 50; //12
	private int macdMASlowLength = 75; //26
	private int macdLength = 9; //9
	
	//my invented ------------------------------------------------------
	private int maUpperShadowLength = 3;
	
	//Cut loss & lock profit
	//private double CUT_WIN = 0.001; //increase percentage % to cut win
	private double cutLoss = 0.009; //drop percentage % to cut loss, 0.005 covers most ranges of (high-avg)
	
	//private double PROFIT_LOSS = - 0.05 * TRADE_UNIT; //absolute price loss from previous profit  	
	
	//Pattern weight
	private int patternWeightMA = 1;
	private int patternWeightRSI = 1;
	private int patternWeightSTO = 1;
	private int patternWeightHL = 1;
	
	//PatternHighLow
	private int highLowListLength =2000;//2 is the min useful size
	private boolean alternativeHighLowPoints = true; //high low alternatively
	
	//PatternEngulf
	private int engulfListLength =500;//saves all engulfing bars
	
	
	//trade direction
	private TradeDirection tradeDirection = TradeDirection.LongOnly;
	public static enum TradeDirection {
		LongOnly, ShortOnly, Both
	}
	
	
	
	public AlgoSetting() {
		super();
		this.id = ++idSeq;
	}
	//setters & getters
	
	public int getBarTimePeriod() {
		return barTimePeriod;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
		//return maHigh2Length;
		return maHLLength;
	}
//	public void setMaHigh2Length(int maHigh2Length) {
//		this.maHigh2Length = maHigh2Length;
//	}
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
		//return maLow2Length;
		return maHLLength;
	}
//	public void setMaLow2Length(int maLow2Length) {
//		this.maLow2Length = maLow2Length;
//	}
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
	public double getTmp() {
		return tmp;
	}
	public void setTmp(double tmp) {
		this.tmp = tmp;
	}
	public double getMaHigh2BBTimes() {
		return maHigh2BBTimes;
	}
	public void setMaHigh2BBTimes(double maHigh2BBTimes) {
		this.maHigh2BBTimes = maHigh2BBTimes;
	}
	public double getMaLow2BBTimes() {
		return maLow2BBTimes;
	}
	public void setMaLow2BBTimes(double maLow2BBTimes) {
		this.maLow2BBTimes = maLow2BBTimes;
	}
	@Override
	public String toString() {
		return "AlgoSetting [id="+id+", barTimePeriod=" + barTimePeriod + ", tradeUnit=" + tradeUnit + ", maFastLength=" + maFastLength
				+ ", maSlowLength=" + maSlowLength + ", ma3Length=" + ma3Length + ", maHLLength=" + maHLLength + ", maHigh2Length="
				+ this.getMaHigh2Length() + ", maHighLength=" + maHighLength + ", maLowLength=" + maLowLength + ", maLow2Length=" + this.getMaLow2Length()
				+ ", bbLength=" + bbLength + ", rsiLength=" + rsiLength + ", rsiUpper=" + rsiUpper + ", rsiLower=" + rsiLower
				+ ", stochasticKLength=" + stochasticKLength + ", stochastickDLength=" + stochastickDLength + ", stochasticUpper="
				+ stochasticUpper + ", stochasticLower=" + stochasticLower + ", macdMAFastLength=" + macdMAFastLength
				+ ", macdMASlowLength=" + macdMASlowLength + ", macdLength=" + macdLength + ", maUpperShadowLength=" + maUpperShadowLength
				+ ", cutLoss=" + cutLoss + ", patternWeightMA=" + patternWeightMA + ", patternWeightRSI=" + patternWeightRSI
				+ ", patternWeightSTO=" + patternWeightSTO + ", patternWeightHL=" + patternWeightHL + ", highLowListLength="
				+ highLowListLength + ", alternativeHighLowPoints=" + alternativeHighLowPoints + ", engulfListLength=" + engulfListLength
				+ ", tradeDirection=" + tradeDirection + "]";
	}
	

	
}
