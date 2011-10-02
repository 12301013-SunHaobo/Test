package modules.exchange.normal;

public class TestMockBrokerClient {

	public static void main(String[] args) {
		
		long b0 = System.currentTimeMillis();
		String stockCode = "qqq";
		
		for (int i=0;i<1000*10;i++){
			String tickStr = MockBrokerClient.getTick(i+":"+stockCode);
			//System.out.println(tickStr);
		}
		long e0 = System.currentTimeMillis();
		System.out.println("total time:"+(e0-b0));
	}
}
