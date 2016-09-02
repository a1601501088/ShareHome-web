package com.vunke.shareHome.service;

import com.vunke.common.DataBase;
import com.vunke.shareHome.dao.EnumDataMapper;
import com.vunke.shareHome.dao.MsgInfoMapper;
import com.vunke.shareHome.model.EnumData;
import com.vunke.shareHome.model.MsgInfo;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能：往手机发送短信
 *
 * @author Lenovo
 *
 */
@Service("sendMsgServ")
public class SendMsg {

	@Resource
	private MsgInfoMapper msgInfoMapper;
	
	@Resource
	private EnumDataMapper enumDataMapper;

	public int create(MsgInfo msgInfo) {
		return msgInfoMapper.insert(msgInfo);
	}

	/**
	 *
	 * @param srcmobile
	 * @param password
	 * @param objmobile
	 * @param smsid
	 * @param smstext
	 * @return 拼接的接口路径
	 */
	public String sendMsg(String srcmobile, String password, String objmobile,
						  String smsid, String smstext) throws Exception {

		StringBuffer sb = new StringBuffer(
				DataBase.SENDURL+"?");
		try {
			smstext = java.net.URLEncoder.encode(smstext, "UTF-8");
			sb.append("srcmobile=" + srcmobile);
			sb.append("&password=" + password);
			sb.append("&objmobile=" + objmobile);
			sb.append("&smsid=" + smsid);
			sb.append("&smstext=" + smstext);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		}
		return sb.toString();
	}

	public String sendSMS(String srcmobile, String password, String objmobile,
						String smsid, String smstext) throws  Exception{
//http://134.175.19.80:17080/httpinterface/ent/sendsms.jsp?srcmobile=073184203359&password=abcd1234&objmobile=18390878104&smsid=183908781041234&smstext=test
		HttpClient httpclient = new HttpClient();
		PostMethod post = new PostMethod(DataBase.SENDURL);
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"gbk");
		post.addParameter("srcmobile", srcmobile);
		post.addParameter("password", password);
		post.addParameter("objmobile", objmobile);
		post.addParameter("smsid", smsid);
		post.addParameter("smstext", smstext);
		int status = httpclient.executeMethod(post);
		System.out.println("status:"+status);
		String code = new String(post.getResponseBody(),"gbk");
		System.out.println("info:"+code);
		if (status==200){
			return code.trim();
		}else {
			return "-1";
		}

	}

	/**
	 * 自动生成msgId，id为时间(精确到毫秒)加序列号
	 *
	 * @return 序列号
	 */
	public String getMsgId() {
		/*
		 * 1、获取日期时间 2、获取序列数字
		 */
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String time = sdf.format(date);

		// 序列号
		String seq_id = msgInfoMapper.selectSequence()+"";

        System.out.println("序列号"+time + seq_id);

		return time + seq_id;
	}

	public MsgInfo queryRandom(String phone){
		return msgInfoMapper.selectRandom(phone);
	}
	
	
	/**
	 * 
	 * @param type 区别IMS和TMS
	 * @param resultCode 结果码
	 * @return 结果描述
	 */
	public String getResultDesc(String type ,String resultCode){
		if(resultCode==null){
			return "error";
		}else{
			EnumData data =  enumDataMapper.selectByKey(new EnumData(type, "resultCode", resultCode));
			return data.getEnumValue();
		}
	}

	/**
	 * 通过电话号码删除
	 * @param moblie
	 * @return
	 * @throws Exception
     */
		public int deleteByMoblie(String moblie) throws Exception{
			int delete = msgInfoMapper.deleteByMoblie(moblie);

			return delete;
		}

	
}