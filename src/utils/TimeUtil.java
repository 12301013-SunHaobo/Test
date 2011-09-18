package utils;

import java.util.Date;

public class TimeUtil {

    
    public static String getCurrentTimeStr(){ 
        return Formatter.DEFAULT_DATETIME_FORMAT.format(new Date());
    }
    
    
}
