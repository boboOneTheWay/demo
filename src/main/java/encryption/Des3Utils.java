package encryption;

/**
 * 3DES加解密主类，加解密调用内部的方法
 *
 */
public class Des3Utils {
    /**
     * dec:(解密).
     * @param key 密钥
     * @param content 密文内容 16位
     * @return 返回结果：String
     */
    public static String decryption(String key, String content) {
        Des3EncryptUtils des = new Des3EncryptUtils();
        String enKey = "";//最终解密秘钥 48位
        String enContent = content;//解密内容
        if(key.length() <= 32){
            enKey = (key + key).substring(0, 48);
        }else if(key.length() >= 48){
            enKey = key.substring(0, 48);
        }
//        if(content.length() == 16){
//            enContent = content;
//        }else{
//            if(content.length() > 16){
//                throw new RuntimeException("the encrypt content length more than 16");
//            }else if(content.length() < 16){
//                throw new RuntimeException("the encrypt content length less than 16");
//            }
//        }
        des.setKey(enKey.getBytes());
        byte[] get3DesDesCode = des.get3DesDesCode(HexUtils.fromString(enContent));
        return HexUtils.toString(get3DesDesCode).trim();
    }


    /**
     * dec:(加密).
     * @param key 密钥
     * @param content  明文内容 为16位十六进制字符串
     * @return 返回结果：String
     */
    public static String encryption(String key, String content) {
        Des3EncryptUtils des = new Des3EncryptUtils();
        String enKey = "";//最终加密秘钥48位
        String enContent = content;//加密内容
        if(key.length() <= 32){
            enKey = (key + key).substring(0, 48);
        }else if(key.length() >= 48){
            enKey = key.substring(0, 48);
        }
//        if(content.length() == 16){
//            enContent = content;
//        }else{
//            if(content.length() > 16){
//                throw new RuntimeException("the encrypt content length more than 16");
//            }else if(content.length() < 16){
//                throw new RuntimeException("the encrypt content length less than 16");
//            }
//        }
        des.setKey(enKey.getBytes());
        byte[] bye = des.get3DesEncCode(HexUtils.fromString(enContent));
        return HexUtils.toString(bye).trim();
    }

    public static void main(String[] args) {
        String str = encryption("10DBCB016838B168F765B62BA6014D06", "06111111FFFFFFFF");
        System.out.println("加密后的密文为====="+str);
        String s = HexUtils.toString("gfpsHCbC5ANB2EW+ZDQ49g".getBytes());
        decryption("10DBCB016838B168F765B62BA6014D06",s);
    }
}