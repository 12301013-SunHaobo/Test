package others.nuo;

import java.util.ArrayList;
import java.util.List;

import utils.Formatter;

public class TestCalculation {

    private static final int GRACE_PERIOD = 90;
    /**
     * nthDate is 1 based, date0 deposit
     */
    public static void main(String[] args) {
        calculate();
    }
    
    private static void calculate() {
        double initDeposit = 2500;
        double rate = 0.013;
        int totalDays = 100;
        List<DailyResult> dailyResults = new ArrayList<DailyResult>();
        DailyResult preDr = new DailyResult(0, 0, initDeposit, initDeposit);
        dailyResults.add(preDr);
        for(int i=1; i<=totalDays; i++){
            DailyResult dr = getNextDailyResult(preDr, rate);
            if(i>90){
                //deduct the gain on date (n-90)
                double deduct = dailyResults.get(i-91).getGain();
                dr.setTotal(dr.getTotal()-deduct);
            }
            dailyResults.add(dr);
            preDr = dr;
        }
        
        for(DailyResult dr : dailyResults){
            System.out.println(dr);
        }
        
    }

    private static DailyResult getNextDailyResult(DailyResult dr, double rate){
        int nextNth = dr.getNth()+1;
        double nextTotal = dr.getTotal()*(1+rate);
        double nextGain = dr.getTotal()*rate;
        DailyResult newDr = new DailyResult(nextNth, 0, nextTotal, nextGain);
        return newDr;
    }
    
    private static double getNthDateTotal(double initDeposit, int nthDate, double rate) {
        return initDeposit*Math.pow((1+rate), nthDate);
    }
 
    private static double getNthDateGain(double initDeposit, int nthDate, double rate) {
        return initDeposit*Math.pow((1+rate), (nthDate-1))*rate;
    }
    
    
    private static class DailyResult {
        private int nth; //0 based
        private double cash;
        private double total;
        private double gain;
        
        public DailyResult(int nth, double cash, double total, double gain) {
            super();
            this.nth = nth;
            this.cash = cash;
            this.total = total;
            this.gain = gain;
        }
        public int getNth() {
            return nth;
        }
        public void setNth(int nth) {
            this.nth = nth;
        }
        public double getCash() {
            return cash;
        }
        public void setCash(double cash) {
            this.cash = cash;
        }
        public double getTotal() {
            return total;
        }
        public void setTotal(double total) {
            this.total = total;
        }
        public double getGain() {
            return gain;
        }
        public void setGain(double gain) {
            this.gain = gain;
        }
        @Override
        public String toString() {
            return "DailyResult [nth=" + nth + ", " +
            		"cash=" + Formatter.DECIMAL_FORMAT.format(cash) + ", " +
            		"total=" + Formatter.DECIMAL_FORMAT.format(total) + ", " +
            		"gain=" + Formatter.DECIMAL_FORMAT.format(gain) + "]";
        }
        
        
    }
}
