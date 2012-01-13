package others.e.model;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import others.e.EUtil;
import utils.FileUtil;


//testing
//abc:      [  ][      ][  ][      ]
//wispy:    [en][en-mp3][us][      ]
//weather:  [en][en-mp3][us][      ] 
//converse: [en][      ][us][us-mp3]

public class Iciba {
	
	//static members
	public static final String MP3_OUTPUT_DIR = EUtil.PHONE_ROOT+"/output/iciba/";
	public static final String ICIBA_URL = "http://www.iciba.com/";// + case insensitive
	
	public static final String ICIBA_EN = "英";
	public static final String ICIBA_US = "美";	

	public static String EN_MISSING = "ENMISSING";
	public static String US_MISSING = "USMISSING";

	
	//instance members
	private String meaning;
	public final static String NOT_FOUND="NotFound";
	
	private String enIPA;//International Phonetic Alphabet
	private String enMp3Url;
	private boolean enHasMp3;//iciba site
	
	private String usIPA;
	private String usMp3url;
	private boolean usHasMp3;//iciba site

	//indicate if local has mp3
	private boolean localEnHasMp3 = true;
	private boolean localUsHasMp3 = true;
	
	
	
	//testing
	public static void main(String [] args) throws IOException{
		System.out.println("aaa");
		String icibaContent = FileUtil.fileToString("D:/projects/java/workspace/Book-JavaThreads3E/src/others/e/html/iciba.html");
		String meaning = extractICIBAMeaning(icibaContent);
		//System.out.println(meaning);
	}
	
	
	public static String extractICIBAMeaning(String icibaContent){
		StringBuilder sb = new StringBuilder();
		String icibaContent2 = icibaContent.replaceAll("<span class=\"show_num\">", "").replaceAll("</span>&nbsp;", "");
		String meaningPattern = "<span class=\"ec_mean\">.*?</span>";
		Pattern p = Pattern.compile(meaningPattern, Pattern.MULTILINE);
		Matcher m = p.matcher(icibaContent2);
		boolean found = m.find();
		while(found) {
			String tmpMeaning = m.group();
			sb.append(tmpMeaning.replaceAll("<span class=\"ec_mean\">", "").replaceAll("</span>", ""));
			sb.append(System.getProperty("line.separator"));
			//System.out.println(tmpMeaning);
			found = m.find();
		}
		//System.out.println(sb.toString());
		return sb.toString();
	}
	
	//get iciba meaning
	public static String extractICIBAMeaningOld(String icibaContent) {
		String tmp=NOT_FOUND;
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
	public static String[] getICIBAPhoneItem(String icibaContent){

		
		String[] resultStrArr = new String[4]; //En, EnUrl, US, USUrl
		String[] tmpResultArr = matchPhoneBlock(ICIBA_EN,icibaContent);
		resultStrArr[0]=tmpResultArr[0];
		resultStrArr[1]=tmpResultArr[1];
		tmpResultArr = matchPhoneBlock(ICIBA_US,icibaContent);
		resultStrArr[2]=tmpResultArr[0];
		resultStrArr[3]=tmpResultArr[1];
		return resultStrArr;
	}
	
	//find phonetic block in icibaContent
	private static String[] matchPhoneBlock(String nation, String icibaContent){
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

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String getEnIPA() {
		return enIPA;
	}

	public void setEnIPA(String enIPA) {
		this.enIPA = enIPA;
	}

	public String getEnMp3Url() {
		return enMp3Url;
	}

	public void setEnMp3Url(String enMp3Url) {
		this.enMp3Url = enMp3Url;
	}

	public boolean isEnHasMp3() {
		return enHasMp3;
	}

	public void setEnHasMp3(boolean enHasMp3) {
		this.enHasMp3 = enHasMp3;
	}

	public String getUsIPA() {
		return usIPA;
	}

	public void setUsIPA(String usIPA) {
		this.usIPA = usIPA;
	}

	public String getUsMp3url() {
		return usMp3url;
	}

	public void setUsMp3url(String usMp3url) {
		this.usMp3url = usMp3url;
	}

	public boolean isUsHasMp3() {
		return usHasMp3;
	}

	public void setUsHasMp3(boolean usHasMp3) {
		this.usHasMp3 = usHasMp3;
	}

	public boolean isLocalEnHasMp3() {
		return localEnHasMp3;
	}

	public void setLocalEnHasMp3(boolean localEnHasMp3) {
		this.localEnHasMp3 = localEnHasMp3;
	}

	public boolean isLocalUsHasMp3() {
		return localUsHasMp3;
	}

	public void setLocalUsHasMp3(boolean localUsHasMp3) {
		this.localUsHasMp3 = localUsHasMp3;
	}

	
	
	
	
}
