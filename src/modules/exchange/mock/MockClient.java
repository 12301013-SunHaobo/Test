package modules.exchange.mock;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import utils.GlobalSetting;

public class EchoClient {

	public static Charset charset = Charset.forName("UTF-8");
	public static CharsetEncoder encoder = charset.newEncoder();
	public static CharsetDecoder decoder = charset.newDecoder();

	
    public static void main(String[] args) throws Exception {
    	   Socket socket = new Socket(GlobalSetting.MOCK_SERVER_IP, GlobalSetting.MOCK_SERVER_PORT);
    	    int c = 0;
    	    
    	    OutputStream os = socket.getOutputStream();
    	    InputStream is = socket.getInputStream();
    	    String query = "thisistest\r\n";
    	    os.write(query.getBytes("iso8859_1"));

    	    while (c != -1) {
    	      c = is.read();
    	      if (c != -1)
    	        System.out.println((char) c);
    	    }
    }
    
    
    
    
}