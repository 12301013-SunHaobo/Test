package modules.at.feed.convert;

import java.util.LinkedList;
import java.util.List;

import modules.at.model.Bar;
import modules.at.model.Tick;

public class TickToBarConverter {

	public static int SECOND = 1 * 1000;
	public static int MINUTE = 60 * SECOND;
	public static int MINUTES_5 = 5 * MINUTE;
	public static int MINUTES_15  = 15 * MINUTE;
	public static int MINUTES_30 = 30 * MINUTE;
	
	//static int timeFrame = 60 * 1000; // milliseconds

	public static List<Bar> convert(List<Tick> tickList, int timeFrame) {
		List<Bar> barList = new LinkedList<Bar>();

		long curTimeLot = -1;

		Bar bar = null;
		for (Tick tick : tickList) {

			long curTime = tick.getDate().getTime();
			long tmpTimeLot = getCurrentTimeLot(curTime, timeFrame);
			if (tmpTimeLot != curTimeLot) {
				// add previous bar to barList
				if (bar != null) {
					barList.add(bar);
					//System.out.println(bar.toString());
				}
				// new bar for current time lot
				bar = new Bar(tick.getDate(), tick.getPrice(), tick.getPrice(), tick.getPrice(), tick.getPrice(), tick.getVolumn());
				curTimeLot = tmpTimeLot;
				// System.out.println("<<new time lot:" +TimeUtil.TICK_TIME_FORMAT.format(new Date(curTimeLot)) +">>");
			} else {
				bar.addTick(tick);
			}

			// System.out.println("[" + curTime + "]:[ "+TimeUtil.TICK_TIME_FORMAT.format(new Date(curTime)) + "]"+tick.toString());
		}
		// add last bar to barList
		barList.add(bar);
		//System.out.println(bar.toString());
		return barList;
	}

	// beginning of the time lot in milliseconds
	private static long getCurrentTimeLot(long curTime, int timeFrame) {
		return curTime / timeFrame * timeFrame;
	}
	
	
	

}
