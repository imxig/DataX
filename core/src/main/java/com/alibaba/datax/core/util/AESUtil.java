package com.alibaba.datax.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * aes加解密
 */
public final class AESUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorRecordChecker.class);

    public static final String CBC_PADDING_5_ALGO = "AES/CBC/PKCS5Padding";

    public static String encrypt(String data, String key, String algo, String offset){
        try {
            // 判断Key是否正确
            if (key == null) {
                LOG.warn("本次aes加密的key为null");
                throw new RuntimeException("aes加密失败,key不能为null");
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                LOG.warn("本次aes加密传入的key长度不合法");
                throw new RuntimeException("aes加密失败,key长度不为16");
            }
            byte[] raw = key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(algo);
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypt = cipher.doFinal(data.getBytes("UTF-8"));
            return byteToHex(encrypt);
        } catch (Exception e) {
            LOG.error("aes加密失败");
            throw new RuntimeException("aes加密失败");
        }

    }

    /**
     * 解密
     *
     * @return
     */
    public static String decrypt(String data, String key, String algo, String offset) throws Exception {
        try {
            // 判断Key是否正确
            if (key == null) {
                LOG.warn("本次aes解密的key为null");
                throw new RuntimeException("aes解密失败,key不能为null");
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                LOG.warn("本次aes解密传入的key长度不合法");
                throw new RuntimeException("aes解密失败,key长度不为16");
            }
            byte[] raw = key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(algo);
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted = hexToBytes(data);//hex转化
            try {
                byte[] original = cipher.doFinal(encrypted);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                LOG.error("aes解密失败");
                throw new RuntimeException("aes解密失败");
            }
        } catch (Exception ex) {
            LOG.error("aes解密失败");
            throw new RuntimeException("aes解密失败");
        }
    }

    /**
     * 解密
     *
     * @return
     */
    public static String decrypt(String data, String key) throws Exception {
        return decrypt(data, key, CBC_PADDING_5_ALGO, key);
    }

    public static String byteToHex(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    public static byte[] hexToBytes(String str) {
        str = str.toUpperCase();
        int len = str.length() / 2;
        int ii = 0;
        byte[] bs = new byte[len];
        char c;
        int h;
        for (int i = 0; i < len; i++) {
            c = str.charAt(ii++);
            if (c <= '9') {
                h = c - '0';
            } else {
                h = c - 'A' + 10;
            }
            h <<= 4;
            c = str.charAt(ii++);
            if (c <= '9') {
                h |= c - '0';
            } else {
                h |= c - 'A' + 10;
            }
            bs[i] = (byte) h;
        }
        return bs;
    }

    public static void main(String[] args) {
        String encrypt = AESUtil.encrypt("测试加解密", "yghygh1234567890", AESUtil.CBC_PADDING_5_ALGO, "yghygh1234567890");
        System.out.println(encrypt);
        String result = null;
        try {
            result = AESUtil.decrypt("52F4306AA6941C00911A01A8817F9B846FEF359C8752A9AF60DFDB02350002D627F1D74EADD8F791B0CF62206D6D4252", "yghygh1234567890", AESUtil.CBC_PADDING_5_ALGO, "yghygh1234567890");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}

