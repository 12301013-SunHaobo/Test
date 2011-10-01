package modules.exchange.mock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import utils.GlobalSetting;

public class Client2 {

	public static Charset charset = Charset.forName("UTF-8");
	public static CharsetEncoder encoder = charset.newEncoder();
	public static CharsetDecoder decoder = charset.newDecoder();

	
    public static void main(String[] args) throws Exception {

    	long b0 = System.currentTimeMillis();
    	for(int i=0;i<100;i++){
    		testReceive();
    		//testSend();
    	}
    	long e0 = System.currentTimeMillis();
    	System.out.println("total time: "+ (e0-b0));
    	
    }
    

    private static void testSend()throws Exception {
  	   Socket socket = new Socket(GlobalSetting.MOCK_SERVER_IP, GlobalSetting.MOCK_SERVER_PORT);
  	   PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
  	   out.close();
 	    
 	   out.println("from Client2.java "+System.currentTimeMillis());
     }

    
    private static void testReceive()throws Exception {
 	   Socket socket = new Socket(GlobalSetting.MOCK_SERVER_IP, GlobalSetting.MOCK_SERVER_PORT);
	    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    
	    String input;
	    while ((input = in.readLine()) != null) {
	        System.out.println("echo: " + input);
	    }
    }
    
    
}