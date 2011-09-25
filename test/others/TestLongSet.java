package others;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import utils.Formatter;

public class TestLongSet {

	static String DATE_STR = "20110923";
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args)throws ParseException {
		
		//testLongSetContains();
		testLong();
	}

	private static void testLong(){
		long tmpLong = 1316784716001L;
		long tmpLong2 = tmpLong/1000*1000;
		
		System.out.println(tmpLong2);
		
	}
	
	private static void testLongSetContains() throws ParseException {
		Date time = Formatter.DEFAULT_DATETIME_FORMAT.parse(DATE_STR+"-09:31:59");
		
		
		Set<Long> noiseTimeSet = new HashSet<Long>();
		try {
			noiseTimeSet.add(Formatter.DEFAULT_DATETIME_FORMAT.parse(DATE_STR+"-09:31:59").getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Long tmpTime = new Long(time.getTime()/1000*1000);
		if(noiseTimeSet.contains(tmpTime)){
			System.out.println("contains");
		} else {
			System.out.println("don't contains");
		}
	}
}
