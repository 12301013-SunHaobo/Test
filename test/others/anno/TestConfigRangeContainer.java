package others.anno;

import modules.at.model.AlgoSetting;

public class TestConfigRangeContainer {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) {
        ConfigRangeContainer<SampleConfig> crc = new ConfigRangeContainer<SampleConfig>(SampleConfig.class);
        //ConfigRangeContainer<AlgoSetting> crc = new ConfigRangeContainer<AlgoSetting>(AlgoSetting.class);
        crc.printConfigs();
    }

    
    private static void test(){
        int start = 0;
        int end = 10;
        int intervals = 3;
        int increment = (end - start)/intervals; 
        for(int d = start; d<=end; d+= increment){
            System.out.println(d);
        }
        if((start+intervals*increment)<end){
            System.out.println(end);
        }
    }
}
