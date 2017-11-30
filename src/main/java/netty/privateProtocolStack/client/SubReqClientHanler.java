package netty.privateProtocolStack.client;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import netty.nettyNIO.TimeClientHandler;

public class SubReqClientHanler extends ChannelHandlerAdapter{

	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

	//private final ByteBuf firstMessage;
	
	private int count;
	
	private byte[] req;

	public SubReqClientHanler() {
		req = ("Query time order" + System.getProperty("line.separator")).getBytes();
//		firstMessage = Unpooled.buffer(req.length);
//		firstMessage.writeBytes(req);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf message = null;
		for(int i = 0; i<=100; i++) {
			System.out.println("**********channelActive" + i);
			message = Unpooled.buffer();
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
		//ctx.writeAndFlush(firstMessage);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("body is : " + body + "; count:" + ++count);
//		byte[] req = new byte[buf.readableBytes()];
//		buf.readBytes(req);
//		String body = new String(req, "UTF-8");
	}
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("释放资源" + cause.getMessage());
		ctx.close();
	}
}
