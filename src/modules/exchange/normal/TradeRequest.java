package modules.exchange.normal;

import java.io.Serializable;

public class TradeRequest implements Serializable {
	
	private static final long serialVersionUID = -6726790033077409245L;

	public enum Type {
		BID_PRICE, ORDER_STATUS, PLACE_ORDER
	}
	
	private Type type;
	private String tickCode;

	//BID_PRICE
	
	//ORDER_STATUS
	
	//PLACE_ORDER
	private double price;
	private int volume;
	
	
	
	public TradeRequest(Type type, String tickCode, double price, int volume) {
		super();
		this.type = type;
		this.tickCode = tickCode;
		this.price = price;
		this.volume = volume;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTickCode() {
		return tickCode;
	}

	public void setTickCode(String tickCode) {
		this.tickCode = tickCode;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "TradeRequest [type=" + type + ", tickCode=" + tickCode + ", price=" + price + ", volume=" + volume + "]";
	}
	
	
}
