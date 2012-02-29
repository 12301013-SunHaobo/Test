package others.af;
 
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.WebUtil;

public class OSChecker implements Callable<String>{

	public static String STATUS_IS = "InStock";
	public static String STATUS_OS = "OutStock";
	public static String STATUS_NOTFOUND = "NotFound";
	
	private static String ENCODING = "UTF-8";

	private static List<String[]> afInStockChangeList; 
	private static List<String[]> afOutStockChangeList;
	private static List<String[]> wsNotFoundList;

	private String[] afRow;
	
	
	public static void init(List<String[]> afInStockChangeList, List<String[]> afOutStockChangeList, List<String[]> wsNotFoundList){
		OSChecker.afInStockChangeList = afInStockChangeList;
		OSChecker.afOutStockChangeList = afOutStockChangeList;
		OSChecker.wsNotFoundList = wsNotFoundList;
		
	}
	
	
	
	//[postId, postTitle, IS|OS]
	public OSChecker(String[] afRow) {
		super();
		this.afRow = afRow;
	}




	@Override
	public String call() throws Exception {
		String postId = this.afRow[0];
		String itemName = this.afRow[1];
		String afStatus = this.afRow[2];
		String[] wholesaleResult = checkWholesale(itemName);

		String[] excelRow = new String[6];
		excelRow[0]=wholesaleResult[2];//itemNumber from wholesale
		excelRow[1]=itemName;
		excelRow[2]="http://asianfashion.us/wp-admin/post.php?post="+postId+"&action=edit";
		excelRow[3]=afStatus;
		String wsStatus = wholesaleResult[1];
		excelRow[4]= wsStatus;
		excelRow[5]= wholesaleResult[0];
		

		if(wsStatus.equals(OSChecker.STATUS_IS)&&afStatus.equals(OSChecker.STATUS_OS)){
			OSChecker.afOutStockChangeList.add(excelRow);
		} else if(wsStatus.equals(OSChecker.STATUS_OS)&&afStatus.equals(OSChecker.STATUS_IS)){
			OSChecker.afInStockChangeList.add(excelRow);
		}else if(wsStatus.equals("Not found in wholesale")){
			OSChecker.wsNotFoundList.add(excelRow);
		}
		
		
		return null;
	}

	/**
	 * 
	 * @param itemName
	 * @return [itemUrlInWholesale, IS|OS|NotFound, itemNumber]
	 * @throws UnsupportedEncodingException
	 */
	private String[] checkWholesale(String itemName) throws UnsupportedEncodingException { 
		String trimedItemName = itemName.trim();
		String[] result = new String[3]; //[itemUrlInWholesale, IS|OS|NotFound]
		
		String baseUrl = "http://www.wholesale-dress.net";
		String itemSearchResultUrl = baseUrl+"/search.php?category=0&keywords="+URLEncoder.encode(trimedItemName, ENCODING);
		String itemSearchResultPage = WebUtil.getPageSource(itemSearchResultUrl, ENCODING);
		String itemHtmlBlockPattern = "(<a href=\").*?(\">"+trimedItemName+"</a>)";
		Pattern p = Pattern.compile(itemHtmlBlockPattern);
		Matcher m = p.matcher(itemSearchResultPage);
		if(m.find()){
			String itemUrl = m.group().replaceAll("<a href=\"", "").replaceAll("\">"+trimedItemName+"</a>", "");
			//search add_cart.gif
			result[0] = baseUrl+"/"+itemUrl;
			String itemPage = WebUtil.getPageSource(baseUrl+"/"+itemUrl, ENCODING);
			if(itemPage!=null && itemPage.indexOf("add_cart.gif")!=-1){
				result[1] = OSChecker.STATUS_IS;
			} else {
				result[1] = OSChecker.STATUS_OS;
			}
			//find itemNumber
			String itemNumberBlockPattern = "(<td class=\"goods-left\">Item number:</td>).*?(\r\n)*.*?(<td class=\"goods-right\">).*?(</td>)";
			Pattern p1 = Pattern.compile(itemNumberBlockPattern,Pattern.DOTALL);
			Matcher m1 = p1.matcher(itemPage);
			if(m1.find()){
				String s = m1.group();
				String itemNumber = s.substring(s.indexOf("\"goods-right\">")).replaceAll("\"goods-right\">", "").replaceAll("</td>", "");
				result[2] = itemNumber;
			}
		}else {
			result[0] = "";
			result[1] = "Not found in wholesale";
		}
		return result;
	}
	
}
