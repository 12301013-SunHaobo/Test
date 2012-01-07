package modules.at.model;

/**
 * Singleton Position class
 * 
 *
 */
public class Position {
	
	private static Position instance;
	
	private int qty = 0; //- short, + long
	private double entryPrice = Double.NaN;

	private double stopLossPrice = Double.NaN;
	
	private Position() {
		super();
	}

	public static synchronized Position getInstance(){
		if(instance == null){
			instance = new Position();
		}
		return instance;
	}
	
	public void updateStopPrice(double curPrice){
		if(this.qty>0){//keep increasing cut loss price for lone
			if(Double.isNaN(stopLossPrice)){
				this.stopLossPrice = 0;
			}
			this.stopLossPrice = Math.max(curPrice*(1-AlgoSetting.cutLoss), this.stopLossPrice);
		} else if(this.qty<0){//keep decreasing for short
			if(Double.isNaN(stopLossPrice)){
				this.stopLossPrice = Double.MAX_VALUE;
			}
			this.stopLossPrice = Math.min(curPrice*(1+AlgoSetting.cutLoss), this.stopLossPrice);
		} else {//qty==0
			this.stopLossPrice = Double.NaN;
		}
	}
	
	
	
	public void setPosition(int qty, double entryPrice){
		this.qty = qty;
		this.entryPrice = entryPrice;
		this.stopLossPrice = Double.NaN;
		updateStopPrice(entryPrice);
	}
	
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getPrice() {
		return entryPrice;
	}
	public void setPrice(double entryPrice) {
		this.entryPrice = entryPrice;
	}

	public double getEntryPrice() {
		return entryPrice;
	}

	public void setEntryPrice(double entryPrice) {
		this.entryPrice = entryPrice;
	}

	public double getStopLossPrice() {
		return stopLossPrice;
	}

	public void setStopLossPrice(double stopLossPrice) {
		this.stopLossPrice = stopLossPrice;
	}



}
