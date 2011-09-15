package others;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {

	private static DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {

		
		Date start = df.parse("10/01/2011 10:20:33");
		Date end = df.parse("10/01/2010 10:20:33");
		System.out.println(daysBetween(start,end));
	}

	
	
	private static long daysBetween(Date start, Date end){
		long diff = end.getTime() - start.getTime();
		return diff / (1000 * 60 * 60 * 24);
	}
}
