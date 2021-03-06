package others.e.model;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;

import others.e.EUtil;
import utils.CookieManager;
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
	public static String URLAudio = "http://www.vocabulary.com/dictionary/audio/en/%s/0";
	public static String URLSite = "http://www.vocabulary.com";
	
	//this url can retrieve sentences
	//"http://corpus.vocabulary.com/examples.json?query=felicitous&maxResults=5&startOffset=0&filter=0";
	public static String UrlSentences = "http://corpus.vocabulary.com/examples.json?query=%s&maxResults="+TOTAL_SAMPLE_SENTENCES+"&startOffset=0&filter=0";
	public static String UrlSentencesBatch = "http://corpus.vocabulary.com/examples.json?query=%s&maxResults=%d&startOffset=%d&filter=0&jsonp=json%d";
	
	private static final int SENTENCE_MIN_WORDS = 30;
	/** set by extractSentences method**/
	private List<Offset> offsetList = new ArrayList<Offset>();
	 
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
		String word = "elegy";//constituency, elegy
		Vcab vcab = new Vcab();
		String sentences = vcab.extractSentences(word);
		System.out.println(sentences);
		
		if(true){
			return;
		}
		
		String vcabContent = WebUtil.getPageSource(Vcab.URL +word, "utf-8");
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
	
	
	public String extractSentences(String word){
    	int batchSize = 5;//24
    	int startOffset = 0;
    	int jsonIdx = 0;
    	
		StringBuilder sb = new StringBuilder();
    	for(int i=0;i<1;i++){
    		jsonIdx = i;
    		startOffset = jsonIdx * batchSize; 
    		String batchUrl = String.format("http://corpus.vocabulary.com/examples.json?query=%s&maxResults=%d&startOffset=%d&filter=0&jsonp=json%d", 
				word, batchSize, startOffset, jsonIdx);
    		
    		//System.out.println(batchUrl);
    		String json = WebUtil.getPageSource(batchUrl, "utf-8");
    		if(json.isEmpty()){//empty means reached the end of sentences list in vcab site
    			break;
    		}
    		extractSentencesOneJsonBatch(sb, json);
    		//System.out.println(jsonIdx+" done");
    	}
    	return sb.toString();
	}

	public void extractSentencesOneJsonBatch(StringBuilder sb, String json){
		List<Offset> tmpOffsetList = new ArrayList<Offset>();
		
		//set sentencesOffSetsArr
		String sentenceOffSetsPattern = "\"offsets\"\\:\\[.*?\\]";
		List<String> offSetsList = RegUtil.getMatchedStrings(json, sentenceOffSetsPattern);
		for(int i=0; i<offSetsList.size(); i++){
			String tmpStr = offSetsList.get(i);

			//set begin=end=0 for multiple occurrence
			int begin = 0;
			int end = 0;
			if(tmpStr.indexOf(",")==tmpStr.lastIndexOf(",")){
				begin = Integer.parseInt(tmpStr.substring(tmpStr.indexOf("[")+1, tmpStr.indexOf(",")));
				end = Integer.parseInt(tmpStr.substring(tmpStr.indexOf(",")+1, tmpStr.indexOf("]")));
			}
			Offset offset = new Offset(begin, end);
			tmpOffsetList.add(offset);

		}
		//update offSets
		String sentencePattern = "\"sentence\":\".*?\",";
		List<String> ddList = RegUtil.getMatchedStrings(json, sentencePattern);
		for(int i=0; i<ddList.size(); i++){
			String s = ddList.get(i);
			if(sb.length()>0){
				sb.append("\r\n");
			}
			Offset offset = tmpOffsetList.get(i);
			offset.setBegin(offset.getBegin() + sb.length());
			offset.setEnd(offset.getEnd() + sb.length());

			String tmp = s.replaceAll("\"sentence\":\"|\",", "");
			tmp = converUtf8ToAscii(tmp);
			sb.append(tmp);
		}
		offsetList.addAll(tmpOffsetList);
	}
	
	public HSSFRichTextString getSentencesRTS(HSSFFont font) {
		String tmpS = "";
		if(this.sentences!=null){
			tmpS = this.sentences;
		}
		HSSFRichTextString rts = new HSSFRichTextString(tmpS);
		for(int i=0; i<offsetList.size(); i++) {
			Offset offset = offsetList.get(i);
			if(offset.getBegin() < tmpS.length()
					&& offset.getEnd() < tmpS.length()
					){
				rts.applyFont(offset.getBegin(), offset.getEnd(), font);
			}
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
	
	public static String getPhoneUrl(String name){
		String tmpName = name.toLowerCase();
		return String.format(Vcab.URLAudio, tmpName);
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
	
	public static CookieManager createCookieManager() throws Exception {
		CookieManager cm = new CookieManager(); 
		URL siteUrl = new URL(URLSite);
		URLConnection siteUc = siteUrl.openConnection();
		siteUc.connect();
		cm.storeCookies(siteUc);
		return cm;
	}
	
	private static String converUtf8ToAscii(String origStr){
		String tmp = origStr
		.replaceAll("u2011", "‑")
		.replaceAll("u2013", "–")
		.replaceAll("u2014", "—")
		.replaceAll("u2018", "‘")
		.replaceAll("u2019", "’")
		.replaceAll("u201c", "“")
		.replaceAll("u201d", "”")
		.replaceAll("u2026", "…")
		.replaceAll("u2032", "′")
		.replaceAll("\\\\", "")		;
		return tmp;
	}
	
	
	private static class Offset {
		private boolean delete;
		int begin;
		int end;
		
		public Offset(int begin, int end) {
			super();
			this.delete = false;
			this.begin = begin;
			this.end = end;
		}

		public boolean isDelete() {
			return delete;
		}

		public void setDelete(boolean delete) {
			this.delete = delete;
		}

		public int getBegin() {
			return begin;
		}

		public void setBegin(int begin) {
			this.begin = begin;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
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
