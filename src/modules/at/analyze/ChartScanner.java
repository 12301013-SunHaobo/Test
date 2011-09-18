package modules.at.analyze;

import java.util.LinkedList;
import java.util.List;

import modules.at.model.Bar;
import modules.at.model.Point;

public class ChartScanner {
	
	/**
	 * Only UPFLAT->DOWN & UP->DOWN , the last point need to be added to highPointList
	 * 
	 *
	 */
	private enum Trend {
		DOWN, UP, DOWNFLAT, UPFLAT, 
		INITFLAT //the very first initial point 
	}
	
	
	public static List<Point> findHighLowPoints(List<Bar> barList){
//		List<Point> highPointList = new LinkedList<Point>();
//		List<Point> lowPointList = new LinkedList<Point>();
		
		List<Point> highLowList = new LinkedList<Point>();
		
		Bar initBar = barList.get(0);

		
		Point prePointHigh = new Point(Point.Type.HIGH, initBar.getDate(), initBar.getHigh());
		Point prePointLow = new Point(Point.Type.LOW, initBar.getDate(), initBar.getLow());
		
		Trend highTrend = Trend.INITFLAT;
		Trend lowTrend = Trend.INITFLAT;
		
		Bar bar = null;
		for(int i=1; i<barList.size(); i++){
			bar = barList.get(i);
		
			//track high point
			if(bar.getHigh()>prePointHigh.getPrice()){
				prePointHigh.setDateTime(bar.getDate());
				prePointHigh.setPrice(bar.getHigh());
				highTrend = Trend.UP;
			}else if(bar.getHigh()<prePointHigh.getPrice()){
				if(Trend.UPFLAT.equals(highTrend) 
						|| Trend.UP.equals(highTrend)
						|| Trend.INITFLAT.equals(highTrend)){
					highLowList.add(prePointHigh);
					prePointHigh = new Point(Point.Type.HIGH, bar.getDate(), bar.getHigh());
				} else {
					prePointHigh.setDateTime(bar.getDate());
					prePointHigh.setPrice(bar.getHigh());
				}
				highTrend = Trend.DOWN;
			}else {
				if(Trend.UP.equals(highTrend)){
					highTrend = Trend.UPFLAT;
				}else if(Trend.DOWN.equals(highTrend)){
					highTrend = Trend.DOWNFLAT;
				}
				prePointHigh.setDateTime(bar.getDate());
				prePointHigh.setPrice(bar.getHigh());
			}
			
			//track low point
			if(bar.getLow()<prePointLow.getPrice()){
				prePointLow.setDateTime(bar.getDate());
				prePointLow.setPrice(bar.getLow());
				
				lowTrend = Trend.DOWN;
			}else if(bar.getLow()>prePointLow.getPrice()){	
				if(Trend.DOWNFLAT.equals(lowTrend) 
						|| Trend.DOWN.equals(lowTrend)
						|| Trend.INITFLAT.equals(lowTrend)){
					highLowList.add(prePointLow);
					prePointLow = new Point(Point.Type.LOW, bar.getDate(), bar.getLow());
				} else {
					prePointLow.setDateTime(bar.getDate());
					prePointLow.setPrice(bar.getLow());
				}
				lowTrend = Trend.UP;
			}else{
				if(Trend.UP.equals(lowTrend)){
					lowTrend = Trend.UPFLAT;
				}else if(Trend.DOWN.equals(lowTrend)){
					lowTrend = Trend.DOWNFLAT;
				}
				prePointLow.setDateTime(bar.getDate());
				prePointLow.setPrice(bar.getLow());
			}
			
		}
		
		return highLowList;
	}
}
