package modules.exchange.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import utils.GlobalSetting;

public class Server2 {

	private static Charset charset = Charset.forName("ISO-8859-1");
	private static CharsetDecoder decoder = charset.newDecoder();
	private static CharsetEncoder encoder = charset.newEncoder();
	private static int counter = 0;
	
	public static void main(String[] args) {
		try {
			Server2 ms = new Server2();
			ms.acceptConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void acceptConnections() throws Exception {
		// Selector for incoming time requests
		Selector acceptSelector = SelectorProvider.provider().openSelector();

		// Create a new server socket and set to non blocking mode
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);

		// Bind the server socket to the local host and port
		InetAddress lh = InetAddress.getLocalHost();
		InetSocketAddress isa = new InetSocketAddress(lh, GlobalSetting.MOCK_SERVER_PORT);
		ssc.socket().bind(isa);
		System.out.println("listening on "+lh.getHostAddress()+":"+GlobalSetting.MOCK_SERVER_PORT);

		// Register accepts on the server socket with the selector. 
		ssc.register(acceptSelector, SelectionKey.OP_ACCEPT);

		while (acceptSelector.select() > 0) {
			// Someone is ready for I/O, get the ready keys
			Set<SelectionKey> readyKeys = acceptSelector.selectedKeys();
			Iterator<SelectionKey> keys = readyKeys.iterator();

			while (keys.hasNext()) {
				SelectionKey key = (SelectionKey) keys.next();
				keys.remove();

				if (key.isAcceptable()) {
	
					ServerSocketChannel keyChannel = (ServerSocketChannel)key.channel();
					ServerSocket serverSocket = keyChannel.socket();
					Socket socket = serverSocket.accept();
					
					//read message
//					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				    
//				    String input;
//				    while ((input = in.readLine()) != null) {
//				        System.out.println("received: " + input);
//				    }
//				    in.close();					
					//send back message
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.write("<<"+"input"+">>"+(++counter));
					out.close();
				} else {
					System.out.println("key is not acceptable");
				}
			}
		}
	}

	private void accept(SelectionKey sk) throws IOException {
		//System.out.println("accept "+(++counter));
	}
	
	private void read(SelectionKey sk) throws IOException {
		System.out.println("read "+(++counter));
	}
	
	
	private void write(SelectionKey sk) throws IOException {
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