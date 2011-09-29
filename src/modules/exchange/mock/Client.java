package modules.exchange.mock;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import utils.GlobalSetting;

public class Client {
    public static void main(String[] argv) throws Exception {
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        SocketChannel sChannel = SocketChannel.open();
        sChannel.configureBlocking(false);
        sChannel.connect(new InetSocketAddress(GlobalSetting.MOCK_SERVER_IP, GlobalSetting.MOCK_SERVER_PORT));

        int numBytesRead = sChannel.read(buf);
        System.out.println("numBytesRead:"+numBytesRead);

        if (numBytesRead == -1) {
            sChannel.close();
        } else {
            buf.flip();
        }
    }
}