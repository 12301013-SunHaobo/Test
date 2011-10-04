package modules.exchange.normal;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import modules.at.feed.history.HistoryLoader;
import modules.at.model.Tick;
import utils.Formatter;
import utils.GlobalSetting;

/**
 * 
 * Mock broker server
 * Performance: 1000*10 requests take 3294 ms
 *
 */
public class MockBrokerServer {

	//change for different code and date begin
	private static String stockcode = "qqq";//change for new code
	private static String nazTickOutputDateStr = "20110919";//change for new date 
	private static String origFileName = "20110919-205230.txt"; //change for new orig file, with prefix SSS, SSS_20110919-205230.txt
	//change for different code and date end
	
	private List<Tick> tickList = null;
	
	private ServerSocket server;
	private long mockServerStartTime = -1; 
	private long realMarketOpenTime = -1;

	public static void main(String[] args) {
		MockBrokerServer s = new MockBrokerServer();
		s.serve();
	}

	private MockBrokerServer() {
		try {
			long b0 = System.currentTimeMillis();
			tickList = HistoryLoader.getNazHistTicksSSS(stockcode, origFileName, nazTickOutputDateStr);
			
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
			mockServerStartTime = System.currentTimeMillis();//set mock server start time
			realMarketOpenTime = Formatter.DEFAULT_DATETIME_FORMAT.parse(nazTickOutputDateStr+"-09:30:00").getTime();
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
		long mockRequestTime = System.currentTimeMillis();
		switch (tradeRequest.getType()){
			case BID_PRICE: return processBidRequest(tradeRequest, mockRequestTime);
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
	private TradeResponse processBidRequest(TradeRequest tradeRequest, long mockRequestTime){
		System.out.println("processBidRequest ["+tradeRequest.toString()+"]");
		Tick tick = getNextRealTick(mockRequestTime, tickList);
		return new TradeResponse(tradeRequest.getType(), tradeRequest.getTickCode(), tick!=null?tick.toString():"null");
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
	
	/**
	 * Find next real tick after current request time
	 * @param dateTime
	 * @param tickList
	 * @return
	 */
	private Tick getNextRealTick(long mockRequestTime, List<Tick> tickList){
		long realRequestTime = convertMockRequestTimeToReal(mockRequestTime);
		for(Tick tick : tickList){
			if(tick.getDate().getTime() > realRequestTime){
				return tick;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param requestTime the time server received this request
	 * @return
	 */
	private long convertMockRequestTimeToReal(long requestTime){
		//speed, how many times of real time 
		long mockTimeSpeed = 10;
		//request time offset relative to mock market open
		long offsetTime = requestTime-this.mockServerStartTime;
		long realRequestTime = realMarketOpenTime + offsetTime * mockTimeSpeed;
		
		return realRequestTime;
	}
}
