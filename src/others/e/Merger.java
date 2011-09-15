package others.e;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import others.e.EUtil.Columns;
import others.e.model.Word;

public class Merger {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		//test();
		merge();

	}

	private static void merge() throws Exception {
		String mainFile = EUtil.PHONE_ROOT+"/output4excel/merged-20110717.xls";//changed-20101120.xls";
		Columns[] columns1 = new Columns[] { 
				Columns.WORD, 
				Columns.MW_MP3, Columns.ICIBA_EN, Columns.ICIBA_US, Columns.PHONE_XR,
				Columns.CN_MEANING, 
				Columns.WWO_SENTENCES, Columns.MW_SENTENCES, Columns.XR_SENTENCES  
				};

		List<Word> mainList = EUtil.excelToList(mainFile, columns1);
		
		String extraFile = EUtil.PHONE_ROOT+"/output4excel/auto20110801-022123.xls";//words-20101003.xls";
		Columns[] columns2 = new Columns[] { 
				Columns.WORD, 
				Columns.ICIBA_EN, Columns.ICIBA_US, Columns.PHONE_XR,
				Columns.CN_MEANING, 
				Columns.WWO_SENTENCES, Columns.MW_SENTENCES, Columns.XR_SENTENCES  
				};

		List<Word> extraList = EUtil.excelToList(extraFile, columns2);
		
		
		Columns[] columnsResult = new Columns[] { 
				Columns.WORD, 
				Columns.MW_MP3, Columns.ICIBA_EN, Columns.ICIBA_US, Columns.PHONE_XR,
				Columns.CN_MEANING, 
				Columns.WWO_SENTENCES, Columns.MW_SENTENCES, Columns.XR_SENTENCES  
				};
		List<Word> listResult = mergeLists(mainList,extraList);
		
		EUtil.listToExcel(EUtil.sortBySentence(listResult), columnsResult);
		//EUtil.listToExcel(listResult, columnsResult);
		
	}
	
	/**
	 * add extraList to mainList
	 * @param mainList
	 * @param extraList
	 */
	private static List<Word> mergeLists(List<Word> mainList, List<Word> extraList){
		List<Word> notInList1 = new ArrayList<Word>();
		boolean foundInList1 = false;
		for(int i=0;i<extraList.size();i++){
			foundInList1 = false;
			Word word2 = extraList.get(i);
			for(int j=0;j<mainList.size();j++){
				Word word1 = mainList.get(j);
				if(word2.getName()!=null && !"".equals(word2.getName().trim())
					&& word2.getName().equals(word1.getName())){
					foundInList1 = true;
					
					//merge from word2 to word1
					String tmpSentence = word1.getMw().getSentences();
					if(tmpSentence==null || "".equals(tmpSentence.trim())){
						word1.getMw().setSentences(word2.getMw().getSentences());
					}
					tmpSentence = word1.getWwo().getSentences();
					if(tmpSentence==null || "".equals(tmpSentence.trim())){
						word1.getWwo().setSentences(word2.getWwo().getSentences());
					}
					tmpSentence = word1.getXr().getSentences();
					if(tmpSentence==null || "".equals(tmpSentence.trim())){
						word1.getXr().setSentences(word2.getXr().getSentences());
					}
					
				}
			}
			
			if(!foundInList1){
				notInList1.add(word2);
			}
		}
		mainList.addAll(notInList1);
		return mainList;
		
	}


	
	private static void test() throws Exception{
		String fileName1 = "C:/user/backup/rong/en/input/changed-20101120.xls";
		InputStream myxls = new FileInputStream(fileName1);
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		HSSFSheet sheet = wb.getSheetAt(0);

		HSSFRow row = sheet.getRow(2);
		HSSFCell cell = row.getCell(5);
		String cellValue;
		HSSFHyperlink hyperlink;

		if (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			// System.out.println(cell.getCellType());
			System.out.println(cell.getStringCellValue());
			hyperlink = cell.getHyperlink();
			System.out.println(hyperlink.getAddress());
			// if (hyperlink != null) {
			// System.out.println(cell.getHyperlink().getAddress());
			// }
		}

	}
}
