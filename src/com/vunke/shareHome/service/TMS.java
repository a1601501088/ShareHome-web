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
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import java.net.URL;
import java.util.Calendar;

public class TMS {

	Call call;
	SOAPEnvelope spEnvelope;
	SOAPBodyElement bodyElement;
	SOAPBodyElement bodyElement1;
	SOAPElement bodyChildElement;
	public static String method;
	public String AUTHENTICATION_user = DataBase.TMSUSER;
	public String AUTHENTICATION_PWD = DataBase.TMSPASS;
	public String secret_key = "00000000000000000000000000000000";
	public String NPINameSpace;

	public String OperationType;
	public String USER_NAME;
	public String PASS_WORD;
	public String MESSAGE_ID;
	public String ADDRESS;
	public String AUTHENTICATION;

	public TMS() {
		NPINameSpace = "http://www.huawei.com/";
		OperationType = "rpc";

		MESSAGE_ID = "MessageID";

		AUTHENTICATION = "Authentication";
		USER_NAME = "Username";
		PASS_WORD = "Password";
	}

	private String InitWebService(String cmdline) throws Exception {
		String resultCode = null ;
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
			// service.createCall();

			call = (Call) service.createCall();
		//	call.setTargetEndpointAddress(new URL("http://134.175.56.233:8080/spg"));
			call.setTargetEndpointAddress(new URL(DataBase.TMSURL));
			spEnvelope = new SOAPEnvelope();
			CreaterequestXml(cmdline.toString());
			spEnvelope.clearBody();
			spEnvelope.addBodyElement(bodyElement);
			
			System.out.println(spEnvelope.getAsString());
			call.setOperationStyle(OperationType);
			SOAPEnvelope res = call.invoke(spEnvelope);
			System.out.println("TMSCode:"+tag+res.getAsString());
			resultCode = getResult(res.getAsString());
			System.out.println("TMSresultCode:"+tag+resultCode);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw e;
		}
		return resultCode;
	}
private String tag = "TMS:";
	public String openTms(String code, String phone, String password){
		tag = "openTms:";
		try {
//			AddSbr: DN="11831726913319551978", AC="", NC="+86", DOMAIN="hu.ctcims.cn", TEMPLATEIDX=65535, DSPIDX=65534, LP=2000, CSC=20000;
		//	ADD_HSUB: devid="+8611831726915367858537", impi="+8611831726915367858537@hu.ctcims.cn", password="******", impu="sip:+8611831726915367858537@hu.ctcims.cn", hnDomain="hu.ctcims.cn", Subscriber/subid="+8611831726915367858537", Subscriber/domain="/app.hu.ctcims.cn/", Subscriber/subscriberName="+8611831726915367858537";
			StringBuffer cmdline = new StringBuffer("ADD_HSUB:SPG:SPG#");
			cmdline.append("devid=+8611831726" + code + phone + ",");
			cmdline.append("impi=+8611831726" + code + phone + "@hu.ctcims.cn,");
			cmdline.append("password=" + password + ",");
			cmdline.append("impu=sip:+8611831726" + code + phone
					+ "@hu.ctcims.cn,");
			cmdline.append("hnDomain=hu.ctcims.cn,");
			cmdline.append("Subscriber/subid=+8611831726" + code + phone + ",");
			if("9".equals(code)){
				cmdline.append("Subscriber/domain=/app.hu.ctcims.cn/,");				
			}else{
				cmdline.append("Subscriber/domain=/stb.hu.ctcims.cn/,");
			}
			cmdline.append("Subscriber/subscriberName=+8611831726" + code
					+ phone);
			System.out.println("cmdline:" + cmdline);
			return InitWebService(cmdline.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String updatePass(String code, String phone, String password){
	//	MOD_PWD: device/impi="+8611831726915367858537@hu.ctcims.cn", device/password="******";
tag = "updatePass:";
		try {
			StringBuffer cmdline = new StringBuffer("MOD_PWD:SPG:SPG#");
			cmdline.append("device/impi=+8611831726"+code+phone+"@hu.ctcims.cn,");
			cmdline.append("device/password="+password+",");
			System.out.println("cmdline:" + cmdline);
			return InitWebService(cmdline.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void main(String[] args) {
		TMS tms = new TMS();
//		tms.updatePass("9", "18153360751", "A121213");
		tms.openTms("8", "18153360751", "A121213");
	}
	
	private String getResult(String result){
		int begin = result.indexOf("<m:ResultCode>")+"<m:ResultCode>".length();
		int end = result.indexOf("</m:ResultCode>");
		System.out.println("begin:"+begin+"\tend:"+end);
		
		System.out.println("resultCode:"+result.substring(begin, end));
		String resultCode =  result.substring(begin, end);
		return resultCode ;
	}
	
	private Document CreaterequestXml(String cmdline) {
		final String XML_ENCODE_GB2312 = "GB2312";

		Document doc = DocumentHelper.createDocument();
		if (doc == null)
			return null;
		doc.setXMLEncoding(XML_ENCODE_GB2312);

		String[] cmd_params = cmdline.split("#");
		if (cmd_params == null)
			return null;
		String[] cmds = cmd_params[0].split(":");
		if (cmds.length > 3)
			return null;
		String mEN = cmds[1];
		String nameSpace = cmds[2];
		NPINameSpace = "http://www.huawei.com/" + nameSpace;
		try {
			spEnvelope.removeBody();
			spEnvelope.removeHeaders();
			spEnvelope.removeNamespaceDeclaration("m");
			spEnvelope.addNamespaceDeclaration("m", NPINameSpace);
			if (!mEN.equals("SPG")) {
				SOAPHeaderElement mENHeader = new SOAPHeaderElement(new QName(
						NPINameSpace, "MEName"), mEN);
				spEnvelope.addHeader(mENHeader);
			}
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
			return null;
		}
		method = cmds[0];
		cmdline = cmd_params[1];
		String[] params = cmdline.split(",");
		String[] name_value = null;
		String tempAttribute, Element;
		bodyElement = new SOAPBodyElement(NPINameSpace, method);
		bodyElement1 = new SOAPBodyElement(NPINameSpace, "USEME");
		SOAPElement useEle;
		try {
			useEle = bodyElement1.addChildElement("MENAME");
			useEle.addTextNode("TMS");
		} catch (Exception e) {
			e.printStackTrace();
		}
		SOAPElement c = null;
		for (int i = 0; i < params.length; i++) {
			name_value = params[i].split("=");
			if (name_value.length < 2) {
				return null;
			}
			Element = name_value[0];
			tempAttribute = name_value[1];

			try {
				System.out.println("Element:" + Element);
				System.out.println("" + Element.indexOf("/"));
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
				return null;
			}
		}

		return doc;
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

}
