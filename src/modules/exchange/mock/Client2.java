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
    	for(int i=0;i<1;i++){
    		String msg = System.currentTimeMillis()+"";
    		testSendAndReceive(msg);
    		//testSend(msg);
    		//testReceive();
    	}
    	long e0 = System.currentTimeMillis();
    	System.out.println("total time: "+ (e0-b0));
    	
    }
    
    
    
    
    private static void testSendAndReceive(String msg)throws Exception {
    	Socket socket = new Socket(GlobalSetting.MOCK_SERVER_IP, GlobalSetting.MOCK_SERVER_PORT);
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.write(msg);
		System.out.println("Client2 sent:["+msg+"]");
		
	    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    String input;
	    while ((input = in.readLine()) != null) {
	        System.out.println("echo from Server2:["+input+"]");
	    }
	    
		out.close();
		in.close();
		socket.close();
    }
    

    private static void testSend(String msg)throws Exception {
    	Socket socket = new Socket(GlobalSetting.MOCK_SERVER_IP, GlobalSetting.MOCK_SERVER_PORT);
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.write(msg);
		out.close();
		System.out.println("Client2 sent:["+msg+"]");
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