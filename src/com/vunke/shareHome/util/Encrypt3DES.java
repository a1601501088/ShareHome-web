package com.vunke.shareHome.util;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.Security;

public class Encrypt3DES {

    private static String strDefaultKey = "ShareHome";

    private   Cipher encryptCipher = null;

    private  Cipher decryptCipher = null;

    //最优雅的线程安全的单例模式
        private static class SingletonHolder {
            private final static Encrypt3DES INSTANCE = new Encrypt3DES(strDefaultKey);
        }
        public static Encrypt3DES getInstance() {
            return SingletonHolder.INSTANCE;
        }


    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB
     *            需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     */
    public  String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍..一个byte转成16进制最多不会超过两位。FF
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16)); // 16代表进制
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn
     *            需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
     */
    public  byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 默认构造方法，使用默认密钥
     *
     * @throws Exception
     */
    private Encrypt3DES() throws Exception {
        this(strDefaultKey);
    }

    /**
     * 指定密钥构造方法
     *
     * @param strKey
     *            指定的密钥
     * @throws Exception
     */
    private Encrypt3DES(String strKey) {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        try {
            Key key = getKey(strKey.getBytes());

            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密字节数组
     *
     * @param arrB
     *            需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public  byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     *
     * @param strIn
     *            需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public  String encrypt(String strIn) {
        try{
            if("".equals(strIn)||null==strIn){
                return strIn;
            }
            return byteArr2HexStr(encrypt(strIn.getBytes()));
        }catch(Exception e){
            e.printStackTrace();
        }
        return strIn;

    }

    /**
     * 解密字节数组
     *
     * @param arrB
     *            需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     *
     * @param strIn
     *            需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception {
        if("".equals(strIn)||null==strIn){
            return strIn;
        }
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp
     *            构成该字符串的字节数组
     * @return 生成的密钥
     * @throws java.lang.Exception
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];

        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

        return key;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
       try {
            String test = "A123456";

            System.out.println("加密前的字符：" + test);
            String encrypt = Encrypt3DES.getInstance().encrypt(test);
            System.out.println("加密后的字符：" + encrypt);
            System.out.println("解密后的字符：" + Encrypt3DES.getInstance().decrypt(encrypt));
            //System.out.println("解密后的字符：" + "45bb7c8a9a699cd7a1c1a39336ce6bf746f72e6520d3cf5d3adb375bc88de01d413fab86e8c69580cc7448be05d937a6".length());
        } catch (Exception e) {
            e.printStackTrace();
        }

      //  JFinal.start("C:\\Users\\Administrator\\Desktop\\onlyWeb\\webShareHome\\ShareHome\\WebRoot", 8080, "/ShareHome", 5);


    }


    static void swap(int a,int b){
        a = a^b;
        b = a^b;
        a= a^b;

        System.out.println(a);
        System.out.println(b);

    }
}