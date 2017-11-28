package netty.javaNIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class ClientNIO {

	public static void client() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		SocketChannel socketChannel = null;
		try {
			//打开SocketChannel 绑定客户端本地地址
			socketChannel = SocketChannel.open();
			//设置SocketChannel为非阻塞模式
			socketChannel.configureBlocking(false);
			//异步连接服务端
			socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
			

			//判断是否连接成功
			if (socketChannel.finishConnect()) {
				
				int i = 0;
				while (true) {
					TimeUnit.SECONDS.sleep(1);
					String info = "I'm " + i++ + "-th information from client";
					buffer.clear();
					buffer.put(info.getBytes());
					buffer.flip();
					while (buffer.hasRemaining()) {
						System.out.println(buffer);
						socketChannel.write(buffer);
					}
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socketChannel != null) {
					socketChannel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		client();
	}
}
