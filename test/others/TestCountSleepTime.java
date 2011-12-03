package others;

import java.io.IOException;
import java.util.List;

import utils.FileUtil;
import utils.RegUtil;

public class TestCountSleepTime {

    private static final String disabledLog = "D:/user/docs/ftp/Jenkins@build-tt.wgenhq.net/jobs/loadtest-grinder-outcomes/loadtestResults-20111129-1426-#332-disabled/out_poe125.wgenhq.net-0.log";
    private static final String enabledLog = "D:/user/docs/ftp/Jenkins@build-tt.wgenhq.net/jobs/loadtest-grinder-outcomes/loadtestResults-20111130-1041-#334-enabled/out_poe125.wgenhq.net-0.log";
    private static final String pattern = "thread .*? run .*? sleeping for .*? ms";
    //thread 9 run 15 test 263201): sleeping for 12 ms
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        
        List<String> lines = FileUtil.fileToList(disabledLog);
        int countSleep = 0;
        for(String line : lines){
            List<String> sleepList = RegUtil.getMatchedStrings(line, pattern);
            countSleep+=sleepList.size();
            for(String sleepStr : sleepList){
                System.out.println(sleepStr.replaceAll("run.*?\\)|sleeping for | ms", ""));
            }
        }
        System.out.println(countSleep);
        

    }

}
