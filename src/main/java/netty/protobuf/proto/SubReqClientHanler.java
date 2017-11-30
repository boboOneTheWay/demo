package netty.protobuf.proto;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import netty.protobuf.SubscribeReqProto;

public class SubReqClientHanler extends ChannelHandlerAdapter{

	public SubReqClientHanler() {
		
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       for(int i= 0; i<10; i++) {
    	   		ctx.write(subReq(i));
       }
       
       ctx.flush();
    }
	
	private 	SubscribeReqProto.SubscribeReq subReq(int i) {
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setAddress("重庆");
		builder.setProductName("麻辣烫");
		builder.setSubReqID(i);
		builder.setUserName("lilei");
		return builder.build();
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("*********receive server response **********: " + msg);
    }
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
    }
}
