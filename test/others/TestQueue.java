package others;

import java.util.Date;
import java.util.LinkedList;

import modules.at.model.Point;

public class TestQueue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		test1();

	}
	
	private static void test1(){
		HighLowPointQueue q = new HighLowPointQueue(5);
		Date tmpDate = new Date();
		q.offer(new Point(Point.Type.HIGH, tmpDate, 51.01));
		q.offer(new Point(Point.Type.LOW, tmpDate, 52.01));
		q.offer(new Point(Point.Type.HIGH, tmpDate, 53.01));
		q.offer(new Point(Point.Type.LOW, tmpDate, 54.01));
		q.offer(new Point(Point.Type.HIGH, tmpDate, 55.01));
		q.offer(new Point(Point.Type.LOW, tmpDate, 56.01));
		
		for(int i=0;i<q.size();i++){
			Point p = q.get(i);
			System.out.println(p);
		}
		
	}

	
	private static class HighLowPointQueue {
		private int size = 4; //default 6 high low points, which is min useful size
		private int elementCount=0;//count of current elements
		private LinkedList<Point> pointList; 
		public HighLowPointQueue(int size) {
			super();
			if(this.size < size){
				this.size = size;
			}
			this.pointList = new LinkedList<Point>();
		}

		/**
		 * @return true | false ; successfully added or not
		 */
		public boolean offer(Point p){
			if(elementCount>=size){
				pointList.poll();
				elementCount--;
			}
			if(pointList.offer(p)){
				elementCount++;
				return true;
			}else{
				return false;
			}
		}
		
		public int size(){
			return this.elementCount;
		}
		public Point get(int index){
			return pointList.get(index); 
		}
		
	}
}
