/**
 * 
 */
package com.jotunheim.mimir.common.utils;

import java.security.InvalidKeyException;

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
	static {
		try {
			DESKeySpec dks = new DESKeySpec(DES_KEY.getBytes());
			SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
			desKey = skf.generateSecret(dks);
			cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE
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
}
