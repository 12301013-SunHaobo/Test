package others.e.synonym;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import others.e.model.Iciba;
import others.e.model.Mw;
import others.e.model.Vcab;
import others.e.model.Word;
import utils.WebUtil;

public class TSynonym implements Callable<String>{
	
	private static List<String> errorList;
	
	private Word word;
	
	public TSynonym(Word word) {
		super();
		this.word = word;
	}
	
	//set static errorList
	public static void init(List<String> errorList){
		TSynonym.errorList = errorList;
	}
	
	@Override
	public String call() throws Exception {
		downloadVcabMp3();
		//downloadMwWav();
		//downloadICIBAMp3();
		return null;
	}
	
	private void downloadVcabMp3() throws Exception{
		String name = this.word.getName();
		Map<String, String> phones = Vcab.getPhones(name);
		
		for(String word: phones.keySet()){
			String phoneUrl = phones.get(word);
			if(!"".equals(phoneUrl)){
				if(!this.word.getVcab().isLocalHasPhone()){
					WebUtil.download(phoneUrl, Vcab.OUTPUT_DIR+"/"+word+".mp3");
					System.out.println(this.word.getInputIndex()+":"+word+".mp3: downloaded.");
				}
			}else {
				//System.out.println(this.word.getInputIndex()+":"+word+":en: not on site.");
				errorList.add(this.word.getInputIndex()+":"+word+":mw: not on site.");
			}
		}
		
	}
	
	private void downloadMwWav() throws Exception{
		String name = this.word.getName();
		Map<String, String> wavs = Mw.getPhones(name);
		
		for(String word: wavs.keySet()){
			String wavUrl = wavs.get(word);
			if(!"".equals(wavUrl)){
				if(!this.word.getMw().isLocalHasWav()){
					WebUtil.download(wavUrl, Mw.WAV_OUTPUT_DIR+"/"+word+".wav");
					System.out.println(this.word.getInputIndex()+":"+word+":mw: downloaded.");
				}
			}else {
				//System.out.println(this.word.getInputIndex()+":"+word+":en: not on site.");
				errorList.add(this.word.getInputIndex()+":"+word+":mw: not on site.");
			}
		}
		
	}
	

	private void downloadICIBAMp3() throws Exception{
		String name = this.word.getName();
		String icibaContent = WebUtil.getPageSource(Iciba.ICIBA_URL + name, "utf-8");
		String [] icibaPhoneArr = Iciba.getICIBAPhoneItem(icibaContent);
		String enMp3Url = icibaPhoneArr[1];
		String usMp3Url = icibaPhoneArr[3];
		if(!"".equals(enMp3Url)){
			if(!this.word.getIciba().isLocalEnHasMp3()){
				WebUtil.download(enMp3Url, Iciba.MP3_OUTPUT_DIR+"/en/"+name+".mp3");
				System.out.println(this.word.getInputIndex()+":"+name+":en: downloaded.");
			}
		}else {
			//System.out.println(this.word.getInputIndex()+":"+name+":en: not on site.");
			errorList.add(this.word.getInputIndex()+":"+name+":en: not on site.");
		}
		
		if(!"".equals(usMp3Url)){
			if(!this.word.getIciba().isLocalUsHasMp3()){
				WebUtil.download(usMp3Url, Iciba.MP3_OUTPUT_DIR+"/us/"+name+".mp3");
				System.out.println(this.word.getInputIndex()+":"+name+":us: downloaded.");
			}
		}else {
			//System.out.println(this.word.getInputIndex()+":"+name+":us: not on site.");
			errorList.add(this.word.getInputIndex()+":"+name+":us: not on site.");
		}
	}
	
	
	
	//////////////////////////////////////////////////////////////////
	// 
	
	
	
	
	
	
	
	

	
}
