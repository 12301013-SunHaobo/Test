package others.e.sentence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.FileUtil;
import utils.GlobalSetting;
import utils.RegUtil;


public class GetSen {

    private static final String ROOT_DIR = GlobalSetting.TEST_HOME+"/src/others/e/sentence"; 
    private static final String DIR_MASTERED = ROOT_DIR+"/mastered";
    private static final String DIR_NOT_MASTERED = ROOT_DIR+"/notmastered";
    private static final String DIR_SAMPLE = ROOT_DIR+"/sample";
    private static final String DIR_SPECIAL = ROOT_DIR+"/special";
    private static final String DIR_UNKNOWN = ROOT_DIR+"/unknown";
    
    private Set<Item> mastered = new HashSet<Item>();
    private Set<Item> notmastered = new HashSet<Item>();
    private Set<Item> special = new HashSet<Item>();
    
    //http://stackoverflow.com/questions/5553410/regular-expression-match-a-sentence
    private static final String SEN_REG_EX = "[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)";
    
    //change here!
    private static boolean isPurge = false;
    private static String sampleFileName = DIR_SAMPLE+"/sample3-test-fullset.txt";
    
    public static void main(String[] args) throws Exception{
        GetSen gs = new GetSen();
        
        //----------- testing ---------------
        //gs.testExtractTextFromHtml();
        //gs.testExtractLinksFromHtml();
        //gs.testListFiles();
        
        //if(true)return;
        
        if(isPurge){
            gs.purge();
        }else {
            gs.load();
            String textOnlyContent = FileUtil.fileToString(sampleFileName);
            gs.process(textOnlyContent);
        }        
    }

    /**
     * process text only content,  extract all items
     */
    private void process(String textOnlyContent) throws Exception {
        List<String> senList = RegUtil.getMatchedStrings(textOnlyContent, SEN_REG_EX);
        
        Set<Item> resultItems = new HashSet<Item>();//all items not in mastered
        for(String sen : senList){
            String[] strArr = sen.split(",|\\.|\\s|\"");
            Set<Item> lineWSet = new HashSet<Item>();
            for(int i=0;i<strArr.length;i++){
              //remove non-word, digit character, 
                String origW = strArr[i].replaceAll("\\W|\\d", "");
                if(origW!=null && !"".equals(origW)){
                    String lowerCaseW = origW.toLowerCase();
                    Item itemW = new Item();
                    itemW.setWord(lowerCaseW);
                    //keep items not in "notmastered" or "mastered" or "special"
                    if(!(notmastered.contains(itemW)
                            ||mastered.contains(itemW)
                            ||special.contains(itemW)
                        )){
                        lineWSet.add(itemW);
                        itemW.setSentence(sen.replaceAll(origW, "["+origW+"]"));
                        resultItems.add(itemW);
                    }
                }
            }
        }
        List<String> itemNames = new ArrayList<String>();
        for(Item item : resultItems){
            itemNames.add(item.toString());
            System.out.println(item.toString());
        }
        String unknownFileFullPath = FileUtil.createFileNameWithTimestamp(DIR_UNKNOWN+"/unknown-%s.txt");
        FileUtil.listToFile(itemNames, unknownFileFullPath); 
    }
    
    /**
     * Deletes duplicate items and consolidate them to a separate file
     */
    private void purge() throws Exception{
        this.mastered = loadOneFolder(DIR_MASTERED);
        String outputFilePath = DIR_MASTERED+"/mastered-purged-%s.txt"; 
        List<String> itemNames = new ArrayList<String>();
        for(Item item : this.mastered){
            itemNames.add(item.getWord());
        }
        Collections.sort(itemNames);
        String fileFullPath = FileUtil.createFileNameWithTimestamp(outputFilePath);
        FileUtil.listToFile(itemNames, fileFullPath); 
    }
    
    
    private void load() throws Exception{
        this.mastered = loadOneFolder(DIR_MASTERED);
        this.special = loadOneFolder(DIR_SPECIAL);
        this.notmastered = loadOneFolder(DIR_NOT_MASTERED);
        //System.out.println("this.mastered.size()="+this.mastered.size());
    }
    
    private Set<Item> loadOneFolder(String dir) throws Exception{
        List<String> fileNames = FileUtil.getAllFileNames(dir, true);
        List<String> lines = new ArrayList<String>();
        for(String fileName : fileNames) {
            //System.out.println(dir+"/"+fileName);
            lines.addAll(FileUtil.fileToList(dir+"/"+fileName));
        }
        Set<Item> items = new HashSet<Item>();
        for(String line : lines) {
            items.add(extractItem(line));
        }
        return items;
    }    
    
    //extract item from one line
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
    
    private void testExtractLinksFromHtml() throws Exception{
        String html = FileUtil.fileToString(DIR_SAMPLE+"/sample1-withTags.txt");
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("a[href]"); // a with href
        for(Element e : links){
            System.out.println(e.attr("href"));    
        }
    }
    
    private void testExtractTextFromHtml() throws Exception{
        String html = FileUtil.fileToString(DIR_SAMPLE+"/sample1-withTags.txt");
        String textOnly = Jsoup.parse(html).text();
        System.out.println(textOnly);
    }
    
    private void testListFiles(){
        List<String> fileNames = FileUtil.getAllFileNames(DIR_SAMPLE, true);
        for(String fileName : fileNames) {
            System.out.println(fileName);
        }
    }
}
