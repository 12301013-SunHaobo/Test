package others.e.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import others.e.EUtil;
import others.utils.WebUtil;

/**
 * merriam-webster
 * @author rong
 *
 */
public class Mw {

	//static members
	public static String MW_URL = "http://www.merriam-webster.com/dictionary/";//  + case insensitive
	private final static int SENTENCE_SIZE_LIMIT = 120;
	
	public static final String WAV_OUTPUT_DIR = EUtil.PHONE_ROOT+"/output/mw/wav/";
	
	//instance members
	private String sentences;
	//<word,wavUrl>
	private Map<String, String> wavs;
	private boolean localHasWav = true;
	
	// get WWO sentences
	public static String extractMwS(String mwContent) {

		Set<String> sentenceSet = new HashSet<String>();
		//in < >
		String htmlSentence = "(&lt;).*?(&gt;)";
		Pattern p = Pattern.compile(htmlSentence);
		Matcher m = p.matcher(mwContent);
		boolean found = m.find();
		while (found) {
			String tmpSentence = m.group().replaceAll("&lt;", "")
			.replaceAll("&gt;", "").replaceAll("<em>", "").replaceAll("</em>", "");
			//sb.append(tmpSentence);
			//sb.append("\n");
			if(tmpSentence!=null && tmpSentence.length()<SENTENCE_SIZE_LIMIT){
				sentenceSet.add(tmpSentence);
			}
			found = m.find();
		}
		
		
		//in Examples of xxx 
		htmlSentence = "(<div class=\"example-sentences\">).*?(</div>)";
		p = Pattern.compile(htmlSentence, Pattern.DOTALL);
		m = p.matcher(mwContent);
		found = m.find();
		while (found) {
			String tmpSentence = m.group();
			Pattern p1 = Pattern.compile("(<li).*?(</li>)", Pattern.DOTALL);
			Matcher m1 = p1.matcher(tmpSentence);
			boolean found1 = m1.find();
			while(found1){
				String tmpExampleSentence = m1.group().replaceAll("<li class=\"always-visible\">", "")
				.replaceAll("<em>", "").replaceAll("</em>", "").replaceAll("</li>", "")
				.replaceAll("<.*?>", "")
				.replaceAll("&lt;", "").replaceAll("&gt;", "");//example sentence may have <>
				//sb.append(tmpExampleSentence);
				//sb.append("\n");
				if(tmpExampleSentence!=null && tmpExampleSentence.length()<SENTENCE_SIZE_LIMIT){
					sentenceSet.add(tmpExampleSentence);
				}
				found1 = m1.find();
			}
			found = m.find();
		}
		
		StringBuilder sb = new StringBuilder();
		for(String s: sentenceSet){
			if(!"[+]more[-]hide".equals(s)){
				sb.append(s);
				sb.append(System.getProperty("line.separator"));
			}
		}
		
		return sb.toString().trim();
	}
	
	
	public static Map<String,String> getPhones(String name){
		Map<String,String> resultMap = new HashMap<String,String>();
		
		String urlWord = "http://www.merriam-webster.com/dictionary/"+name;
		String pageWord = WebUtil.getPageSource(urlWord, "utf-8");
		
		String phonePattern = "(onclick=\"return au).*?(;\")";
		Pattern p = Pattern.compile(phonePattern, Pattern.DOTALL);
		Matcher m = p.matcher(pageWord);
		boolean found = m.find();
		while(found){
			String tmpPhoneBlock = m.group();
			tmpPhoneBlock = tmpPhoneBlock.replaceAll("onclick=\"return au\\(", "").replaceAll("\\);\"", "").replaceAll("'", "").replaceAll(" ", "").trim();
			//System.out.println("tmpPhoneBlock:"+tmpPhoneBlock);
			String phoneKey = tmpPhoneBlock.substring(0, tmpPhoneBlock.indexOf(","));
			String word = tmpPhoneBlock.substring(tmpPhoneBlock.indexOf(",")+1);;
			//System.out.println("["+phoneKey+"]:["+word+"]");
			
			String pagePhone = WebUtil.getPageSource("http://www.merriam-webster.com/audio.php?file="+phoneKey+"&word="+word, "utf-8");
			String wavPattern = "(<a HREF=\"http://media.merriam-webster.com/).*(\">Click here to listen with your default audio player</a>)";
			Pattern pWav = Pattern.compile(wavPattern, Pattern.DOTALL);
			Matcher mWav = pWav.matcher(pagePhone);
			boolean foundWav = mWav.find();
			if(foundWav){
				String wavUrl = mWav.group();
				wavUrl = wavUrl.replaceAll("<a HREF=\"", "").replaceAll("\">Click here to listen with your default audio player</a>", "");
				//System.out.println(wavUrl);
				resultMap.put(word, wavUrl);
			}
			
			found = m.find();//loop for word page
		}
		return resultMap;
	
	}
	
	
	
	
	
	
	
	public String getSentences() {
		return sentences;
	}


	public void setSentences(String sentences) {
		this.sentences = sentences;
	}


	
	
	
	
	
	
	
	
	public boolean isLocalHasWav() {
		return localHasWav;
	}


	public void setLocalHasWav(boolean localHasWav) {
		this.localHasWav = localHasWav;
	}


	// for testing
	public static void main(String args[]){
//		String mwContent = WebUtil.getPageSource(Mw.MW_URL +"discretion", "utf-8");
//		Mw mw = new Mw();
//		String result = mw.extractMwS(mwContent);
//		
//		System.out.print(result);
		
		//get phones
		Map<String,String> map = getPhones("ticket");
		for(String word: map.keySet()){
			System.out.println(word+":"+map.get(word));
		}
		
	}
	
	
	
}
