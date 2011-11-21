package others.e.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.WebUtil;

public class Wwo {
	
	//static members
	public static String WWO_URL = "http://www.wordwebonline.com/en/";// +UPPERCASE
	
	
	
	
	//instance members
	public String sentences;
	
	
	// get WWO sentences
	public static String getWWOS(String wwoContent) {
		//String wwoContent = WebUtil.getPageSource(WWO_URL + word.toUpperCase(), "utf-8");
		StringBuilder sb = new StringBuilder();

		String htmlSentence = "(&quot;).*?(&quot;)";
		Pattern p = Pattern.compile(htmlSentence);
		Matcher m = p.matcher(wwoContent);
		boolean found = m.find();
		while (found) {
			String tmpSentence = m.group().replaceAll("\\<.*?\\>", "").replaceAll("&quot;", "");
			sb.append(tmpSentence);
			sb.append("\n");
			found = m.find();
		}
		return sb.toString().trim();
	}


	public String getSentences() {
		return sentences;
	}


	public void setSentences(String sentences) {
		this.sentences = sentences;
	}
		
	
	
	// for testing
	public static void main(String args[]){
		String mwContent = WebUtil.getPageSource(Wwo.WWO_URL +"suffuse".toUpperCase(), "utf-8");
		Wwo wwo = new Wwo();
		String result = Wwo.getWWOS(mwContent);
		
		System.out.print(result);
	}

	
}
