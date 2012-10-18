package others.e.model;

import java.io.IOException;
import java.util.List;

import utils.RegUtil;
import utils.WebUtil;
/**
 * 
 * haven't found a way to download
 *
 */
public class Dictcn {
	//static members
	public static String URLws = "http://dict.cn/ws.php?q=";//  + lowercase
	public static String URLhttp = "http://dict.cn/";//  + lowercase
	public static String ENCODINGws = "GBK";
	public static String ENCODINGhttp = "UTF-8";
	
    //instance members
    private String meaning;
    public final static String NOT_FOUND="NotFound";
    
    //testing
    public static void main(String [] args) throws IOException{
		String pageContent = WebUtil.getPageSource(Dictcn.URLhttp +"hook", Dictcn.ENCODINGhttp);
		//System.out.println(pageContent);
		String meaning = extractMeaningHttp(pageContent);
		System.out.println(meaning);

    }
    
	public static String extractMeaningHttp(String pageContent){
		
		String ddPattern = "<div class=\"layout basic\">.*?</div>";
		List<String> ddList = RegUtil.getMatchedStrings(pageContent, ddPattern);
		if(ddList.size()>0){
			StringBuilder sb = new StringBuilder();
			String tmpBasicMeaning = ddList.get(0);
			String basicDDPattern = "<li>.*?</li>";
			List<String> basicDDList = RegUtil.getMatchedStrings(tmpBasicMeaning, basicDDPattern);
			if(basicDDList.size()>0){
				for(String basicDD : basicDDList) {
					if(sb.length()>0){
						sb.append("\r\n");
					}
					sb.append(basicDD);
				}
			}
			if(sb.length()>0){
				return sb.toString();
			}
		}
		return NOT_FOUND;
	}
    
	public static String extractMeaningWS(String pageContent){
		
		String ddPattern = "<def>.*?</def>";
		List<String> ddList = RegUtil.getMatchedStrings(pageContent, ddPattern);
		if(ddList.size()>0){
			return ddList.get(0).replaceAll("<def>", "").replaceAll("</def>", "");
		}
		return NOT_FOUND;
	}

	//auto generated
	public String getMeaning() {
		return meaning;
	}
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	
	
	
}
