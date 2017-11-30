package netty.privateProtocolStack;

public enum MessageType {
	BUSINESS_REQ((byte) 0), BUSINESS_RESP((byte) 1), BUSINESS_REQ_RESP((byte) 2),
	HANDSHAKE_REQ((byte) 3), HANDSHAKE_RESP((byte) 4), HEARTBEAT_REQ((byte) 5), HEARTBEAT_RESP((byte) 6);
	
	private byte type;

	/**
	 *4.type：类型Byte，长度为8
	 * 			0:业务请求消息
	 * 			1:业务响应消息
	 * 			2.业务ONE WAY消息（即是请求又是响应消息）
	 * 			3.握手请求信息
	 * 			4.握手应答消息
	 * 			5.心跳请求消息
	 * 			6.心跳应答消息
	 */
	private MessageType(byte type) {
		this.type = type;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public static MessageType getMessageType(byte type) {
		for (MessageType b : MessageType.values()) {
			if (b.getType() == type) {
				return b;
			}
		}
		return null;
	}

}
