package modules.at.analyze;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import modules.at.model.Point;
import modules.at.pattern.HighLowMatch;
import modules.at.pattern.HighLowPattern;

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
    	
    	HighLowPattern hlPattern = new HighLowPattern();
    	Set<HighLowPattern.Type> typeFilter = new HashSet<HighLowPattern.Type>();
    	typeFilter.add(HighLowPattern.Type.HighHighLowHigh);
    	List<HighLowMatch>hlMatchList = hlPattern.match(highLowPointList,typeFilter);
    	
    	System.out.println("Total match:"+hlMatchList.size());
    	for(HighLowMatch oneMatch : hlMatchList){
    		System.out.println("<<--------- one match ------------------->> "+oneMatch.getType());
    		System.out.println(oneMatch);
    	}
    	
    	

    		

		
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
    	
    	System.out.println("<< ============ All high&low points =====================================>>");
    	for(Point p : highLowPoints){
   			System.out.println(p);
    	}
    	/*
    	System.out.println("<< ============ All high points =====================================>>");
    	for(Point p : highLowPoints){
    		if(Point.Type.HIGH.equals(p.getType())){
    			System.out.println(p);
    		}
    	}
    	System.out.println("<< ============ All low points =====================================>>");
    	for(Point p : highLowPoints){
    		if(Point.Type.LOW.equals(p.getType())){
    			System.out.println(p);
    		}
    	}
    	*/
    	
    }
    
    
}
