package modules.at.feed.fchart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.at.feed.convert.FChartConverter;
import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import modules.at.model.Tick;
import utils.FileUtil;
import utils.Formatter;
import utils.GlobalSetting;

public class GetFChartData {

	//use 19800101 as start date
	private static final String fchartInputMockDailyStartDate = "19800101";
	
	/**
	 * convert from naz tick file 
	 * /Test/data/naz/tick/output/qqq/20110916-195420.txt
	 * to fchart mock daily file
	 * /Test/data/FChart/input/QQQ-20110915-mock-daily.txt
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		//change begin -> for new date
		String nazTickOutputDateStr = "20110916";//change for new date 
		List<Tick> tickList = HistoryLoader.getNazHistTicks("qqq", "20110916-195420.txt", nazTickOutputDateStr); //change for new file
		//change end
		
		List<Bar> barList = TickToBarConverter.convert(tickList);
		List<String> fchartInputMockDaily = convertToFChartInput(barList);
		String mockDailyFile = GlobalSetting.TEST_HOME+"/data/FChart/input/QQQ-"+nazTickOutputDateStr+"-mock-tick"+".txt";
		FileUtil.listToFile(fchartInputMockDaily, mockDailyFile);

	}

    //use 19800101 as start date
    private static List<String> convertToFChartInput(List<Bar> barList) throws Exception{
    	long oneDay = 1000 * 3600 * 24;
    	long curDate = Formatter.FCCHART_DATE_FORMAT.parse(fchartInputMockDailyStartDate).getTime();
    	
    	List<String> fcharMockDaily = new ArrayList<String>();
    	for(Bar bar : barList){
    		//mock daily 
    		bar.setDate(new Date(curDate + bar.getId()*oneDay));
    		fcharMockDaily.add(FChartConverter.toRow(bar));
    	}
    	return fcharMockDaily;
    }	
}
