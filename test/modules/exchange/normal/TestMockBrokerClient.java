package modules.exchange.normal;

public class TestMockBrokerClient {

	public static void main(String[] args) {
		
		long b0 = System.currentTimeMillis();

		testObject();
		
		long e0 = System.currentTimeMillis();
		System.out.println("total time:"+(e0-b0));
	}
	

	private static void testObject(){
		TradeRequest tradeRequest = new TradeRequest(TradeRequest.Type.BID_PRICE, "qqq", -1, -1);
		TradeResponse tradeResponse = MockBrokerClient.request(tradeRequest);
		System.out.println(tradeResponse);
	}
	
	private static void testOld(){
		String stockCode = "qqq";
		
		int totalTimes = 1; // 1000*10;
		
		for (int i=0;i<totalTimes;i++){
			String tickStr = MockBrokerClient.getTick(i+":"+stockCode);
			//System.out.println(tickStr);
		}
	}
}
