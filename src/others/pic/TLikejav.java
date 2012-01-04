package others.pic;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import utils.RegUtil;
import utils.WebUtil;

public class TLikejav implements Callable<String>{
	
	private static String encoding = "UTF-8";
	private static String ROOT_PIC_DIR="D:/downloads/pics/likejav";
	
	private String category;
	private int pageNo;
	
	public TLikejav(String category, int pageNo) {
		super();
		this.category = category;
		this.pageNo = pageNo;
	}

	public static void main(String[] args) throws Exception {
		TLikejav t = new TLikejav("uncensor", 0);
		t.call();
		
	}
	
	@Override
	public String call() throws Exception {
		String pageUrl = getPageUrl(this.category, this.pageNo);
		String page = WebUtil.getPageSource(pageUrl, encoding);

		String dataTablePattern1 = "<h2 class=\"entry-title\">.*?</h2>";
		
        List<String> dataTableList = RegUtil.getMatchedStrings(page, dataTablePattern1);
        System.out.println(category+","+pageNo+", has "+dataTableList.size()+" pics.");
        for(String str : dataTableList){
        	String imgPageUrl = RegUtil.getMatchedStrings(str, "href=\".*?\"").get(0).replaceAll("href=", "").replaceAll("\"", "");
        	String imgPage = WebUtil.getPageSource(imgPageUrl, encoding);
        	String title = RegUtil.getMatchedStrings(imgPage, "<h1 class=\"post-title entry-title\">.*?</h1>").get(0);
        	title = title.replaceAll("<h1 class=.*?title=\"", "").replaceAll("\" .*", "").replaceAll("/", "_");
        	System.out.println("title="+title);
        	//movie_main.jpg
        	String imgUrl = RegUtil.getMatchedStrings(imgPage, "<p style=\"text-align: center;\"><img class=\"aligncenter\" src=\"http:.*?jpg").get(0);
        	imgUrl = imgUrl.replaceAll("<p style=\"text-align: center;\"><img class=\"aligncenter\" src=\"","");
        	System.out.println(imgUrl);
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
	//http://blog.likejav.com/category/uncensor/page/10/
	//http://blog.likejav.com/category/censor/max-a/page/2/
	private String getPageUrl(String category, int pageNo){
		if("uncensor".equals(category)){
			return "http://blog.likejav.com/category/"+category+"/page/"+pageNo+"/";
		}
		return "http://blog.likejav.com/category/censor/"+category+"/page/"+pageNo+"/";
		
	}	
	
	
}