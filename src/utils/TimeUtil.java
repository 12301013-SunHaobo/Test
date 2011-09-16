package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
    public static DateFormat TICK_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
    public static DateFormat TICK_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    
    public static String getCurrentTimeStr(){
        return DEFAULT_DATE_FORMAT.format(new Date());
    }
    
    
}
