/**
 * 
 */
package com.jotunheim.mimir.common.utils;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.springframework.security.crypto.codec.Base64;

import sun.misc.BASE64Encoder;

/**
 * @author dannyzha
 *
 */
public final class CipherHelper {
    private static final String DES_KEY = "zyt_des_key";
    private static SecretKey desKey;
    private static Cipher cipher;
    private static MessageDigest md5Instance;

    static {
        try {
            DESKeySpec dks = new DESKeySpec(DES_KEY.getBytes());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            desKey = skf.generateSecret(dks);
            cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for
                                                // SunJCE

            md5Instance = MessageDigest.getInstance("MD5");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static final String byte2hexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static String md5sum(String input) {
        byte[] buff = md5Instance.digest(input.getBytes());
        return byte2hexString(buff);
    }

    public static String encrypt(String input) {
        return encryptOrDecrypt(input, true);
    }

    public static String decrypt(String input) {
        return encryptOrDecrypt(input, false);
    }

    public static String encryptWithKey(String source, String key) {
        try {
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            SecureRandom random = new SecureRandom();
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            BASE64Encoder base64 = new BASE64Encoder();
            String result = base64.encode(cipher.doFinal(source.getBytes()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String encryptOrDecrypt(String input, boolean isEncrypt) {
        String result = input;
        try {
            if (isEncrypt) {
                cipher.init(Cipher.ENCRYPT_MODE, desKey);
                byte[] resultBytes = cipher.doFinal(input.getBytes());
                result = new String(Base64.encode(resultBytes));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, desKey);
                byte[] inputBytes = Base64.decode(input.getBytes());
                byte[] resultBytes = cipher.doFinal(inputBytes);
                result = new String(resultBytes);
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String MD5(String input) {
        String result = input;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = bytesToHex(md.digest(input.getBytes()));
        } catch(Exception ex) {
            ex.printStackTrace();
            result = input;
        }
        return result;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];
            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString();
    }

    public static void main(String[] args) {
        String text = "yzy-zl-123456";
        System.out.println(md5sum(text));
    }
}
