package netty.protobuf.proto;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import netty.protobuf.SubscribeReqProto;
import netty.protobuf.SubscribeRespProto;

import java.io.UnsupportedEncodingException;

import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class SubReqServerHander extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
		SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq)msg;
		
		if("lilei".equals(req.getUserName())) {
			System.out.println("service accept : " + new String(req.toString().getBytes("UTF-8"), "UTF-8").toString());
			System.out.println("address : " + req.getAddress());
			ctx.writeAndFlush(resp(req.getSubReqID()));
		}
	}
	
	private SubscribeRespProto.SubscribeResp resp(int id) {
		SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
		builder.setSubReqID(id);
		builder.setRespCode(0);
		builder.setDesc("************netty book send***********");
		return builder.build();
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
