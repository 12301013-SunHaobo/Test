package others.e.sentence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.FileUtil;
import utils.GlobalSetting;
import utils.RegUtil;


public class GetSen {

    private static final String ROOT_DIR = GlobalSetting.TEST_HOME+"/src/others/e/sentence"; 
    private static final String DIR_MASTERED = ROOT_DIR+"/mastered";
    private static final String DIR_NOT_MASTERED = ROOT_DIR+"/notmastered";
    private static final String DIR_SAMPLE = ROOT_DIR+"/sample";
    private static final String DIR_UNKNOWN = ROOT_DIR+"/unknown";
    
    private List<Item> mastered = new ArrayList<Item>();
    private List<Item> notmastered = new ArrayList<Item>();
    private List<Item> unknown = new ArrayList<Item>();
    
    private static final String SEN_REG_EX = "[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)";
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
        GetSen gs = new GetSen();
        gs.load();
        gs.process(DIR_SAMPLE+"/c.txt");
        
        
        //gs.testExtractTextFromHtml();
        //gs.testListFiles();
        
    }
    
    private Set<Item> process(String fileFullPath) throws Exception {
        Set<Item> resultItems = new HashSet<Item>();//all items not in mastered
        String content = FileUtil.fileToString(fileFullPath);
        List<String> senList = RegUtil.getMatchedStrings(content, SEN_REG_EX);
        
        for(String sen : senList){
            String[] strArr = sen.split(",|\\.|\\s|\"");
            Set<Item> lineWSet = new HashSet<Item>();
            for(int i=0;i<strArr.length;i++){
                String origW = strArr[i];
                String lowerCaseW = origW.toLowerCase();
                Item itemW = new Item();
                itemW.setWord(lowerCaseW);
                if(notmastered.contains(itemW)
                        || false //add more conditions here
                        ){
                    lineWSet.add(itemW);
                    itemW.setSentence(sen.replaceAll(origW, "["+origW+"]"));
                    resultItems.add(itemW);
                }
            }
        }
        for(Item item : resultItems){
            System.out.println(item.toString());
        }
        return resultItems;
    }
    
    private void load() throws Exception{
        this.mastered = loadOneFolder(DIR_MASTERED);
        this.notmastered = loadOneFolder(DIR_NOT_MASTERED);
        this.unknown = loadOneFolder(DIR_UNKNOWN);
        System.out.println("this.mastered.size()="+this.mastered.size());
    }
    
    private List<Item> loadOneFolder(String dir) throws Exception{
        List<String> fileNames = FileUtil.getAllFileNames(dir, true);
        List<String> lines = new ArrayList<String>();
        for(String fileName : fileNames) {
            System.out.println(dir+"/"+fileName);
            lines.addAll(FileUtil.fileToList(dir+"/"+fileName));
        }
        List<Item> items = new ArrayList<Item>();
        for(String line : lines) {
            items.add(extractItem(line));
        }
        return items;
    }    
    
    private Item extractItem(String line){
        String[] lineArr = line.split(";");
        Item item = new Item();
        if(lineArr.length>=1){
            item.setWord(lineArr[0].trim().toLowerCase());
        }
        if(lineArr.length>=2){
            item.setMeaning(lineArr[1].trim());
        }
        if(lineArr.length>=3){
            item.setSentence(lineArr[2].trim());
        }
        return item;
    }
    
    
    private void testExtractTextFromHtml() throws Exception{
        String html = FileUtil.fileToString(DIR_SAMPLE+"/sample1.txt");
        String noHtml = html.toString().replaceAll("\\<.*?>","");
        System.out.println(noHtml);
    }
    
    private void testListFiles(){
        List<String> fileNames = FileUtil.getAllFileNames(DIR_SAMPLE, true);
        for(String fileName : fileNames) {
            System.out.println(fileName);
        }
    }
}
