package others.pic;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import utils.RegUtil;
import utils.WebUtil;

public class THotPPic implements Callable<String>{
	
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
		//WebUtil.downloadUnknownLength("https://docs.google.com/document/pubimage?id=1UZX8qMdh5bBctHu5kCsWokDKkYJQlcH0Lj61i5F-lcw&image_id=1xPuoLNHX8U4GCXoze3tROoJfSY03YfA", "D:/downloads/pics/hotp/2.jpg");
		
		THotPPic t = new THotPPic("uncensored", 10);
		t.call();
	}
	
	@Override
	public String call() throws Exception {
		//String pageUrl = getPageUrl(this.category, this.pageNo);
		String pageUrl = "http://jav.hotporndl.com/category/"+this.category+"/page/"+this.pageNo;
		String page = WebUtil.getPageSource(pageUrl, encoding);
		//System.out.println(page);
		
		//String dataTablePattern1 = "<p><a href=\"http://jav.hotporndl.com/wp-content/uploads.*?<p align";
		String dataTablePattern1 = "<div class=\"entry\">.*?</div><!-- END entry -->";
		
        List<String> dataTableList = RegUtil.getMatchedStrings(page, dataTablePattern1);
		
        for(String str : dataTableList){
//        	System.out.println("<<----------------------------------");
//        	System.out.println(str);
//        	System.out.println("---------------------------------->>");
        	String imgPattern = "<img src=\".*?\"";
        	List<String> imgTagList = RegUtil.getMatchedStrings(str, imgPattern);
        	String imgTag = imgTagList.get(0);
        	String imgUrl = imgTag.replaceAll("<img src=|\"", "").replace("&amp;","&");
        	
        	System.out.println(imgUrl);
        	
        	String titlePattern = "<p>.*?</p>";
        	List<String> titleTagList = RegUtil.getMatchedStrings(str, titlePattern);
        	String title = titleTagList.get(1);
        	title = title.replaceAll("<|>|:|\"|\\|\\?|\\*|/", "+");
        	title = title.substring(title.length()-25, title.length()-4);
        	
        	String fileName = ROOT_PIC_DIR+"/"+this.category+"/"+title+".jpg";
        	if(!(new File(fileName)).exists()){
	        	try {
					WebUtil.downloadUnknownLength(imgUrl, fileName);
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