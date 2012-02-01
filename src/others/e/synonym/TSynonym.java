package others.e.synonym;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import others.e.model.Vcab;
import others.e.model.Word;
import utils.WebUtil;

public class TSynonym implements Callable<String>{
	
	/** saves result word->synonyms **/
	public static ConcurrentHashMap<String, Set<String>> wordSynonymsMap = new ConcurrentHashMap<String, Set<String>>();
	
	private Word word;
	
	public TSynonym(Word word) {
		super();
		this.word = word;
	}
	
	//set static errorList
	public static void init(List<String> errorList){

	}
	
	@Override
	public String call() throws Exception {
		downloadVcabSynonyms();
		return null;
	}
	
	private void downloadVcabSynonyms() throws Exception{
		String name = this.word.getName();
		Set<String> synonyms = Vcab.getSynonyms(name);
		wordSynonymsMap.put(name, synonyms);
		
	}
	

	
	
	
	
	

	
}
