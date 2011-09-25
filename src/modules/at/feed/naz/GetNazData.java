package modules.at.feed.naz;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import utils.FileUtil;
import utils.GlobalSetting;
import utils.RegUtil;
import utils.TimeUtil;
import utils.WebUtil;

public class GetNazData {

	private static String stockcode = "tna";//qqq, tna, 
    /**
     * to make tick order by : earliest -> latest
     * timeLot 1->13
     * pages@timeLot max->1
     * ticks@page bottom->top
     */
    public static void main(String[] args) throws IOException {
    	
    	long b0 = System.currentTimeMillis();
        LinkedList<String> allList = new LinkedList<String>();
        initTotalPageArr();

        for(int i=0;i<totalPageArr.length;i++){
        	for(int pageno=totalPageArr[i];pageno>=1;pageno--){

				Stack<String> onePageStack = extractPageTicks((i+1), pageno);
				while (!onePageStack.isEmpty()) {
					allList.add(onePageStack.pop());
				}

        		System.out.println("timeLot:"+(i+1)+" page:"+pageno+" done");
        	}
        }

      String tickOutputFilePath = GlobalSetting.TEST_HOME+"/data/naz/tick/output/"+stockcode+"/"+TimeUtil.getCurrentTimeStr()+".txt";
      FileUtil.listToFile(allList, tickOutputFilePath);
      
      long e0 = System.currentTimeMillis();
      System.out.println("Total used time: "+(e0-b0)/1000+" seconds. Output file:"+tickOutputFilePath);

    }

    
    
    
    //initialize totalPageArr
    private static void initTotalPageArr(){
        for(int i=0;i<13;i++){
            int totalPageNum = getTotalPages(i+1);
            totalPageArr[i] = totalPageNum;
            System.out.println("page "+(i+1)+" done.");
        }

        for(int i=0;i<13;i++){
            System.out.println((i+1)+":"+totalPageArr[i]);
        }
    }
    
    //get total pages for specified time lot
    private static int getTotalPages(int timeLot){
        String urlStr = baseUrlStr+"?time="+timeLot;
        String tmpPage = WebUtil.getPageSource(urlStr, encoding);
        String totalPagePattern = "<span id=\"TotalPagesLabel\">.*?</span>";
        List<String> totalPageList = RegUtil.getMatchedStrings(tmpPage, totalPagePattern);
        String totalPageStr = totalPageList.get(0).replace("<span id=\"TotalPagesLabel\">", "").replace("</span>", "").trim();
        return Integer.parseInt(totalPageStr);
    }
    
    
    private static Stack<String> extractPageTicks(int timeLot, int pageno){
        String page = WebUtil.getPageSource(baseUrlStr+"?pageno="+pageno+"&time="+timeLot, encoding);

        String dataTablePattern = "<table class=\"AfterHoursPagingContents\" name=\"AfterHoursPagingContents_Table\".*</table>";
        List<String> dataTableList = RegUtil.getMatchedStrings(page, dataTablePattern);
        String dataTable = dataTableList.get(0);
        
        String trPattern = "<tr>.*?</tr>";
        List<String> trList = RegUtil.getMatchedStrings(dataTable, trPattern);
        
        String tdPattern = "<td>.*?</td>";
        
        Stack<String> onePageTickStack = new Stack<String>();
        for(String trStr: trList){
            List<String> tdList = RegUtil.getMatchedStrings(trStr, tdPattern);
            String tick = "";
            for(String tdStr: tdList){
                //remove chars: "<td>","</td>","$","&nbsp;"," ",","
                tick += tdStr.replaceAll("<td>|</td>|\\$|&nbsp;| |,", "")+",";
            }
            //System.out.println(tick);
            if(!tick.trim().equals("")){
                //onePageList.addLast(tick);
                onePageTickStack.push(tick);
            }
        }
        return onePageTickStack;
        
    }
    
    //?pageno=4&time=7   //all starting from 1
    private static String baseUrlStr = "http://www.nasdaq.com/symbol/"+stockcode+"/time-sales";
    private static String filePath = GlobalSetting.TEST_HOME+"/tmp/tick/pageoutput/output1.html";
    private static String encoding = "iso-8859-1";
    
    
    private static int[] totalPageArr = new int[13];
    /*
    <option value="1">9:30 - 9:59</option>
    <option value="2">10:00 - 10:29</option>
    <option value="3">10:30 - 10:59</option>
    <option value="4">11:00 - 11:29</option>
    <option value="5">11:30 - 11:59</option>
    <option value="6">12:00 - 12:29</option>
    <option value="7">12:30 - 12:59</option>
    <option value="8">13:00 - 13:29</option>
    <option value="9">13:30 - 13:59</option>
    <option value="10">14:00 - 14:29</option>
    <option value="11">14:30 - 14:59</option>
    <option value="12">15:00 - 15:29</option>
    <option value="13">15:30 - 16:00</option>
     */
}
