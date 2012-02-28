package others.urlconn;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import utils.CookieManager;
import utils.FileUtil;

public class TestUrlConnection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test1();
	}

	private static void test1() {
		String rootDir = "D:/projects/java/workspace/Test/tmp/excel/";
		String siteUrlStr = "http://www.vocabulary.com";
		String wordUrlStr = "http://www.vocabulary.com/dictionary/audio/en/";
		String word = "amusement";
		try {
//			CookieManager cm = new CookieManager(); 
//			URL siteUrl = new URL(siteUrlStr);
//			URLConnection siteUc = siteUrl.openConnection();
//			siteUc.connect();
//			cm.storeCookies(siteUc);
			
			CookieManager cm1 = createCookieManager();
			CookieManager cm2 = createCookieManager();
			CookieManager cm3 = createCookieManager();

			System.out.println(cm1.toString());
			String word1="red";
			download(wordUrlStr+word1, rootDir+word1+".mp3", cm1);

			System.out.println(cm2.toString());
			String word2="good";
			download(wordUrlStr+word2, rootDir+word2+".mp3", cm2);

			System.out.println(cm3.toString());
			String word3="yellow";
			download(wordUrlStr+word3, rootDir+word3+".mp3", cm3);
			
			/*
			URL wordUrl = new URL(wordUrlStr);
			URLConnection wordUc = wordUrl.openConnection();
			cm.setCookies(wordUc);
			wordUc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");//some sites require this, otherwise 403 error
			//wordUc.setReadTimeout(5*60*1000);
			wordUc.connect();
			//String headerField = wordUc.getHeaderField("Location");
			
			printAllHeaders(wordUc);
			//System.out.println("headerField="+headerField);
			 */
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	
	
	private static void printAllHeaders(URLConnection uc){
	    for (int i=0; ; i++) {
	        String headerName = uc.getHeaderFieldKey(i);
	        String headerValue = uc.getHeaderField(i);
	        System.out.println(headerName+"*************>"+headerValue);
	        if (headerName == null && headerValue == null) {
	            // No more headers
	            break;
	        }
	        if (headerName == null) {
	            // The header value contains the server's HTTP version
	        }
	    }
	}
	
	public static CookieManager createCookieManager() throws Exception {
		String siteUrlStr = "http://www.vocabulary.com";
		CookieManager cm = new CookieManager(); 
		URL siteUrl = new URL(siteUrlStr);
		URLConnection siteUc = siteUrl.openConnection();
		siteUc.connect();
		cm.storeCookies(siteUc);
		return cm;
	}
	
	//download a binary file
	public static void download(String urlStr, String fileName, CookieManager cm) throws Exception{
		if(urlStr==null || urlStr.trim().equals("")){
			return;
		}
		URL u = new URL(urlStr);
	    URLConnection uc = u.openConnection();
	    uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");//some sites require this, otherwise 403 error
	    cm.setCookies(uc);
	    String contentType = uc.getContentType();
	    int contentLength = uc.getContentLength();
	    if (contentType.startsWith("text/") || contentLength == -1) {
	      throw new IOException("This is not a binary file.");
	    }
	    InputStream raw = uc.getInputStream();
	    InputStream in = new BufferedInputStream(raw);
	    byte[] data = new byte[contentLength];
	    int bytesRead = 0;
	    int offset = 0;
	    while (offset < contentLength) {
	      bytesRead = in.read(data, offset, data.length - offset);
	      if (bytesRead == -1)
	        break;
	      offset += bytesRead;
	    }
	    in.close();

	    if (offset != contentLength) {
	      throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
	    }
		
	    FileUtil.createFolderIfNotExist(fileName);
	    
	    FileOutputStream out = new FileOutputStream(fileName);
	    out.write(data);
	    out.flush();
	    out.close();	
	}	
	
	
	
	


	

}

