package others.e.old;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.StopWatch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import others.utils.WebUtil;

public class TestW {
	static PrintStream sysout = null;
	static {
		try {
			sysout = new PrintStream(System.out, true, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	// http://www.wordwebonline.com/en/BOOK

	static String TEST_ICIBA_FILE = "C:/user/workspace/risk/test/others/e/iciba.html";
	static String TEST_WWO_FILE = "C:/user/workspace/risk/test/others/e/wwo.html";
	static String WWO_URL = "http://www.wordwebonline.com/en/";// +UPPERCASE
	static String ICIBA_URL = "http://www.iciba.com/";// + case insensitive

	static String INPUT_DIR = "C:/user/backup/rong/en/input/GW-list.txt";
	static String OUTPUT_DIR = "C:/user/backup/rong/en/output/";
	static String MP3_OUTPUT_DIR = "C:/user/backup/rong/en/output/iciba/";
	static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd-HHmmss");
	static String LOG_FILE = "C:/user/backup/rong/en/output/error.log";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StopWatch sw = new StopWatch();
		sw.start();

		List<String> wordList = new ArrayList<String>();

		try {
			wordList = getAllWords();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String[]> resultList = new ArrayList<String[]>();

		for (int i = 0; i < wordList.size(); i++) { //wordList.size()
			try {
				long b1 = sw.getTime();
				String word = wordList.get(i);
				String icibaContent = WebUtil.getPageSource(ICIBA_URL + word, "utf-8");
				String explain = getICIBAExplain(icibaContent);
				
				String[] phone = getICIBAPhone(icibaContent);
				

				//String wwoContent = getPageSource(WWO_URL + word.toUpperCase(), "utf-8");
				//String sentences = getWWOS(wwoContent);

				String[] oneWord = new String[7];
				oneWord[0] = word;
				oneWord[1] = phone[0];//En
				oneWord[2] = phone[2];//Us
				oneWord[3] = explain;
				//oneWord[4] = sentences;
				oneWord[5] = phone[1];//En mp3 url
				oneWord[6] = phone[3];//Us mp3 url
				resultList.add(oneWord);
				
				//download
				try {
					download(phone[1], MP3_OUTPUT_DIR+"/en/"+word+".mp3");
					download(phone[3], MP3_OUTPUT_DIR+"/us/"+word+".mp3");
				} catch (Exception e) {
					log(i+":"+wordList.get(i)+":download failed.");
					e.printStackTrace();
				}
				
				long e1 = sw.getTime();
				long remaining = (e1 - b1) * (wordList.size() - i) / 1000;
				System.out.println(i + ":" + word + ":" + remaining);
			} catch (Throwable e) {
				e.printStackTrace();
				log(i+":"+wordList.get(i));
				log(e.getMessage());
			}

		}
		toExcel(resultList);

	}

	public static void log(String error) {

		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(LOG_FILE, true));
			bw.write(error);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally { // always close the file
			if (bw != null)
				try {
					bw.close();
				} catch (IOException ioe2) {
				} // end try/catch/finally
		}
	}

	private static List<String> getAllWords() throws Exception {
		List<String> allWords = new ArrayList<String>();
		BufferedReader input = new BufferedReader(new FileReader(INPUT_DIR));
		String line = null;
		while ((line = input.readLine()) != null) {
			allWords.add(line.trim());
		}
		return allWords;
	}

	static public void toExcel(List<String[]> strArr) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		
		CellStyle cs = workbook.createCellStyle();
		cs.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cs.setAlignment(CellStyle.ALIGN_LEFT);
		
		

		Sheet sheet = workbook.createSheet("Auto");
		for (int i = 0; i < strArr.size(); i++) {
			String[] oneWord = strArr.get(i);
			Row row = sheet.createRow(i);
			for (int j = 0; j <= 4; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(oneWord[j]);
				cell.setCellStyle(cs);
				if(j==1 ){
					cell.setCellFormula("HYPERLINK(\"./iciba/en/"+oneWord[0]+".mp3\",\""+oneWord[j]+"\")");
				} else if(j==2){
					cell.setCellFormula("HYPERLINK(\"./iciba/us/"+oneWord[0]+".mp3\",\""+oneWord[j]+"\")");
				}
			}
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(OUTPUT_DIR + "auto" + SDF.format(new Date()) + ".xls");
			workbook.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	static public String getContents(String aFile) {
		StringBuilder contents = new StringBuilder();
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(aFile));
			String line = null;
			while ((line = input.readLine()) != null) {
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return contents.toString();
	}

	// get iciba explain
	private static String getICIBAExplain(String content) {
		String tmpStr1 = content.substring(content.indexOf("<span class=\"ec_mean\"><span class=\"show_num\">"));
		String tmpStr2 = tmpStr1.substring(0, tmpStr1.indexOf("</ul>"));
		String tmp = tmpStr2.replaceAll("<span class=\"ec_mean\"><span class=\"show_num\">", "").replaceAll("</span>&nbsp;", "").replaceAll(
				"</span>", "\r\n");

		// System.out.println(tmp);
		return tmp.trim();
	}
	// get iciba pronounciation
	private static String[] getICIBAPhone(String content){
		String[] resultStrArr = new String[4]; //En, EnUrl, US, USUrl
		String phoneBlockPattern = "(<span class=\"font_666\">).*?(src=\"/images/orange_result/sound.gif\"/></a>)";
		Pattern p = Pattern.compile(phoneBlockPattern, Pattern.DOTALL);
		Matcher m = p.matcher(content);
		boolean found = m.find();
		int i=0;
		while (found) {
			String nation="";
			String pronounce="";
			String tmpPhoneBlock = m.group();
			String bracket = "\\[.*?\\]";
			Pattern p1 = Pattern.compile(bracket);
			Matcher m1 = p1.matcher(tmpPhoneBlock);
			if(m1.find()){//nation
				nation=m1.group();
			}
			if(m1.find()){//pronounce
				pronounce = m1.group().replaceAll("<span lang=\"EN-US\" class=\"phonetic fontb font14\">", "").replaceAll("</span>", "");
			}
			resultStrArr[i] = nation+pronounce;
			
			String mp3 = "(http).*?(\\.mp3)";
			Pattern pMp3 = Pattern.compile(mp3);
			Matcher mMp3 = pMp3.matcher(tmpPhoneBlock);
			if(mMp3.find()){//mp3
				String mp3Url = mMp3.group();
				resultStrArr[i+1]=mp3Url;
			}
			
			i=i+2;
			found = m.find();
		}
		return resultStrArr;
		
		
	}

	// get WWO sentence
	private static String getWWOS(String content) {
		StringBuilder sb = new StringBuilder();

		String htmlSentence = "(&quot;).*?(&quot;)";
		Pattern p = Pattern.compile(htmlSentence);
		Matcher m = p.matcher(content);
		boolean found = m.find();
		while (found) {
			String tmpSentence = m.group().replaceAll("\\<.*?\\>", "").replaceAll("&quot;", "");
			sb.append(tmpSentence);
			sb.append("\n");
			found = m.find();
		}
		return sb.toString().trim();
	}



	static public void download(String urlStr, String fileName) throws Exception{
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
