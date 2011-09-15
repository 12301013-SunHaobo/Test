package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WebUtil {

	public static void main(String[] args) throws Exception {
		// getPageSource(s, "UTF8");//UTF8,GBK,gb2312,,,,,,,,,

	}

	// working
	public static String getPageSource(String urlStr, String encoding) {
		StringBuilder sb = new StringBuilder();

		try {
//			Properties props = System.getProperties();
//			props.put("http.proxyHost", "proxy.ml.com");
//			props.put("http.proxyPort", "8083");

			URL url = new URL(urlStr);
			URLConnection uc = url.openConnection();
			uc.setReadTimeout(60000);
			BufferedReader in = new BufferedReader(new InputStreamReader(uc
					.getInputStream(), encoding));
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line).append(System.getProperty("line.separator"));
			}
			in.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		//System.out.println(sb.toString());// testing
		return sb.toString();
	}

	//download a binary file
	public static void download(String urlStr, String fileName) throws Exception{
		if(urlStr==null || urlStr.trim().equals("")){
			return;
		}
		URL u = new URL(urlStr);
	    URLConnection uc = u.openConnection();
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

	    FileOutputStream out = new FileOutputStream(fileName);
	    out.write(data);
	    out.flush();
	    out.close();	
	}

}
