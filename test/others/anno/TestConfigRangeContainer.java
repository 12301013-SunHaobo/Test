package others.anno;

import modules.at.model.Setting;
import modules.at.model.ConfigRangeContainer;

public class TestConfigRangeContainer {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) {
    	//test();

    	//ConfigRangeContainer<SampleConfig> crc = new ConfigRangeContainer<SampleConfig>(SampleConfig.class);
        ConfigRangeContainer<Setting> crc = new ConfigRangeContainer<Setting>(Setting.class);
        crc.printConfigsByToString();
    }

    
    private static void test(){
        int start = 14;
        int end = 20;
        int intervals = 5;
        
//        int start = 0;
//        int end = 10;
//        int intervals = 3;
        
        int increment = (end - start)/intervals; 
        System.out.println("increment="+increment);
        int d;
        for(d = start; d<=end; d+= increment){
            System.out.println(d);
        }
        if((d-increment)<end) {
            System.out.println(end);
        }
    }
}
