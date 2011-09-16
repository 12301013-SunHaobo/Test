package modules.at.feed.history;

import java.util.LinkedList;
import java.util.List;

import modules.at.model.Tick;
import utils.FileUtil;
import utils.GlobalSetting;
import utils.TimeUtil;

public class HistoryLoader {
    
    public static List<Tick> getHistTciks() throws Exception{
        String dateStr = "20110915";
        List<String> strList = FileUtil.fileToList(GlobalSetting.TEST_HOME+"/tmp/tick/dataoutput/20110915-194819.txt");
        List<Tick> tickList = new LinkedList<Tick>();
        
        for(String str: strList){
            Tick tick = new Tick();
            String[] strArr = str.split(",");
            tick.setDate(TimeUtil.TICK_TIME_FORMAT.parse(dateStr+"-"+strArr[0]));
            tick.setPrice(Double.parseDouble(strArr[1]));
            tick.setVolumn(Integer.parseInt(strArr[2]));
            
            tickList.add(tick);
        }
        return tickList;
    }
}
