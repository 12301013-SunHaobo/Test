package modules.exchange.normal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import utils.GlobalSetting;

public class MockBrokerClient {

	public static String getTick(String stockCode){
		String tickStr = null;
		try {
			Socket socket = new Socket(GlobalSetting.MOCK_SERVER_IP, GlobalSetting.MOCK_SERVER_PORT);
			BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);
			socketWriter.println(stockCode);//send to server
			tickStr = socketReader.readLine();//read from server
			socketWriter.close();
			socketReader.close();
		} catch (Exception err) {
			System.err.println(err);
		}
		return tickStr;
	}
	
}