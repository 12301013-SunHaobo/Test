package modules.at.feed.convert;

import java.util.Date;

import modules.at.model.Bar;
import utils.Formatter;
/**
 * Name,yyyyMMdd,open,high,low,close,volume
 * QQQ,19800102,55.97,55.98,55.83,55.88,192521
 * 
 * @author r
 *
 */
public class FChartConverter {
	
	/**
	 * from row in (file like /Test/data/FChart/input/QQQ-20110915-mock-daily.txt) to bar
	 */
	public static Bar toBar(String row) throws Exception{
		String[] strArr = row.split(",");
		Date date = Formatter.FCCHART_DATE_FORMAT.parse(strArr[1].trim());
		double open = Double.parseDouble(strArr[2].trim());
		double high = Double.parseDouble(strArr[3].trim());
		double low = Double.parseDouble(strArr[4].trim());
		double close = Double.parseDouble(strArr[5].trim());
		int volumn = Integer.parseInt(strArr[6].trim());
		Bar bar = new Bar(date, open, high, low, close, volumn);
		return bar;
	}
	
	/**
	 * from Bar to row in (file like /Test/data/FChart/input/QQQ-20110915-mock-daily.txt)
	 * @param bar
	 * @return
	 * @throws Exception
	 */
	public static String toRow(Bar bar) throws Exception{
		return "QQQ,"+
		Formatter.DEFAULT_DATE_FORMAT.format(bar.getDate())+"," +
		Formatter.DECIMAL_FORMAT.format(bar.getOpen())+"," +
		Formatter.DECIMAL_FORMAT.format(bar.getHigh())+"," +
		Formatter.DECIMAL_FORMAT.format(bar.getLow())+"," +
		Formatter.DECIMAL_FORMAT.format(bar.getClose())+"," +
		bar.getVolume();
	}
}
