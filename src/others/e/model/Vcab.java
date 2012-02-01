package others.e.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import others.e.EUtil;
import utils.RegUtil;
import utils.WebUtil;

/**
 * vocabulary.com has good pronouncation
 */
public class Vcab {

	//static members
	public static String URL = "http://www.vocabulary.com/definition/";//  + lowercase
	private final static int SENTENCE_SIZE_LIMIT = 120;
	
	public static final String OUTPUT_DIR = EUtil.PHONE_ROOT+"/output/vcab/mp3/";
	
	//instance members
	private String sentences;
	//<word,wavUrl>
	private Map<String, String> mp3s;
	private boolean localHasPhone = true;
	
	private Set<String> synonyms;
	
	// for testing
	public static void main(String args[]){
		String pageContent = WebUtil.getPageSource(Vcab.URL +"red", "utf-8");
//		System.out.println(pageContent);
//		Map<String,String> map = getPhones("ticket");
//		for(String word: map.keySet()){
//			System.out.println(word+":"+map.get(word));
//		}
		
		Set<String> synonyms = extractSynonyms(pageContent);
		synonyms.size();

		
	}	
	
	public static Map<String,String> getPhones(String name){
		String tmpName = name.toLowerCase();
		
		Map<String,String> resultMap = new HashMap<String,String>();
		
		String urlWord = Vcab.URL+tmpName;
		String pageWord = WebUtil.getPageSource(urlWord, "utf-8");
		
		String phonePattern = "(http://audio.vocabulary.com.*?\\.mp3)";
		Pattern p = Pattern.compile(phonePattern, Pattern.DOTALL);
		Matcher m = p.matcher(pageWord);
		boolean found = m.find();
		while(found){
			String tmpPhoneBlock = m.group();
			resultMap.put(tmpName, tmpPhoneBlock);
			
			found = m.find();//loop for next
		}
		return resultMap;
	}
	
	
	public static Set<String> extractSynonyms(String pageContent){
		Set<String> synonyms = new HashSet<String>();
		String ddPattern = "<dd>.*?</dd>";
		List<String> ddList = RegUtil.getMatchedStrings(pageContent, ddPattern);
		
		for(int i=0;i<ddList.size();i++){
			String tmpSynonyms = ddList.get(i);   
			tmpSynonyms = tmpSynonyms.replaceAll("<dd>|<a.*?>|</a>|</dd>| ", "");
			String[] tmpSynonymsArr = tmpSynonyms.split(",");
			for(String s : tmpSynonymsArr){
				synonyms.add(s.toLowerCase());
			}
		}
		return synonyms;
	}
	
	
	
	
	
	
	public String getSentences() {
		return sentences;
	}

	public void setSentences(String sentences) {
		this.sentences = sentences;
	}

	public boolean isLocalHasPhone() {
		return localHasPhone;
	}

	public void setLocalHasPhone(boolean localHasPhone) {
		this.localHasPhone = localHasPhone;
	}
	

	
}
