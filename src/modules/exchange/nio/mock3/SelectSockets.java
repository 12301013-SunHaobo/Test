package modules.exchange.nio.mock3;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Simple echo-back server which listens for incoming stream connections and
 * echoes back whatever it reads. A single Selector object is used to listen to
 * the server socket (to accept new connections) and all the active socket
 * channels.
 * 
 * @author Ron Hitchens (ron@ronsoft.com)
 */

public class SelectSockets {
	public static int PORT_NUMBER = 8900;

	public static void main(String[] argv) throws Exception {
		new SelectSockets().go(argv);
	}

	public void go(String[] argv) throws Exception {
		int port = PORT_NUMBER;

		System.out.println("Listening on port " + port);
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		ServerSocket serverSocket = serverChannel.socket();
		Selector selector = Selector.open();
		serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
		serverChannel.configureBlocking(false);
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		while (true) {
			int n = selector.select();
			if (n == 0) {
				continue; // nothing to do
			}
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();

			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();
				if (key.isAcceptable()) {
					ServerSocketChannel server = (ServerSocketChannel) key.channel();
					SocketChannel channel = server.accept();
					registerChannel(selector, channel, SelectionKey.OP_READ);
					sayHello(channel);
				}
				if (key.isReadable()) {
					readDataFromSocket(key);
				}
				it.remove();
			}
		}
	}

	// ----------------------------------------------------------
	/**
	 * Register the given channel with the given selector for the given
	 * operations of interest
	 */
	protected void registerChannel(Selector selector, SelectableChannel channel, int ops) throws Exception {
		if (channel == null) {
			return; // could happen
		}
		// Set the new channel nonblocking
		channel.configureBlocking(false);
		// Register it with the selector
		channel.register(selector, ops);
	}

	// ----------------------------------------------------------
	// Use the same byte buffer for all channels. A single thread is
	// servicing all the channels, so no danger of concurrent acccess.
	private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

	/**
	 * Sample data handler method for a channel with data ready to read.
	 * 
	 * @param key
	 *            A SelectionKey object associated with a channel determined by
	 *            the selector to be ready for reading. If the channel returns
	 *            an EOF condition, it is closed here, which automatically
	 *            invalidates the associated key. The selector will then
	 *            de-register the channel on the next select call.
	 */
	protected void readDataFromSocket(SelectionKey key) throws Exception

	{
		SocketChannel socketChannel = (SocketChannel) key.channel();
		int count;
		buffer.clear(); // Empty buffer
		// Loop while data is available; channel is nonblocking
		while ((count = socketChannel.read(buffer)) > 0) {
			buffer.flip(); // Make buffer readable
			// Send the data; don't assume it goes all at once
			while (buffer.hasRemaining()) {
				socketChannel.write(buffer);
			}
			// WARNING: the above loop is evil. Because
			// it's writing back to the same nonblocking
			// channel it read the data from, this code can
			// potentially spin in a busy loop. In real life
			// you'd do something more useful than this.
			buffer.clear(); // Empty buffer
		}
		if (count < 0) {
			// Close channel on EOF, invalidates the key
			socketChannel.close();
		}
	}

	// ----------------------------------------------------------
	/**
	 * Spew a greeting to the incoming client connection.
	 * 
	 * @param channel
	 *            The newly connected SocketChannel to say hello to.
	 */
	private void sayHello(SocketChannel channel) throws Exception {
		buffer.clear();
		buffer.put("Hi there!\r\n".getBytes());
		buffer.flip();
		channel.write(buffer);

	}
}
