package modules.at.formula.ma;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import modules.at.feed.convert.TickToBarConverter;
import modules.at.feed.history.HistoryLoader;
import modules.at.model.Bar;
import modules.at.model.Tick;
import utils.Formatter;
import utils.TimeUtil;

public class TestMA {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        //long b0 = System.currentTimeMillis();
        List<Tick> tickList = HistoryLoader.getHistTciks();
        //long b1 = System.currentTimeMillis();
        List<Bar> barList = TickToBarConverter.convert(tickList);
        long b2 = System.currentTimeMillis();
        testMA(barList);
        //testConvertToFChartIntraday(barList);
        //testConvertToFChart(barList);
        
        long e0 = System.currentTimeMillis();
        System.out.println("Total time : "+(e0-b2));
        
    }
    
    private static void testConvertToFChartIntraday(List<Bar> barList) throws Exception{
    	DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    	for(Bar bar : barList){
    		
    		System.out.println("QQQ,"+
    				df.format(bar.getDate())+"," +
    				Formatter.DECIMAL_FORMAT.format(bar.getOpen())+"," +
    				Formatter.DECIMAL_FORMAT.format(bar.getHigh())+"," +
    				Formatter.DECIMAL_FORMAT.format(bar.getLow())+"," +
    				Formatter.DECIMAL_FORMAT.format(bar.getClose())+"," +
    				bar.getVolume()
    				);
    	}
    }
    
    
    
    private static void testConvertToFChart(List<Bar> barList) throws Exception{
    	MA ma = new MA(13);
    	 
    	long oneDay = 1000 * 3600 * 24;
    	long curDate = TimeUtil.TICK_DATE_FORMAT.parse("19800101").getTime();
    	for(Bar bar : barList){
    		ma.addPrice(bar.getClose());
    		System.out.println("QQQ,"+
    				TimeUtil.TICK_DATE_FORMAT.format(new Date(curDate + bar.getId()*oneDay))+"," +
    				Formatter.DECIMAL_FORMAT.format(ma.getAvg())
    				);
    				
    				
    		/*//mock daily 
    		System.out.println("QQQ,"+
    				TimeUtil.TICK_DATE_FORMAT.format(new Date(curDate + bar.getId()*oneDay))+"," +
    				Formatter.DECIMAL_FORMAT.format(bar.getOpen())+"," +
    				Formatter.DECIMAL_FORMAT.format(bar.getHigh())+"," +
    				Formatter.DECIMAL_FORMAT.format(bar.getLow())+"," +
    				Formatter.DECIMAL_FORMAT.format(bar.getClose())+"," +
    				bar.getVolume()
    				);
    		*/
    	}
    }
    
    private static void testMA(List<Bar> barList){
        MA ma = new MA(13);
        for(Bar bar : barList){
        	ma.addPrice(bar.getClose());
        	//System.out.println(bar+","+Formatter.DECIMAL_FORMAT.format(ma.getAvg()));
        	System.out.println(bar.getId()+","+Formatter.DECIMAL_FORMAT.format(ma.getAvg()));
        	
        }
    	
    }

}
