package modules.at.formula.rsi;

import java.util.LinkedList;
import java.util.Queue;

public class Rsi {
    private int length;
    private final Queue<GL> rQ = new LinkedList<GL>(); // store recent 13 GL

    private double prePrice = -1;

    public Rsi(int length) throws Exception {
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