package mima;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

//@SuppressWarnings("restriction")
public class CCBDES {

    private static final Logger logger = LoggerFactory.getLogger(CCBDES.class);
    // basekey
    private static final String BASE_KEY = "333705661205A5E3D950933325240713";

    /**
     * 加密模式
     */
    private static final String EN_MODE = "DES/ECB/NoPadding";

    private static final String CHAR_SET_GBK = "GB18030";

    private static final String CHAR_SET_UTF8 = "UTF-8";

    public static final String CHAR_SET_GB2312 = "GB18030";

    /**
     * 加密函数
     *
     * @param data
     *            加密数据
     * @param key
     *            密钥
     * @return 返回加密后的数据
     */
    private static byte[] ECBencrypt(byte[] data, byte[] key) {

        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(EN_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("加密函数异常", e);
        }

        return null;
    }

    /**
     * 解密函数
     *
     * @param data
     *            解密数据
     * @param key
     *            密钥
     * @return 返回解密后的数据
     */
    private static byte[] ECBdecrypt(byte[] data, byte[] key) {

        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(EN_MODE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("解密失败", e);
        }
        return null;
    }

    /**
     * 3DES加密
     *
     * @param buf
     *            需要加密数据
     * @param key
     *            密匙
     * @return
     */
    private static byte[] encrypt3DESofdouble(byte[] buf, byte[] key) {
        byte[] leftkey = new byte[8];
        byte[] rightkey = new byte[8];
        System.arraycopy(key, 0, leftkey, 0, 8);
        System.arraycopy(key, 8, rightkey, 0, 8);

        return ECBencrypt(ECBdecrypt(ECBencrypt(buf, leftkey), rightkey), leftkey);
    }

    /**
     * 3DES解密
     *
     * @param buf
     *            需要解密的数据
     * @param key
     *            密匙
     * @return
     */
    private static byte[] decrypt3DESofdouble(byte[] buf, byte[] key) {
        byte[] leftkey = new byte[8];
        byte[] rightkey = new byte[8];
        System.arraycopy(key, 0, leftkey, 0, 8);
        System.arraycopy(key, 8, rightkey, 0, 8);
        return ECBdecrypt(ECBencrypt(ECBdecrypt(buf, leftkey), rightkey), leftkey);
    }

    /**
     * 将16进制字符串字节数组转换成字节数组
     *
     * @param bytes
     *            字符串字节数组
     * @param i
     *            字符串字节数组长度
     * @return
     */
    private static byte[] fromHexString(byte bytes[], int i) {
        int j = 0;
        if (bytes[0] == 48 && (bytes[1] == 120 || bytes[1] == 88)) {
            j += 2;
            i -= 2;
        }
        int k = i / 2;
        byte abyte1[] = new byte[k];
        for (int l = 0; l < k;) {
            abyte1[l] = (byte) ((hexValueOf(bytes[j]) << 4 | hexValueOf(bytes[j + 1])) & 0xff);
            l++;
            j += 2;
        }

        return abyte1;
    }

    private static int hexValueOf(int i) {
        if (i >= 48 && i <= 57) {
            return i - 48;
        }
        if (i >= 97 && i <= 102) {
            return (i - 97) + 10;
        }
        if (i >= 65 && i <= 70) {
            return (i - 65) + 10;
        } else {
            return 0;
        }
    }

    /**
     * 将字节数组转换成字符串
     *
     * @param b
     * @return
     */
    private static String printHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 获得工作密钥
     *
     * @return
     */
    //@SuppressWarnings("restriction")
    public static String getWorkKey(String workkey, String base_key) {
        //将字节数组转换成字符串
        String key = printHex(java.util.Base64.getDecoder().decode(workkey));
        //System.out.println(key);
        //将16进制字符串字节数组转换成字节数组
        byte[] workKey = fromHexString(key.getBytes(), 32);
        byte[] baseKey = fromHexString(base_key.getBytes(), 32);
        return printHex(decrypt3DESofdouble(workKey, baseKey));
    }

    /**
     * 解密字符串
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    //@SuppressWarnings("restriction")
    public static String decryptStr(String str, String work_key)
            throws UnsupportedEncodingException {
        byte[] srcStr = Base64.decodeBase64(str);
        String src = printHex(srcStr);
        int len = src.getBytes().length;
        byte[] srcBytes = decrypt3DESofdouble(fromHexString(src.getBytes(), len),
                fromHexString(work_key.getBytes(), 32));
        String result = new String(srcBytes, CHAR_SET_GBK);
        if (result != null && !"".equals(result)) {
            int i = result.lastIndexOf('>');
            if (i > 0) {
                result = result.substring(0, i + 1);
            }
        }
        return result;
    }

    /**
     * 加密字符串
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    //@SuppressWarnings("restriction")
    public static String encryptStr(String str, String workKey) {
        logger.info("加密xml:{} , 工作key:{}", str, workKey);
        byte[] src = null;
        try {
            src = str.getBytes(CHAR_SET_GBK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int len = src.length;
        byte[] ibyte = new byte[len + 8 - len % 8];
        System.arraycopy(src, 0, ibyte, 0, len);
        byte[] key = fromHexString(workKey.getBytes(), 32);
        byte[] enSrc = encrypt3DESofdouble(ibyte, key);
        String srcStr = printHex(enSrc);

        byte[] srcBytes = fromHexString(srcStr.getBytes(), srcStr.getBytes().length);

        String baseEncodeStr = Base64.encodeBase64String(srcBytes);
        return baseEncodeStr.replaceAll("[\\s*\t\n\r]", "");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String workKey = getWorkKey("7xaLb1XOBXQ9uXXEg17KNA==", "333705661205A5E3D950933325240713");
        System.out.println(workKey);

        String s = decryptStr("d11fSU3/z9Zjme+h5h+OpxIpU9ykTL/DL8D8uXN9wpWO3Ds7w7431HYeF/5yqu7aDgo2A4sZdXJOAlMot8/rZvVjwu1a6O+IyW+dfX2DKxTKPZgaDgpDVG5EXc2pA4dm4tmuIL4w5t4=", "612F9785BC91B5325D62D55DEADC7A61");
        System.out.println(s);


    }

}

