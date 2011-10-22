package others.pic;

import java.util.List;

import utils.RegUtil;
import utils.WebUtil;

//http://litejav.com/label/巨乳/page/2/
//http://litejav.com/box/uncensored/page/100/

public class GetPicNTitles {

	private static String encoding = "UTF-8";
	
	private static int censoredTotalPgNo = 11036;
	private static int uncensoredTotalPgNo = 718;
	
	private static String ROOT_PIC_DIR="D:/downloads/pics";
	
	public static void main(String[] args) throws Exception {
		long b0 = System.currentTimeMillis();
		
		String category = "uncensored";
		int pageNo = 0;
		
		int rtnCode = 0;
		while(rtnCode==0){
			pageNo++;
			String url = getPageUrl(category, pageNo);
			rtnCode = getOnePage(url);
			System.out.println("Page:"+pageNo);
			
			//remaining time
			long remainTime = 0;
			if("uncensored".equals(category)) {
				remainTime = (uncensoredTotalPgNo-pageNo)*(System.currentTimeMillis()-b0)/pageNo;
			}else if("censored".equals(category)) {
				remainTime = (censoredTotalPgNo-pageNo)*(System.currentTimeMillis()-b0)/pageNo;
			}
			if(remainTime!=0){
				System.out.println("    remaining time:"+remainTime/1000+" seconds.");
			}
		}
		long e0 = System.currentTimeMillis();
		System.out.println("Total time:"+ (e0-b0)/(1000*60)+" minutes.");
	}


	
	private static int getOnePage(String url){
		String page = WebUtil.getPageSource(url, encoding);

		if("".equals(page)){
			return 404;
		}
		
		String dataTablePattern = "http://litejav.com/wp-content/uploads.*?/>";
        List<String> dataTableList = RegUtil.getMatchedStrings(page, dataTablePattern);
        for(String str : dataTableList){
        	//<img onload="NcodeImageResizer.createOn(this);" src="http://litejav.com/wp-content/uploads/2011/09/DVD1CWPBD-50.jpg" alt="DVD1CWPBD 50 [CWPBD 50] キャットウォーク ポイズン 50 : AIKA (ブルーレイ版)"  title="[CWPBD 50] キャットウォーク ポイズン 50 : AIKA (ブルーレイ版)" /><br />
        	String imgUrl = str.substring(0, str.indexOf("\""));
        	String title = str.substring(str.indexOf("[")+1, str.indexOf("]")).replace(" ", "-");
        	//System.out.println("-----------");
        	//System.out.println(str);
        	//System.out.println(url+","+title);
        	try {
				WebUtil.download(imgUrl, ROOT_PIC_DIR+"/"+title+".jpg");
				//System.out.println(title+" downloaded.");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return 0;
		
	}

	//pageNo start from 1
	private static String getPageUrl(String category, int pageNo){
		if("uncensored".equals(category) || "censored".equals(category)){
			return "http://litejav.com/box/"+category+"/page/"+pageNo+"/";
		}
		return "http://litejav.com/label/"+category+"/page/"+pageNo+"/";
		
	}
}
