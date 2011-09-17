package modules.at.model;

import java.util.Date;

import utils.TimeUtil;

public class Bar {
	private static int idSeq = 0; //sequence number to count how many bars are created
	
    private int id;
    private Date date; //close time
    private double open;
    private double high;
    private double low;
    private double close;
    private int volume;
    
    public Bar(Date date, double open, double high, double low, double close, int volume) {
        super();
        this.id = ++idSeq;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
    
    public void addTick(Tick tick){
    	this.date = tick.getDate();
    	if(tick.getPrice()>this.high){
    		this.high = tick.getPrice();
    	}
    	if(tick.getPrice()<this.low){
    		this.low = tick.getPrice();
    	}
    	this.close = tick.getPrice();
    	this.volume += tick.getVolumn();
    }
    
    public void merge(Bar bar){
    	this.date = bar.getDate();
    	if(bar.getHigh()>this.high){
    		this.high = bar.getHigh();
    	}
    	if(bar.getLow()<this.low){
    		this.low = bar.getLow();
    	}
    	this.close = bar.getClose();
    }
    
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public double getOpen() {
        return open;
    }
    public void setOpen(double open) {
        this.open = open;
    }
    public double getHigh() {
        return high;
    }
    public void setHigh(double high) {
        this.high = high;
    }
    public double getLow() {
        return low;
    }
    public void setLow(double low) {
        this.low = low;
    }
    public double getClose() {
        return close;
    }
    public void setClose(double close) {
        this.close = close;
    }
    public int getVolume() {
        return volume;
    }
    public void setVolume(int volume) {
        this.volume = volume;
    }
	@Override
	public String toString() { 
		//return "Bar [id=" + id + ", date=" + TimeUtil.DEFAULT_DATETIME_FORMAT.format(date) + ", open=" + open + ", high=" + high + ", low=" + low + ", close=" + close + ", volume="+ volume + "]";
		return "Bar [id=" + id + ", date=" + TimeUtil.DISPLAY_DEFAULT_DATE_FORMAT.format(date) + ", o=" + open + ", h=" + high + ", l=" + low + ", c=" + close  + "]";
	}

}