package others.e.copy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import others.e.EUtil;
import others.utils.FileUtil;
/**
 * create textOnly file by words under specified folder
 */
public class Mp3Folder2TextOnlyFile {
	private static final String ENCODING = "UTF-8";
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String rootDir = "D:/user/english/en";
		String folderName = "low";
		String textOnlyFile = rootDir+"/output/text-only-unsorted.txt";
		String mp3Dir = rootDir+"/mp3/all/"+folderName;
		String outputFile = rootDir+"/output/text-only-unsorted-"+folderName+"-"+EUtil.SDF.format(new Date())+".txt";
	
		List<String> textOnlyList = FileUtil.fileToList(textOnlyFile, ENCODING);
		HashMap<String, String> textOnlyHM = new HashMap<String, String>();
		for(String line: textOnlyList){
			if(line!=null){
				textOnlyHM.put(extractWord(line), line);
				//System.out.println(line);
			}
		}
		
		//if(true) return;
		
		List<String> mp3List = EUtil.getLocalMp3ListOrderedByLastModified(mp3Dir);
		
		List<String> sortedTextOnlyList = new ArrayList<String>();	
		for (String word: mp3List){
			//System.out.println(word);
			if(textOnlyHM.get(word)!=null){
				sortedTextOnlyList.add(textOnlyHM.get(word));
			}
		}
		FileUtil.listToFile(sortedTextOnlyList, outputFile, ENCODING);
		
	
	}

	private static String extractWord(String line){
		if(line.indexOf("  ")>=0){
			return line.substring(0, line.indexOf("  ")).trim();
		}else {
			return "";
		}
	}
	
}
