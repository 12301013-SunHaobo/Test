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
	public static String URL = "http://dict.cn/ws.php?q=";//  + lowercase
	public static String ENCODING = "GBK";
	
    //instance members
    private String meaning;
    public final static String NOT_FOUND="NotFound";
    
    //testing
    public static void main(String [] args) throws IOException{
		String pageContent = WebUtil.getPageSource(Dictcn.URL +"hook", Dictcn.ENCODING);
		//System.out.println(pageContent);
		String meaning = extractMeaning(pageContent);
		System.out.println(meaning);

    }
    
    
	public static String extractMeaning(String pageContent){
		
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
