package com.msgemployee.EmployeeSkillSearchProject.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class LoginUtil {
	
	private static final String AES_KEY = "TOKEN_SECURITY_MOGLIX_AES_KEY_IN_JWT";
	
	public String encrypt(String data) {
		AES aes = new AES(AES_KEY);
		return aes.encrypt(data);
	}

	public String decrypt(String data) {
		AES aes = new AES(AES_KEY);
		return aes.decrypt(data);
	}
	
	private class AES{
		private SecretKeySpec secretKey;
		private byte[] key;
		
		AES(String secret){
			MessageDigest sha = null;
			try {
				key = secret.getBytes(StandardCharsets.ISO_8859_1);
				sha = MessageDigest.getInstance("SHA-1");
				key = sha.digest(key);
				key = Arrays.copyOf(key, 16);
				secretKey = new SecretKeySpec(key, "AES");
			}catch(NoSuchAlgorithmException nse){
				nse.printStackTrace();
			}
		}
		
		String encrypt(String strToEncrypt) {
			try {
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.ISO_8859_1)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		String decrypt(String strToDecrypt) {
			try {
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
