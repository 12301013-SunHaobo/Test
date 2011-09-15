package others.e.old;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import others.utils.WebUtil;

//testing
// abc:      [  ][      ][  ][      ]
// wispy:    [en][en-mp3][us][      ]
// weather:  [en][en-mp3][us][      ] 
// converse: [en][      ][us][us-mp3]

public class PhoneCrawler implements Callable<String>{
	
	
	static String WWO_URL = "http://www.wordwebonline.com/en/";// +UPPERCASE
	static String ICIBA_URL = "http://www.iciba.com/";// + case insensitive
	static String ICIBA_EN = "英";
	static String ICIBA_US = "美";
	
	public static String EN_MISSING = "ENMISSING";
	public static String US_MISSING = "USMISSING";
	
	static String MP3_OUTPUT_DIR = "C:/user/backup/rong/en/output/iciba/";
	
	private static List<String[]> excelRowList;
	
	private String word;
	private String[] missingWord;
	
	public PhoneCrawler(String word, String[] missingWord) {
		super();
		this.word = word;
		//download mp3
		this.missingWord = missingWord;
		if(word==null && missingWord!=null){
			this.word= missingWord[0]; 
		}
	}
	
	
	public static void init(List<String[]> excelRowList){
		PhoneCrawler.excelRowList = excelRowList;
	}
	

	@Override
	public String call() throws Exception {
		
		//addToExcelRowList();
		downloadMp3();
		
		return null;
	}

	//add to excelRowList
	private void addToExcelRowList(){
		String icibaContent = WebUtil.getPageSource(ICIBA_URL + this.word, "utf-8");
		String [] icibaPhoneArr = getICIBAPhoneItem(icibaContent);
		String icibaMeaning = getICIBAMeaning(icibaContent);
		String wwoSentences = getWWOS(this.word);
		String[] excelRow = new String[7];
		excelRow[0] = this.word;
		excelRow[1] = icibaPhoneArr[0];
		excelRow[2] = icibaPhoneArr[2];
		excelRow[3] = icibaMeaning;
		excelRow[4] = wwoSentences;
		excelRow[5] = icibaPhoneArr[1];//en-mp3
		excelRow[6] = icibaPhoneArr[3];//us-mp3
		PhoneCrawler.excelRowList.add(excelRow);
	}
	
	private void downloadMp3() throws Exception{
		String icibaContent = WebUtil.getPageSource(ICIBA_URL + this.word, "utf-8");
		String [] icibaPhoneArr = getICIBAPhoneItem(icibaContent);
		String enMp3Url = icibaPhoneArr[1];
		String usMp3Url = icibaPhoneArr[3];
		
		if(!"".equals(enMp3Url)){
			if(PhoneCrawler.EN_MISSING.equals(missingWord[1]) && !"".equals(enMp3Url)){
				download(enMp3Url, MP3_OUTPUT_DIR+"/en/"+this.word+".mp3");
				System.out.println(this.word+":en: downloaded.");
			}
		}else {
			System.out.println(this.word+":en: not on site.");
		}
		
		if(!"".equals(usMp3Url)){
			if(PhoneCrawler.US_MISSING.equals(missingWord[2]) && !"".equals(usMp3Url)){
				download(usMp3Url, MP3_OUTPUT_DIR+"/us/"+this.word+".mp3");
				System.out.println(this.word+":us: downloaded.");
			}
		}else {
			System.out.println(this.word+":us: not on site.");
		}
		
	}
	
	
	
	//////////////////////////////////////////////////////////////////
	// 
	//get iciba meaning
	private static String getICIBAMeaning(String icibaContent) {
		String tmp="NotFound";
		try {
			String tmpStr1 = icibaContent.substring(icibaContent.indexOf("<span class=\"ec_mean\"><span class=\"show_num\">"));
			String tmpStr2 = tmpStr1.substring(0, tmpStr1.indexOf("</ul>"));
			tmp = tmpStr2.replaceAll("<span class=\"ec_mean\"><span class=\"show_num\">", "").replaceAll("</span>&nbsp;", "").replaceAll(
					"</span>", "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp.trim();
	}	
	
	
	
	// get iciba pronounciation
	private String[] getICIBAPhoneItem(String icibaContent){

		
		String[] resultStrArr = new String[4]; //En, EnUrl, US, USUrl
		String[] tmpResultArr = matchPhoneBlock(ICIBA_EN,icibaContent);
		resultStrArr[0]=tmpResultArr[0];
		resultStrArr[1]=tmpResultArr[1];
		tmpResultArr = matchPhoneBlock(ICIBA_US,icibaContent);
		resultStrArr[2]=tmpResultArr[0];
		resultStrArr[3]=tmpResultArr[1];
		return resultStrArr;
	}
	
	
	
	private String[] matchPhoneBlock(String nation, String icibaContent){
		String[] resultArr = {"",""};//new String[2];
		String phoneBlockPattern = "(<span class=\"font_666\">\\["+nation+"\\]</span>).*?(</a>)"; ;
		Pattern p = Pattern.compile(phoneBlockPattern, Pattern.DOTALL);
		Matcher m = p.matcher(icibaContent);
		boolean found = m.find();
		if (found) {
			String pronounce="";
			String tmpPhoneBlock = m.group();
			String bracket = "\\[.*?\\]";
			Pattern p1 = Pattern.compile(bracket);
			Matcher m1 = p1.matcher(tmpPhoneBlock);
			String foundNation = null;
			if(m1.find()){//nation
				foundNation=m1.group();
			}
			if(m1.find()){//pronounce
				pronounce = m1.group().replaceAll("<span lang=\"EN-US\" class=\"phonetic fontb font14\">", "").replaceAll("</span>", "");
			}
			resultArr[0] = foundNation+pronounce;
			
			String mp3 = "(http).*?(\\.mp3)";
			Pattern pMp3 = Pattern.compile(mp3);
			Matcher mMp3 = pMp3.matcher(tmpPhoneBlock);
			if(mMp3.find()){//mp3
				String mp3Url = mMp3.group();
				resultArr[1]=mp3Url;
			}else{
				resultArr[1]="";
			}
			//for [en][][us][us-mps]
			if(ICIBA_EN.equals(nation)&&tmpPhoneBlock.indexOf(ICIBA_US)!=-1 ){
				resultArr[1]="";
			}
		}
		return resultArr;
	}
	
	
	// get WWO sentence
	private String getWWOS(String word) {
		String wwoContent = WebUtil.getPageSource(WWO_URL + word.toUpperCase(), "utf-8");
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
	
	public static void download(String urlStr, String fileName) throws Exception{
		if(urlStr==null || urlStr.trim().equals("")){
			return;
		}
		URL u = new URL(urlStr);
	    URLConnection uc = u.openConnection();
	    String contentType = uc.getContentType();
	    int contentLength = uc.getContentLength();
	    if (contentType.startsWith("text/") || contentLength == -1) {
	      throw new IOException("This is not a binary file.");
	    }
	    InputStream raw = uc.getInputStream();
	    InputStream in = new BufferedInputStream(raw);
	    byte[] data = new byte[contentLength];
	    int bytesRead = 0;
	    int offset = 0;
	    while (offset < contentLength) {
	      bytesRead = in.read(data, offset, data.length - offset);
	      if (bytesRead == -1)
	        break;
	      offset += bytesRead;
	    }
	    in.close();

	    if (offset != contentLength) {
	      throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
	    }

	    FileOutputStream out = new FileOutputStream(fileName);
	    out.write(data);
	    out.flush();
	    out.close();	
	}
	
}
