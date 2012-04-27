package others.e;

import java.io.IOException;
import java.util.Set;

import utils.FileUtil;

/**
 * Change mp3 file name to .log suffix for excel to open
 * @author r
 *
 */
public class Audio2Log {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		copyAudio2Log("D:/user/english/en/output/vcab/mp3","mp3","D:/user/english/en/output4excel/vcab/mp3");
		//copyAudio2Log("D:/user/english/en/output/iciba/us","mp3","D:/user/english/en/output4excel/iciba/us");
		//copyAudio2Log("D:/user/english/en/output/iciba/en","mp3","D:/user/english/en/output4excel/iciba/en");
		//copyAudio2Log("D:/user/english/en/output/mw/mp3","mp3","D:/user/english/en/output4excel/mw/mp3");
		//copyAudio2Log("D:/user/english/en/output/xr/mp3","mp3","D:/user/english/en/output4excel/xr/mp3");	
	}

	//incrementally copy from mp3 folder to log folder
	static void copyAudio2Log(String srcDir, String srcFormat, String destDir){
		Set<String> destAudioSet = EUtil.getLocalMp3Set(destDir);
		Set<String> srcAudioSet = EUtil.getLocalMp3Set(srcDir);
		srcAudioSet.removeAll(destAudioSet);
		int total = srcAudioSet.size();
		int count = 0;
		for(String audio: srcAudioSet){
			try {
				FileUtil.copyFile(srcDir+"/"+audio+"."+srcFormat, destDir+"/"+audio+".log");
				System.out.println((++count)+"/"+total+ ", "+ srcDir+"/"+audio+"."+srcFormat +" --> "+ destDir+"/"+audio+".log");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
