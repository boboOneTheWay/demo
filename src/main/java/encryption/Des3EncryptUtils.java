package encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
/**
 * 3DES加密工具类
 */
public class Des3EncryptUtils {
    /** 密钥 */
    private SecretKey securekey;
    /**
     * 功能:算法中需要通过秘钥的字节数组来得到加密用的key
     * @param key  秘钥的字节数组
     */
    public void setKey(byte[] key) {
        try {
            DESedeKeySpec dks = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            securekey = keyFactory.generateSecret(dks);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * 功能：对明文进行加密
     * @param byteS  明文对应的字节数组
     * @return 密文对应的字节数组
     */
    public byte[] get3DesEncCode(byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DESede/ECB/NoPadding");    //算法/分组模式/填充模式
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            cipher = null;
        }
        return byteFina;
    }
    /**
     * 功能：对密文进行解密
     * @param byteD  密文对应的字节数组
     * @return 明文对应的字节数组
     */
    public byte[] get3DesDesCode(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            cipher = null;
        }
        return byteFina;
    }
}
