package modules.exchange.nio.mock2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author zhu
 * 
 */
public class EchoClient {
	private int port;
	private InetAddress host;
	private SocketChannel channel;
	private Selector selector;
	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	private LinkedList<byte[]> messages = new LinkedList<byte[]>();

	public EchoClient(InetAddress host, int port) throws Exception {
		this.host = host;
		this.port = port;
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		selector = Selector.open();
	}

	public void sendMsg() {
		try {
			channel.connect(new InetSocketAddress(host, port));
			channel.register(selector, SelectionKey.OP_CONNECT);
			while (true) {
				synchronized (messages) {
					// 检查队列是否有可写的数据
					if (!messages.isEmpty()) {
						SelectionKey key = channel.keyFor(selector);
						key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
					}
				}
				int count = selector.select();
				if (count > 0) {
					Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
					while (iterator.hasNext()) {
						SelectionKey key = iterator.next();
						iterator.remove();
						if (!key.isValid()) {
							continue;
						}

						if (key.isConnectable()) {
							channel.finishConnect();
							channel.register(selector, SelectionKey.OP_READ);
						} else if (key.isWritable()) {
							System.out.println("before write");
							write(key);
							System.out.println("after write");
						} else if (key.isReadable()) {
							System.out.println("before read");
							read(key);
							System.out.println("after read");
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 向服务端发送数据
	 * 
	 * @param msg
	 */
	public void send(byte[] msg) {
		synchronized (messages) {
			messages.addLast(msg);
			// 将阻塞中的select调用直接返回
			selector.wakeup();
		}
	}

	/**
	 * 写操作
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void write(SelectionKey key) throws IOException {
		SocketChannel aChannel = (SocketChannel) key.channel();
		synchronized (messages) {
			if (!messages.isEmpty()) {
				byte[] buf = messages.getFirst();
				messages.removeFirst();
				buffer.clear();
				buffer.put(buf);
				buffer.flip();
				aChannel.write(buffer);
				key.interestOps(SelectionKey.OP_READ);
				System.out.println("write to: " + aChannel.socket().getRemoteSocketAddress() + "; message: " + new String(buf));
			}
		}
	}

	/**
	 * 读操作
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void read(SelectionKey key) throws IOException {
		SocketChannel aChannel = (SocketChannel) key.channel();
		buffer.clear();
		int len = aChannel.read(buffer);
		if (len > 0) {
			byte[] buf = Arrays.copyOfRange(buffer.array(), 0, len);
			System.out.println("read from: " + aChannel.socket().getRemoteSocketAddress() + "; message: " + new String(buf));
		}
	}

	public static void main(String[] args) throws Exception {
		
		EchoClient client = new EchoClient(InetAddress.getLocalHost(), 8900);
		client.messages.add("msg001".getBytes());
		client.messages.add("msg002".getBytes());
		client.messages.add("msg003".getBytes());
		client.messages.add("msg004".getBytes());
		client.sendMsg();
		
		//CommandReader reader = new CommandReader(client);
		//threadStart(reader);
	}
}