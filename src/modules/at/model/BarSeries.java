package modules.at.model;

import java.util.LinkedList;
import java.util.Queue;

public class BarSeries {
    
    int size;
    private final Queue<Bar> window = new LinkedList<Bar>();
    
    double sumClose;
    
    public void addBar(Bar bar){
        sumClose += bar.getClose();
        window.add(bar);
        if (window.size() > size) {
            sumClose -= window.remove().getClose();
        }
    }
    
}
