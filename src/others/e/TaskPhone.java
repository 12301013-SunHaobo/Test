package others.e;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

import others.e.model.Word;
import utils.FileUtil;
import utils.BoundedExecutor;

/**
 * Grab phones from
 * Vcab
 * MW
 * Iciba
 * and save to mp3 folders
 *
 */
public class TaskPhone {
	
	
	private static final String INPUT_FILE= "phoneList.txt"; //"GW-list-full.txt";"all-list.txt"
	private static final String INPUT_DIR = EUtil.PHONE_ROOT+"input/";
	private static final String VCAB_OUTPUT_DIR = EUtil.PHONE_ROOT+"output/vcab/";
	private static final String MP3_OUTPUT_DIR = EUtil.PHONE_ROOT+"output/iciba/";
	private static final String WAV_OUTPUT_DIR = EUtil.PHONE_ROOT+"output/mw/";
	private static final String TODAY_OUTPUT_DIR = EUtil.PHONE_ROOT+"output/today/iciba/en/";
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
		
		//updateMWPhones();
		//updateICIBAPhones();
		//createTodayPhones();
		
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
		Set<String> vcabSet = getLocalMp3Set(VCAB_OUTPUT_DIR + "/mp3/");
		Set<String> enSet = getLocalMp3Set(MP3_OUTPUT_DIR + "/en/");
		Set<String> usSet = getLocalMp3Set(MP3_OUTPUT_DIR + "/us/");
		Set<String> wavSet = getLocalMp3Set(WAV_OUTPUT_DIR + "/wav/");
		
		//create missingMp3Words list
		List<Word> missingMp3Words = new ArrayList<Word>();
		String tmpWord;
		for (int i = 0; i < allWords.size(); i++) {
			tmpWord = allWords.get(i);
			if (!vcabSet.contains(tmpWord)
					|| !enSet.contains(tmpWord)  
					|| !usSet.contains(tmpWord)
					|| !wavSet.contains(tmpWord)
					) {

				Word word = new Word(i, tmpWord);
				if (!vcabSet.contains(tmpWord)) {
					word.getVcab().setLocalHasPhone(false);
				}
				if (!enSet.contains(tmpWord)) {
					word.getIciba().setLocalEnHasMp3(false);
				}
				if (!usSet.contains(tmpWord)) {
					word.getIciba().setLocalUsHasMp3(false);
				}
				if (!wavSet.contains(tmpWord)) {
					word.getMw().setLocalHasWav(false);
				}				
				missingMp3Words.add(word);
			}
		}
		//create errorList
		List<String> errorList = Collections.synchronizedList(new ArrayList<String>());
		TPhone.init(errorList);
		
		//submit tasks for downloading mp3
		BoundedExecutor be = new BoundedExecutor(200);
		for (int i = 0; i < missingMp3Words.size(); i++) {
			TPhone tp = new TPhone(missingMp3Words.get(i));
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
		//log errors
		DateFormat dfLog = new SimpleDateFormat("yyyyMMdd-hhmmss");
		String errorFileName = LOG_DIR + "/error-" + dfLog.format(new Date()) + ".txt";
		FileUtil.listToFile(errorList, errorFileName);
		System.out.println("updateAllPhones() finished.");
		
	}
	
	
	
	
	/*
	 * 1. input list
	 * 2. missingList: missing phones in both en&&us
	 * 3. download missing phones
	 */
	private static void updateMWPhones() throws Exception{
		// [word, en-missing, us-missing]
		List<String> allWords = getAllWords();
		Set<String> wavSet = getLocalMp3Set(WAV_OUTPUT_DIR + "/");

		//create missingMp3Words list
		List<Word> missingMp3Words = new ArrayList<Word>();
		String tmpWord;
		for (int i = 0; i < allWords.size(); i++) {
			tmpWord = allWords.get(i);
			if (!wavSet.contains(tmpWord)) {
				Word word = new Word(i, tmpWord);
				word.getMw().setLocalHasWav(false);
				missingMp3Words.add(word);
			}
		}
		//create errorList
		List<String> errorList = Collections.synchronizedList(new ArrayList<String>());
		TPhone.init(errorList);
		
		//submit tasks for downloading mp3
		BoundedExecutor be = new BoundedExecutor(200);
		for (int i = 0; i < missingMp3Words.size(); i++) {
			TPhone tp = new TPhone(missingMp3Words.get(i));
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
		//log errors
		DateFormat dfLog = new SimpleDateFormat("yyyyMMdd-hhmmss");
		String errorFileName = LOG_DIR + "/error-" + dfLog.format(new Date()) + ".txt";
		FileUtil.listToFile(errorList, errorFileName);
		System.out.println("updateAllPhones() finished.");
		
	}
	
	
	
	/*
	 * 1. input list
	 * 2. missingList: missing phones in both en&&us
	 * 3. download missing phones
	 */
	private static void updateICIBAPhones() throws Exception{
		// [word, en-missing, us-missing]
		List<String> allWords = getAllWords();
		Set<String> enSet = getLocalMp3Set(MP3_OUTPUT_DIR + "/en/");
		Set<String> usSet = getLocalMp3Set(MP3_OUTPUT_DIR + "/us/");

		//create missingMp3Words list
		List<Word> missingMp3Words = new ArrayList<Word>();
		String tmpWord;
		for (int i = 0; i < allWords.size(); i++) {
			tmpWord = allWords.get(i);
			if (!enSet.contains(tmpWord) || !usSet.contains(tmpWord)) {

				Word word = new Word(i, tmpWord);
				if (!enSet.contains(tmpWord)) {
					word.getIciba().setLocalEnHasMp3(false);
				}
				if (!usSet.contains(tmpWord)) {
					word.getIciba().setLocalUsHasMp3(false);
				}
				missingMp3Words.add(word);
			}
		}
		//create errorList
		List<String> errorList = Collections.synchronizedList(new ArrayList<String>());
		TPhone.init(errorList);
		
		//submit tasks for downloading mp3
		BoundedExecutor be = new BoundedExecutor(200);
		for (int i = 0; i < missingMp3Words.size(); i++) {
			TPhone tp = new TPhone(missingMp3Words.get(i));
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
		//log errors
		DateFormat dfLog = new SimpleDateFormat("yyyyMMdd-hhmmss");
		String errorFileName = LOG_DIR + "/error-" + dfLog.format(new Date()) + ".txt";
		FileUtil.listToFile(errorList, errorFileName);
		System.out.println("updateAllPhones() finished.");
		
	}
	
	/*
	 * 1. delete all phones in folder today
	 * 2. input list "today.txt"
	 * 2. copy from iciba/en to /today
	 * 3. update modified date basing on index in input list
	 */
	private static void createTodayPhones() throws IOException{
		//delete all files in today/iciba/in folder
		File folder = new File(TODAY_OUTPUT_DIR);
		File[] files = folder.listFiles();
		for(File f: files){
			f.delete();
		}
		//input list "input/today.txt"
		List<String> words = FileUtil.fileToList(INPUT_DIR+"/"+"today.txt");
		//copy from iciba/en to /today
		String name;
		for(int i=0;i<words.size();i++){
			name = words.get(i);
			FileUtil.copyFile(MP3_OUTPUT_DIR+"/en/"+name+".mp3", TODAY_OUTPUT_DIR+"/"+name+".mp3");
		}
		//update modified date basing on index in input list
		long modifiedTime = System.currentTimeMillis();
		for(int i=0;i<words.size();i++){
			modifiedTime = modifiedTime - 1000*60;
			name = words.get(i);
			File file = new File(TODAY_OUTPUT_DIR+"/"+name+".mp3");
			if(file.exists()){
				file.setLastModified(modifiedTime);
			}
		}
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

	private static Set<String> getLocalMp3Set(String localMp3Dir){
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
