package com.vunke.shareHome.action;

import com.vunke.shareHome.service.OpenAcService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class OpenAc extends BaseAction{
	
	@Resource
	private OpenAcService openAcServ;
	
	
	@RequestMapping(value="/shareHome/openAc")
	public void openAc(@RequestParam("json")String json ,HttpServletRequest request ,HttpServletResponse response){
		String message = "fail";
		String code = "400";
		try {
			JSONObject jsonObject = JSONObject.fromObject(json);
			String type = jsonObject.getString("type");
			String phone = jsonObject.getString("phone");
			String password = jsonObject.getString("password");
			if("TV".equals(type))type = "8";
			if("APP".equals(type))type = "9";
			if("8".equals(type)||"9".equals(type)){
				openAcServ.openIms(type, phone, password);
				//openAcServ.openTms(type, phone, password);	
				message = "success";
				code = "200";
			}
		} catch (Exception e) {
			message = "error";
			code = "500";
		}
		JSONObject result = new JSONObject();
		result.put("code", code);
		
		result.put("message", message);
		ajaxJson(result.toString()
				, response);
	}
	
	
}
