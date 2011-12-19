package others.nuo;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYSeries;

import utils.Formatter;

public class TestCalculation {
	
    private static final int GRACE_PERIOD = 90;
    private static boolean saveToFile = true;
    /**
     * nthDate is 1 based, date0 deposit
     */
    public static void main(String[] args) {
    	createAllCharts();
    }
    
    public static void createAllCharts(){
        double initDeposit = 10000;//2500
        double rate = 0.014;
        int totalDays = 1000;
    	for(int i=0;i<PERCENTAGES.length;i++){
    		//double percentToCash = 0.5;
    		double percentToCash = PERCENTAGES[i];
        	List<XYSeries> oneChartSeries = new ArrayList<XYSeries>();
    		List<DailyResult> results = calculateOne(initDeposit, rate, totalDays, percentToCash);
    		oneChartSeries.add(convert(results, "Points("+Formatter.DECIMAL_FORMAT.format(percentToCash)+")", ConvertType.Points));
    		oneChartSeries.add(convert(results, "Cash", ConvertType.Cash));
    		oneChartSeries.add(convert(results, "PointsGain", ConvertType.PointsGain));
    		oneChartSeries.add(convert(results, "PointsGain_(-90)", ConvertType.PointsGainBefore90));
    		
            NuoLineChart chart = new NuoLineChart("Calculation[cash%="+Formatter.DECIMAL_FORMAT.format(percentToCash)+"]", oneChartSeries, saveToFile);
            if(saveToFile){
	            String fileName = "D:/user/stock/us/screen-snapshot/MAStrategy/tmpNuo/"+i+"_[Cash%="+Formatter.DECIMAL_FORMAT.format(percentToCash)+"].png"; 
	            chart.toFile(fileName);
	            System.out.println("Saved to "+fileName);
            }
            printOut(results);
    	}
    	
    }
    
    private static List<DailyResult> calculateOne(double initDeposit, double rate, int totalDays, double percentToCash) {
        List<DailyResult> dailyResults = new ArrayList<DailyResult>();
        DailyResult preDr = new DailyResult(0, 0, initDeposit, initDeposit);
        dailyResults.add(preDr);
        for(int i=1; i<=totalDays; i++){
        	DailyResult drBefore90 = null;
        	if(i>=90){
        		drBefore90 = dailyResults.get(i-90);
        	}
            DailyResult dr = getNextDailyResult(preDr, drBefore90, rate, percentToCash);
            dailyResults.add(dr);
            preDr = dr;
        }

        return dailyResults;
    }

    private static DailyResult getNextDailyResult(DailyResult dr, DailyResult dr_90, double rate, double percentToCash){
        int nextNth = dr.getNth()+1;
        double nextGain = dr.getPoints()*rate;
        double nextTotal = dr.getPoints();
        double nextCash = dr.getCash();
        if(nextNth<90){
        	nextTotal += nextGain;
        } else {
        	nextCash += nextGain*percentToCash;
        	nextTotal += nextGain*(1-percentToCash);
        	if(dr_90!=null){
        		//nextTotal -= dr_90.getPointsGain();
        		nextTotal = Math.max(nextTotal - dr_90.getPointsGain(), 0);
        	}
        }
        
        DailyResult newDr = new DailyResult(nextNth, nextCash, nextTotal, nextGain);
        return newDr;
    }
    
    enum ConvertType {
    	Cash, Points, PointsGain, PointsGainBefore90
    }
    private static XYSeries convert(List<DailyResult> results, String legend, ConvertType convertType){
    	//TimeSeries timeseries = new TimeSeries(legend);
    	XYSeries xyseries = new XYSeries(legend);
    	
    	for(int i=0;i<results.size();i++){
    		DailyResult dr = results.get(i);
    		switch (convertType){
    			case Cash:  xyseries.add(dr.getNth(), dr.getCash()); break;
	    		case Points :  xyseries.add(dr.getNth(), dr.getPoints()); break;
	    		case PointsGain:  xyseries.add(dr.getNth(), dr.getPointsGain()); break;
	    		case PointsGainBefore90:
	    			if(90<=i){
	    				DailyResult drBefore90 = results.get(i-90);
	    				xyseries.add(dr.getNth(), drBefore90.getPointsGain());
	    			}else{
	    				xyseries.add(dr.getNth(), 0);
	    			}
	    			break;
    		}
    	}
    	return xyseries;
    }
    
    
    private static class DailyResult {
        private int nth; //0 based
        private double cash;
        private double points;
        private double PointsGain;
        
        public DailyResult(int nth, double cash, double total, double gain) {
            super();
            this.nth = nth;
            this.cash = cash;
            this.points = total;
            this.PointsGain = gain;
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

		public double getPoints() {
			return points;
		}

		public void setPoints(double points) {
			this.points = points;
		}

		public double getPointsGain() {
			return PointsGain;
		}

		public void setPointsGain(double pointsGain) {
			PointsGain = pointsGain;
		}

		@Override
        public String toString() {
            return "DailyResult [nth=" + nth + ", " +
            		"cash=" + Formatter.DECIMAL_FORMAT.format(cash) + ", " +
            		"total=" + Formatter.DECIMAL_FORMAT.format(points) + ", " +
            		"gain=" + Formatter.DECIMAL_FORMAT.format(PointsGain) + "]";
        }
        
        
    }
    
    private static double[] PERCENTAGES = new double[] {
    	//1,0.9,0.8,0.7,0.6,0.5,0.4,0.3,0.2,0.1,0
    	0.02,0.03,0.04,0.05,0.06,0.07,0.08,0.09
    };
    
    private static void printOut(List<DailyResult> dailyResults){
        for(DailyResult dr : dailyResults){
            System.out.println(dr);
        }
    }
}
