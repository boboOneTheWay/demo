package netty.decoder.DelimiterBasedFrameDecoderTest;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeServerHandler extends ChannelHandlerAdapter {
	
	private int count;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

		String body  = (String)msg;
		System.out.println("time recive order: " + body + ",count : " + count);
		System.out.println("body is : " + body + "; count : " + ++count);
		body += "$_";
		ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
		
//		String currentTime = "Query time order".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"bad Order";
//		currentTime	= currentTime + System.getProperty("line.separator");
//		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(echo);
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
