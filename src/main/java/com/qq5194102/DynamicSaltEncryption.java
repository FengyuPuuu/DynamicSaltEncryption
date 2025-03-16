package com.qq5194102;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 基于saltKey(可以是userId 可以是username...)动态加盐加密
 *
 * @author caiyh
 * @since 2025/3/17
 */
public class DynamicSaltEncryption {
    private static final String DEFAULT_ALGORITHM = "SHA-256";
    /**
     * 生成盐值（基于 saltKey）
     *
     * @param saltKey 盐值生成的关键字（如 userId 或 username）
     * @return 生成的盐值
     */
    public static String generateSalt(String saltKey) {
        return String.valueOf(Math.abs(saltKey.hashCode()));
    }

    /**
     * 加盐并加密
     *
     * @param value     需要加密的值
     * @param saltKey   盐值生成的关键字
     * @param algorithm 加密算法（如 SHA-256）
     * @return 加密后的值
     * @throws NoSuchAlgorithmException 如果指定的加密算法不存在
     */
    public static String encryptValue(String value, String saltKey, String algorithm) throws NoSuchAlgorithmException, NoSuchAlgorithmException {
        String salt = generateSalt(saltKey);
        System.out.println("salt:"+salt);
        StringBuilder saltedValue = new StringBuilder(value);
        // 将盐值的每一位插入到密码中
        for (int i = 0; i < salt.length(); i++) {
            int insertIndex = salt.charAt(salt.length()-i-1) - '0'; // 取模确定插入位置
            if (insertIndex < 0 || insertIndex >= saltedValue.length()) {
                // 如果取模后的位置越界，则插入到末尾
                saltedValue.append(salt.charAt(i));
            } else {
                // 否则插入到指定位置
                saltedValue.insert(insertIndex, salt.charAt(i));
            }
        }
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(saltedValue.toString().getBytes());
        byte[] digest = md.digest();

        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    /**
     * 加盐并加密（使用默认算法 SHA-256）
     *
     * @param value   需要加密的值
     * @param saltKey 盐值生成的关键字
     * @return 加密后的值
     * @throws NoSuchAlgorithmException 如果指定的加密算法不存在
     */
    public static String encryptValue(String value, String saltKey) throws NoSuchAlgorithmException, NoSuchAlgorithmException {
        return encryptValue(value,saltKey,DEFAULT_ALGORITHM);
    }

    /**
     * 验证值是否匹配
     *
     * @param value          原始值
     * @param encryptedValue 加密后的值
     * @param saltKey        盐值生成的关键字
     * @param algorithm      加密算法
     * @return 如果匹配返回 true，否则返回 false
     * @throws NoSuchAlgorithmException 如果指定的加密算法不存在
     */
    public static boolean verifyValue(String value, String encryptedValue, String saltKey, String algorithm) throws NoSuchAlgorithmException {
        return encryptValue(value, saltKey, algorithm).equals(encryptedValue);
    }
    /**
     * 验证值是否匹配（使用默认算法 SHA-256）
     *
     * @param value          原始值
     * @param encryptedValue 加密后的值
     * @param saltKey        盐值生成的关键字
     * @return 如果匹配返回 true，否则返回 false
     * @throws NoSuchAlgorithmException 如果指定的加密算法不存在
     */
    public static boolean verifyValue(String value, String encryptedValue, String saltKey) throws NoSuchAlgorithmException {
        return verifyValue(value,encryptedValue,saltKey,DEFAULT_ALGORITHM);
    }

}
