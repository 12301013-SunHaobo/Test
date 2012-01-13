package others.e.sentence;

import java.util.List;

import utils.FileUtil;
import utils.GlobalSetting;


public class GetSen {

    private static final String ROOT_DIR = GlobalSetting.TEST_HOME+"/src/others/e/sentence"; 
    /**
     * @param args
     */
    public static void main(String[] args) {
        GetSen gs = new GetSen();
        gs.test1();
        
    }
    
    private void test1(){
        List<String> fileNames = FileUtil.getAllFileNames(ROOT_DIR, true);
        for(String fileName : fileNames) {
            System.out.println(fileName);
        }
    }

}
