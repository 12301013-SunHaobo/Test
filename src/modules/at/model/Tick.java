package modules.at.model;

import java.util.Date;

import utils.Formatter;

public class Tick {
	private static int idSeq = 0; //sequence number to count how many bars are created
	
    int id;
    Date date;
    double price;
    int volumn;
    
    public Tick(Date date, double price, int volumn) {
		super();
		this.id = ++idSeq;
		this.date = date;
		this.price = price;
		this.volumn = volumn;
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
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getVolumn() {
        return volumn;
    }
    public void setVolumn(int volumn) {
        this.volumn = volumn;
    }
    @Override
    public String toString() {
        return "Tick [id=" + id + ", date=" + Formatter.DEFAULT_DATETIME_FORMAT.format(date) + ", price=" + price + ", volumn=" + volumn + "]";
    }
    
    
}
