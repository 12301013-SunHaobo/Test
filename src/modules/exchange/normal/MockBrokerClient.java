package modules.exchange.normal;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utils.GlobalSetting;

public class MockBrokerClient {

	public static TradeResponse request(TradeRequest tradeRequest){
		TradeResponse tradeResponse = null;
		try {
			Socket socket = new Socket(GlobalSetting.MOCK_SERVER_IP, GlobalSetting.MOCK_SERVER_PORT);
			ObjectOutputStream socketWriter = new ObjectOutputStream(socket.getOutputStream());
			socketWriter.writeObject(tradeRequest);

			ObjectInputStream socketReader = new ObjectInputStream(socket.getInputStream());
			tradeResponse = (TradeResponse)socketReader.readObject();
			
			socket.close();
			
			return tradeResponse;
		} catch (Exception err) {
			System.err.println(err);
		}
		return null;
	}
	
}