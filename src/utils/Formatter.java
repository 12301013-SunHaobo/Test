package utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Formatter {
	public static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");
	public static DecimalFormat DECIMAL_FORMAT4 = new DecimalFormat("#0.0000");
	
    //public static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
	public static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static DateFormat DEFAULT_DATETIME_FORMAT = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
    public static DateFormat DEFAULT_DATETIME_MM_FORMAT = new SimpleDateFormat("yyyyMMdd-HH:mm");
    public static DateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public static DateFormat DEFAULT_FILENAME_DATETIME_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
    public static DateFormat FCCHART_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    
    public static DateFormat DISPLAY_DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    public static DateFormat DATETIME_FORMAT_SSS = new SimpleDateFormat("yyyyMMdd-HH:mm:ss:SSS");
    public static DateFormat TIME_FORMAT_SSS = new SimpleDateFormat("HH:mm:ss:SSS");
    
    
    
}
