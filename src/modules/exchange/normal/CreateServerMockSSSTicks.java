package modules.exchange.normal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Tick;
import utils.FileUtil;
import utils.Formatter;
import utils.GlobalSetting;
import utils.MathUtil;

public class CreateServerMockSSSTicks {

	private static String stockcode = "qqq";//change for new code
	private static String nazTickOutputDateStr = "20110919";//change for new date 
	private static String origFileName = "20110919-205230.txt"; //change for new orig file

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		List<Tick>origTickList = HistoryLoader.getNazHistTicks(stockcode, origFileName, nazTickOutputDateStr);
		List<Tick> tickList = appendSSSToTickList(origTickList);
		
		List<String> outputList = new ArrayList<String>();
		for(Tick tick : tickList){
			outputList.add(Formatter.TIME_FORMAT_SSS.format(tick.getDate())+","
					+Formatter.DECIMAL_FORMAT.format(tick.getPrice())+","
					+tick.getVolumn()+",");
		}
	    String tickOutputFilePath = GlobalSetting.TEST_HOME+"/data/naz/tick/output-mock-sss/"+stockcode+"/SSS_"+origFileName;
	    FileUtil.listToFile(outputList, tickOutputFilePath);
		
	}

	
	private static int MOCK_TIME_SLOT = 1000;//minimum is 1000 millisecond
	private static List<Tick> appendSSSToTickList(List<Tick> tickList) throws Exception{
		Date startTime = Formatter.DEFAULT_DATETIME_FORMAT.parse(nazTickOutputDateStr+"-09:30:00");
		Date endTime = Formatter.DEFAULT_DATETIME_FORMAT.parse(nazTickOutputDateStr+"-16:00:00");
		
		//append mock milliseconds to every tick
		List<Tick> resultTickList = new ArrayList<Tick>();
		for (long tmpStartTime = startTime.getTime(); tmpStartTime < endTime.getTime(); tmpStartTime = tmpStartTime + MOCK_TIME_SLOT) {
			List<Tick> timeSlotTickList = getTimeSlotTickList(new Date(tmpStartTime), tickList);
			Random random = new Random();
			List<Long> randomList = MathUtil.getUniqueRandomLongSet(
					tmpStartTime+1,tmpStartTime + MOCK_TIME_SLOT-1,random,timeSlotTickList.size());
			for(int i=0;i<timeSlotTickList.size();i++){
				Tick tmpTick = timeSlotTickList.get(i);
				tmpTick.setDate(new Date(randomList.get(i)));
			}
			resultTickList.addAll(timeSlotTickList);
		}
//		System.out.println("<<---- begin ----");
//		for(Tick tick : resultTickList){
//			System.out.println(tick);
//		}
//		System.out.println("---- end ---->>");
		return resultTickList;
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
