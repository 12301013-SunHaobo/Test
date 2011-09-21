package modules.at.feed.convert;

import java.util.LinkedList;
import java.util.List;

import modules.at.model.Bar;
import modules.at.model.Tick;

public class TickToBarConverter {

	static int timeFrame = 60 * 1000; // milliseconds

	public static List<Bar> convert(List<Tick> tickList) {
		List<Bar> barList = new LinkedList<Bar>();

		long curTimeLot = -1;

		Bar bar = null;
		for (Tick tick : tickList) {

			long curTime = tick.getDate().getTime();
			long tmpTimeLot = getCurrentTimeLot(curTime);
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
	private static long getCurrentTimeLot(long curTime) {
		return curTime / timeFrame * timeFrame;
	}
	
	
	

}
