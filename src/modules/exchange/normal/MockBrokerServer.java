package modules.exchange.normal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

	public MockBrokerServer() {
		try {
			server = new ServerSocket(GlobalSetting.MOCK_SERVER_PORT, 50, InetAddress.getLocalHost());
			System.out.println("MockBrokerServer listening at "+GlobalSetting.MOCK_SERVER_IP+":"+GlobalSetting.MOCK_SERVER_PORT);
		} catch (Exception err) {
			System.out.println(err);
		}
	}

	public void serve() {
		try {
			while (true) {
				Socket client = server.accept();
				BufferedReader socketReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter socketWriter = new PrintWriter(client.getOutputStream(), true);
				String line;
				line = socketReader.readLine();
				if (line != null) {
					String echoStr = line + " price:" + System.currentTimeMillis();
					socketWriter.println(echoStr);
					//System.out.println("sent [" + echoStr + "]");
				}
				client.close();
			}
		} catch (Exception err) {
			System.err.println(err);
		}
	}

}
