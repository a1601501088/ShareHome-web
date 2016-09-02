package com.vunke.shareHome.service;

import com.vunke.basicframe.util.AESEncrypt;
import com.vunke.common.DataBase;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.configuration.EngineConfigurationFactoryFinder;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPHeaderElement;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import java.net.URL;
import java.util.Calendar;

public class Ims {

	Call call;
	SOAPEnvelope spEnvelope;
	SOAPBodyElement bodyElement;
	SOAPBodyElement bodyElement1;
	SOAPElement bodyChildElement;
	public static String method;
	public String AUTHENTICATION_user = DataBase.IMSUSER;
	public String AUTHENTICATION_PWD = DataBase.IMSPASS;
	public String secret_key = "00000000000000000000000000000000";
	public String NPINameSpace;
	public String OperationType;
	public String USER_NAME;
	public String PASS_WORD;
	public String MESSAGE_ID;
	public String ADDRESS;
	public String AUTHENTICATION;

	public Ims() {
		NPINameSpace = "http://www.huawei.com/";
		OperationType = "rpc";
		MESSAGE_ID = "MessageID";
		AUTHENTICATION = "Authentication";
		USER_NAME = "Username";
		PASS_WORD = "Password";
	}

	private String InitWebService(SOAPBodyElement body) throws Exception {
		
		String flag = null; 
		try {
			Service service = new Service();

			EngineConfiguration defaultConfig = EngineConfigurationFactoryFinder
					.newFactory().getClientEngineConfig();

			org.apache.axis.configuration.SimpleProvider config = new org.apache.axis.configuration.SimpleProvider(
					defaultConfig);
			config.deployTransport(
					org.apache.axis.transport.http.HTTPTransport.DEFAULT_TRANSPORT_NAME,
					new org.apache.axis.transport.http.CommonsHTTPSender());
			service = new org.apache.axis.client.Service(config);

			call = (Call) service.createCall();
			//	call.setTargetEndpointAddress(new URL("http://127.0.0.78:8080/spg"));
			call.setTargetEndpointAddress(new URL(DataBase.IMSURL));
			//axis1 axis2 x-fire ,cxf
			spEnvelope = new SOAPEnvelope();
			setHeader();
			spEnvelope.clearBody();

			spEnvelope.addBodyElement(body);
			System.out.println(spEnvelope.getAsString());
			call.setOperationStyle(OperationType);
			SOAPEnvelope res = call.invoke(spEnvelope);
			
			System.out.println(res.getAsString());
			flag = res.getAsString() ;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw e;
		}
		return flag;
	}

	private SOAPBodyElement CreaterequestXml(String cmdline)throws Exception {

		String[] cmd_params = cmdline.split("#");
		if (cmd_params == null)
			return null;
		String[] cmds = cmd_params[0].split(":");
		if (cmds.length > 3)
			return null;
		String nameSpace = cmds[2];

		NPINameSpace = "http://www.huawei.com/" + nameSpace;

		method = cmds[0];
		cmdline = cmd_params[1];
		String[] params = cmdline.split(",");

		String[] name_value = null;
		String tempAttribute, Element;
		SOAPBodyElement bodyElement = new SOAPBodyElement(NPINameSpace, method);

		SOAPElement c = null;
		for (int i = 0; i < params.length; i++) {
			name_value = params[i].split("=");
			if (name_value.length < 2) {
				return null;
			}
			Element = name_value[0];
			tempAttribute = name_value[1];

			try {
				if (Element.equals("AC")) {
					SOAPElement ac = bodyElement.addChildElement(Element);
					ac.addTextNode("");
					continue;
				}
//				System.out.println("Element:" + Element);
//				System.out.println("" + Element.indexOf("/"));
				if (Element.indexOf("/") != -1) {
					String[] childTwo = Element.split("/");
					NodeList nl = bodyElement.getElementsByTagName(childTwo[0]);
					System.out.println("bodyElement:" + nl.getLength());

					if (nl.getLength() == 0) {
						c = bodyElement.addChildElement(childTwo[0]);
					}
					SOAPElement b = c.addChildElement(childTwo[1]);
					b.addTextNode(tempAttribute);
				} else {

					String[] substr1 = name_value[1].split("%");
					if (substr1.length > 1) {
						bodyChildElement = bodyElement.addChildElement(Element);
						String[] substr = name_value[1].split("%");
						for (int a = 0; a < substr.length; a++) {

							if ("null".equals(substr[a].toLowerCase())) {
								continue;
							} else {
								String[] arrStr = substr[a].split("~");
								System.out.println(substr.length + "=====" + a
										+ "=====" + arrStr[0] + "===="
										+ arrStr[1]);
								bodyChildElement.addChildElement(arrStr[0])
										.addTextNode(arrStr[1]);
							}
						}
					} else {
						bodyChildElement = bodyElement.addChildElement(Element);
						if (Element.equals("REGEXP"))
							tempAttribute = tempAttribute.replace('-', '=');

						if (!tempAttribute.equals("NULL"))
							bodyChildElement.addTextNode(tempAttribute);
					}
				}
			} catch (Exception s) {
				throw s;
			}
		}

		return bodyElement;
	}

	private void setHeader() throws Exception{
		NPINameSpace = "http://www.huawei.com/" + "SPG";
		try {
			spEnvelope.removeBody();
			spEnvelope.removeHeaders();
			spEnvelope.removeNamespaceDeclaration("m");
			spEnvelope.addNamespaceDeclaration("m", NPINameSpace);

			SOAPHeaderElement messageHeader = new SOAPHeaderElement(new QName(
					NPINameSpace, MESSAGE_ID), getMessage_id());

			spEnvelope.addHeader(messageHeader);

			SOAPHeaderElement authenticationHeader = new SOAPHeaderElement(
					new QName(NPINameSpace, AUTHENTICATION));

			SOAPElement ele = authenticationHeader.addChildElement(USER_NAME);
			ele.addTextNode(AUTHENTICATION_user);
			ele = authenticationHeader.addChildElement(PASS_WORD);
			ele.addTextNode(AESEncrypt.encryptAES(AUTHENTICATION_PWD,
					secret_key).toUpperCase());
			spEnvelope.addHeader(authenticationHeader);
		} catch (Exception e) {
			e.printStackTrace();
			throw e ;
		}

	}


	public String openIms(String code, String phone, String password){
		String flag = "0" ;
		try {
			SOAPBodyElement body = CreaterequestXml(cmdAddIMSSub(code, phone,
					password));
			String result = InitWebService(body);
			String resultCode = getResult(result);
			if(!"0".equals(resultCode ) ) flag = resultCode;
			
			body = CreaterequestXml(cmdAddNAPTRRecord(code,phone));
			result = InitWebService(body);
			resultCode = getResult(result);
			if(!"0".equals(resultCode ) ) flag = resultCode;
			
			body = CreaterequestXml(cmdAddSbr(code,phone));
			result = InitWebService(body);
			resultCode = getResult(result);
			if(!"0".equals(resultCode ) ) flag = resultCode;
			
			body = CreaterequestXml(cmdAddSifc(code, phone));
			result = InitWebService(body);
			resultCode = getResult(result);
			if(!"0".equals(resultCode ) ) flag = resultCode;
			
			body = CreaterequestXml(cmdModSbr(code, phone));
			result = InitWebService(body);
			resultCode = getResult(result);
			if(!"0".equals(resultCode ) ) flag = resultCode;
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return flag ;
	}


	
	public String updatePass(String code ,String phone,String password)throws Exception{
		try {
			SOAPBodyElement body = CreaterequestXml(cmdModSubPwd(code, phone, password));
			String result = InitWebService(body);
			String resultCode = getResult(result);
			if("0".equals(resultCode ) )return resultCode;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null ;
		
	}
	
	private String getResult(String result){
		int begin = result.indexOf("<m:ResultCode>")+"<m:ResultCode>".length();
		int end = result.indexOf("</m:ResultCode>");
		System.out.println("resultCode:"+result.substring(begin, end));
		String resultCode =  result.substring(begin, end);
		return resultCode ;
	}
	
	private String getMessage_id() {
		Calendar curTime = Calendar.getInstance();
		int iYear = curTime.get(Calendar.YEAR);
		int iMonth = curTime.get(Calendar.MONTH) + 1;
		int iDate = curTime.get(Calendar.DATE);
		int iHour = curTime.get(Calendar.HOUR_OF_DAY);
		int iMin = curTime.get(Calendar.MINUTE);
		int iSec = curTime.get(Calendar.SECOND);
		int iMillsecond = curTime.get(Calendar.MILLISECOND);
		String Timestamp = String.valueOf(iYear) + String.valueOf(iMonth)
				+ String.valueOf(iDate) + String.valueOf(iHour)
				+ String.valueOf(iMin) + String.valueOf(iSec)
				+ String.valueOf(iMillsecond);
		return Timestamp + (int) (Math.random() * 100);
	}

	/**
	 * 
	 * @param code
	 *            TV端为8，手机端为9
	 * @param phone
	 *            用户名
	 * @param password
	 *           密码
	 * @return
	 */
	private String cmdAddIMSSub(String code, String phone, String password) {
		StringBuffer cmdline = new StringBuffer("AddIMSSub:SPG:SPG#");
		cmdline.append("DN=11831726" + code + phone + ",");
		cmdline.append("AC=NULL,");
		cmdline.append("NC=+86,");
		cmdline.append("DOMAIN=hu.ctcims.cn,");
		cmdline.append("PASSWORD=" + password + ",");
		cmdline.append("USERTYPE=0,");
		cmdline.append("IMPUTPLID=5,");
		cmdline.append("CHARGTPLID=1,");
		cmdline.append("CAPTPLID=31");
		System.out.println("cmdAddIMSSub:" + cmdline.toString());
		return cmdline.toString();
	}

	/**
	 * @param code
	 *            :TV端为8，手机端为9
	 * @param phone
	 *           用户名
	 * @return  指令
	 */
	private String cmdAddNAPTRRecord(String code, String phone) {
		char[] array = phone.toCharArray();
		StringBuffer cp = new StringBuffer();
		int length = array.length;
		for (int i = length-1; i >= 0; i--) {
			cp.append(array[i] + ".");
		}
		System.out.println("产生的名字："+cp.toString());
		cp.append(code + ".");
		StringBuffer cmdline = new StringBuffer("AddNAPTRRecord:SPG:SPG#");
		cmdline.append("NAME=" + cp.toString()
				+ "6.2.7.1.3.8.1.1.6.8.e164.arpa,");
		cmdline.append("ZONENAME=6.2.7.1.3.8.1.1.6.8.e164.arpa,");
		cmdline.append("ORDER=10,");
		cmdline.append("PREFERENCE=20,");
		cmdline.append("FLAGS=U,");
		cmdline.append("SERVICE=E2U+sip,");
		cmdline.append("REGEXP=!^.*$!sip:+8611831726" + code + phone
				+ "@hu.ctcims.cn!,");
		cmdline.append("REPLACEMENT=.,");
		cmdline.append("TTL=86400");
		System.out.println("cmdAddNAPTRRecord:" + cmdline);
		return cmdline.toString();
	}

	public static void main(String[] args) {
		Ims ims = new Ims();
		try {
			ims.updatePass("9", "18153360751", "A121212");
			//ims.openIms("8", "18153360751", "A121213");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param code
	 *            :TV端为8，手机端为9
	 * @param phone
	 *           用户名
	 * @return  指令
	 */
	private String cmdAddSbr(String code, String phone) {
		StringBuffer cmdline = new StringBuffer("AddSbr:SPG:SPG#");
		cmdline.append("DN=11831726" + code + phone + ",");
		cmdline.append("AC=NULL,");
		cmdline.append("NC=+86,");
		cmdline.append("DOMAIN=hu.ctcims.cn,");
		cmdline.append("TEMPLATEIDX=65535,");
		cmdline.append("DSPIDX=65534,");
		cmdline.append("LP=2000,");
		cmdline.append("CSC=20000,");
		cmdline.append("VTFLAG=1,");
		cmdline.append("NSCLIP=1");
		System.out.println("cmdAddSbr:" + cmdline);
		return cmdline.toString();
	}

	/**
	 * @param code
	 *            :TV端为8，手机端为9
	 * @param phone
	 *           用户名
	 * @return  指令
	 */
	private String cmdAddSifc(String code, String phone) {
		StringBuffer cmdline = new StringBuffer("AddSifc:SPG:SPG#");
		cmdline.append("DN=11831726" + code + phone + ",");
		cmdline.append("AC=NULL,");
		cmdline.append("NC=+86,");
		cmdline.append("DOMAIN=hu.ctcims.cn,");
		cmdline.append("SIFCID=800,");
		System.out.println("cmdAddSifc:" + cmdline);
		return cmdline.toString();
	}

	/**
	 * @param code
	 *            :TV端为8，手机端为9
	 * @param phone
	 *           用户名
	 * @return  指令
	 */
	private String cmdModSbr(String code, String phone) {
		StringBuffer cmdline = new StringBuffer("ModSbr:SPG:SPG#");
		cmdline.append("DN=11831726" + code + phone + ",");
		cmdline.append("NC=+86,");
		cmdline.append("DOMAIN=hu.ctcims.cn,");
		cmdline.append("VTFLAG=1,");
		cmdline.append("NSCLIP=1");
		System.out.println("cmdModSbr:" + cmdline);
		return cmdline.toString();
	}
	
	private String cmdModSubPwd(String code, String phone,String password) {		
		StringBuffer cmdline = new StringBuffer("ModSubPwd:SPG:SPG#");
		cmdline.append("DN=11831726" + code + phone + ",");
		cmdline.append("AC=731,");
		cmdline.append("NC=+86,");
		cmdline.append("DOMAIN=hu.ctcims.cn,");
		cmdline.append("NPASSWORD="+password);
		System.out.println("cmdModSubPwd:" + cmdline);
		return cmdline.toString();
	}

}
