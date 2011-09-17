package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    //public static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
	public static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static DateFormat DEFAULT_DATETIME_FORMAT = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
    public static DateFormat FCCHART_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    
    public static DateFormat DISPLAY_DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    
    public static String getCurrentTimeStr(){ 
        return DEFAULT_DATETIME_FORMAT.format(new Date());
    }
    
    
}
