package netty.privateProtocolStack.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class SubReqServerHander extends ChannelHandlerAdapter {

	private int count;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

		String body  = (String)msg;
		System.out.println("time recive order: " + body + ",count : " + count);
//		ByteBuf buf = (ByteBuf)msg;
//		byte[] req = new byte[buf.readableBytes()];
//		buf.readBytes(req);
//		String body = new String(req, "UTF-8").substring(0,req.length - System.getProperty("line.separator").length());
		System.out.println("body is : " + body + "; count : " + ++count);
		String currentTime = "Query time order".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"bad Order";
		currentTime	= currentTime + System.getProperty("line.separator");
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(resp);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
