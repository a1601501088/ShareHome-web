package com.vunke.common;

public class DataBase {

	/**
	 * 发送短信的地址
	 */
	//外网
	public final static String SENDURL = "http://220.168.248.90:17080/httpinterface/ent/sendsms.jsp";
	//iptv网
	//public final static String SENDURL = "http://134.175.19.80:17080/httpinterface/ent/sendsms.jsp";

	
	/**
	 * IMS接口地址
	 */
	//public final static String IMSURL = "http://134.161.106.78:8080/spg";
	public final static String IMSURL = "http://127.0.0.78:8082/spg";

	/**
	 * IMS操作用户名
	 */
	public final static String IMSUSER = "APPBSS";

	/**
	 * IMS操作密码
	 */
	public final static String IMSPASS = "Appbss@spg2800";
	
	/**
	 * TMS接口地址
	 */
	//public final static String TMSURL = "http://134.175.56.233:8080/spg";
	public final static String TMSURL = "http://127.0.0.233:8081/spg";

	/**
	 * TMS操作用户名
	 */
	public final static String TMSUSER = "tmsbss";
	
	/**
	 * TMS操作密码
	 */
	public final static String TMSPASS = "Tmsspg2800@bss";


	/**
	 * ios端加密的key
	 */
	public final static String KEY_IOS = "2:2";
}

