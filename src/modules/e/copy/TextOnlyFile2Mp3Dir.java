package others.e.copy;

import java.util.ArrayList;
import java.util.List;

import others.utils.FileUtil;

/**
 * 1. filter words with ` and save to a new textOnly file with '20110707' suffix
 * 2. copy corresponding mp3s to folder xxx-20110707
 * 
 * @author user
 *
 */
public class TextOnlyFile2Mp3Dir {
	private static final String ENCODING = "UTF-8";
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String rootDir = "D:/user/english/en";

		// change begin
		String textOnlyFile = rootDir+"/output/text-only-unsorted-low-20110723-0.txt";
		String mp3Dir =  rootDir+"/mp3/all/low";
		String newMp3Dir = rootDir+"/mp3/all/text-only-unsorted-low-20110723-1";
		String textOnlyDateFile = newMp3Dir+"/text-only-unsorted-low-20110723-1.txt";
		// change end
		
		List<String> wordList = new ArrayList<String>(); 
		//copy textOnlyFile
		List<String> textOnlyList = FileUtil.fileToList(textOnlyFile, ENCODING);
		
		List<String> textOnlyDateList = new ArrayList<String>();
		for(String line: textOnlyList){
			if(line!=null && !line.trim().startsWith("`")){
				textOnlyDateList.add(line);
				wordList.add(extractWord(line));
			}
		}
		FileUtil.listToFile(textOnlyDateList, textOnlyDateFile, ENCODING);
		
		//copy mp3 files
		int i=0;
		for(String word: wordList){
			
			System.out.println((i++)+" "+mp3Dir+"/"+word+".wav -> "+newMp3Dir+"/"+word+".wav");
			FileUtil.copyFile(mp3Dir+"/"+word+".wav", newMp3Dir+"/"+word+".wav");
		}
		
	}
	
	
	private static String extractWord(String line){
		if(line.indexOf("  ")>=0){
			return line.substring(0, line.indexOf("  ")).trim();
		}else {
			return "";
		}
	}
}
