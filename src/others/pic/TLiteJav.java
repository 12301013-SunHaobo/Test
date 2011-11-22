package others.pic;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import utils.RegUtil;
import utils.WebUtil;

public class TLiteJav implements Callable<String>{
	
	private static String encoding = "UTF-8";
	private static String ROOT_PIC_DIR="D:/downloads/liteJav/pics";
	
	private String category;
	private int pageNo;
	
	public TLiteJav(String category, int pageNo) {
		super();
		this.category = category;
		this.pageNo = pageNo;
	}


	@Override
	public String call() throws Exception {
		String pageUrl = getPageUrl(this.category, this.pageNo);
		String page = WebUtil.getPageSource(pageUrl, encoding);

		String dataTablePattern1 = "<div class=\"entry\">.*?<br />";
		
        List<String> dataTableList = RegUtil.getMatchedStrings(page, dataTablePattern1);
        System.out.println(category+","+pageNo+", has "+dataTableList.size()+" pics.");
        for(String str : dataTableList){
        	//<img onload="NcodeImageResizer.createOn(this);" src="http://litejav.com/wp-content/uploads/2011/09/DVD1CWPBD-50.jpg" alt="DVD1CWPBD 50 [CWPBD 50] キャットウォーク ポイズン 50 : AIKA (ブルーレイ版)"  title="[CWPBD 50] キャットウォーク ポイズン 50 : AIKA (ブルーレイ版)" /><br />
        	List<String> imgUrlList = RegUtil.getMatchedStrings(str, "src=.*?jpg");
        	String tmpImgUrl = imgUrlList.get(0);
        	String imgUrl =tmpImgUrl.replaceAll("src|=|\"", "");
        	//System.out.println(str);
        	String title = str.substring(str.indexOf("[")+1, str.indexOf("]")).replace(" ", "_");
        	//System.out.println("-----------");
        	//System.out.println(str);
        	//System.out.println(url+","+title);
        	String fileName = ROOT_PIC_DIR+"/"+this.category+"/"+title+".jpg";
        	if(!(new File(fileName)).exists()){
	        	try {
					WebUtil.download(imgUrl, fileName);
					System.out.println(this.category+","+pageNo+","+title+" downloaded.");
				} catch (Exception e) {
					System.out.print("imgUrl:"+imgUrl);
					System.out.print("fileName:"+fileName);
					e.printStackTrace();
				}
        	}else{
        		System.out.println(this.category+","+pageNo+","+title+" already exists.");
        	}
        }
        return "";
	}
	

	//pageNo start from 1
	private String getPageUrl(String category, int pageNo){
		if("uncensored".equals(category) || "censored".equals(category)){
			return "http://litejav.com/box/"+category+"/page/"+pageNo+"/";
		}
		return "http://litejav.com/label/"+category+"/page/"+pageNo+"/";
		
	}	
}