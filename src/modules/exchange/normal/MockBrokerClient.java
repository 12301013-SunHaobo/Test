package modules.exchange.normal;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utils.GlobalSetting;

public class MockBrokerClient {

	public static TradeResponse request(TradeRequest tradeRequest) throws Exception {
		TradeResponse tradeResponse = null;

		Socket socket = new Socket(GlobalSetting.MOCK_SERVER_IP, GlobalSetting.MOCK_SERVER_PORT);

		ObjectOutputStream socketWriter = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream socketReader = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

		socketWriter.writeObject(tradeRequest);
		tradeResponse = (TradeResponse) socketReader.readObject();

		socket.close();

		return tradeResponse;
	}

}