package modules.exchange.normal;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import utils.GlobalSetting;

/**
 * 
 * Mock broker server
 * Performance: 1000*10 requests take 3294 ms
 *
 */
public class MockBrokerServer {

	private ServerSocket server;

	public static void main(String[] args) {
		MockBrokerServer s = new MockBrokerServer();
		s.serve();
	}

	private MockBrokerServer() {
		try {
			server = new ServerSocket(GlobalSetting.MOCK_SERVER_PORT, 50, InetAddress.getLocalHost());
			System.out.println("MockBrokerServer listening at "+GlobalSetting.MOCK_SERVER_IP+":"+GlobalSetting.MOCK_SERVER_PORT);
		} catch (Exception err) {
			System.out.println(err);
		}
	}

	private void serve() {
		try {
			while (true) {
				Socket socket = server.accept();
				//BufferedReader socketReader = new BufferedReader(new InputStreamReader(new ObjectInputStream(client.getInputStream())));
				ObjectInputStream socketReader = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream socketWriter = new ObjectOutputStream(socket.getOutputStream());
				TradeRequest tradeRequest = (TradeRequest)socketReader.readObject();
				if (tradeRequest != null) {
					TradeResponse tradeResponse = processTradeRequest(tradeRequest);
					socketWriter.writeObject(tradeResponse);
				}
				socket.close();
			}
		} catch (Exception err) {
			System.err.println(err);
		}
	}

	
	private TradeResponse processTradeRequest(TradeRequest tradeRequest){
		switch (tradeRequest.getType()){
			case BID_PRICE: return processBidRequest(tradeRequest);
			case ORDER_STATUS: return processOrderStatusRequest(tradeRequest);
			case PLACE_ORDER: return processPlaceOrder(tradeRequest);
			default : return new TradeResponse(tradeRequest.getType(), tradeRequest.getTickCode(), "unknown type");
		}
	}
	
	/**
	 * tick price,volume next to current request time
	 * request [QQQ;BID_PRICE;]
	 * @param request
	 */
	private TradeResponse processBidRequest(TradeRequest tradeRequest){
		System.out.println("processBidRequest ["+tradeRequest.toString()+"]");
		return new TradeResponse(tradeRequest.getType(), tradeRequest.getTickCode(), "processed");
	}
	
	/**
	 * check order filled or not
	 * request [QQQ;ORDER_STATUS;]
	 * @param request
	 */
	private TradeResponse processOrderStatusRequest(TradeRequest tradeRequest){
		System.out.println("processOrderRequest ["+tradeRequest.toString()+"]");
		return new TradeResponse(tradeRequest.getType(), tradeRequest.getTickCode(), "processed");
	}
	
	/**
	 * Place order
	 * request [QQQ;PLACE_ORDER;price;volume;]
	 * @param request
	 */
	private TradeResponse processPlaceOrder(TradeRequest tradeRequest){
		System.out.println("processPlaceOrder ["+tradeRequest.toString()+"]");
		return new TradeResponse(tradeRequest.getType(), tradeRequest.getTickCode(), "processed");
	}
	
	
	
	
	
	
	
}
