package com.vunke.basicframe.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * 
 * <p>
 * Company: Transfar
 * </p>
 * 
 * @version 1.0
 */
public class Md5e implements Serializable {

	private static final long serialVersionUID = 1L;
	/** ����BBMS�ӿڼ�����Կ **/
	private static String iIOSKey = "FB7B16608E7BC29FF5223198D765D658";

	public Md5e() {
	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	/**
	 * �������
	 * 
	 * @param srcStr
	 *            ���û�����
	 * @return����������
	 * @throws IIOSException
	 */
	public static String encrypt(String srcStr) throws Exception {

		String resStr = "";
		byte[] byteRes = new byte[16], md5IIOSKey = new byte[16];
		int i;

		try {
			/* ������ */
			if (srcStr == null || srcStr.length() > 16)
				// throw new
				// IIOSException(IIOSException.PARAM_VALUE_ERROR,"�û�����������");
				System.out.println("�û�����������");
			/* ת�� */
			md5IIOSKey = ascToByte(iIOSKey);

			/* ��Կ��������򣬵õ���� */
			for (i = 0; i < srcStr.length(); i++) {
				byteRes[i] = (byte) (srcStr.charAt(i) ^ md5IIOSKey[i]);
			}

			// ��λ�����벻��16λ���������λ��Ϊmd5IIOSKey����λ
			for (; i < 16; i++) {
				byteRes[i] = (byte) md5IIOSKey[i];
			}

			// ת����16���Ƶ�ASCII��ʾ
			for (i = 0; i < 16; i++) {
				String str = byteHEX(byteRes[i]);

				if (str.length() == 1)
					str = "0" + str;

				resStr = resStr + str;
			}

		} catch (Exception e) {
		//	mdfunc.LogException(e, true);
			e.printStackTrace();
			// throw new
			// IIOSException(e,IIOSException.OPERATION_NOT_PERMIT,"��ݼ��ܴ���");
			System.out.println("�û�����������");
		}
		return resStr;
	}

	/**
	 * ���Ľ���
	 * 
	 * @param encStr
	 *            ����ݿ�洢������
	 * @return������
	 * @throws IIOSException
	 */
	public static String decrypt(String encStr) throws Exception {

		String resStr = "";
		byte[] byteRes = new byte[16], md5IIOSKey = new byte[16], pasStr = new byte[16];
		int i;

		try {
			/* ������ */
			if (encStr == null || encStr.length() != 32)
				// throw new
				// IIOSException(IIOSException.PARAM_VALUE_ERROR,"�û�������Ĵ���");

				/* ת�� */
				md5IIOSKey = ascToByte(iIOSKey);
			pasStr = ascToByte(encStr);

			/* ��Կ�����������򣬵õ���� */
			for (i = 0; i < 16; i++) {
				char charRes = (char) (pasStr[i] ^ md5IIOSKey[i]);
				if (charRes != '\0')
					resStr += charRes;
			}
		} catch (Exception e) {
			//mdfunc.LogException(e, true);
			e.printStackTrace();
			// throw new
			// IIOSException(e,IIOSException.OPERATION_NOT_PERMIT,"��ݽ��ܴ���");
			System.out.println("��ݽ��ܴ���");
		}
		return resStr;
	}

	/*
	 * byteHEX()��������һ��byte���͵���ת����ʮ����Ƶ�ASCII��ʾ��
	 * ��Ϊjava�е�byte��toString�޷�ʵ����һ�㣬������û��C�����е� sprintf(outbuf,"%02X",ib)
	 */
	public static String byteHEX(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}

	/* ��MD5���ܺ��ֵ��ASCII��16���Ʊ�ʾ��ʽ��ԭ */
	public static byte[] ascToByte(String inStr) {

		int len = inStr.length();
		byte[] buf = new byte[((len + 1) / 2)];

		int i = 0, j = 0;
		if ((len % 2) == 1)
			buf[j++] = (byte) fromDigit(inStr.charAt(i++));

		while (i < len) {
			buf[j++] = (byte) ((fromDigit(inStr.charAt(i++)) << 4) | fromDigit(inStr
					.charAt(i++)));
		}
		return buf;
	}

	public static int fromDigit(char ch) {

		if (ch >= '0' && ch <= '9')
			return ch - '0';

		if (ch >= 'A' && ch <= 'F')
			return ch - 'A' + 10;

		if (ch >= 'a' && ch <= 'f')
			return ch - 'a' + 10;
		else
			return 0;
	}

	// ������ת�ַ�
	public static String byte2hex(byte[] b) {

		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + "";
		}
		// return hs.toUpperCase();
		return hs;
	}

	public static void main(String[] args) {
		try {
			System.out.println("Result:" + Md5e.encrypt("test"));
			System.out.println("Result:"
					+ Md5e.decrypt("8F1E65148E7BC29FF5223198D765D658"));

		} catch (Exception ie) {
			System.out.println(ie);
		}
	}
}
