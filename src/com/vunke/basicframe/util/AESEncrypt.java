package com.vunke.basicframe.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * ����Ϊ����AES���ܵ��࣬���ڶԱ����û�������ܣ������Ŀ��
 * <p>
 * author: nieming 2011-01-26
 * </p>
 */
public class AESEncrypt {
	// Ĭ�ϵ���Կ��00000000000000000000000000000000
	public static String secret_key = "00000000000000000000000000000000";

	/**
	 * ���ܣ�ת��Ϊ16����
	 * 
	 * @param buf
	 *            [] byte ��Ҫת�����ֽ�����
	 * @return string ת����16���ƺ���ַ�
	 */
	@SuppressWarnings("unused")
	private static String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append("0");

			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}
		System.out.println(strbuf.toString());
		return strbuf.toString();
	}

	/**
	 * ���ܣ����ַ�ת����2��������
	 * 
	 * @param src
	 *            String ��Ҫת�����ַ�
	 * @return byte[] ת�����2��������
	 */
	@SuppressWarnings("unused")
	private static byte[] asBin(String src) {
		if (src.length() < 1)
			return null;
		byte[] encrypted = new byte[src.length() / 2];
		for (int i = 0; i < src.length() / 2; i++) {
			int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);

			encrypted[i] = (byte) (high * 16 + low);
		}
		return encrypted;
	}

	/**
	 * ���ܣ�����
	 * 
	 * @param data
	 *            String ��Ҫ���ܵ��ַ�
	 * @param secret_key
	 *            String ��Կ
	 * @return string ���ؼ��ܺ������
	 */
	public static String encryptAES(String data, String secret_key) {
		byte[] key = asBin(secret_key);
		SecretKeySpec sKey = new SecretKeySpec(key, "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, sKey);
			byte[] encrypted = cipher.doFinal(data.getBytes());

			return asHex(encrypted);

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * ���ܣ�����
	 * 
	 * @param data
	 *            String ��Ҫ���ܵ��ַ�
	 * @param secret_key
	 *            String ��Կ
	 * @return string ���ؽ��ܺ������
	 */
	public static String decryptAES(String encData, String secret_key) {
		byte[] tmp = asBin(encData);
		byte[] key = asBin(secret_key);
		SecretKeySpec sKey = new SecretKeySpec(key, "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, sKey);
			byte[] decrypted = cipher.doFinal(tmp);
			return new String(decrypted);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		/*
		 * 256 bit key
		 */
		String key = "00000000000000000000000000000000";
		/*
		 * source data
		 */
		System.out.println("source -------- 123456");

		String enBytes = AESEncrypt.encryptAES("123456", key);

		System.out.println("mi wen --------------- " + enBytes.toString());

		String deBytes = AESEncrypt.decryptAES(enBytes, key);

		System.out.println("ming wen------------" + new String(deBytes));

	}
}
