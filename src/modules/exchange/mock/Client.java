package modules.exchange.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class Client {

	private static String HOST = "192.168.1.2";
	private static int PORT = 8900;

	private static Charset charset = Charset.forName("ISO-8859-1");
	private static CharsetDecoder decoder = charset.newDecoder();
	private static CharsetEncoder encoder = charset.newEncoder();

	public static void main(String[] argv) throws Exception {

		//test1();
		 testSocket();
		// testSocketChannel();
	}

	public static void test1() {

	}

	private static void testSocket() throws Exception {
		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			echoSocket = new Socket(HOST, PORT);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: taranis.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " + "the connection to: taranis.");
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;

		while ((userInput = stdIn.readLine()) != null) {
			out.println(userInput);
			System.out.println("echo: " + in.readLine());
		}

		out.close();
		in.close();
		stdIn.close();
		echoSocket.close();
	}

	private static void testSocketChannel() throws Exception {
		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
		SocketChannel sChannel = SocketChannel.open();
		sChannel.configureBlocking(false);
		sChannel.connect(new InetSocketAddress(HOST, PORT));

		while (!sChannel.finishConnect()) {
			Thread.sleep(100);
			System.out.println("client waiting for connection...");
		}

		buf.clear();
		int numBytesRead = sChannel.read(buf);
		Thread.sleep(100);
		CharBuffer cb = decoder.decode(buf);
		String cbStr = cb.toString();
		System.out.print("cb:" + cbStr);

		if (numBytesRead == -1) {
			sChannel.close();
		} else {
			buf.flip();
		}
	}
}
