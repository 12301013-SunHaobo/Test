package modules.at.model;

public class Position {
	int qty;
	double price;

	public Position() {
		super();
		this.qty = 0;
		this.price = 0;
	}
	public Position(int qty, double price) {
		super();
		this.qty = qty;
		this.price = price;
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
}
