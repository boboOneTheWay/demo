package netty.protobuf.proto;

import com.google.protobuf.InvalidProtocolBufferException;

import netty.protobuf.SubscribeReqProto;
import netty.protobuf.SubscribeReqProto.SubscribeReq;

public class TestSubscribeReqProto {

	private 	static byte[] encode(SubscribeReqProto.SubscribeReq req) {
		return req.toByteArray();
	}
	
	private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
		return SubscribeReqProto.SubscribeReq.parseFrom(body);
	}

	private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
		SubscribeReqProto.SubscribeReq.Builder build = SubscribeReqProto.SubscribeReq.newBuilder();
		build.setSubReqID(1);
		build.setUserName("LiLei");
		build.setProductName("netty book");
		build.setAddress("lanzhou");
		return build.build();
	}
	
	public static void main(String[] args) throws InvalidProtocolBufferException {
		SubscribeReq req = createSubscribeReq();
		System.out.println("before : " + req.toString());
		for(byte b : encode(req)) {
			System.out.println(b);
		}
		SubscribeReq req2 = decode(encode(req));
		System.out.println("after : " + req.toString());
		System.out.println(req2.equals(req));
		
	}
}
