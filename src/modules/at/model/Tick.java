package modules.at.model;

import java.util.Date;

import utils.TimeUtil;

public class Tick {
    int id;
    Date date;
    double price;
    int volumn;
    
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
        return "Tick [id=" + id + ", date=" + TimeUtil.TICK_TIME_FORMAT.format(date) + ", price=" + price + ", volumn=" + volumn + "]";
    }
    
    
}
