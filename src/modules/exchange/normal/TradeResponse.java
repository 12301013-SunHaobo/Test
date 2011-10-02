package modules.exchange.normal;

import java.io.Serializable;

import modules.exchange.normal.TradeRequest.Type;

public class TradeResponse implements Serializable {

	private static final long serialVersionUID = -2591815021156346893L;
	
	private Type type;
	private String tickCode;
	
	private String msg;

	
	public TradeResponse(Type type, String tickCode, String msg) {
		super();
		this.type = type;
		this.tickCode = tickCode;
		this.msg = msg;
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
