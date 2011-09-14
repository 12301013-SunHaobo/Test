package others.e.dict;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import others.utils.AuthUtil;





public class DictUtil {

	public static String INPUT_FILE="C:/user/backup/rong/en/input/GW-list.txt";

	public static String LOGIN_URL = "http://www18.dict.cn/login.php?url=%2Fscb%2F";
	public static String SCB_URL = "http://www18.dict.cn/scb/";
	public static String DEL_URL ="http://www18.dict.cn/scb/index.php?range_method=&range_level=&wsort=&page_start=&word_class=all&delete_words=1";
	public static String ADD_URL ="http://www18.dict.cn/scb/index.php?range_method=&range_level=&wsort=&page_start=&word_class=all";
	public static String PAGE_URL = "http://www18.dict.cn/scb/index.php?range_method=&range_level=&wsort=&word_class=all&page_start="; 
	
	public static String CHARSET = "gbk";

	

	public static HttpClient login() throws Exception{
		//create thread safe conneciton manager 
		HttpParams params = new BasicHttpParams();
		
		// Increase max total connection to 200
		ConnManagerParams.setMaxTotalConnections(params, 100);
		// Increase default max connection per route to 20
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(100);
		// Increase max connections for localhost:80 to 50
		HttpHost localhost = new HttpHost("locahost", 80);
		connPerRoute.setMaxForRoute(new HttpRoute(localhost), 50);
		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
		HttpClient httpclient = new DefaultHttpClient(cm, params);
		
        //set proxy
        if("nbkpcvt".equals(System.getProperty("user.name"))){
            HttpHost proxy = new HttpHost("proxy.ml.com", 8083); 
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        //log in
        HttpPost httpost = new HttpPost(DictUtil.LOGIN_URL);
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

	public static int pageCount(String content){
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

	public static List<String> getIds(String content){
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

	public static void delete(HttpClient httpclient, List<String> idList) throws Exception{
        HttpPost httpost = new HttpPost(DictUtil.DEL_URL);
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
	
	public static String getContent(HttpClient httpclient, String url) throws Exception{
        HttpGet httpGet =  new HttpGet(url);
        HttpResponse scbResp = httpclient.execute(httpGet);
        HttpEntity scbEntity = scbResp.getEntity();
        //System.out.println("getContent:"+url + scbResp.getStatusLine());
        if (scbEntity != null) {
        	String content =  EntityUtils.toString(scbEntity, CHARSET);
        	scbEntity.consumeContent();
        	return content;
        }
        return null;

	}
	
	public static void addWord(HttpClient httpclient, String word) throws Exception{
		 HttpPost httpost = new HttpPost(DictUtil.ADD_URL);
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
	
}