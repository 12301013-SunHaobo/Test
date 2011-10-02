package modules.at.feed.history;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import modules.at.feed.convert.FChartConverter;
import modules.at.feed.convert.NazConverter;
import modules.at.model.Bar;
import modules.at.model.Tick;
import utils.FileUtil;
import utils.GlobalSetting;

public class HistoryLoader {
    
    public static List<Tick> getNazHistTicks(String code, String tickFileName, String dateStr) throws Exception{
        List<String> strList = FileUtil.fileToList(GlobalSetting.TEST_HOME+"/data/naz/tick/output/"+code+"/"+tickFileName);
        List<Tick> tickList = new ArrayList<Tick>();
        
        for(String row: strList){
            Tick tick = NazConverter.toTick(row, dateStr);
            tickList.add(tick);
        }
        return tickList;
    }

    public static List<Bar> getNazHistDailyBars(String code, String dailyFileName) throws Exception {
    	List<String> strList = FileUtil.fileToList(GlobalSetting.TEST_HOME+"/data/naz/bar/output/"+code+"/"+dailyFileName);
    	List<Bar> barList = new ArrayList<Bar>();
    	for(String row : strList){
    		Bar bar = NazConverter.toBar(row);
    		barList.add(bar);
    	}
    	return barList;
    }
    
    public static List<Bar> getFChartHistBars(String fchartFileName) throws Exception {
    	List<String> strList = FileUtil.fileToList(GlobalSetting.TEST_HOME+"/data/FChart/input/"+fchartFileName);//QQQ-20110915-mock-daily.txt");
    	List<Bar> barList = new ArrayList<Bar>();
    	for(String row : strList){
    		Bar bar = FChartConverter.toBar(row);
    		barList.add(bar);
    	}
    	return barList;
    	
    }
}
