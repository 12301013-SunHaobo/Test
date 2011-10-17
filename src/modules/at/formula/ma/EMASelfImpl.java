package modules.at.formula.ma;


/**
 * EMA(13) stabilize at 59th bar
 * @author r
 *
 */
public class EMASelfImpl implements MA{
	private final int length; // how many bars to calculate
	private double preEma = Double.NaN;
	private double curEma = Double.NaN;       

	private double initSum = 0;
	
	private int barCounter = 0;
	
	public EMASelfImpl(int length) {
		assert length > 0 : "Period must be a positive integer";
		this.length = length;
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
