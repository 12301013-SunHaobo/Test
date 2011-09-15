package others;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TestFormat {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//testSplit();
		System.out.println(Double.MAX_VALUE);

	}
	
	
	private static void testSplit(){
		String[] strArr = "US335811AT22;335811AT2;;FNT-1989-IC1;0;;;;;".split("\\;", -1);
		
		System.out.println(strArr.length);
	}
	
	private static void parseInteger(){
		long i= Long.parseLong("4001057117");
		System.out.println(Integer.MAX_VALUE);
	}

	private static void dateFormat(){
		DateFormat df = new SimpleDateFormat("d/M/yyyy hh:mm:ss a"); //1/1/1900 12:00:00 AM
		try {
			Date date = df.parse("1/1/1900 12:00:00 AM");
			System.out.println("date = " + df.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private static void workbookFormat(){
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFDataFormat format = workbook.createDataFormat();

		System.out.println(format.toString());
		short s = format.getFormat("#,##0");
		
		System.out.println("s:"+s);
	}

}
