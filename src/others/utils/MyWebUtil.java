package others.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MyWebUtil {

	// this gets right html code
	static String sRight = "http://www.wholesale-dress.net/fashion-style-letters-printed-messenger-bag-white-g1157530.html";
	// this gets wrong html code
	static String sWrong = "http://www.wholesale-dress.net/sweet-round-collar-long-sleeve-lace-dress-white-g1169699.html";

	public static void main(String[] args) throws Exception {
		// System.out.println(Locale.getDefault());

		String pageSource = getPageSource(sWrong, "UTF8");// I have tried UTF8,GBK,gb2312,,,,,,,,,
		System.out.println(pageSource);
		
		//getWebSite(sWrong);
	}

	public static String getPageSource(String urlStr, String encoding) {
		StringBuilder sb = new StringBuilder();

		try {
			// http://proxy.ml.com:8083/
			// Properties props = System.getProperties();
			// props.put("http.proxyHost", "proxy.ml.com");
			// props.put("http.proxyPort", "8083");

			URL url = new URL(urlStr);
			URLConnection uc = url.openConnection();
			uc.setReadTimeout(15000);
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
		System.out.println("sb.length():"+sb.length());
		return sb.toString();
	}

	// other testing

	public static void getWebSite(String siteUrl) {

		try {

			URL url = new URL(siteUrl);//"http://www.google.com"
			URLConnection urlc = url.openConnection();

			BufferedInputStream buffer = new BufferedInputStream(urlc
					.getInputStream());

			StringBuilder builder = new StringBuilder();
			int byteRead;
			while ((byteRead = buffer.read()) != -1)
				builder.append((char) byteRead);

			buffer.close();

			System.out.println(builder.toString());
			System.out.println("The size of the web page is "
					+ builder.length() + " bytes.");

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
