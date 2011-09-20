package modules.at.analyze;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import modules.at.model.Point;
import modules.at.pattern.highlow.HighLowMatch;
import modules.at.pattern.highlow.HighLowPattern;

import org.apache.commons.lang.StringUtils;

import utils.Formatter;

public class TestHighLow {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		testDirectionAfterPattern();
		//testFindAllHighLows();
		
	}
	
	//HighLowHighLow, (next high - last low) 
	public static void testNextSpread(){
		
	}
	
	
	public static void testDirectionAfterPattern() throws Exception{
		
		String mockDateStr = "20110916";
    	List<Bar> barList = HistoryLoader.getFChartHistBars("QQQ-"+mockDateStr+"-mock-daily.txt");
    	List<Point> highLowPointList = HighLowPattern.findHighLowPoints(barList);
    	
    	HighLowPattern hlPattern = new HighLowPattern();
    	//add wanted types
    	Set<HighLowPattern.Type> typeFilter = new HashSet<HighLowPattern.Type>();
    	
    	HighLowPattern.Type testType = HighLowPattern.Type.HighLowLowLow;
    	//typeFilter.add(HighLowPattern.Type.HighHighLowHigh);
    	typeFilter.add(testType);
    	//find all matched patters
    	List<HighLowMatch>hlMatchList = hlPattern.match(highLowPointList,typeFilter);
    	
    	System.out.println("<<------------------------------------------------------->> ");
    	System.out.println("Total match:"+hlMatchList.size()+" for "+ testType + " on "+ mockDateStr);
    	Map<HighLowPattern.Type, Integer> statistics = new HashMap<HighLowPattern.Type, Integer>();
    	for(HighLowMatch oneMatch : hlMatchList){
    		//System.out.println("<<--------- one match ------------------->> ");
    		//System.out.println(oneMatch.getType());
    		HighLowPattern.Type nextTrendType = getNextTrendType(oneMatch);
    		//System.out.println(nextTrendType);
    		countType(statistics, nextTrendType);
    	}
    	
    	printStatistics(statistics);
	}
	

	private static void printStatistics(Map<HighLowPattern.Type, Integer> statistics){
		
    	System.out.println("<<--------- follow up trend statistics ------------------->> ");
    	System.out.println(oneStatistic(statistics, HighLowPattern.Type.HighHighLowHigh));
    	System.out.println(oneStatistic(statistics, HighLowPattern.Type.HighHighLowFlat));
    	System.out.println(oneStatistic(statistics, HighLowPattern.Type.HighHighLowLow));
    	
    	System.out.println(oneStatistic(statistics, HighLowPattern.Type.HighFlatLowHigh));
    	System.out.println(oneStatistic(statistics, HighLowPattern.Type.HighFlatLowFlat));
    	System.out.println(oneStatistic(statistics, HighLowPattern.Type.HighFlatLowLow));
    	
    	System.out.println(oneStatistic(statistics, HighLowPattern.Type.HighLowLowHigh));
    	System.out.println(oneStatistic(statistics, HighLowPattern.Type.HighLowLowFlat));
    	System.out.println(oneStatistic(statistics, HighLowPattern.Type.HighLowLowLow));
    	
    	System.out.println(oneStatistic(statistics, HighLowPattern.Type.NA));
	}
	
	private static String oneStatistic(Map<HighLowPattern.Type, Integer> statistics, HighLowPattern.Type type){
		int all = 0;
		for(HighLowPattern.Type tmpType : statistics.keySet()){
			if(statistics.get(tmpType)!=null){
				all += statistics.get(tmpType);
			}
		}

		int oneCount = 0;
		if(statistics.containsKey(type)){
			oneCount = statistics.get(type);
		}
		return (StringUtils.rightPad(type.toString(),15)+" : "+
				StringUtils.leftPad(statistics.get(type)+"/"+all+"="+Formatter.DECIMAL_FORMAT.format((double)oneCount/all), 
						12)
				);
	}
	
	
	
	
	private static void countType(Map<HighLowPattern.Type, Integer> statistics, HighLowPattern.Type type){
		if(statistics.containsKey(type)){
			statistics.put(type, statistics.get(type)+1);
		} else {
			statistics.put(type, 1);
		}
	}
	
	private static HighLowPattern.Type getNextTrendType(HighLowMatch highLowMatch){
		Point secondHigh = null;
		Point secondLow = null;
		
		Point tmpPoint = highLowMatch.getMatchList().get(2);
		if(Point.Type.HIGH.equals(tmpPoint.getType())){
			secondHigh = tmpPoint;
		} else if(Point.Type.LOW.equals(tmpPoint.getType())){
			secondLow = tmpPoint;
		}
		tmpPoint = highLowMatch.getMatchList().get(3);
		if(Point.Type.HIGH.equals(tmpPoint.getType())){
			secondHigh = tmpPoint;
		} else if(Point.Type.LOW.equals(tmpPoint.getType())){
			secondLow = tmpPoint;
		}
		
		Point nextHigh = getNextHighOrLowPoint(secondHigh);
		Point nextLow = getNextHighOrLowPoint(secondLow);

		return HighLowPattern.getType(secondHigh.getPrice(), nextHigh.getPrice(), secondLow.getPrice(), nextLow.getPrice());
	}
	
	
	private static Point getNextPointByType(Point p, Point.Type type){
		Point tmpPoint = p.getNext();
		while(tmpPoint!=null && !tmpPoint.getType().equals(p.getType())){
			tmpPoint = tmpPoint.getNext();
		}
		return tmpPoint;
	}
	
	private static Point getNextHighOrLowPoint(Point p){
		Point tmpPoint = p.getNext();
		while(tmpPoint!=null && !tmpPoint.getType().equals(p.getType())){
			tmpPoint = tmpPoint.getNext();
		}
		return tmpPoint;
	}
	


	/**
	 * high missing:
	 * 2011/2/28 ; 3/3; 3/25; ...
	 * @throws Exception
	 */
    
    public static void testFindAllHighLows() throws Exception{
    	//List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");
    	List<Bar> barList = HistoryLoader.getFChartHistBars("QQQ-20110915-mock-daily.txt");

    	List<Point> highLowPoints = HighLowPattern.findHighLowPoints(barList);
    	
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
