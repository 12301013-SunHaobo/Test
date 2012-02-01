package others.e.model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import others.e.EUtil;
import utils.WebUtil;

/**
 * WordReference has relatively better meaning than iciba.com
 */
public class WR {

	//static members
	public static String URL = "http://www.wordreference.com/enzh/";//  + lowercase
	
	private String meaning;

	private String synonyms;//TODO:
	
	// for testing
	public static void main(String args[]){
		String pageContent = WebUtil.getPageSource(WR.URL +"red", "utf-8");
		String meaning = extractMeaning(pageContent);
		System.out.println(meaning);
		
	}	
	
	public static String extractMeaning(String pageContent){
		StringBuilder sb = new StringBuilder();
		String meaningPattern = "ToW2.*?<span";
		Pattern p = Pattern.compile(meaningPattern, Pattern.MULTILINE);
		Matcher m = p.matcher(pageContent);
		boolean found = m.find();
		while(found) {
			String tmpMeaning = m.group();
			if(sb.length()>0){
				sb.append(System.getProperty("line.separator"));
			}
			sb.append(tmpMeaning.replaceAll("ToW2' >", "").replaceAll(" <span", ""));
			//System.out.println(tmpMeaning);
			found = m.find();
			break;//only use the first meaning
		}
		//System.out.println(sb.toString());
		return sb.toString();
	}

//	public static String extractSynonyms(String pageContent){
//		
//	}
	
	
	
	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	

	

	
}
