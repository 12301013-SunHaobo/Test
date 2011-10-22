package others.e.old;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import utils.BoundedExecutor;

public class TestCrawler {
	static String INPUT_FILE="GW-list-full.txt";
	static String INPUT_DIR = "C:/user/backup/rong/en/input/";
	static String MP3_OUTPUT_DIR = "C:/user/backup/rong/en/output/iciba/";

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		StopWatch sw = new StopWatch();
		sw.start();
		
		long b0 = sw.getTime();
		
		// init ThreadPoolExecutor
		int poolSize = 200;
		int maxPoolSize = poolSize+5;
		long keepAliveTime = 30;
		final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(poolSize, maxPoolSize,
				keepAliveTime, TimeUnit.MINUTES, queue);
		BoundedExecutor be = new BoundedExecutor(tpe, maxPoolSize);

		List<String> wordList = new ArrayList<String>();
		List<String[]> allMissingWords = getAllMissingMp3Words();
		try {
			wordList = getAllWords();
			//wordList.add("converse");//testing

			allMissingWords = getAllMissingMp3Words();

			String[] missingWord;
			for(int i=0;i<allMissingWords.size();i++){
				missingWord = allMissingWords.get(i);
				System.out.println(i+":"+missingWord[0]+","+missingWord[1]+","+missingWord[2]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//submit task for mp3
		for (int i = 0; i < allMissingWords.size(); i++) {
			PhoneCrawler dp = new PhoneCrawler(null, allMissingWords.get(i));
			try {
				be.submit(dp);
				System.out.println(i + ":submited:");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		tpe.shutdown();
		try {
			tpe.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
		/*
		// submit tasks for excel
		List<String[]> excelList = Collections.synchronizedList(new ArrayList<String[]>());
		PhoneCrawler.init(excelList);
		for (int i = 0; i < wordList.size(); i++) { //wordList.size()
			PhoneCrawler dp = new PhoneCrawler(wordList.get(i),null);
			try {
				be.submit(dp);
				System.out.println(i + ":submited:");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		tpe.shutdown();
		try {
			tpe.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		toExcel(excelList);
		*/
		long e0 = sw.getTime();
		System.out.println("total time:" + (e0-b0));
	}

	//[word, en-missing, us-missing]
	private static List<String[]> getAllMissingMp3Words() throws Exception {
		List<String> allWords = getAllWords();
		Set<String> enSet = new HashSet<String>();
		Set<String> usSet = new HashSet<String>();
		
		List<String[]> missingMp3Words = new ArrayList<String[]>();
		//en set
		File folder = new File(MP3_OUTPUT_DIR + "/en/");
		File[] files = folder.listFiles();
		String tmpFileName;
		for(File f:files){
			tmpFileName = f.getName();
			enSet.add(tmpFileName.substring(0, tmpFileName.lastIndexOf(".")));
		}
		//us set
		folder = new File(MP3_OUTPUT_DIR + "/us/");
		files = folder.listFiles();
		
		for(File f:files){
			tmpFileName = f.getName();
			usSet.add(tmpFileName.substring(0, tmpFileName.lastIndexOf(".")));
		}
		
		for(String word: allWords){
			if(!enSet.contains(word) || !usSet.contains(word)){
				String[] missingWord = new String[3];
				missingWord[0]= word;
				if(!enSet.contains(word)){
					missingWord[1] = PhoneCrawler.EN_MISSING;
				}
				if(!usSet.contains(word)){
					missingWord[2] = PhoneCrawler.US_MISSING;
				}
				missingMp3Words.add(missingWord);
			}
		}

		return missingMp3Words;
	}


	private static List<String> getAllWords() throws Exception {
		List<String> allWords = new ArrayList<String>();
		BufferedReader input = new BufferedReader(new FileReader(INPUT_DIR+"/"+INPUT_FILE));
		String line = null;
		while ((line = input.readLine()) != null) {
			if(!"".equals(line.trim())){
				allWords.add(line.trim());
			}
		}
		return allWords;
	}

	static String OUTPUT_DIR = "C:/user/backup/rong/en/output/";
	static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd-HHmmss");
	static public void toExcel(List<String[]> strArrList) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		//for hyperlink
		CreationHelper createHelper = workbook.getCreationHelper();
		CellStyle hlink_style = workbook.createCellStyle();
	    Font hlink_font = workbook.createFont();
	    hlink_font.setUnderline(Font.U_SINGLE);
	    hlink_font.setColor(IndexedColors.BLUE.getIndex());
	    hlink_style.setFont(hlink_font);
	    hlink_style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
	    hlink_style.setAlignment(CellStyle.ALIGN_LEFT);
	    //for cell style
		CellStyle cs = workbook.createCellStyle();
		cs.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cs.setAlignment(CellStyle.ALIGN_LEFT);
		
		Sheet sheet = workbook.createSheet("Auto");
		for (int i = 0; i < strArrList.size(); i++) {
			String[] oneWord = strArrList.get(i);
			Row row = sheet.createRow(i);
			for (int j = 0; j <= 4; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(oneWord[j]);
				cell.setCellStyle(cs);
				if(j==1 ){
					if(oneWord[5]!=null && !"".equals(oneWord[5])){
						Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
					    link.setAddress("./iciba/en/"+oneWord[0]+".mp3");
					    cell.setHyperlink(link);
					    cell.setCellStyle(hlink_style);
						//cell.setCellFormula("HYPERLINK(\"./iciba/en/"+oneWord[0]+".mp3\",\""+oneWord[j]+"\")");
					}
				} else if(j==2){
					if(oneWord[6]!=null && !"".equals(oneWord[6])){
						Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
					    link.setAddress("./iciba/us/"+oneWord[0]+".mp3");
					    cell.setHyperlink(link);
					    cell.setCellStyle(hlink_style);
						//cell.setCellFormula("HYPERLINK(\"./iciba/us/"+oneWord[0]+".mp3\",\""+oneWord[j]+"\")");
					}
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
	
}
