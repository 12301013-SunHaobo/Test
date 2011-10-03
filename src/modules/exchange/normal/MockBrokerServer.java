package modules.exchange.normal;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Tick;

import utils.Formatter;
import utils.GlobalSetting;
import utils.MathUtil;

/**
 * 
 * Mock broker server
 * Performance: 1000*10 requests take 3294 ms
 *
 */
public class MockBrokerServer {

	private List<Tick> tickList;
	
	private ServerSocket server;

	public static void main(String[] args) {
		MockBrokerServer s = new MockBrokerServer();
		s.serve();
	}

	private MockBrokerServer() {
		try {
			long b0 = System.currentTimeMillis();
			
			
			server = new ServerSocket(GlobalSetting.MOCK_SERVER_PORT, 50, InetAddress.getLocalHost());
			System.out.println("MockBrokerServer listening at "+GlobalSetting.MOCK_SERVER_IP+":"+GlobalSetting.MOCK_SERVER_PORT);
			long e0 = System.currentTimeMillis();
			System.out.println("Server start in "+(e0-b0)+ " milliseconds.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void serve() {
		try {
			while (true) {
				Socket socket = server.accept();
				ObjectInputStream socketReader = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				/**
				 * TECHDEBT
				 * If i use new BufferedOutputStream, it will halt where outputs are sent, even if I call socketWriter.flush()
				 * new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				 * 
				 */
				ObjectOutputStream socketWriter = new ObjectOutputStream(socket.getOutputStream());
				TradeRequest tradeRequest = (TradeRequest)socketReader.readObject();
				if (tradeRequest != null) {
					TradeResponse tradeResponse = processTradeRequest(tradeRequest);
					socketWriter.writeObject(tradeResponse);
				}
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
