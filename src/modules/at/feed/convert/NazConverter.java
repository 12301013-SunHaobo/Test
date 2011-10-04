package modules.at.feed.convert;

import java.text.DateFormat;
import java.util.Date;

import modules.at.model.Bar;
import modules.at.model.Tick;
import utils.Formatter;

/**
 *yyyyMMdd,open,high,low,close,volume
 * 20010917,31.25,32.8,31.09,31.2,97736900
 * 
 * @author r
 *
 */
public class NazConverter {
	
	/**
	 * time, price, volume
	 *  09:30:01,56.20,400,
	 *  
	 *  String dateStr yyyyMMdd
	 */
//	public static Tick toTick(String row, String dateStr) throws Exception {
//		return toTick(row, dateStr, Formatter.DEFAULT_DATETIME_FORMAT);
//	}
	public static Tick toTick(String row, String dateStr, DateFormat df) throws Exception {
		String[] strArr = row.split(",");
		Date date = df.parse(dateStr+"-"+strArr[0]);
		double price = Double.parseDouble(strArr[1]);
		int volume = Integer.parseInt(strArr[2]);
		Tick tick = new Tick(date, price, volume);
		return tick;
	}	
	
	
	/**
	 * yyyyMMdd,open,high,low,close,volume
	 * 20010917,31.25,32.8,31.09,31.2,97736900
	 */ 
	public static Bar toBar(String row) throws Exception {
		
		String[] strArr = row.split(",");
		Date date = Formatter.FCCHART_DATE_FORMAT.parse(strArr[0].trim());
		double open = Double.parseDouble(strArr[1].trim());
		double high = Double.parseDouble(strArr[2].trim());
		double low = Double.parseDouble(strArr[3].trim());
		double close = Double.parseDouble(strArr[4].trim());
		int volumn = Integer.parseInt(strArr[5].trim());
		Bar bar = new Bar(date, open, high, low, close, volumn);
		return bar;
	}

}
