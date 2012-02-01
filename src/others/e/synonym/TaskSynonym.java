package others.e.synonym;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;

import others.e.EUtil;
import others.e.model.Vcab;
import others.e.model.Word;
import utils.BoundedExecutor;
import utils.FileUtil;

/**
 * Grab phones from
 * Vcab
 * MW
 * Iciba
 * and save to mp3 folders
 *
 */
public class TaskSynonym {
	
	
	private static final String INPUT_FILE= "all-list.txt"; //"GW-list-full.txt"; "test-synonyms-list.txt"
	private static final String INPUT_DIR = EUtil.PHONE_ROOT+"input/";
	private static final String VCAB_SYNONYM_OUTPUT_DIR = EUtil.PHONE_ROOT+"output/vcab/synonym";
	private static final String LOG_DIR = EUtil.PHONE_ROOT+"output/log/";

	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		StopWatch sw = new StopWatch();
		sw.start();
		long b0 = sw.getTime();
		
		updatePhones();
		
		long e0 = sw.getTime();
		System.out.println("total used time:" + (e0-b0));
	}
	
	
	
	/*
	 * 1. input list
	 * 2. missingList: missing phones in both en&&us
	 * 3. download missing phones
	 */
	private static void updatePhones() throws Exception{
		// [word, en-missing, us-missing]
		List<String> allWords = getAllWords();
		
		//create missingMp3Words list
		List<Word> missingMp3Words = new ArrayList<Word>();
		String tmpWord;
		for (int i = 0; i < allWords.size(); i++) {
			tmpWord = allWords.get(i);
			Word word = new Word(i, tmpWord);
			missingMp3Words.add(word);
		}
		//create errorList
		List<String> errorList = Collections.synchronizedList(new ArrayList<String>());
		
		//submit tasks for downloading mp3
		BoundedExecutor be = new BoundedExecutor(200);
		for (int i = 0; i < missingMp3Words.size(); i++) {
			TSynonym tp = new TSynonym(missingMp3Words.get(i));
			try {
				be.submit(tp);
				System.out.println(i + ":submited:");
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
		
		
		List<String> synonymLines = new ArrayList<String>();
		for(Entry<String, Set<String>> entry : TSynonym.wordSynonymsMap.entrySet()) {
			String line = Vcab.getSynonymLine(entry.getKey(), entry.getValue());
			synonymLines.add(line);
		}
		FileUtil.listToFile(synonymLines, FileUtil.createFileNameWithTimestamp(VCAB_SYNONYM_OUTPUT_DIR+"/synonym_%s.txt"));
		
		//log errors
		DateFormat dfLog = new SimpleDateFormat("yyyyMMdd-hhmmss");
		String errorFileName = LOG_DIR + "/error-" + dfLog.format(new Date()) + ".txt";
		FileUtil.listToFile(errorList, errorFileName);
		System.out.println("updateAllPhones() finished.");
		
	}
	
	

	////////////////////////////////////////////////////////////////
	
	private static List<String> getAllWords() throws Exception {
		List<String> allWords = new ArrayList<String>();
		BufferedReader input = new BufferedReader(new FileReader(INPUT_DIR+"/"+INPUT_FILE));
		String line = null;
		while ((line = input.readLine()) != null) {
			if(!"".equals(line.trim())){
				allWords.add(line.trim().toLowerCase());
			}
		}
		return allWords;
	}

	private static Set<String> getLocalSynonymsSet(String localMp3Dir){
		Set<String> localMp3Set = new HashSet<String>();
		File folder = new File(localMp3Dir);
		File[] files = folder.listFiles();
		String tmpFileName;
		for(File f:files){
			tmpFileName = f.getName().toLowerCase();
			localMp3Set.add(tmpFileName.substring(0, tmpFileName.lastIndexOf(".")));
		}
		return localMp3Set;
	}
}
