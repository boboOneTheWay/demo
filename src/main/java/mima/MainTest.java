package mima;
/**
 * @author 作者 高江萍 E-mail:
 * @version 创建时间：Nov 1, 2011 12:59:09 PM
 * 类说明
 */
public class MainTest {
	public static void main(String[] args) {
		try{
			
		//AC6AD56DC
		//	0030(0048)   46 34 33 44 30 39 36 43 52                        F43D096C
		 String key="12345678";
		 String key1="12345678";
		 
		 String msg="06091765";
		 
		 System.out.println("十六进制key1：："+DesTwo.byteArr2HexStr(key.getBytes()));
		 System.out.println("十六进制key2：："+DesTwo.byteArr2HexStr(key1.getBytes()));
		 System.out.println("明文："+DesTwo.byteArr2HexStr(msg.getBytes()));


		 DESAlgorithm des=new DESAlgorithm();
		 des.setKey(key.getBytes());
		 
		   System.out.println("加密--------------------------");
		   byte[] ee= des.encrypt(msg.getBytes());
		   System.out.println("十六进制加密结果：：："+DesTwo.byteArr2HexStr(ee));
		   
		   //解密
		   System.out.println("解密--------------------------");
		   des.setKey(key1.getBytes());
		   byte[] dd=des.decrypt(ee);
		   System.out.println("十六进制加密结果：：："+DesTwo.byteArr2HexStr(dd));		   
		   //加密
		   des.setKey(key.getBytes());
		   byte[] result= des.encrypt(dd);
		   System.out.println("十六进制加密结果：：："+DesTwo.byteArr2HexStr(result));
		   
		   
		   System.out.println("======================解3des===================================");
		   //解密
		   des.setKey(key.getBytes());
		   byte ji1[] = des.decrypt(result);
		   System.out.println("十六进制加密结果：：："+DesTwo.byteArr2HexStr(ji1));

		   //加密
		   des.setKey(key1.getBytes());
		   byte jia1[] = des.encrypt(ji1);
		   System.out.println("十六进制加密结果：：："+DesTwo.byteArr2HexStr(jia1));

		   
		   //解密
		   des.setKey(key.getBytes());
		   byte ji2[] = des.decrypt(jia1);
		   System.out.println("解密结果：："+new String(ji2));
		   System.out.println("十六进制加密结果：：："+DesTwo.byteArr2HexStr(ji2));

		   
		   
		   
		   
		   
		   
		   
		}catch (Exception e) {
			// TODO: handle exception
		}
		  
		
	}
	
	
	 

}



