package others.e.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import others.e.EUtil;
import utils.FileUtil;
import utils.RegUtil;
import utils.WebUtil;

/**
 * vocabulary.com has good pronouncation
 */
public class Vcab {

	//static members
	public static String URL = "http://www.vocabulary.com/definition/";//  + lowercase
	
	public static final String OUTPUT_DIR = EUtil.PHONE_ROOT+"/output/vcab/mp3/";
	
	//instance members
	private String sentences;
	//<word,wavUrl>
	private Map<String, String> mp3s;
	private boolean localHasPhone = true;

	//synonyms
	private Set<String> synonyms = new HashSet<String>();
	private static List<String> allWords = null;
	private static boolean filterWithAllWords = false;//switch to use all-list.txt to filter or not 
		static {
			if(filterWithAllWords){
				try {
					//"all-list.txt"; "GW-list-full.txt"; "test-synonyms-list.txt"
					allWords = FileUtil.fileToList(EUtil.PHONE_ROOT+"input/all-list.txt");
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
	
	// for testing
	public static void main(String args[]){
//		String pageContent = WebUtil.getPageSource(Vcab.URL +"red", "utf-8");
//		System.out.println(pageContent);
//		Map<String,String> map = getPhones("ticket");
//		for(String word: map.keySet()){
//			System.out.println(word+":"+map.get(word));
//		}
		
		Set<String> synonyms = getSynonyms("red");
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
	
	
	public static Set<String> getSynonyms(String pageContent){
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
	
	/** to save to synonyms file  **/
	public static String toSynonymLine(String name, Set<String> synonymSet, List<String> allWords){
		boolean contains = false;
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(" [");
		for(String s : synonymSet) {
			if(allWords.contains(s)){
				contains = true;
				sb.append(s);
				sb.append(",");
			}
		}
		sb.append("]");
		if(contains){
			return sb.toString();
		}else{
			return null;
		}
	}
	
	/** to excel cell **/
	public String toSynonymsStr(){
		boolean contains = false;
		StringBuilder sb = new StringBuilder();
		for(String s : synonyms) {
			if(filterWithAllWords && allWords.contains(s) || !filterWithAllWords){
				contains = true;
				sb.append(s);
				sb.append(", ");
			}
		}
		if(contains){
			return sb.toString();
		}else{
			return "";
		}
	}
	/** from excel synonyms cell to set **/
	public void fromSynonymsStr(String synonymsStr) {
		if(synonymsStr!=null){
			String[] arr = synonymsStr.split(",");
			for(String s : arr) {
				String tmpStr = s.trim();
				if(tmpStr!=null && !"".equals(tmpStr)){
					synonyms.add(tmpStr);
				}
			}
		}
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

	public Set<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(Set<String> synonyms) {
		this.synonyms = synonyms;
	}
	
	
	
}
