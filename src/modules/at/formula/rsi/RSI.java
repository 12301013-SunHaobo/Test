package modules.at.formula.rsi;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import modules.at.model.Bar;

public class RRecal {
    private int length;
    private final Queue<GL> rQ = new LinkedList<GL>(); // store recent 13 GL

    private double prePrice = -1;

    public RRecal(int length) throws Exception {
        this.length = length;
    }

    public void addPrice(double price) {
        GL gl = null;
        if (prePrice == -1) {
            gl = new GL(0, 0);
            rQ.add(gl);
        } else {
            double diff = price - prePrice;

            double gain = Math.max(0, diff);
            double loss = Math.max(0, -diff);
            gl = new GL(gain, loss);
            rQ.add(gl);
        }
        prePrice = price;
        if(rQ.size()>length){
            rQ.remove();
        }
    }

    public double calculate() {
        if(rQ.size()<length){
            return -1;
        }
        
        double totalGain = 0;
        double totalLoss = 0;

        for(GL gl : rQ){
            totalGain+=gl.getGain();
            totalLoss+=gl.getLoss();
        }
        double avgGain = totalGain/length;
        double avgLoss = totalLoss/length;
        
        double rs = avgGain/avgLoss;
        double rsi = 100 - 100/(1+rs);
        
        return rsi;
    }

    /*
     * 
     * public double calculate() { double value = 0; int pricesSize =
     * rsiQ.size(); int lastPrice = pricesSize - 1; int firstPrice = lastPrice -
     * length + 1;
     * 
     * double gains = 0; double losses = 0; double avgUp = 0; double avgDown =
     * 0;
     * 
     * double delta = rsiQ.get(lastPrice).getClose() - rsiQ.get(lastPrice -
     * 1).getClose(); gains = Math.max(0, delta); losses = Math.max(0, -delta);
     * 
     * if (avgList.isEmpty()) { for (int bar = firstPrice + 1; bar <= lastPrice;
     * bar++) { double change = rsiQ.get(bar).getClose() - rsiQ.get(bar -
     * 1).getClose(); gains += Math.max(0, change); losses += Math.max(0,
     * -change); } avgUp = gains / length; avgDown = losses / length;
     * avgList.push(new Averages(avgUp, avgDown));
     * 
     * } else {
     * 
     * Averages avg = avgList.pop(); avgUp = avg.getAvgUp(); avgDown =
     * avg.getAvgDown(); avgUp = ((avgUp * (length - 1)) + gains) / (length);
     * avgDown = ((avgDown * (length - 1)) + losses) / (length); avgList.add(new
     * Averages(avgUp, avgDown)); } value = 100 - (100 / (1 + (avgUp /
     * avgDown)));
     * 
     * return Math.round(value); }
     */

    // both are positive values
    private class GL {
        double gain;
        double loss;

        public GL(double gain, double loss) {
            super();
            this.gain = gain;
            this.loss = loss;
        }

        public double getGain() {
            return gain;
        }

        public void setGain(double gain) {
            this.gain = gain;
        }

        public double getLoss() {
            return loss;
        }

        public void setLoss(double loss) {
            this.loss = loss;
        }

    }
}