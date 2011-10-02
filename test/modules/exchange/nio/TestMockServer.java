package modules.exchange.nio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.Formatter;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Tick;

public class TestMockServer {

	private static int MOCK_TIME_SLOT = 1000;//minimum is 1000 millisecond
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		//change begin -> for new date
		String nazTickOutputDateStr = "20110919";//change for new date 
		List<Tick> tickList = HistoryLoader.getNazHistTicks("qqq", "20110919-205230.txt", nazTickOutputDateStr); //change for new file
		//change end -> for new date
		
		long b0 = System.currentTimeMillis();
		
		Tick tmpTick = tickList.get(0);
		Date startTime = tmpTick.getDate();
		Date endTime = tickList.get(tickList.size()-1).getDate();
		
		System.out.println("startTime:"+Formatter.DEFAULT_DATETIME_FORMAT.format(startTime));
		System.out.println("endTime:"+Formatter.DEFAULT_DATETIME_FORMAT.format(endTime));
		
		long totalTimeSlots = (endTime.getTime()-startTime.getTime())/MOCK_TIME_SLOT;
		//for(long i=0;i<=totalTimeSlots;i++){
		for(long i=0;i<=1;i++){
			long b1 = System.currentTimeMillis();
			Date slotStartTime = new Date(startTime.getTime()+i*MOCK_TIME_SLOT);
			List<Tick> timeSlotTickList = getTimeSlotTickList(slotStartTime, tickList);
			
			
			for(Tick tick: timeSlotTickList){
				System.out.println(tick.toString());
			}
			long e1 = System.currentTimeMillis();
			System.out.println(Formatter.DEFAULT_DATETIME_FORMAT.format(slotStartTime)+"["+timeSlotTickList.size()+"] used time:"+(e1-b1));
		}
		

		long e0 = System.currentTimeMillis();
		System.out.println("total time:"+(e0-b0));
	}
	
	
	private static List<Tick> appendRandomTime(List<Tick> tickList, Date slotStartTime, long timeSlot){
		List<Long> timeToAppendList = new ArrayList<Long>();
		
		
		return tickList;
	}
	
	
	/**
	 * Get all ticks belongs to slotStartTime
	 * @param slotStartTime
	 * @return
	 */
	private static List<Tick> getTimeSlotTickList(Date slotStartTime, List<Tick> tickList){
		List<Tick> timeSlotTickList = new ArrayList<Tick>();
		Tick tmpTick = null;
		for(int i = 0; i<tickList.size();i++){
			tmpTick = tickList.get(i);
			if(tmpTick.getDate().getTime()/MOCK_TIME_SLOT == slotStartTime.getTime()/MOCK_TIME_SLOT){
				timeSlotTickList.add(tmpTick);
			}
		}
		
		return timeSlotTickList;
		
	}

}
