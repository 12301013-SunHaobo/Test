package others.e;

import java.util.List;
import java.util.concurrent.Callable;

import others.e.model.Dictcn;
import others.e.model.Iciba;
import others.e.model.Mw;
import others.e.model.Vcab;
import others.e.model.Word;
import others.e.model.Wwo;
import utils.WebUtil;

public class TExcel implements Callable<String>{
	
	private static List<String> errorList;
	
	private Word word;
	
	public TExcel(Word word) {
		super();
		this.word = word;
	}
	
	//set static errorList
	public static void init(List<String> errorList){
		TExcel.errorList = errorList;
	}
	
	@Override
	public String call() throws Exception {
		
		try {
			createExcelRow();
			//add NOT FOUND words in errorList
			if(Iciba.NOT_FOUND.equals(this.word.getIciba().getMeaning())){
				errorList.add(this.word.getName()+":"+Iciba.NOT_FOUND);
			}
		} catch (Throwable e) {
			errorList.add(this.word.getName()+":"+e.getMessage());
		}
		return null;
	}

	private void createExcelRow() throws Exception{
		String name = this.word.getName();

		String dictcnContent = WebUtil.getPageSource(Dictcn.URLhttp + name, Dictcn.ENCODINGhttp);
		Dictcn dictcn = this.word.getDictcn();
		dictcn.setMeaning(Dictcn.extractMeaningHttp(dictcnContent));

		//get iciba meanings
//		String icibaContent = WebUtil.getPageSource(Iciba.ICIBA_URL + name, "utf-8");
//		Iciba iciba = this.word.getIciba();
//		iciba.setMeaning(Iciba.extractICIBAMeaning(icibaContent));
		
		//get vcab info
		String vcabContent =  WebUtil.getPageSource(Vcab.URL + name, "utf-8");
		Vcab vcab = this.word.getVcab();
		vcab.setSynonyms(Vcab.getSynonyms(vcabContent));
		vcab.setBlurbShort(Vcab.getBlurbShort(vcabContent));
		vcab.setBlurbLong(Vcab.getBlurbLong(vcabContent));
		vcab.setSentences(vcab.extractSentences(this.word.getName()));
		vcab.setMeaning(Vcab.getFullDefinitions(vcabContent));

/*		working, but I'm not using below information
		//get WWO sentences
		String wwoContent = WebUtil.getPageSource(Wwo.WWO_URL + name.toUpperCase(), "utf-8");
		Wwo wwo = this.word.getWwo();
		wwo.setSentences(Wwo.getWWOS(wwoContent));
		
		//get MW sentences
		String mwContent = WebUtil.getPageSource(Mw.MW_URL +name, "utf-8");
		Mw mw = this.word.getMw();
		mw.setSentences(Mw.extractMwS(mwContent));
*/		
	}
	
	
	
	//////////////////////////////////////////////////////////////////
	// 
	
	
	
	
	
	
	
	

	
}
