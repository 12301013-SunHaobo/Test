package modules.at.formula.ma;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

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

        long b0 = System.currentTimeMillis();
    	
        //testDailyMA();
        testDailyMAByMathLib();
        
        long e0 = System.currentTimeMillis();
        System.out.println("Total time : "+(e0-b0));
        
    }
    
    private static void testDailyMA() throws Exception{
    	List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");
        MASelfImpl ma = new MASelfImpl(13);
        for(Bar bar : barList){
        	ma.addPrice(bar.getClose());
        	System.out.println(bar+" "+Formatter.DECIMAL_FORMAT.format(ma.getAvg()));
        	
        }
    	
    	
    }
    
    private static void testDailyMAByMathLib() throws Exception{
    	List<Bar> barList = HistoryLoader.getNazHistDailyBars("qqq", "daily-20010917-20110916.txt");
    	DescriptiveStatistics ds = new DescriptiveStatistics(13);
        for(Bar bar : barList){
        	ds.addValue(bar.getClose());
        	System.out.println(bar+" "+Formatter.DECIMAL_FORMAT.format(ds.getSum()/ds.getN()));
        	
        }
    	
    	
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
    	MASelfImpl ma = new MASelfImpl(13);
    	 
    	long oneDay = 1000 * 3600 * 24;
    	long curDate = TimeUtil.FCCHART_DATE_FORMAT.parse("19800101").getTime();
    	for(Bar bar : barList){
    		ma.addPrice(bar.getClose());
    		System.out.println("QQQ,"+
    				TimeUtil.FCCHART_DATE_FORMAT.format(new Date(curDate + bar.getId()*oneDay))+"," +
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
        MASelfImpl ma = new MASelfImpl(13);
        for(Bar bar : barList){
        	ma.addPrice(bar.getClose());
        	//System.out.println(bar+","+Formatter.DECIMAL_FORMAT.format(ma.getAvg()));
        	System.out.println(bar.getId()+","+Formatter.DECIMAL_FORMAT.format(ma.getAvg()));
        	
        }
    	
    }

}
