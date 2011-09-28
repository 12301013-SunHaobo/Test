package modules.exchange.mock;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class Client {

	private static Charset charset = Charset.forName("ISO-8859-1");
	private static CharsetDecoder decoder = charset.newDecoder();
	private static CharsetEncoder encoder = charset.newEncoder();

	public static void main(String[] argv) throws Exception {

		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
		SocketChannel sChannel = SocketChannel.open();
		sChannel.configureBlocking(false);
		sChannel.connect(new InetSocketAddress("192.168.1.2", 8900));

		while (!sChannel.finishConnect()) {
			Thread.sleep(100);
			System.out.println("client waiting for connection...");
		}

		buf.clear();
		int numBytesRead = sChannel.read(buf);
		buf.flip();
	    String cb = decoder.decode(buf).toString();
	    System.out.print("cb:" + cb);

		if (numBytesRead == -1) {
			sChannel.close();
		} else {
			buf.flip();
		}
	}
}
