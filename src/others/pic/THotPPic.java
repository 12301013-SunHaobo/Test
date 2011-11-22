package others.pic;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import utils.RegUtil;
import utils.WebUtil;

public class THotPPic {
	
	private static String encoding = "UTF-8";
	private static String ROOT_PIC_DIR="D:/downloads/pics/hotp";
	
	private String category;
	private int pageNo;
	
	public THotPPic(String category, int pageNo) {
		super();
		this.category = category;
		this.pageNo = pageNo;
	}


	public static void main(String[] args) throws Exception{
		THotPPic t = new THotPPic("uncensored", 3);
		t.call();
	}
	
	public String call() throws Exception {
		//String pageUrl = getPageUrl(this.category, this.pageNo);
		String pageUrl = "http://jav.hotporndl.com/category/"+this.category+"/page/"+this.pageNo;
		String page = WebUtil.getPageSource(pageUrl, encoding);
		System.out.println(page);
		
		//String dataTablePattern1 = "<p><a href=\"http://jav.hotporndl.com/wp-content/uploads.*?<p align";
		String dataTablePattern1 = "src=\"http:.*?<p align";
		
        List<String> dataTableList = RegUtil.getMatchedStrings(page, dataTablePattern1);
		
        for(String str : dataTableList){
        	str = str.replaceAll("<p> <object.*?</object>", "");
        	List<String> imgUrlList = RegUtil.getMatchedStrings(str, "src=.*?jpg");
        	String tmpImgUrl = imgUrlList.get(0);
        	String imgUrl =tmpImgUrl.replaceAll("src|=|\"", "");

        	str = str.replaceFirst("<p>.*?</p>", "").replaceAll("\r|\n|<p>", "").replaceAll("\\|", " ");
        	String title = str.substring(0, 25);
        	
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
		//return "http://tdarkangel.com/page/"+pageNo+"/";
		return "http://tdarkangel.com/page/3/";
		
	}	
	

}