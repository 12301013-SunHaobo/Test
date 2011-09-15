package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtil {

    public static List<String> getMatchedStrings(String content, String regEx){
        List<String> list = new ArrayList<String>();
        String dataTablePattern = regEx;
        Pattern p = Pattern.compile(dataTablePattern, Pattern.DOTALL);
        Matcher m = p.matcher(content);
        boolean found = m.find();
        while(found) {
            list.add(m.group());
            found = m.find();
        }
        return list;
    }
}
