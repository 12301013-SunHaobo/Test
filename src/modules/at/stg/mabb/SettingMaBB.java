package modules.at.stg.mabb;

import modules.at.stg.Setting;

public class SettingMaBB extends Setting{
    	private static int idSeq = 0; //sequence number to count how many bars are created
    	private int id;
    	
    	private int barTimePeriod = 1 * 60 * 1000 ; //milliseconds
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
    	//Cut loss & lock profit
    	//private double CUT_WIN = 0.001; //increase percentage % to cut win
    	private double cutLoss = 0.009; //drop percentage % to cut loss, 0.005 covers most ranges of (high-avg)
    	//trade direction
    	private TradeDirection tradeDirection = TradeDirection.LongOnly;
    	
    	public SettingMaBB() {
    		super();
    		this.id = ++idSeq;
    	}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getBarTimePeriod() {
			return barTimePeriod;
		}

		public void setBarTimePeriod(int barTimePeriod) {
			this.barTimePeriod = barTimePeriod;
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

		public double getCutLoss() {
			return cutLoss;
		}

		public void setCutLoss(double cutLoss) {
			this.cutLoss = cutLoss;
		}

		public TradeDirection getTradeDirection() {
			return tradeDirection;
		}

		public void setTradeDirection(TradeDirection tradeDirection) {
			this.tradeDirection = tradeDirection;
		}
    	
    	
    }