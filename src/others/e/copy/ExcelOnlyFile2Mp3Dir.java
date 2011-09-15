package others.e.copy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import others.e.EUtil;
import others.e.EUtil.Columns;
import others.e.model.Word;
import others.utils.FileUtil;

/**
 * 1. filter words with ` and save to a new textOnly file with '20110707' suffix
 * 2. copy corresponding mp3s to folder xxx-20110707
 * 
 * @author user
 *
 */
public class ExcelOnlyFile2Mp3Dir {
	private static final String ENCODING = "UTF-8";
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String rootDir = "D:/user/english/en";

		// change begin
		String textOnlyFile = rootDir+"/output4excel/merged-20110801-1.xls";
		String srcMp3DirLow =  rootDir+"/mp3/all/low";
		String newMp3DirLow = rootDir+"/mp3/all/20110801-1/low";
		
		String srcMp3DirHigh =  rootDir+"/mp3/all/high";
		String newMp3DirHigh = rootDir+"/mp3/all/20110801-1/high";
		// change end
		
		List<String> wordList = getWordList(textOnlyFile);
		Set<String> lowWordsSet = new HashSet<String>();
		//copy mp3 files
		int i=0;
		for(String word: wordList){
			String sourceFilePath = srcMp3DirLow+"/"+word+".wav";
			File sourceFile=new File(sourceFilePath);
			
			if(sourceFile.exists()){
				lowWordsSet.add(word); 
			}
			System.out.println((i++)+" "+srcMp3DirLow+"/"+word+".wav -> "+newMp3DirLow+"/"+word+".wav");
			FileUtil.copyFile(sourceFilePath, newMp3DirLow+"/"+word+".wav");
		}
		
		for(String word: wordList){
			if(!lowWordsSet.contains(word)){
				System.out.println((i++)+" "+srcMp3DirHigh+"/"+word+".wav -> "+newMp3DirHigh+"/"+word+".wav");
				FileUtil.copyFile(srcMp3DirHigh+"/"+word+".wav", newMp3DirHigh+"/"+word+".wav");
			}
		}
	}
	
	
	private static List<String> getWordList(String excelFile){
		Columns[] columns1 = new Columns[] { 
				Columns.WORD
				};

		List<Word> mainList = null; 
		try {
			mainList = EUtil.excelToList(excelFile, columns1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> wordList = new ArrayList<String>(); 
		for(Word word: mainList){
			wordList.add(word.getName());
		}
		return wordList;

	}
	
	
	private static String extractWord(String line){
		if(line.indexOf("  ")>=0){
			return line.substring(0, line.indexOf("  ")).trim();
		}else {
			return "";
		}
	}
}
