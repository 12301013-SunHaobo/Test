package modules.at;

import java.util.Date;

import utils.Formatter;

public class TestTimeSeries {

	static final long timeFrame = 3 * 1000;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {


		Date d1 = Formatter.DEFAULT_DATETIME_FORMAT.parse("20110915-13:21:21");
		Date d2 = Formatter.DEFAULT_DATETIME_FORMAT.parse("20110915-13:21:26");
		Date d3 = Formatter.DEFAULT_DATETIME_FORMAT.parse("20110915-13:21:26");
		
		System.out.println(d1.getTime());
		System.out.println(d2.getTime());
		System.out.println(d3.getTime());
		
		long curTimeLot = -1;
		for(int i=0;i<1000*10;i++){
			long curTime = System.currentTimeMillis();
			long tmpTimeLot = getCurrentTimeLot(curTime);
			if(tmpTimeLot!=curTimeLot){
				curTimeLot = tmpTimeLot;
				System.out.println("[new time lot:"+Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(curTimeLot)) +"]");
			}else {
				System.out.println("["+curTime+"]:["
						+Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(curTimeLot)) +"]:[ "
						+Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(tmpTimeLot)) +"]:[ "
						+Formatter.DEFAULT_DATETIME_FORMAT.format(new Date(curTime))+"]");
			}
			Thread.sleep(547);
		}
		
	}

	//beginning of the time lot in milliseconds
	private static long getCurrentTimeLot(long curTime){
		long currentTimeLot = curTime / timeFrame * timeFrame;
		return currentTimeLot;
	}
	
}
