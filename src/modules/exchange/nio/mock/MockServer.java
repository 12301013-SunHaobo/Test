package modules.exchange.nio.mock;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Set;

public class MockServer {

	private static Charset charset = Charset.forName("ISO-8859-1");
	private static CharsetDecoder decoder = charset.newDecoder();
	private static CharsetEncoder encoder = charset.newEncoder();

	
	private static final int DEFAULT_TIME_PORT = 8900;

	public MockServer() throws Exception {
		acceptConnections(DEFAULT_TIME_PORT);
	}

	public MockServer(int port) throws Exception {
		acceptConnections(port);
	}

	private static void acceptConnections(int port) throws Exception {
		// Selector for incoming time requests
		Selector acceptSelector = SelectorProvider.provider().openSelector();

		// Create a new server socket and set to non blocking mode
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);

		// Bind the server socket to the local host and port

		InetAddress lh = InetAddress.getLocalHost();
		InetSocketAddress isa = new InetSocketAddress(lh, port);
		ssc.socket().bind(isa);
		System.out.println("listening on "+lh.getHostAddress()+":"+port);

		// Register accepts on the server socket with the selector. This
		// step tells the selector that the socket wants to be put on the
		// ready list when accept operations occur, so allowing multiplexed
		// non-blocking I/O to take place.
		SelectionKey acceptKey = ssc.register(acceptSelector, SelectionKey.OP_ACCEPT);

		int keysAdded = 0;
		int counter = 0;

		// Here's where everything happens. The select method will
		// return when any operations registered above have occurred, the
		// thread has been interrupted, etc.
		while ((keysAdded = acceptSelector.select()) > 0) {
			// Someone is ready for I/O, get the ready keys
			Set<SelectionKey> readyKeys = acceptSelector.selectedKeys();
			Iterator<SelectionKey> i = readyKeys.iterator();

			// Walk through the ready keys collection and process date requests.
			while (i.hasNext()) {
				SelectionKey sk = (SelectionKey) i.next();
				i.remove();
				// The key indexes into the selector so you
				// can retrieve the socket that's ready for I/O
				ServerSocketChannel nextReady = (ServerSocketChannel) sk.channel();
				// Accept the date request and send back the date string
				Socket s = nextReady.accept().socket();
				// Write the current time to the socket
				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				//Date now = new Date();
				String strToSend = "testStringToSend "+(++counter);
				System.out.print("<< ---- sending '"+strToSend+"'");
				//out.println(strToSend);
				out.write(strToSend);
				System.out.println("sent >> ");
				out.flush();
				out.close();
			}
		}
	}

	// Entry point.
	public static void main(String[] args) {
		// Parse command line arguments and
		// create a new time server (no arguments yet)
		try {
			MockServer nbt = new MockServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}