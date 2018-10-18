package mima;

public class DESAlgorithm {
	private byte[] key;

	public byte[] encrypt(byte[] plainMsg) {
		byte[] perMsg;
		if (plainMsg.length % 8 == 0)
			perMsg = plainMsg;
		else
			perMsg = perDo(plainMsg);
		byte[] encrypted = new byte[8];
		for (int i = 0; i < perMsg.length / 8; i++) {
			System.arraycopy(perMsg, i * 8, encrypted, 0, 8);
			encrypted = encryptMainProcess(encrypted, key, true);
			System.arraycopy(encrypted, 0, perMsg, i * 8, 8);
		}
		return perMsg;
	}

	public byte[] decrypt(byte[] decryptMsg) {
		byte[] perMsg = decryptMsg;
		byte[] decrypted = new byte[8];
		for (int i = 0; i < decryptMsg.length / 8; i++) {
			System.arraycopy(perMsg, i * 8, decrypted, 0, 8);
			decrypted = encryptMainProcess(decrypted, key, false);
			System.arraycopy(decrypted, 0, perMsg, i * 8, 8);
		}
		return clearPadding(perMsg);
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	// 加密主过程
	private byte[] encryptMainProcess(byte[] plainMsg, byte[] key,
			boolean isEncrypted) {
		char[] result = replace_ip(ToBinaryString(plainMsg));
		char[] leftMsg = new char[32];
		char[] rightMsg = new char[32];
		char[] tempLeft = new char[32];
		String[] subkey = getSubKey(key);
		String useSubkey;
		System.arraycopy(result, 0, leftMsg, 0, 32);
		System.arraycopy(result, 32, rightMsg, 0, 32);
		for (int i = 0; i < 16; i++) {
			System.arraycopy(leftMsg, 0, tempLeft, 0, 32);
			System.arraycopy(rightMsg, 0, leftMsg, 0, 32);
			if (isEncrypted)
				useSubkey = subkey[i];
			else
				useSubkey = subkey[15 - i];
			rightMsg = xor(tempLeft, fProcess(extend_E(new String(rightMsg)),
					useSubkey.toCharArray()));
		}
		System.arraycopy(rightMsg, 0, result, 0, 32);
		System.arraycopy(leftMsg, 0, result, 32, 32);

		return toBinary(new String(replace_ip_1(new String(result))));
	}

	// 明文的初始置换
	private char[] replace_ip(String binaryStr) {
		char[] result_ip = new char[64];
		for (int i = 0; i < result_ip.length; i++) {
			result_ip[i] = binaryStr.charAt(DesHelper.IP[i] - 1);
		}
		return result_ip;
	}

	// 密文的最终置换
	private char[] replace_ip_1(String binaryStr) {
		char[] result_ip_1 = new char[64];
		for (int i = 0; i < result_ip_1.length; i++) {
			result_ip_1[i] = binaryStr.charAt(DesHelper.IP_1[i] - 1);
		}
		return result_ip_1;
	}

	// 用于产生迭代运算中的子密钥
	private String[] getSubKey(byte[] key) {
		String[] subKey = new String[16];
		String keyStr = ToBinaryString(key);
		char[] keyAfterA = this.KeyReplaceA(keyStr);
		char[] keyLeft28 = new char[28];
		char[] keyRight28 = new char[28];
		System.arraycopy(keyAfterA, 0, keyLeft28, 0, 28);
		System.arraycopy(keyAfterA, 28, keyRight28, 0, 28);
		for (int i = 0; i < 16; i++) {
			ratoteLeft(keyLeft28, DesHelper.loopLeftMove[i]);
			ratoteLeft(keyRight28, DesHelper.loopLeftMove[i]);
			char[] keyAfterB = keyReplaceB(String.copyValueOf(keyLeft28)
					+ String.copyValueOf(keyRight28));
			subKey[i] = String.copyValueOf(keyAfterB);
		}
		return subKey;
	}

	private char[] KeyReplaceA(String keyStr) {
		char[] keyArray56 = new char[56];
		for (int i = 0; i < 56; i++) {
			keyArray56[i] = keyStr.charAt(DesHelper.KeyReplaceA[i] - 1);
		}
		return keyArray56;
	}

	private void ratoteLeft(char[] src, int times) {
		char[] copy = new char[src.length];
		System.arraycopy(src, 0, copy, 0, src.length);
		for (int i = 0; i < src.length; i++) {
			src[i] = copy[(i + times) % src.length];
		}
	}

	private char[] keyReplaceB(String keyStr) {
		char[] keyArray48 = new char[48];
		for (int i = 0; i < 48; i++) {
			keyArray48[i] = keyStr.charAt(DesHelper.keyReplaceB[i] - 1);
		}
		return keyArray48;
	}

	// s-盒处理过程
	private char[] extend_E(String src) {
		char[] extended = new char[48];
		for (int i = 0; i < 48; i++) {
			extended[i] = src.charAt(DesHelper.extendSecret[i] - 1);
		}
		return extended;
	}

	private char[] compass_P(String src) {
		char[] compassed = new char[32];
		for (int i = 0; i < 32; i++) {
			compassed[i] = src.charAt(DesHelper.compassSecret[i] - 1);
		}
		return compassed;
	}

	private char[] sBoxProcess(char[] xorResult) {
		StringBuffer sStrBuf = new StringBuffer();
		String xorResultStr = new String(xorResult);
		String binaryStr;
		int xPos, yPos, value;
		for (int i = 0; i < 8; i++) {
			char[] s = xorResultStr.substring(i * 6, (i + 1) * 6).toCharArray();
			xPos = (s[5] == '1' ? 1 : 0) * (int) Math.pow(2, 0)
					+ (s[0] == '1' ? 1 : 0) * (int) Math.pow(2, 1);
			yPos = (s[4] == '1' ? 1 : 0) * (int) Math.pow(2, 0)
					+ (s[3] == '1' ? 1 : 0) * (int) Math.pow(2, 1)
					+ (s[2] == '1' ? 1 : 0) * (int) Math.pow(2, 2)
					+ (s[1] == '1' ? 1 : 0) * (int) Math.pow(2, 3);
			value = DesHelper.S_Box[i][xPos][yPos];
			binaryStr = ToBinaryString(new byte[] { (byte) value });
			sStrBuf.append(binaryStr.substring(binaryStr.length() - 4,
					binaryStr.length()));
		}
		return sStrBuf.toString().toCharArray();
	}

	// f函数
	private char[] fProcess(char[] midSecret, char[] subey) {
		char[] result = sBoxProcess(xor(midSecret, subey));
		return compass_P(new String(result));
	}

	// 二进制字符串转化成byte数组
	private byte[] toBinary(String binaryString) {
		int bytes = binaryString.length() / 8;
		byte[] arrayByte = new byte[bytes];
		String byteStr;
		for (int i = 0; i < bytes; i++) {
			byteStr = binaryString.substring(i * 8, (i + 1) * 8);
			byte s = 0;
			for (int j = 0; j < 8; j++) {
				if (byteStr.charAt(j) == '1')
					s += Math.pow(2, 7 - j);
			}
			arrayByte[i] = (byte) ((s <= 127) ? s : (s - 256));
		}
		return arrayByte;

	}

	// 字节数组转化为字符串
	private String ToBinaryString(byte[] binary) {
		StringBuffer strBuf = new StringBuffer();
		String intStr;
		for (int i = 0; i < binary.length; i++) {
			intStr = Integer.toBinaryString(binary[i]);
			if (intStr.length() == 8)
				strBuf.append(intStr);
			else if (intStr.length() > 8) {
				strBuf.append(intStr.substring(intStr.length() - 8, intStr
						.length()));
			} else {
				char[] padding = new char[8 - intStr.length()];
				for (int j = 0; j < padding.length; j++)
					padding[j] = '0';
				strBuf.append(padding);
				strBuf.append(intStr);
			}
		}
		return strBuf.toString();
	}

	// 字符数组模拟异或运算
	private char[] xor(char[] byteArrayA, char[] byteArrayB) {
		if (byteArrayA.length != byteArrayB.length)
			return null;
		else {
			char[] xorResult = new char[byteArrayA.length];
			for (int i = 0; i < byteArrayA.length; i++) {
				if (byteArrayA[i] == byteArrayB[i])
					xorResult[i] = '0';
				else
					xorResult[i] = '1';
			}
			return xorResult;
		}
	}

	// 如果明文的位数不是64的整数倍要进行扩充,扩充的第一位为1其他位为0
	private byte[] perDo(byte[] plainMsg) {
		int length = plainMsg.length;
		int padding = 0;
		byte[] paddingByte;
		if ((padding = length % 8) != 0)
			padding = 8 - padding;
		paddingByte = new byte[padding];
		paddingByte[0] = -128;
		for (int i = 1; i < padding; i++) {
			paddingByte[i] = 0;
		}
		byte[] doByte = new byte[plainMsg.length + paddingByte.length];
		System.arraycopy(plainMsg, 0, doByte, 0, plainMsg.length);
		System.arraycopy(paddingByte, 0, doByte, plainMsg.length,
				paddingByte.length);
		return doByte;
	}

	private byte[] clearPadding(byte[] plainMsg) {
		int padPos = 0, length = plainMsg.length;
		for (int i = length - 1; i >= 0; i--) {
			if (plainMsg[i] == -128) {
				padPos = i;
				break;
			}
		}
		if (padPos == 0)
			return plainMsg;
		else {
			byte[] data = new byte[padPos];
			System.arraycopy(plainMsg, 0, data, 0, padPos);
			return data;
		}
	}
}
