package others.apachemath;

import java.util.ArrayList;
import java.util.List;

import modules.at.model.Tick;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import utils.Formatter;


public class TestCommonsMathSTDEV {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		testSTDEV();
	}

	//result consistents with excel STDEV, but no same as http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:standard_deviation_v
	private static void testSTDEV() throws Exception{
		List<Tick> tickList = new ArrayList<Tick>();
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101101"),52.22,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101102"),52.78,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101103"),53.02,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101104"),53.67,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101105"),53.67,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101108"),53.74,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101109"),53.45,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101110"),53.72,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101111"),53.39,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101112"),52.51,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101115"),52.32,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101116"),51.45,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101117"),51.6 ,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101118"),52.43,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101119"),52.47,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101122"),52.91,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101123"),52.07,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101124"),53.12,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101126"),52.77,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101129"),52.73,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101130"),52.09,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101201"),53.19,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101202"),53.73,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101203"),53.87,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101206"),53.85,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101207"),53.88,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101208"),54.08,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101209"),54.14,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101210"),54.5 ,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101213"),54.3 ,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101214"),54.4 ,-1));
		tickList.add(new Tick(Formatter.DEFAULT_DATE_FORMAT.parse("20101215"),54.16,-1));
		
		int length = 10;
		DescriptiveStatistics ds = new DescriptiveStatistics();
		ds.setWindowSize(length);
		for(int i=0;i<tickList.size();i++){
			Tick tick = tickList.get(i);
			ds.addValue(tick.getPrice());
			if(i>=length){
				System.out.println(
						Formatter.DISPLAY_DEFAULT_DATE_FORMAT.format(tick.getDate())+", "+
						tick.getPrice()+", "+
						Formatter.DECIMAL_FORMAT.format(ds.getStandardDeviation()));
			}
		}
		
	}
	
	private static void test1(){
		DescriptiveStatistics ds = new DescriptiveStatistics();
		ds.setWindowSize(5);
		ds.addValue(1);
		ds.addValue(2);
		ds.addValue(3);
		ds.addValue(4);
		ds.addValue(5);
		ds.addValue(6);
		ds.addValue(7);
		
		System.out.println("getN():"+ds.getN());
		System.out.println("getSum():"+ds.getSum());
		
	}
	
}
