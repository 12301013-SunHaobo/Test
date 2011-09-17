package modules.at.formula.ma;


public class EMA {
	private final int length; // how many bars to calculate
	private double preEma = Double.NaN;
	private double curEma = Double.NaN;       

	private double initSum = 0;
	
	private int barCounter = 0;
	
	public EMA(int period) {
		assert period > 0 : "Period must be a positive integer";
		this.length = period;
	}

	public void addPrice(double price) {
		barCounter++;
		if(barCounter<length){
			initSum+=price;
		} else if(barCounter==length){
			curEma = (initSum+price)/length;
			preEma = curEma;
		} else {
			double k = 2d/(length+1); //multiplier
			curEma = (price-preEma)*k + preEma;
			preEma = curEma;
		}
	}

	public double getValue() {
		return curEma;
	}

}
