package com.vunke.shareHome.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseAction {

	public static final String VIEW = "view";
	public static final String LIST = "list";
	public static final String STATUS = "status";
	public static final String WARN = "warn";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String MESSAGE = "message";
	public static final String CONTENT = "content";


	
	
	// AJAX输出，返回null
		public String ajax(String content, String type, HttpServletResponse response) {
			try {
				response.setContentType(type + ";charset=UTF-8");
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				response.getWriter().write(content);
				response.getWriter().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		// AJAX输出文本，返回null
		public String ajaxText(String text, HttpServletResponse response) {
			return ajax(text, "text/plain", response);
		}

		// AJAX输出HTML，返回null
		public String ajaxHtml(String html, HttpServletResponse response) {
			return ajax(html, "text/html", response);
		}

		// AJAX输出XML，返回null
		public String ajaxXml(String xml, HttpServletResponse response) {
			return ajax(xml, "text/xml", response);
		}

		// 根据字符串输出JSON，返回null
		public String ajaxJson(String jsonString, HttpServletResponse response) {
			return ajax(jsonString, "text/html", response);
		}
		
		// 根据Map输出JSON，返回null
		public String ajaxJson(Map<String, String> jsonMap, HttpServletResponse response) {
			JSONObject jsonObject = JSONObject.fromObject(jsonMap);
			return ajax(jsonObject.toString(), "text/html", response);
		}
		
		// 根据Object输出JSON，返回null
		public String ajaxJson(Object obj, HttpServletResponse response) {
			JSONObject jsonObject = JSONObject.fromObject(obj);
			return ajax(jsonObject.toString(), "text/html", response);
		}
		
		// 根据Object输出JSON，返回null
		public String ajaxJsonList(Object obj, HttpServletResponse response) {
			JSONArray jsonObject = JSONArray.fromObject(obj);
			System.out.println(jsonObject.toString());
			return ajax(jsonObject.toString(), "text/html", response);
		}
		
		// 输出JSON警告消息，返回null
		public String ajaxJsonWarnMessage(String message, HttpServletResponse response) {
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put(STATUS, WARN);
			jsonMap.put(MESSAGE, message);
			JSONObject jsonObject = JSONObject.fromObject(jsonMap);
			return ajax(jsonObject.toString(), "text/html", response);
		}
		
		// 输出JSON成功消息，返回null
		public String ajaxJsonSuccessMessage(String message, HttpServletResponse response) {
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put(STATUS, SUCCESS);
			jsonMap.put(MESSAGE, message);
			JSONObject jsonObject = JSONObject.fromObject(jsonMap);
			return ajax(jsonObject.toString(), "text/html", response);
		}
		
		// 输出JSON错误消息，返回null
		public String ajaxJsonErrorMessage(String message, HttpServletResponse response) {
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put(STATUS, ERROR);
			jsonMap.put(MESSAGE, message);
			JSONObject jsonObject = JSONObject.fromObject(jsonMap);
			return ajax(jsonObject.toString(), "text/html", response);
		}
		
		// 设置页面不缓存
		public void setResponseNoCache(HttpServletResponse response) {
			response.setHeader("progma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setDateHeader("Expires", 0);
		}

	public Date String2Date(String dstr){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟

		try {
			Date date=sdf.parse(dstr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  null;
	}
	
}
