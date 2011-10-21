package modules.at.model;

/**
 * Singleton Position class
 * 
 *
 */
public class Position {
	
	private static Position instance;
	
	int qty;
	double price;
	
	//absolute cut price relative to price, e.g cutLossTotal = 10, then cut if (curPrice-this.price)*qty< cutLossTotal
	double cutWinLossTotal;

	private Position() {
		super();
		this.qty = 0;
		this.price = 0;
		this.cutWinLossTotal = AlgoSetting.INIT_CUT_WIN_LOSS_TOTAL;
	}

	public static synchronized Position getInstance(){
		if(instance == null){
			instance = new Position();
		}
		return instance;
	}
	
	public void setPosition(int qty, double price){
		this.qty = qty;
		this.price = price;
	}
	
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public double getCutWinLossTotal() {
		return cutWinLossTotal;
	}

	public void setCutWinLossTotal(double cutWinLossTotal) {
		this.cutWinLossTotal = cutWinLossTotal;
	}


}
