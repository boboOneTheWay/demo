//package secrit.cashway.md;

/**
 * @author 作者 高江萍 E-mail:
 * @version 创建时间：Oct 26, 2011 10:29:13 AM
 * 类说明
 */

package mima;
/**
 * DES单倍长度-加密解密
 * @author Administrator
 *
 */
public class DesTwo {
	//private static String strDefaultKey = "1234567887654321"; // 默认密钥
	
	private static String DEFAULT_BUMA="F";

	/**
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
	 * hexStr2ByteArr(String strIn) 互为可逆的转换过程
	 * 
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
				
		//////////////////////////////////////////////
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 * 
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	/**
	 * 指定密钥构造方法
	 * 
	 * @param strKey
	 *            指定的密钥
	 *        pwd 需要加密数据
	 * @throws Exception
	 */
	public String desEncrypt(String strKey,String pwd) throws Exception {
		
//		 String key="a2345678";
//		 String key1="88654321";
		 //String msg="92345678";
		 if(strKey.length() !=16){
			 for(int i=0;i<16;i++){
				 strKey=strKey+DEFAULT_BUMA;
			 }
		 }
		 
		 String key =strKey.substring(0, 8);
		 String key1 = strKey.substring(8, strKey.length());
		 
		 
		 String msg =pwd;
		 if(pwd !=null || pwd.length() > 0){
			 if(msg.length() >8){
				 msg.substring(0, 16);
			 }else if(msg.length() <8){
				 for(int j=0;j<8;j++){
					 msg=msg+DEFAULT_BUMA;
				 }
			 }
		 }
				 
//		 System.out.println("十六进制key1：："+DesTwo.byteArr2HexStr(key.getBytes()));
//		 System.out.println("十六进制key2：："+DesTwo.byteArr2HexStr(key1.getBytes()));
//		 System.out.println("明文："+DesTwo.byteArr2HexStr(msg.getBytes()));


		 DESAlgorithm des=new DESAlgorithm();
		 des.setKey(key.getBytes());
		 
		  // System.out.println("加密--------------------------");
//		   byte[] ee= des.encrypt(msg.getBytes());
		 
		 byte[] ee= des.encrypt(hexStr2ByteArr(msg));//压缩
		  // System.out.println("十六进制加密结果：：："+DesTwo.byteArr2HexStr(ee));
		   
		   //解密
		  // System.out.println("解密--------------------------");
		   des.setKey(key1.getBytes());
		   byte[] dd=des.decrypt(ee);
		  // System.out.println("十六进制加密结果：：："+byteArr2HexStr(dd));		   
		   //加密
		   des.setKey(key.getBytes());
		   byte[] result= des.encrypt(dd);
		  // System.out.println("字符串：：："+new String(ee));
//		   for(int n=0;n<result.length;n++){
//			   System.out.println(result[n]+"--");
//		   }
		  /// System.out.println("十六进制加密结果：：："+byteArr2HexStr(result));
		   
		//   byte [] b2 = hexStr2ByteArr(byteArr2HexStr(result));
//		   for(int b=0;b<b2.length;b++){
//			   System.out.println("++"+b2[b]);
//		   }
		   
		return byteArr2HexStr(result);
	}
	
	
	/**
	 * 
	 * @param strKey 密钥
	 * @param result 要解密的数据
	 * @throws Exception
	 */
	public String desDecrypt(String strKey,byte[] result) throws Exception {
		
		 if(strKey.length() !=16){
			 for(int i=0;i<16;i++){
				 strKey=strKey+DEFAULT_BUMA;
			 }
		 }
		 
		 String key =strKey.substring(0, 8);
		 String key1 = strKey.substring(8, strKey.length());
		 
//				 
//		 System.out.println("十六进制key1：："+DesTwo.byteArr2HexStr(key.getBytes()));
//		 System.out.println("十六进制key2：："+DesTwo.byteArr2HexStr(key1.getBytes()));
//		 System.out.println("密文：：："+DesTwo.byteArr2HexStr(result));

		
		 DESAlgorithm des=new DESAlgorithm();

		 //解密
		   des.setKey(key.getBytes());
		   byte ji1[] = des.decrypt(result);

		   //加密
		   des.setKey(key1.getBytes());
		   byte jia1[] = des.encrypt(ji1);

		   
		   //解密
		   des.setKey(key.getBytes());
		   byte ji2[] = des.decrypt(jia1);
		   
//		   System.out.println("十六进制解密结果：：："+DesTwo.byteArr2HexStr(ji1));
//		   System.out.println("十六进制加密结果：：："+DesTwo.byteArr2HexStr(jia1));
//
//		   System.out.println("解密结果：："+new String(ji2));
//		   System.out.println("十六进制加密结果：：："+DesTwo.byteArr2HexStr(ji2));
		   return byteArr2HexStr(ji2);
		   
		   //return new String(ji2);
	}
	

	public static void main(String[] args) throws Exception{
		
		String key="1111111111111111";
		 String msg="06091765FFFFFFFF";
		 
		 DesTwo des = new DesTwo();
		 System.out.println("加密结果----end：："+des.desEncrypt(key, msg));
		 
		 byte by[] = des.hexStr2ByteArr(des.desEncrypt(key, msg));
		System.out.println("解密结果----end::::"+des.desDecrypt(key, by));
	

		
	}
}




