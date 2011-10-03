package modules.exchange.normal;

public class TestMockBrokerClient {

	public static void main(String[] args) throws Exception {
		
		long b0 = System.currentTimeMillis();

		int totalTimes = 1;//1000*10;
		for(int i=0;i<totalTimes;i++){
			testObject();
		}
		
		long e0 = System.currentTimeMillis();
		System.out.println("total time:"+(e0-b0));
	}
	

	private static void testObject() throws Exception{
		TradeRequest request = new TradeRequest(TradeRequest.Type.BID_PRICE, "qqq", -1, -1);
		TradeResponse response = MockBrokerClient.request(request);
		System.out.println(response);
		
		request = new TradeRequest(TradeRequest.Type.ORDER_STATUS, "qqq", -1, -1);
		response = MockBrokerClient.request(request);
		System.out.println(response);
		
		request = new TradeRequest(TradeRequest.Type.PLACE_ORDER, "qqq", -1, -1);
		response = MockBrokerClient.request(request);
		System.out.println(response);

	}
	
}
