package netty.decoder.DelimiterBasedFrameDecoderTest;

import org.apache.log4j.Logger;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeClientHandler extends ChannelHandlerAdapter {

	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

	//private final ByteBuf firstMessage;
	
	private int count;
	

	static final String ECHO_REQ = "HI man,Hello man.$_";
	public TimeClientHandler() {
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(int i = 0; i<=100; i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("body is : " + body + "; count:" + ++count);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("释放资源" + cause.getMessage());
		ctx.close();
	}

}
