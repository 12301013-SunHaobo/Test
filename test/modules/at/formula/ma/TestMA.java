package modules.at.formula.ma;

import java.util.List;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import modules.at.model.Tick;

public class TestMA {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        List<Tick> tickList = HistoryLoader.getHistTciks();
        List<Bar> barList = TickToBarConverter.convert(tickList);
        
        
//        System.out.println(tickList.size());
//        
//        for(Tick t : tickList){
//            System.out.println(t.toString());
//        }
        
    }

}
