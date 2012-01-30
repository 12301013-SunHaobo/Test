package others.e;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import others.e.model.Iciba;
import others.e.model.Word;
import others.e.model.Xr;

public class EUtil {

	public static final String AUDIO_SUFFIX = ".log";
	
	public static final String PHONE_ROOT = "D:/user/english/en/";

	private static final String MP3_OUTPUT_DIR = PHONE_ROOT+"output4excel";

	public static Set<String> vcabSet = getLocalMp3Set(MP3_OUTPUT_DIR + "/vcab/mp3/");
	public static Set<String> mwSet = getLocalMp3Set(MP3_OUTPUT_DIR + "/mw/mp3/");
	public static Set<String> enSet = getLocalMp3Set(MP3_OUTPUT_DIR + "/iciba/en/");
	public static Set<String> usSet = getLocalMp3Set(MP3_OUTPUT_DIR + "/iciba/us/");
	public static Set<String> xrSet = getLocalMp3Set(MP3_OUTPUT_DIR + "/xr/mp3/");

	
	public enum Columns {
		WORD, VCAB_MP3, MW_MP3, ICIBA_EN, ICIBA_US, PHONE_XR, CN_MEANING, MW_SENTENCES, WWO_SENTENCES, XR_SENTENCES, EMPTY
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	static String OUTPUT_DIR = "D:/user/english/en/output4excel/";
	public static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd-HHmmss");

	/**
	 * Load excel to list
	 * @param fileName
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public static List<Word> excelToList(String fileName, Columns[] columns)
			throws Exception {
		List<Word> list = new ArrayList<Word>();

		InputStream myxls = new FileInputStream(fileName);
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		HSSFSheet sheet = wb.getSheetAt(0);

		HSSFRow row;
		HSSFCell cell;
		String cellValue;
		HSSFHyperlink hyperlink;

		int lastRowNum = sheet.getLastRowNum();
		for (int i = 0; i <= lastRowNum; i++) {
			System.out.println("i:" + i);
			row = sheet.getRow(i);
			if(row == null) {
				continue;
			}
				
			Word word = new Word();
			for (int j = 0; j < columns.length; j++) {
				cell = row.getCell(j);
				if (cell != null
						&& cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					cellValue = cell.getStringCellValue();

					switch (columns[j]) {
					case WORD:
						word.setName(cellValue);
						break;
					case VCAB_MP3:
						hyperlink = cell.getHyperlink();
						if (hyperlink == null) {
							word.getVcab().setLocalHasPhone(false);
						}
						break;
					case ICIBA_EN:
						hyperlink = cell.getHyperlink();
						if (hyperlink == null) {
							word.getIciba().setLocalEnHasMp3(false);
						}
						break;
					case ICIBA_US:
						hyperlink = cell.getHyperlink();
						if (hyperlink == null) {
							word.getIciba().setLocalUsHasMp3(false);
						}
						break;
					case PHONE_XR:
						hyperlink = cell.getHyperlink();
						if (hyperlink == null) {
							word.getXr().setLocalXrHasMp3(false);
						}
						break;
					case CN_MEANING:
						word.getIciba().setMeaning(cellValue);
						break;
					case MW_SENTENCES:
						word.getMw().setSentences(cellValue);
						break;
					case WWO_SENTENCES:
						word.getWwo().setSentences(cellValue);
						break;
					case XR_SENTENCES:
						word.getXr().setSentences(cellValue);
						break;
					case EMPTY:
						;//do nothing
						break;
					default:
						;//do nothing
						break;
					}
				}
			}
			list.add(word);

		}

		return list;
	}

	/**
	 * Word list to Excel
	 * 
	 * @param strArr
	 */
	public static void listToExcel(List<Word> strArr, Columns[] columns) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		// for hyperlink
		CreationHelper createHelper = workbook.getCreationHelper();
		CellStyle hlink_style = workbook.createCellStyle();
		Font hlink_font = workbook.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);
		hlink_style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		hlink_style.setAlignment(CellStyle.ALIGN_LEFT);

		CellStyle cs = workbook.createCellStyle();
		cs.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cs.setAlignment(CellStyle.ALIGN_LEFT);

		Sheet sheet = workbook.createSheet("Auto");
		int wordCount = 0;
		for (int i = 0; i < strArr.size(); i++) {
			Word word = strArr.get(i);

			// ignore NOT FOUND words in excel
			if (Iciba.NOT_FOUND.equals(word.getIciba().getMeaning())) {
				continue;
			}

			Row row = sheet.createRow(wordCount);

			// create cells of the row
			for (int j = 0; j < columns.length; j++) {

				switch (columns[j]) {
				case WORD:
					// word name
					Cell nameCell = row.createCell(j);
					nameCell.setCellValue(word.getName());
					nameCell.setCellStyle(cs);
					break;

				case VCAB_MP3:
					// vcab mp3
					Cell vcabMp3Cell = row.createCell(j);
					vcabMp3Cell.setCellValue("[V]");
					//if (word.getIciba().isLocalEnHasMp3()) {
					if(mwSet.contains(word.getName())){
						Hyperlink mwLink = createHelper
								.createHyperlink(Hyperlink.LINK_URL);
						mwLink.setAddress("./vcab/mp3/" + word.getName()
								+ AUDIO_SUFFIX);
						vcabMp3Cell.setHyperlink(mwLink);
						vcabMp3Cell.setCellStyle(hlink_style);
					} else {
						vcabMp3Cell.setCellStyle(cs);
					}
					break;
					
				case MW_MP3:
					// mw mp3
					Cell mwMp3Cell = row.createCell(j);
					mwMp3Cell.setCellValue("[M]");
					//if (word.getIciba().isLocalEnHasMp3()) {
					if(mwSet.contains(word.getName())){
						Hyperlink mwLink = createHelper
								.createHyperlink(Hyperlink.LINK_URL);
						mwLink.setAddress("./mw/mp3/" + word.getName()
								+ AUDIO_SUFFIX);
						mwMp3Cell.setHyperlink(mwLink);
						mwMp3Cell.setCellStyle(hlink_style);
					} else {
						mwMp3Cell.setCellStyle(cs);
					}
					break;
				case ICIBA_EN:
					// en phone
					Cell enCell = row.createCell(j);
					enCell.setCellValue("[" + Iciba.ICIBA_EN + "]");
					//if (word.getIciba().isLocalEnHasMp3()) {
					if(enSet.contains(word.getName())){
						Hyperlink enLink = createHelper
								.createHyperlink(Hyperlink.LINK_URL);
						enLink.setAddress("./iciba/en/" + word.getName()
								+ AUDIO_SUFFIX);
						enCell.setHyperlink(enLink);
						enCell.setCellStyle(hlink_style);
					} else {
						enCell.setCellStyle(cs);
					}
					break;
				case ICIBA_US:
					// us phone
					Cell usCell = row.createCell(j);
					usCell.setCellValue("[" + Iciba.ICIBA_US + "]");
					//if (word.getIciba().isLocalUsHasMp3()) {
					if(usSet.contains(word.getName())){
						Hyperlink usLink = createHelper
								.createHyperlink(Hyperlink.LINK_URL);
						usLink.setAddress("./iciba/us/" + word.getName()
								+ AUDIO_SUFFIX);
						usCell.setHyperlink(usLink);
						usCell.setCellStyle(hlink_style);
					} else {
						usCell.setCellStyle(cs);
					}
					break;
				case PHONE_XR:
					// xr phone
					Cell xrCell = row.createCell(j);
					xrCell.setCellValue("[" + Xr.PHONE_XR + "]");
					//if (word.getXr().isLocalXrHasMp3()) {
					if(xrSet.contains(word.getName())){
						Hyperlink xrLink = createHelper
								.createHyperlink(Hyperlink.LINK_URL);
						xrLink.setAddress("./xr/mp3/" + word.getName()
								+ AUDIO_SUFFIX);
						xrCell.setHyperlink(xrLink);
						xrCell.setCellStyle(hlink_style);
					} else {
						xrCell.setCellStyle(cs);
					}
					break;
				case CN_MEANING:
					// iciba meaning
					Cell meaningCell = row.createCell(j);
					meaningCell.setCellValue(word.getIciba().getMeaning());
					meaningCell.setCellStyle(cs);
					break;
				case MW_SENTENCES:
					// mw sentence
					Cell mwCell = row.createCell(j);
					mwCell.setCellValue(word.getMw().getSentences());
					mwCell.setCellStyle(cs);
					break;
				case WWO_SENTENCES:
					// wwo sentence
					Cell wwoCell = row.createCell(j);
					wwoCell.setCellValue(word.getWwo().getSentences());
					wwoCell.setCellStyle(cs);
					break;
				case XR_SENTENCES:
					// wwo sentence
					Cell xrMeaningCell = row.createCell(j);
					xrMeaningCell.setCellValue(word.getXr().getSentences());
					xrMeaningCell.setCellStyle(cs);
					break;
				case EMPTY:
					// empty column
					Cell emptyCell = row.createCell(j);
					emptyCell.setCellValue("");
					emptyCell.setCellStyle(cs);
					break;
				default:
					;// do nothing
					break;
				}

			}
			wordCount++;

		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(OUTPUT_DIR + "auto"
					+ SDF.format(new Date()) + ".xls");
			workbook.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Word list to Excel
	 * 
	 * @param strArr
	 */
	public static void toExcel(List<Word> strArr) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		// for hyperlink
		CreationHelper createHelper = workbook.getCreationHelper();
		CellStyle hlink_style = workbook.createCellStyle();
		Font hlink_font = workbook.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);
		hlink_style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		hlink_style.setAlignment(CellStyle.ALIGN_LEFT);

		CellStyle cs = workbook.createCellStyle();
		cs.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cs.setAlignment(CellStyle.ALIGN_LEFT);

		Sheet sheet = workbook.createSheet("Auto");
		int wordCount = 0;
		for (int i = 0; i < strArr.size(); i++) {
			Word word = strArr.get(i);

			// ignore NOT FOUND words in excel
			if (Iciba.NOT_FOUND.equals(word.getIciba().getMeaning())) {
				continue;
			}

			Row row = sheet.createRow(wordCount);
			wordCount++;

			// word name
			Cell nameCell = row.createCell(0);
			nameCell.setCellValue(word.getName());
			nameCell.setCellStyle(cs);

			// en phone
			Cell enCell = row.createCell(1);
			enCell.setCellValue("[" + Iciba.ICIBA_EN + "]");
			if (word.getIciba().isLocalEnHasMp3()) {
				Hyperlink enLink = createHelper
						.createHyperlink(Hyperlink.LINK_URL);
				enLink.setAddress("./iciba/en/" + word.getName() + AUDIO_SUFFIX);
				enCell.setHyperlink(enLink);
				enCell.setCellStyle(hlink_style);
			} else {
				enCell.setCellStyle(cs);
			}

			// us phone
			Cell usCell = row.createCell(2);
			usCell.setCellValue("[" + Iciba.ICIBA_US + "]");
			if (word.getIciba().isLocalUsHasMp3()) {
				Hyperlink usLink = createHelper
						.createHyperlink(Hyperlink.LINK_URL);
				usLink.setAddress("./iciba/us/" + word.getName() + AUDIO_SUFFIX);
				usCell.setHyperlink(usLink);
				usCell.setCellStyle(hlink_style);
			} else {
				usCell.setCellStyle(cs);
			}

			// xr phone
			Cell xrCell = row.createCell(3);
			xrCell.setCellValue("[" + Xr.PHONE_XR + "]");
			if (word.getXr().isLocalXrHasMp3()) {
				Hyperlink xrLink = createHelper
						.createHyperlink(Hyperlink.LINK_URL);
				xrLink.setAddress("./xr/mp3/" + word.getName() + AUDIO_SUFFIX);
				xrCell.setHyperlink(xrLink);
				xrCell.setCellStyle(hlink_style);
			} else {
				xrCell.setCellStyle(cs);
			}

			// iciba meaning
			Cell meaningCell = row.createCell(4);
			meaningCell.setCellValue(word.getIciba().getMeaning());
			meaningCell.setCellStyle(cs);

//			// wordreference meaning
//			Cell meaningCell = row.createCell(4);
//			meaningCell.setCellValue(word.getWr().getMeaning());
//			meaningCell.setCellStyle(cs);

			// wwo sentence
			Cell wwoCell = row.createCell(5);
			wwoCell.setCellValue(word.getWwo().getSentences());
			wwoCell.setCellStyle(cs);

			// mw sentence
			Cell mwCell = row.createCell(6);
			mwCell.setCellValue(word.getMw().getSentences());
			mwCell.setCellStyle(cs);

		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(OUTPUT_DIR + "auto"
					+ SDF.format(new Date()) + ".xls");
			workbook.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Put word without sentences at the bottom
	 * 
	 * @param list
	 * @return
	 */
	public static List<Word> sortBySentence(List<Word> list) {
		List<Word> newListWithS = new ArrayList<Word>();
		List<Word> newListNoS = new ArrayList<Word>();
		for (Word w : list) {
			if ((w.getWwo().getSentences() != null && !"".equals(w.getWwo()
					.getSentences()))
					|| (w.getMw().getSentences() != null && !"".equals(w
							.getMw().getSentences()))
					|| (w.getXr().getSentences() != null && !"".equals(w
							.getXr().getSentences()))		
			) {
				newListWithS.add(w);
			} else {
				newListNoS.add(w);
			}
		}
		newListWithS.addAll(newListNoS);
		return newListWithS;

	}
	
	public static Set<String> getLocalMp3Set(String localMp3Dir){
		Set<String> localMp3Set = new HashSet<String>();
		File folder = new File(localMp3Dir);
		File[] files = folder.listFiles();
		String tmpFileName;
		for(File f:files){
			tmpFileName = f.getName();
			localMp3Set.add(tmpFileName.substring(0, tmpFileName.lastIndexOf(".")));
		}
		return localMp3Set;
	}
	
	/**
	 * Get mp3 name list, ordered by last modified date
	 * @param localMp3Dir
	 * @return
	 */
	public static List<String> getLocalMp3ListOrderedByLastModified(String localMp3Dir){
		List<String> localMp3List = new ArrayList<String>();
		File folder = new File(localMp3Dir); 
		File[] files = folder.listFiles();

		Arrays.sort(files, 
				new Comparator<File>() {

					@Override
					public int compare(File o1, File o2) {
						File f1 = (File)o1;
						File f2 = (File)o2;
						
						long t1 = f1.lastModified();
						long t2 = f2.lastModified();
						
						return t1<t2? -1:t1==t2?0:1;
					}

				}
				);
		String tmpFileName;
		for(File f:files){
			tmpFileName = f.getName();
			localMp3List.add(tmpFileName.substring(0, tmpFileName.lastIndexOf(".")));
		}
		return localMp3List;
	}

}
