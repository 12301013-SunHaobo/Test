package others;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TestExcel {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		test0();
	}
	
	private static void test0() throws Exception {
		String tagOpen = "<i>";
		String tagClose = "</i>";
		String sampleString = "This one's easy. An <i>advert</i> is an advertisement: just shorten <i>advertisement</i>, and you get <i>advert</i>. Of course, as a verb, to <i>advert</i> to something means to refer to it.";
		File writeXcel = new File("D:/projects/java/workspace/Test/tmp/excel/newOUT.xls");
		FileOutputStream fileOut = new FileOutputStream(writeXcel);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Sheet 1");

		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell((short) 1);

		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		//HSSFRichTextString rts = convertToRTS(sampleString, tagOpen, tagClose, font);
		HSSFRichTextString rts = convertToRTS(sampleString, "advert", font);

		cell.setCellValue(rts);
		wb.write(fileOut);
		fileOut.close();

	}
	
	
	
	private static HSSFRichTextString convertToRTS(String str, String word, HSSFFont font) {
		int startIdx = str.indexOf(word);
		int endIdx = startIdx+word.length();
		HSSFRichTextString rts = new HSSFRichTextString(str);
		rts.applyFont(startIdx, endIdx, font);
		return rts;
	}
	
	/**
	 * remove the first tag, and return the index of the char after the tag 
	 */
	private static HSSFRichTextString convertToRTS(String str, String tagOpen, String tagClose, HSSFFont font) {
		List<Integer> tagOpenIdxList = new ArrayList<Integer>();
		List<Integer> tagCloseIdxList = new ArrayList<Integer>();
		
		String tmpStr = str;
		int idxOpen = tmpStr.indexOf(tagOpen);
		int idxClose = tmpStr.indexOf(tagClose);
		while((idxOpen != -1) && (idxClose != -1)) {
			tmpStr = tmpStr.replaceFirst(tagOpen, "");
			idxClose = idxClose - tagOpen.length();
			tmpStr = tmpStr.replaceFirst(tagClose, "");
			tagOpenIdxList.add(idxOpen);
			tagCloseIdxList.add(idxClose);

			idxOpen = tmpStr.indexOf(tagOpen);
			idxClose = tmpStr.indexOf(tagClose);
		}
		HSSFRichTextString rts = new HSSFRichTextString(tmpStr);
		if(tagOpenIdxList.size() != tagCloseIdxList.size()){
			System.out.println("tag counts don't match :"+tagOpen+"|"+tagClose);
			return rts; 
		}

		for(int i=0; i<tagOpenIdxList.size(); i++) {
			rts.applyFont(tagOpenIdxList.get(i), tagCloseIdxList.get(i), font);
		}
		System.out.println();
		return rts;
	}
	
	private static void original() throws Exception {
		String sampleString = "This one's easy. An <i>advert</i> is an advertisement: just shorten <i>advertisement</i>, and you get <i>advert</i>. Of course, as a verb, to <i>advert</i> to something means to refer to it.";
		File writeXcel = new File("D:/projects/java/workspace/Test/tmp/excel/newOUT.xls");
		FileOutputStream fileOut = new FileOutputStream(writeXcel);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Sheet 1");

		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell((short) 1);

		HSSFRichTextString rts = new HSSFRichTextString(sampleString);

		HSSFCellStyle style = cell.getCellStyle();
		int fontIdx = style.getFontIndex();
		HSSFFont font = wb.getFontAt((short) fontIdx);

		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		HSSFFont italicFont = wb.createFont();
		italicFont.setItalic(true);
		rts.applyFont(4, 7, italicFont);

		HSSFFont underlinedFont = wb.createFont();
		underlinedFont.setUnderline(HSSFFont.U_SINGLE);
		rts.applyFont(8, 11, underlinedFont);

		cell.setCellValue(rts);
		wb.write(fileOut);
		fileOut.close();

	}
}
