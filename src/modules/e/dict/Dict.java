package others.e.dict;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.StopWatch;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import others.utils.AuthUtil;
import others.utils.FileUtil;



public class Dict {

	public static String LOGIN_URL = "http://www18.dict.cn/login.php?url=%2Fscb%2F";
	public static String SCB_URL = "http://www18.dict.cn/scb/";
	public static String DEL_URL ="http://www18.dict.cn/scb/index.php?range_method=&range_level=&wsort=&page_start=&word_class=all&delete_words=1";
	public static String ADD_URL ="http://www18.dict.cn/scb/index.php?range_method=&range_level=&wsort=&page_start=&word_class=all";
	
	static String INPUT_FILE="C:/user/backup/rong/en/input/GW-list.txt"; 
	
	public static void main(String[] args) throws Exception {

		StopWatch sw = new StopWatch();
		sw.start();
		long b0 = sw.getTime();

		//login
		DefaultHttpClient httpclient = login();
		
		//delete all
		deleteAll(httpclient);
		
		//add
		//addAll(httpclient);

        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();        
        
		long e0 = sw.getTime();
		System.out.println("Total used time: "+ (e0-b0));
    }
	
	private static void addAll(DefaultHttpClient httpclient) throws Exception{
		
		StopWatch sw = new StopWatch();
		sw.start();
		long b0 = sw.getTime();
		
		List<String> words = FileUtil.fileToList(INPUT_FILE);
		for(int i=0;i<words.size();i++){
			addWord(httpclient, words.get(i));
			long e0 = sw.getTime();
			long remainingTime = (words.size()-i-1)*(e0-b0)/(i+1);
			System.out.println(i+":"+words.get(i)+": remaining time:"+remainingTime);
		}
		
		
	}
	
	
	private static void addWord(DefaultHttpClient httpclient, String word) throws Exception{
		 HttpPost httpost = new HttpPost(ADD_URL);
		 List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		 nvps.add(new BasicNameValuePair("showwordmanage", ""));
		 nvps.add(new BasicNameValuePair("addnewword_class", "0"));
		 nvps.add(new BasicNameValuePair("addnewwordlevel", "3"));
		 nvps.add(new BasicNameValuePair("addnewword", word));
		 nvps.add(new BasicNameValuePair("wordfile", ""));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();

		//System.out.println("Add : " + response.getStatusLine());
		if (entity != null) {
			//String content = EntityUtils.toString(entity);
			//System.out.println(content);
			entity.consumeContent();
		}
		 
	}
	
	
	private static int pageCount(String content){
		String prefix = "/";
		String suffix = "页";
		String countPattern = prefix+"\\d*"+suffix;
		
        Pattern p = Pattern.compile(countPattern, Pattern.DOTALL);
		Matcher m = p.matcher(content);
		boolean found = m.find();
		if(found){
			String tmpBlock = m.group();
			int count = Integer.parseInt(tmpBlock.replaceAll(prefix, "").replaceAll(suffix, ""));
			return count;
		}
		return 0;
	}
	
	private static void deleteAll(DefaultHttpClient httpclient) throws Exception{
		String scbContent = getContent(httpclient, SCB_URL);
		int remainingPage = pageCount(scbContent);
		
		while(remainingPage>0){
			System.out.println("page "+remainingPage);
	        List<String> idList = getIds(scbContent);
	        delete(httpclient, idList);
	        
	        scbContent = getContent(httpclient, SCB_URL);
	        remainingPage = pageCount(scbContent);
		}
	}
	
	
	private static void delete(DefaultHttpClient httpclient, List<String> idList) throws Exception{
        HttpPost httpost = new HttpPost(DEL_URL);
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();

        for(int i=0;i<idList.size();i++){
        	nvps.add(new BasicNameValuePair("wids[]", idList.get(i)));
        }
        
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        HttpResponse response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        //System.out.println("Delete : " + response.getStatusLine());
        if (entity != null) {
        	//String content = EntityUtils.toString(entity);
        	//System.out.println(content);
            entity.consumeContent();
        }
		
	}
	
	
	private static List<String> getIds(String content){
		List<String> idList = new ArrayList<String>();
        String chkboxPattern ="(<input type=\"checkbox\" name=\"wids\\[\\]\" value=\").*?(\" />)";
        Pattern p = Pattern.compile(chkboxPattern, Pattern.DOTALL);
		Matcher m = p.matcher(content);
		boolean found = m.find();
		while(found){
			String tmpBlock = m.group();
			//System.out.println(tmpBlock.replaceAll("\"", ""));
			idList.add(tmpBlock.replaceAll("<input type=\"checkbox\" name=\"wids\\[\\]\" value=\"", "")
					.replaceAll("\" />", "")); 
			found = m.find();//loop for next
		}
		return idList;
	}
	
	private static String getContent(DefaultHttpClient httpclient, String url) throws Exception{
        HttpGet httpGet =  new HttpGet(SCB_URL);
        HttpResponse scbResp = httpclient.execute(httpGet);
        HttpEntity scbEntity = scbResp.getEntity();
        System.out.println("scb get: " + scbResp.getStatusLine());
        if (scbEntity != null) {
        	String content =  EntityUtils.toString(scbEntity);
        	scbEntity.consumeContent();
        	return content;
        }
        return null;

	}
	
	private static DefaultHttpClient login() throws Exception{
        DefaultHttpClient httpclient = new DefaultHttpClient();
        //set proxy
        if("nbkpcvt".equals(System.getProperty("user.name"))){
            HttpHost proxy = new HttpHost("proxy.ml.com", 8083); 
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        //log in
        HttpPost httpost = new HttpPost(LOGIN_URL);
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("username", AuthUtil.uid));
        nvps.add(new BasicNameValuePair("password", AuthUtil.pwd));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        HttpResponse response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
        	//String content = EntityUtils.toString(entity);
        	//System.out.println(content);
            entity.consumeContent();
        }

        return httpclient;
	}
	
	
}
