package modules.at.model;

import java.util.LinkedList;
import java.util.Queue;

public class BarSeries {
    
    int period;
    private final Queue<Bar> window = new LinkedList<Bar>();
    
    double sumClose;
    
    public void addBar(Bar bar){
        sumClose += bar.getClose();
        window.add(bar);
        if (window.size() > period) {
            sumClose -= window.remove().getClose();
        }
    }
    
    public double getAvg() throws Exception {
        if(window.size()<period){
            throw new Exception("data less than period, cannot calculate.");
        }
        if(period==0){
            throw new Exception("Period is 0, please set period.");
        }
        return sumClose / period;
    }
    
    
}
