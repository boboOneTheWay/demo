package netty.javaNIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServerNIO {
	private static final int BUF_SIZE = 1024;
	private static final int PORT = 8080;
	private static final int TIMEOUT = 3000;

	public static void main(String[] args) {
		selector();
	}

	public static void handleAccept(SelectionKey key) throws IOException {
		ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
		//处理新接入请求，完成TCP三次握手，建立物理链路
		SocketChannel sc = ssChannel.accept();
		//设置非阻塞模式
		sc.configureBlocking(false);
		//将新接入的客户端链接注册到Rector线程的多路复用器上，监听读操作，读取客户端发送的网络信息
		sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(BUF_SIZE));
	}

	public static void handleRead(SelectionKey key) throws IOException {
		SocketChannel sc = (SocketChannel) key.channel();
		ByteBuffer buf = (ByteBuffer) key.attachment();
		long bytesRead = sc.read(buf);
		while (bytesRead > 0) {
			buf.flip();
			while (buf.hasRemaining()) {
				System.out.print((char) buf.get());
			}
			System.out.println();
			buf.clear();
			bytesRead = sc.read(buf);
		}
		if (bytesRead == -1) {
			sc.close();
		}
	}

	public static void handleWrite(SelectionKey key) throws IOException {
		ByteBuffer buf = (ByteBuffer) key.attachment();
		buf.flip();
		SocketChannel sc = (SocketChannel) key.channel();
		while (buf.hasRemaining()) {
			sc.write(buf);
		}
		buf.compact();
	}

	public static void selector() {
		Selector selector = null;
		ServerSocketChannel ssc = null;
		try {
			selector = Selector.open();
			//打开ServerSocketChannel，用于监听客户端链接，它是所有客户端链接的父管道
			ssc = ServerSocketChannel.open();
			//绑定监听端口
			ssc.socket().bind(new InetSocketAddress(PORT));
			//设置为非阻塞模式
			ssc.configureBlocking(false);
			//将ServerSocketChannel 注册到 多路复用器selector上，监听accept区别
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			//多路复用器在线run方法里无限循环体内准备就绪的key
			while (true) {
				if (selector.select(TIMEOUT) == 0) {
					System.out.println("==");
					continue;
				}
				Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					//有新的客户端接入
					if (key.isAcceptable()) {
						handleAccept(key);
					}
					//异步读取客户端信息到缓存区
					if (key.isReadable()) {
						handleRead(key);
					}
					//将对象encode成ByteBuffer，调用SocketChannel的异步write接口
					if (key.isWritable() && key.isValid()) {
						handleWrite(key);
					}
					if (key.isConnectable()) {
						System.out.println("isConnectable = true");
					}
					iter.remove();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (selector != null) {
					selector.close();
				}
				if (ssc != null) {
					ssc.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
