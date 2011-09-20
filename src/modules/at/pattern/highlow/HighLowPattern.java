package modules.at.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import modules.at.model.Point;

public class HighLowPattern {

	public static enum Type {
		HighHighLowHigh, //up 
		HighHighLowFlat, //open up triangle
		HighHighLowLow, //open triangle
		
		HighFlatLowHigh, //shrink up triangle
		HighFlatLowFlat, //range
		HighFlatLowLow, //open down triangle

		HighLowLowHigh, //shrink triangle
		HighLowLowFlat, //shrink down triangle
		HighLowLowLow, //down
		
		NA 
	}
	

	//1 is older then 2
	/**
	 * Match to find (high,low,high,low) or (low,high,low,high) sets, by wanted Types 
	 */
	public List<HighLowMatch> match(List<Point> highLowPointList, Set<Type> wantedTypes) throws Exception{

		List<HighLowMatch> matchList = new ArrayList<HighLowMatch>();
		for(Point p : highLowPointList) {
			//find next match after p
			List<Point> oneMatchList = new ArrayList<Point>();
			
			Point prePoint = p;
			Point curPoint = p;
			int highCount = 0;
			int lowCount = 0;
			
			Point tmpHigh1 = null;
			Point tmpHigh2 = null;
			Point tmpLow1 = null;
			Point tmpLow2 = null;
			
			while((highCount<2 || lowCount<2) && curPoint.getNext()!=null){
				curPoint = curPoint.getNext();
				if(prePoint.getType().equals(curPoint.getType())) {
					if(Point.Type.HIGH.equals(curPoint.getType())) {
						if(curPoint.getPrice()>prePoint.getPrice()){
							prePoint = curPoint;
						}
					} else if(Point.Type.LOW.equals(curPoint.getType())){
						if(curPoint.getPrice()<prePoint.getPrice()){
							prePoint = curPoint;
						}
					}
				}else {
					oneMatchList.add(prePoint);
					if(Point.Type.HIGH.equals(prePoint.getType())){
						highCount++;
						if(highCount==1){
							tmpHigh1 = prePoint;
						}else if(highCount==2){
							tmpHigh2 = prePoint;
						}
					}
					if(Point.Type.LOW.equals(prePoint.getType())){
						lowCount++;
						if(lowCount==1){
							tmpLow1 = prePoint;
						}else if(lowCount==2){
							tmpLow2 = prePoint;
						}
					}
					prePoint = curPoint;
				}
			}
			//discard partial match, since it's not reliable
			if(highCount==2 && lowCount==2){
				Type type = getType(tmpHigh1.getPrice(), 
						tmpHigh2.getPrice(), 
						tmpLow1.getPrice(), 
						tmpLow2.getPrice());
				if(wantedTypes==null || wantedTypes.contains(type)){
					HighLowMatch highLowMatch = new HighLowMatch(oneMatchList, type);
					matchList.add(highLowMatch);
				}
			}
		}
		return matchList;
	}
	


	public static Type getType(double high1, double high2, double low1, double low2){
		if(high1 < high2 && low1 < low2){
			return Type.HighHighLowHigh;
		}else if (high1 < high2 && low1 == low2){
			return Type.HighHighLowFlat;
		}else if (high1 < high2 && low1 > low2){
			return Type.HighHighLowLow;
			
		}else if (high1 == high2 && low1 < low2){
			return Type.HighFlatLowHigh;
		}else if (high1 == high2 && low1 == low2){
			return Type.HighFlatLowFlat;
		}else if (high1 == high2 && low1 > low2){
			return Type.HighFlatLowLow;
			
		}else if (high1 > high2 && low1 < low2){
			return Type.HighLowLowHigh;
		}else if (high1 > high2 && low1 == low2){
			return Type.HighLowLowFlat;
		}else if (high1 > high2 && low1 > low2){
			return Type.HighLowLowLow;
		}
		return Type.NA;
	}
	
	
	
	private void printList(List<Point> pointList){
		Point tmpP = pointList.get(0);
		while(tmpP.getNext()!=null){
			System.out.println(tmpP);
			tmpP = tmpP.getNext();
		}
		System.out.println(tmpP);		
	}
}
