package modules.at.model;

import java.util.Date;

import utils.Formatter;

public class Point {
	
	private static int idSeq = 0; //sequence number to count how many bars are created
	
	public static enum Type {
		HIGH, LOW
	}
	
	private Type type;
	
	private int id;
	private Date dateTime;
	private double price;
	
	private Point next;
	
	public Point(Type type, Date dateTime, double price) {
		super();
		this.id = ++idSeq;
		this.type = type;
		this.dateTime = dateTime;
		this.price = price;
	}

	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Point getNext() {
		return next;
	}

	public void setNext(Point next) {
		this.next = next;
	}


	@Override
	public String toString() {
		return "Point [id="+id+", type=" + type + ", dateTime=" + Formatter.DISPLAY_DEFAULT_DATE_FORMAT.format(dateTime) + ", price=" + price + "]";
	}
	
	
	
	
}
