package others.e.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;

import others.e.EUtil;
import utils.FileUtil;
import utils.RegUtil;
import utils.WebUtil;

/**
 * vocabulary.com has good pronouncation
 */
public class Vcab {

	private static final int TOTAL_SAMPLE_SENTENCES = 5;
	//static members
	public static String URL = "http://www.vocabulary.com/definition/";//  + lowercase
	
	//this url can retrieve sentences
	//"http://corpus.vocabulary.com/examples.json?query=felicitous&maxResults=5000&startOffset=0&filter=0";
	public static String UrlSentences = "http://corpus.vocabulary.com/examples.json?query=%s&maxResults="+TOTAL_SAMPLE_SENTENCES+"&startOffset=0&filter=0";
	/** set by extractSentences method**/
	private int[][] sentencesOffSetsArr = new int[TOTAL_SAMPLE_SENTENCES][2];
	//private int[] sentencesLengthArr = new int[TOTAL_SAMPLE_SENTENCES];
	 
	public static final String OUTPUT_DIR = EUtil.PHONE_ROOT+"/output/vcab/mp3/";
	
	//blurb
	private String blurbShort;
	private String blurbLong;
	
	//meaning
	private String meaning;
	//sentences
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
		String word = "mooring";
		String vcabContent = WebUtil.getPageSource(Vcab.URL +word, "utf-8");
		Vcab vcab = new Vcab();
		vcab.setSynonyms(Vcab.getSynonyms(vcabContent));
		vcab.setBlurbShort(Vcab.getBlurbShort(vcabContent));
		vcab.setBlurbLong(Vcab.getBlurbLong(vcabContent));
		vcab.setSentences(vcab.extractSentences(word));
		vcab.setMeaning(Vcab.getFullDefinitions(vcabContent));
		
		System.out.println("----- meaning ----");
		System.out.println(vcab.getMeaning());

		System.out.println("----- blurbShort ----");
		System.out.println(vcab.getBlurbShort());
		
		System.out.println("----- blurbLong ----");
		System.out.println(vcab.getBlurbLong());
		
		System.out.println("----- Sentences ----");
		System.out.println(vcab.getSentences());
		
		System.out.println("----- synonyms ----");
		System.out.println(vcab.getSynonyms());
		
	}	
	
	//cannot get word type like: adj, n
	@Deprecated
	public static String getPrimaryMeanings(String pageContent){
		StringBuilder sb = new StringBuilder();
		String meaningPattern = "class=\"def(\\s|\").*?</div>";
		List<String> ddList = RegUtil.getMatchedStrings(pageContent, meaningPattern);
		for(String s : ddList){
			System.out.println(s);
		}
		return sb.toString();
	}
	
	
	public String extractSentences(String word){
		StringBuilder sb = new StringBuilder();
		String url = String.format(UrlSentences, word);
		String json = WebUtil.getPageSource(url, "utf-8");
		
		//set sentencesOffSetsArr
		String sentenceOffSetsPattern = "\"offsets\"\\:\\[.*?\\]";
		List<String> offSetsList = RegUtil.getMatchedStrings(json, sentenceOffSetsPattern);
		for(int i=0; i<offSetsList.size(); i++){
			String tmpStr = offSetsList.get(i);
			//tmpStr = tmpStr.replaceAll("\"offsets\":[", "");
			sentencesOffSetsArr[i][0] = Integer.parseInt(tmpStr.substring(tmpStr.indexOf("[")+1, tmpStr.indexOf(",")));
			sentencesOffSetsArr[i][1] = Integer.parseInt(tmpStr.substring(tmpStr.indexOf(",")+1, tmpStr.indexOf("]")));
		}
		//update offSets
		String sentencePattern = "\"sentence\":\".*?\",";
		List<String> ddList = RegUtil.getMatchedStrings(json, sentencePattern);
		for(int i=0; i<ddList.size(); i++){
			String s = ddList.get(i);
			if(sb.length()>0){
				sb.append("\r\n");
			}
			sentencesOffSetsArr[i][0] = sentencesOffSetsArr[i][0]+sb.length();
			sentencesOffSetsArr[i][1] = sentencesOffSetsArr[i][1]+sb.length();
			String tmp = s.replaceAll("\"sentence\":\"|\",|\\\\", "");
			sb.append(tmp);
		}
		return sb.toString();
	}
	public HSSFRichTextString getSentencesRTS(HSSFFont font) {
		HSSFRichTextString rts = new HSSFRichTextString(this.sentences);
		for(int i=0; i<this.sentencesOffSetsArr.length; i++) {
			rts.applyFont(this.sentencesOffSetsArr[i][0], this.sentencesOffSetsArr[i][1], font);
		}
		return rts;
	}
	
	public static String getBlurbShort(String pageContent){
		String blurbShortPattern = "<p class=\"short\">.*?</p>";
		List<String> ddList = RegUtil.getMatchedStrings(pageContent, blurbShortPattern);
		String tmp = "";
		if(ddList.size()>0){
			tmp = ddList.get(0);
			tmp = tmp.replaceAll("<p.*?>|</p>", "");
		}
		return tmp;
		
	}
	public static String getBlurbLong(String pageContent){
		String blurbShortPattern = "<p class=\"long\">.*?</p>";
		List<String> ddList = RegUtil.getMatchedStrings(pageContent, blurbShortPattern);
		String tmp = "";
		if(ddList.size()>0){
			tmp = ddList.get(0);
			tmp = tmp.replaceAll("<p.*?>|</p>", "");
		}
		return tmp;
	}
	public static String getFullDefinitions(String pageContent){
		StringBuilder sb = new StringBuilder();
		String meaningPattern = "<h3.*?h3>";
		List<String> ddList = RegUtil.getMatchedStrings(pageContent, meaningPattern);
		for(String s : ddList){
			if(sb.length()>0){
				sb.append("\r\n");
			}
			String tmpS = s.replaceAll("\\s(\\s)+", "");
			tmpS = tmpS.replaceAll("</a>", ". ");
			tmpS = tmpS.replaceAll("<.*?>|\r|\n", "");
			
			sb.append(tmpS);
		}
		return sb.toString();
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
			tmpSynonyms = tmpSynonyms.replaceAll("<dd>|<a.*?>|</a>|</dd>", "");
			String[] tmpSynonymsArr = tmpSynonyms.split(",");
			for(String s : tmpSynonymsArr){
				synonyms.add(s.trim().toLowerCase());
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
	
	//////////////////////////////////////
	public String getBlurbShort() {
		return blurbShort;
	}

	public void setBlurbShort(String blurbShort) {
		this.blurbShort = blurbShort;
	}

	public String getBlurbLong() {
		return blurbLong;
	}

	public void setBlurbLong(String blurbLong) {
		this.blurbLong = blurbLong;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
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
