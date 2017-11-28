package netty.decoder.DelimiterBasedFrameDecoderTest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 分隔符码流
 *
 */
public class TimeServer {

	public void bind(int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();

		ServerBootstrap b = new ServerBootstrap();
		try {
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
							ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast(new TimeServerHandler());
							
						}});
			// 绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

	/**
	 * LineBasedFrameDecoder
	 * 原理是依次遍历ByteBuf中的可用字节，判断是否有“\n”或者“\r\n”，如果有就以此位置为结束位置，从可读索引到结束位置就组成可一行。它是以换行符为结束标志的解码器，同时支持配置单行最大长度。
	 * 如果读到最大长度后仍然没有发现换行符就抛出异常，同时忽略之前读到的异常码流。 StringDecoder：
	 * 将接收的对象转换为字符串，然后调用后面的handler
	 *
	 */
//	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
//
//		@Override
//		protected void initChannel(SocketChannel ch) throws Exception {
//			ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
//			ch.pipeline().addLast(new StringDecoder());
//			ch.pipeline().addLast(new TimeServerHandler());
//
//		}
//
//	}

	public static void main(String[] args) {
		try {
			new TimeServer().bind(8080);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
