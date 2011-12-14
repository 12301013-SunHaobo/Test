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
        double initDeposit = 2500;
        double rate = 0.013;
        int totalDays = 360;
    	for(int i=0;i<PERCENTAGES.length;i++){
    		//double percentToCash = 0.5;
    		double percentToCash = PERCENTAGES[i];
        	List<XYSeries> oneChartSeries = new ArrayList<XYSeries>();
    		List<DailyResult> results = calculateOne(initDeposit, rate, totalDays, percentToCash);
    		oneChartSeries.add(convert(results, "Points_("+Formatter.DECIMAL_FORMAT.format(percentToCash)+")", ConvertType.Points));
    		oneChartSeries.add(convert(results, "Cash_"+i, ConvertType.Cash));
    		oneChartSeries.add(convert(results, "PointsGain_"+i, ConvertType.PointsGain));
    		
            NuoLineChart chart = new NuoLineChart("Calculation[cash%="+Formatter.DECIMAL_FORMAT.format(percentToCash)+"]", oneChartSeries, saveToFile);
            if(saveToFile){
	            String fileName = "D:/user/stock/us/screen-snapshot/MAStrategy/tmpNuo/"+i+"_[Cash%="+Formatter.DECIMAL_FORMAT.format(percentToCash)+"].png"; 
	            chart.toFile(fileName);
	            System.out.println("Saved to "+fileName);
            }
            //printOut(results);
    	}
    	
    }
    
    private static List<DailyResult> calculateOne(double initDeposit, double rate, int totalDays, double percentToCash) {
        List<DailyResult> dailyResults = new ArrayList<DailyResult>();
        DailyResult preDr = new DailyResult(0, 0, initDeposit, initDeposit);
        dailyResults.add(preDr);
        for(int i=1; i<=totalDays; i++){
            DailyResult dr = getNextDailyResult(preDr, rate);
            if(i>GRACE_PERIOD){
                //deduct the gain on date (n-90)
                double deduct = dailyResults.get(i-91).getPointsGain();
                double pointsAfterDeduct = Math.max(dr.getPoints()-deduct, 0);
                double pointsGainAfterDeduct = 0;
                if(deduct<preDr.getPoints()){
                	pointsGainAfterDeduct = dr.getPointsGain();
                } else if(preDr.getPoints()<=deduct && deduct<(dr.getPoints())){
                	pointsGainAfterDeduct = dr.getPoints()-deduct;
                } else {
                	pointsGainAfterDeduct = 0;
                }
                
                double toCash = pointsGainAfterDeduct*percentToCash;
                dr.setPoints(pointsAfterDeduct);
                dr.setCash(dr.getCash()+toCash);
            }
            dailyResults.add(dr);
            preDr = dr;
        }

        return dailyResults;
    }

    private static DailyResult getNextDailyResult(DailyResult dr, double rate){
        int nextNth = dr.getNth()+1;
        double nextTotal = dr.getPoints()*(1+rate);
        double nextGain = dr.getPoints()*rate;
        DailyResult newDr = new DailyResult(nextNth, dr.getCash(), nextTotal, nextGain);
        return newDr;
    }
    
    enum ConvertType {
    	Cash, Points, PointsGain
    }
    private static XYSeries convert(List<DailyResult> results, String legend, ConvertType convertType){
    	//TimeSeries timeseries = new TimeSeries(legend);
    	XYSeries xyseries = new XYSeries(legend);
    	
    	for(DailyResult dr : results){
    		switch (convertType){
    			case Cash:  xyseries.add(dr.getNth(), dr.getCash()); break;
	    		case Points :  xyseries.add(dr.getNth(), dr.getPoints()); break;
	    		case PointsGain:  xyseries.add(dr.getNth(), dr.getPointsGain()); break;
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
    	1,0.95, 0.9, 0.85, 0.8, 0.75, 0.7, 0.65, 0.6, 0.55, 0.5, 0.45, 0.4, 0.35, 0.3, 0.25, 0.2, 0.15, 0.1, 0.05, 0
    };
    
    private static void printOut(List<DailyResult> dailyResults){
        for(DailyResult dr : dailyResults){
            System.out.println(dr);
        }
    }
}
