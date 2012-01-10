package modules.at.model;

import modules.at.stg.Setting;
import modules.at.stg.other.Strategy.Decision;

/**
 * Singleton Position class
 * 
 *
 */
public class Position {

	private Setting as = null;
	private static Position instance;
	
	private int qty = 0; //- short, + long
	private double entryPrice = 0;

	private double stopLossPrice = Double.NaN;
	
	private Position(Setting as) {
		super();
		this.as = as;
	}

	public static synchronized Position getInstance(Setting as){
		if(instance == null){
			instance = new Position(as);
		}
		return instance;
	}
	
	public void updateStopPrice(double curPrice){
		if(this.qty>0){//keep increasing cut loss price for lone
			if(Double.isNaN(stopLossPrice)){
				this.stopLossPrice = 0;
			}
			this.stopLossPrice = Math.max(curPrice*(1-this.as.getCutLoss()), this.stopLossPrice);
		} else if(this.qty<0){//keep decreasing for short
			if(Double.isNaN(stopLossPrice)){
				this.stopLossPrice = Double.MAX_VALUE;
			}
			this.stopLossPrice = Math.min(curPrice*(1+this.as.getCutLoss()), this.stopLossPrice);
		} else {//qty==0
			this.stopLossPrice = Double.NaN;
		}
	}
	
	public void updatePosition(int qty, double curPrice) {
		this.entryPrice = (this.qty*this.entryPrice + qty*curPrice)/(this.qty+qty);
		this.qty += qty;
		this.stopLossPrice = Double.NaN;
		updateStopPrice(curPrice);
	}
	
	public Decision getCutLossDecision(Bar bar){
		//check if need to cutloss for next bar
		//lock profit & cut loss checking, including short|long
		if(this.qty>0){//cut loss for long
			if(bar.getLow()<this.stopLossPrice){
				return Decision.CutLossForLong;
			}
		}else if(this.qty<0){//cut loss for short
			if(bar.getHigh()>this.stopLossPrice){
				return Decision.CutLossForShort;
			}
		}
		return Decision.NA;
	}
	
//	public void setPosition(int qty, double entryPrice){
//		this.qty = qty;
//		this.entryPrice = entryPrice;
//		this.stopLossPrice = Double.NaN;
//		updateStopPrice(entryPrice);
//	}
	
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
