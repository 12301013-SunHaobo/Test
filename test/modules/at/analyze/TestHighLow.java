package modules.at.analyze;

import java.util.List;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import modules.at.model.Point;

public class TestHighLow {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		testDirectionAfterPattern();
		//testFindAllHighLows();
		
	}
	
	
	private static void testDirectionAfterPattern() throws Exception{
		
    	List<Bar> barList = HistoryLoader.getFChartHistBars("QQQ-20110915-mock-daily.txt");
    	List<Point> highLowPointList = ChartScanner.findHighLowPoints(barList);
		
    	double high1 = -1; //left
    	double high2 = -1; //right
    	double low1 = -1;
    	double low2 = -1;
    	
    	for(Point p : highLowPointList){
    		String pattern = getPattern(high1, high2, low1, low2);
    		if(pattern != null){
    			System.out.println(pattern);	
    		}
    		System.out.println(p);
    		if(Point.Type.HIGH.equals(p.getType())){
    			if(high1==-1){
    				high1 = p.getPrice();
    			}else if(high2==-1){
    				high2 = p.getPrice();
    			}else {
    				high1 = high2;
    				high2 = p.getPrice();
    			}
    		} else { //LOW
    			if(low1 == -1){
    				low1 = p.getPrice();
    			}else if(low2 == -1){
    				low2 = p.getPrice();
    			}else{
    				low1 = low2;
    				low2 = p.getPrice();
    			}
    		}
    		
    		
    		
    	}
		
	}
	
	private static String getPattern(double high1, double high2, double low1, double low2){
		if(high1 == -1 || high2 == -1 || low1 == -1 || low2 == -1) {
			return null;
		}
		
		if((high1 < high2) && (low1 < low2)) {
			return "HHLH-up";
		}
		if((high1 > high2) && (low1 > low2)) {
			return "HLLL-down";
		}
		return null;
	}

	/**
	 * high missing:
	 * 2011/2/28 ; 3/3; 3/25; ...
	 * @throws Exception
	 */
    
    private static void testFindAllHighLows() throws Exception{
    	//List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");
    	List<Bar> barList = HistoryLoader.getFChartHistBars("QQQ-20110915-mock-daily.txt");

    	List<Point> highLowPoints = ChartScanner.findHighLowPoints(barList);
    	
    	System.out.println("<< ============ All highLow points =====================================>>");
    	
    	for(Point p : highLowPoints){
    		System.out.println(p);
    	}
    	
    }
    
    
}
