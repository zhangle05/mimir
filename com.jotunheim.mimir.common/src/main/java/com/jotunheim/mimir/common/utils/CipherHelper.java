/**
 * 
 */
package com.jotunheim.mimir.common.utils;

import java.security.InvalidKeyException;
import java.security.MessageDigest;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.springframework.security.crypto.codec.Base64;

/**
 * @author dannyzha
 *
 */
public final class CipherHelper {
    private static final String DES_KEY = "mimir_des_key";
    private static SecretKey desKey;
    private static Cipher cipher;
    private static MessageDigest md5Instance;
    static {
        try {
            DESKeySpec dks = new DESKeySpec(DES_KEY.getBytes());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            desKey = skf.generateSecret(dks);
            cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE
            md5Instance = MessageDigest.getInstance("MD5");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static String encrypt(String input) {
        return encryptOrDecrypt(input, true);
    }
    
    public static String decrypt(String input) {
        return encryptOrDecrypt(input, false);
    }
    
    private static String encryptOrDecrypt(String input, boolean isEncrypt) {
        String result = input;
        try {
            if(isEncrypt) {
                cipher.init(Cipher.ENCRYPT_MODE, desKey);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, desKey);
            }
            byte[] resultByte = cipher.doFinal(input.getBytes());
            result = new String(Base64.encode(resultByte));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return result;
    }
    

    public static String md5sum(String input) {
        byte[] buff = md5Instance.digest(input.getBytes());
        return byte2hexString(buff);
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
}
