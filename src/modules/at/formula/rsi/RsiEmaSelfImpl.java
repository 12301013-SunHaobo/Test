package modules.at.formula.rsi;

import java.util.LinkedList;
import java.util.Queue;

import modules.at.formula.ma.EMASelfImpl;

/**
 * RsiEma(14) stabilize at 73rd bar
 * @author r
 *
 */
public class RsiEmaSelfImpl {
    private int length;
    private final Queue<GL> rsiQ = new LinkedList<GL>(); // store recent 13 GL

    private double prePrice = -1;
    
    private EMASelfImpl gainEma;
    private EMASelfImpl lossEma;
    
    private double curRsi = Double.NaN;

    public RsiEmaSelfImpl(int length) {
        this.length = length;
        gainEma = new EMASelfImpl(length);
        lossEma = new EMASelfImpl(length);
    }

    public void addPrice(double price) {
    	//gain, loss
        GL gl = null;
        if (prePrice == -1) {
            gl = new GL(0, 0);
            rsiQ.add(gl);
        } else {
            double diff = price - prePrice;

            double gain = Math.max(0, diff);
            double loss = Math.max(0, -diff);
            gl = new GL(gain, loss);
            rsiQ.add(gl);
        }
        prePrice = price;
        if(rsiQ.size()>length){
            rsiQ.remove();
        }
        //set ema
        gainEma.addPrice(gl.getGain());
        lossEma.addPrice(gl.getLoss());
        //calculate curRsi
        if(rsiQ.size()>=length){
        	double avgGain = gainEma.getValue();
        	double avgLoss = lossEma.getValue();
            double rs = avgGain/avgLoss;
            double rsi = 100 - 100/(1+rs);
            curRsi = rsi;
        }
        
    }

    public double getValue(){
    	return curRsi;
    }
    
    @Deprecated
    public double calculate() {
        if(rsiQ.size()<length){
            return -1;
        }
        
        double totalGain = 0;
        double totalLoss = 0;

        for(GL gl : rsiQ){
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