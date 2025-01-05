package com.soundmentor.soundmentorbase.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加密工具类
 * @Author: Make
 * @DATE: 2025/01/05
 **/
public class AESUtil {

    // 加密算法
    private static final String ALGORITHM = "AES";
    // 加密模式和填充方式
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    /**
     * 生成加密密钥
     *
     * @return 密钥的Base64编码字符串
     * @throws Exception 异常
     */
    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128, new SecureRandom());
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * AES加密
     *
     * @param plainText 明文
     * @param key       密钥，Base64编码字符串
     * @return 加密后的Base64编码字符串
     * @throws Exception 异常
     */
    public static String encrypt(String plainText, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * AES解密
     *
     * @param encryptedText 加密文本，Base64编码字符串
     * @param key           密钥，Base64编码字符串
     * @return 解密后的明文
     * @throws Exception 异常
     */
    public static String decrypt(String encryptedText, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(encryptedText), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(key));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}