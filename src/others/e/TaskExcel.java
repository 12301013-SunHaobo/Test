package others.e;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;

import others.e.EUtil.Columns;
import others.e.model.Word;
import utils.BoundedExecutor;
import utils.FileUtil;

/**
 * Grab 
 * iciba meaning
 * WWO sentences
 * MW sentences
 * and save to excel
 */
public class TaskExcel {
	private static final String INPUT_FILE="manualUpdated.txt"; //"GW-list.txt, GW-list-full.txt , test-small.txt, remaining.txt 
	private static final String INPUT_DIR = EUtil.PHONE_ROOT+"/input/";
	private static final String LOG_DIR = EUtil.PHONE_ROOT+"/output/log/";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		StopWatch sw = new StopWatch();
		sw.start();
		long b0 = sw.getTime();
		
		createExcel();
		
		long e0 = sw.getTime();
		System.out.println("total used time:" + (e0-b0));
	}
	
	/*
	 * 1. input list
	 * 2. missingList: missing phones in both en&&us
	 * 3. download missing meanings
	 */
	private static void createExcel() throws Exception{

		List<Word> allWords = getAllWords();

//		if(true){
//			return;
//		}
		
		Word tmpWord;
		for (int i = 0; i < allWords.size(); i++) {
			
			//set phone flag
			tmpWord = allWords.get(i);
			if (!EUtil.vcabSet.contains(tmpWord.getName())) {
				tmpWord.getVcab().setLocalHasPhone(false);
			}
			if (!EUtil.enSet.contains(tmpWord.getName())) {
				tmpWord.getIciba().setLocalEnHasMp3(false);
			}
			if (!EUtil.usSet.contains(tmpWord.getName())) {
				tmpWord.getIciba().setLocalUsHasMp3(false);
			}
			if (!EUtil.xrSet.contains(tmpWord.getName())) {
				tmpWord.getXr().setLocalXrHasMp3(false);
			}
				
		}
		//create errorList
		List<String> errorList = Collections.synchronizedList(new ArrayList<String>());
		TExcel.init(errorList);
		
		//submit tasks for downloading mp3
		BoundedExecutor be = new BoundedExecutor(200);//default 300
		for (int i = 0; i < allWords.size(); i++) {
			tmpWord = allWords.get(i);
			TExcel tp = new TExcel(tmpWord);
			try {
				be.submit(tp);
				System.out.println(i + ":"+tmpWord.getName()+":submited:");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		be.getExecutorService().shutdown();
		try {
			be.getExecutorService().awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//EUtil.toExcel(EUtil.sortBySentence(allWords));
		//EUtil.toExcel(allWords);
		Columns[] columnsResult = new Columns[] { 
				Columns.VCAB_SYNONYMS,
				Columns.DICTCN_MEANING,
				Columns.WORD, 
				Columns.VCAB_MP3, 
				//Columns.ICIBA_EN, Columns.ICIBA_US, Columns.PHONE_XR,
				Columns.VCAB_MEANING, 
				Columns.VCAB_BLURB_SHORT, 
				Columns.VCAB_BLURB_LONG, 
				Columns.VCAB_SENTENCES
				};
		EUtil.listToExcel(allWords, columnsResult);
		
		//log errors
		DateFormat dfLog = new SimpleDateFormat("yyyyMMdd-hhmmss");
		String errorFileName = LOG_DIR + "/error-" + dfLog.format(new Date()) + ".txt";
		FileUtil.listToFile(errorList, errorFileName);
		System.out.println("createExcel() finished.");
		
	}
	

	
	////////////////////////////////////////////////////////////////
	
	
	private static List<Word> getAllWords() throws Exception {
		Set<String> addedWords = new HashSet<String>();
		List<Word> allWords = new ArrayList<Word>();
		BufferedReader input = new BufferedReader(new FileReader(INPUT_DIR+"/"+INPUT_FILE));
		String line = null;
		int index = 0;
		
		int excelIndex = 0;
		String wordStr = null;
		
		while ((line = input.readLine()) != null) {
			wordStr = line.trim();
			if(addedWords.contains(wordStr)){
				System.out.println(excelIndex+":"+wordStr);
			}
			if(!"".equals(wordStr) && !addedWords.contains(wordStr)){
				allWords.add(new Word(index,wordStr));
				addedWords.add(wordStr);
				index++;
			}
			excelIndex++;
		}
		//remove duplicates begin
		System.out.println("allWords.size():"+allWords.size());
		
		//remove duplicates end
		return allWords;
	}

	
}
