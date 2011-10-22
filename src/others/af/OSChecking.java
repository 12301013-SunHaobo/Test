package others.af;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import utils.BoundedExecutor;

public class OSChecking {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		StopWatch sw = new StopWatch();
		sw.start();

		long b1 = sw.getTime();

		String inputFolder="C:/user/backup/rong/af/input/af-export/";
		//List<String[]> afIsList = getAFItems(SQL_AF_IS_ITEMS,OSChecker.STATUS_IS);
		List<String[]> afIsList = getAFItemsFromCSV(inputFolder+"af-is.csv",OSChecker.STATUS_IS);
		System.out.println("afIsList.size():" + afIsList.size());

		//List<String[]> afOsList = getAFItems(SQL_AF_OS_ITEMS,OSChecker.STATUS_OS);
		List<String[]> afOsList = getAFItemsFromCSV(inputFolder+"af-os.csv",OSChecker.STATUS_OS);
		System.out.println("afOsList.size():" + afOsList.size());
		
		List<String[]> afStatusList = new ArrayList<String[]>();
		afStatusList.addAll(afIsList);
		afStatusList.addAll(afOsList);

		// testing begin
		// Temptation Cop Style Three-Piece Suit //has
		// Sweet Round Collar Long Sleeve Lace Dress Black //no itemNumber
		// afStatusList.add(new
		// String[]{"123456","Sweet Round Collar Long Sleeve Lace Dress Black",OSChecker.STATUS_IS});
		// testing end

		List<String[]> afInStockChangeList = Collections
				.synchronizedList(new LinkedList<String[]>());
		List<String[]> afOutStockChangeList = Collections
				.synchronizedList(new LinkedList<String[]>());
		List<String[]> wsNotFoundList = Collections
				.synchronizedList(new LinkedList<String[]>());

		OSChecker.init(afInStockChangeList, afOutStockChangeList,
				wsNotFoundList);

		// init ThreadPoolExecutor
		int poolSize = 40;
		int maxPoolSize = poolSize+5;
		long keepAliveTime = 3;
		final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(poolSize, maxPoolSize,
				keepAliveTime, TimeUnit.MINUTES, queue);
		BoundedExecutor be = new BoundedExecutor(tpe, maxPoolSize);

		// submit tasks
		for (int i = 0; i < afStatusList.size(); i++) { // afIsList.size()
			OSChecker dp = new OSChecker(afStatusList.get(i));
			try {
				be.submit(dp);
				System.out.println(i + ":submited:");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		tpe.shutdown();
		try {
			tpe.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// print lists
		printList(afInStockChangeList, "afInStockChangeList");
		printList(afOutStockChangeList, "afOutStockChangeList");
		printList(wsNotFoundList, "wsNotFoundList");

		// combine to one final list
		List<String[]> finalExcelList = afInStockChangeList;
		finalExcelList.add(new String[] { "", "", "", "", "", "" });
		finalExcelList.addAll(afOutStockChangeList);
		finalExcelList.add(new String[] { "", "", "", "", "", "" });
		finalExcelList.addAll(wsNotFoundList);

		toExcel(finalExcelList);

		long e1 = sw.getTime();
		System.out.println("used time:" + (e1 - b1));

	}

	private static void printList(List<String[]> list, String listName) {
		System.out.println("---- " + listName + " ----");
		for (int i = 0; i < list.size(); i++) {
			String[] row = list.get(i);
			for (int j = 0; j < row.length; j++) {
				System.out.print("[" + row[j] + "]");
			}
			System.out.println("");
		}
	}

	private static void toExcel(List<String[]> resultList) {

		HSSFWorkbook workbook = new HSSFWorkbook();

		CreationHelper createHelper = workbook.getCreationHelper();

		CellStyle hlink_style = workbook.createCellStyle();
		Font hlink_font = workbook.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);

		CellStyle cs = workbook.createCellStyle();
		cs.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cs.setAlignment(CellStyle.ALIGN_LEFT);

		Sheet sheet = workbook.createSheet("Auto");

		// header
		CellStyle hcs = workbook.createCellStyle();
		Font boldFont = workbook.createFont();
		boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		hcs.setFont(boldFont);
		String[] headerArr = { "ItemNumber", "ItemName", "EditPostUrl",
				"AsianFashionStatus", "WholesaleStatus", "WholesaleItemUrl" };
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headerArr.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headerArr[i]);
			cell.setCellStyle(hcs);
		}

		for (int i = 0; i < resultList.size(); i++) {
			String[] oneRow = resultList.get(i);
			Row row = sheet.createRow(i + 1);
			for (int j = 0; j <= 5; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(oneRow[j]);
				cell.setCellStyle(cs);
				if (j == 2 || j == 5) {
					Hyperlink link = createHelper
							.createHyperlink(Hyperlink.LINK_URL);
					link.setAddress(oneRow[j]);
					cell.setHyperlink(link);
					cell.setCellStyle(hlink_style);
				}
			}
		}
		SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd-HHmmss");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("C:/user/backup/rong/af/output/result"
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

	private static List<String[]> getAFItemsFromCSV(String filePath,String status) {
		List<String[]> resultList = new ArrayList<String[]>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(filePath));
			String str;
			String[] strArr;
			while ((str = in.readLine()) != null) {
				strArr = str.split("\\|", -1);
				strArr[2]=status;
				resultList.add(strArr);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return resultList;
		
	}

	// [postId, postTitle, IS|OS]
	private static List<String[]> getAFItems(String sql, String status) {
		List<String[]> resultList = new ArrayList<String[]>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://asianfashiondb.db.6669278.hostedresource.com:3306/asianfashiondb";
			Connection con = DriverManager.getConnection(url, "asianfashiondb",
					"Nuo9713");
			System.out.println("URL: " + url);
			System.out.println("Connection: " + con);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String postId = rs.getString("ID");
				String postTitle = rs.getString("post_title");
				String[] row = new String[3];
				row[0] = postId;
				row[1] = postTitle;
				row[2] = status;
				resultList.add(row);
				// System.out.println(id+", "+postTitle);
			}

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	private static String SQL_AF_IS_ITEMS = ""
			+ "select ID, post_title, post_type from wp_posts "
			+ "where post_type='post' and ID in ( "
			+ "select object_id from wp_term_relationships where term_taxonomy_id in ( "
			+ "   select term_taxonomy_id from wp_term_taxonomy where term_id in ( "
			+ "     select term_id from wp_terms where TRIM(name) <> 'Out of Stock Items' "
			+ "     and TRIM(name) not in ( "
			+ "     'Ties','Boots','Underwear','Shoes','Sandals', "
			+ "     'Flats','Wedges','Slippers','Brogues','Platforms', "
			+ "     'Wallets','Clutches','Luggage','Cosplay') " + "    ) "
			+ "  ) " + ") ";

	private static String SQL_AF_OS_ITEMS = ""
			+ "select ID, post_title, post_type from wp_posts "
			+ "where post_type='post' and ID in ( "
			+ "select object_id from wp_term_relationships where term_taxonomy_id in ( "
			+ "   select term_taxonomy_id from wp_term_taxonomy where term_id in ( "
			+ "     select term_id from wp_terms where TRIM(name) = 'Out of Stock Items' "
			+ "     and TRIM(name) not in ( "
			+ "     'Ties','Boots','Underwear','Shoes','Sandals', "
			+ "     'Flats','Wedges','Slippers','Brogues','Platforms', "
			+ "     'Wallets','Clutches','Luggage','Cosplay') " + "    ) "
			+ "  ) " + ") ";

}
