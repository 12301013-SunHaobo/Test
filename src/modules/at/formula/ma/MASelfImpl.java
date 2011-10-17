package modules.at.formula.ma;

import java.util.LinkedList;
import java.util.Queue;
public class MASelfImpl implements MA{
    private final Queue<Double> maQ = new LinkedList<Double>(); //how many bars to store
    private final int length; //how many bars to calculate
    private double sum;
 
    public MASelfImpl(int length) {
        assert length > 0 : "Period must be a positive integer";
        this.length = length;
    }
 
    public void addPrice(double num) {
        sum += num;
        maQ.add(num);
        if (maQ.size() > length) {
            sum -= maQ.remove();
        }
    }
 
    public double getValue() {
        if (maQ.size()<this.length) return -1; // technically the average is undefined
        return sum / maQ.size();
    }
 
 
}