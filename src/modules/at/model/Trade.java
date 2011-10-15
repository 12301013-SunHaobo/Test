package modules.at.model;

import java.util.Date;

import utils.Formatter;

public class Trade {
	private static int idSeq = 0; //sequence number to count how many trades are created
	
	public enum Type {
		Long, Sell, 
		Short, CoverShort, 
		CutLoss, 
		WrapUp  
	}
	int id;
	double price;
	int qty;
	long dateTime;
	Type type;
	
	

	
	
	public Trade(double price, int qty, long dateTime, Type type) {
		super();
		this.id = ++idSeq;
		this.price = price;
		this.qty = qty;
		this.dateTime = dateTime;
		this.type = type;
		
	}
	
	@Override
	public String toString() {
		return "Trade [id="+id+", type="+type+", price=" + price + ", qty=" + qty + ", dateTime=" + Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(dateTime)) + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public long getDateTime() {
		return dateTime;
	}
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
}
