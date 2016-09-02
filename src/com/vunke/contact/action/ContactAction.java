package com.vunke.contact.action;

import com.vunke.contact.model.Contact;
import com.vunke.contact.service.ContactCoreService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供一个接口修改和新增通讯录
 * @author lsx
 *
 */
@Controller
public class ContactAction {

	@Resource
	private ContactCoreService conCoreServ;


	@RequestMapping(value="/contact/asynContact",method=RequestMethod.POST)
	public void asynContact(@RequestParam("json")String json ,HttpServletResponse response,HttpServletRequest request){
		String message = "fail";
		String code = "400";
		Map<String,Long> map = null ;
		JSONObject result = new JSONObject();
		try {
			JSONObject jsonObject =  JSONObject.fromObject(json);
			String contacts=  jsonObject.getString("contacts");
			String userName = jsonObject.getString("userName");
			JSONArray jsonarray = JSONArray.fromObject(contacts);
			List<Contact> list = JSONArray.toList(jsonarray, Contact.class);
			map = conCoreServ.saveOrUpdate(list,userName);
			System.out.println("map:"+map);
			result.putAll(map);
			message = "success";
			code = "200";
		} catch (Exception e) {
			message = "error";
			code="500";
			e.printStackTrace();
		}
		result.put("code", code);
		result.put("message", message);
		ajax(result.toString(),response);

	}
	/**
	 * 获取所有的联系人
	 * @param response
	 * @param request
     */
	@RequestMapping(value="/contact/getContactAll",method=RequestMethod.POST)
	public void getContactAll(HttpServletResponse response,HttpServletRequest request){
		String message = "fail";
		String code = "400";
		JSONObject result =   new JSONObject();
        List<Contact> contacts = null;
		try {
			String json =	request.getParameter("json");
            JSONObject jsonObject = JSONObject.fromObject(json);
            String userName = jsonObject.getString("userName");

				contacts = conCoreServ.getContactAll(userName);



			message = "success";
			code = "200";
		} catch (Exception e) {
			e.printStackTrace();
            contacts = new ArrayList<Contact>();
			message = "fail";
			code = "500";
		}
		result.put("message",message);
		result.put("code",code);
        result.put("contacts",JSONArray.fromObject(contacts));
		ajax(result.toString(),response);
	}

	/**
	 * 分页获取联系人
	 * @param response
	 * @param request
     */
	@RequestMapping(value="/contact/findPageList",method=RequestMethod.POST)
	public void findPageList(HttpServletResponse response,HttpServletRequest request){
        String message = "fail";
        String code = "400";
        JSONObject result =   new JSONObject();
        String json = request.getParameter("json");
        JSONObject jsonObject = JSONObject.fromObject(json);
        long page =jsonObject.getLong("page");
        String userName = jsonObject.getString("userName");
        if (null==userName){
            userName = "";
            message = "用户名为空";
            result.put("message",message);
            result.put("code","400");
            ajax(result.toString(),response);
            return;
        }
        List<Contact> contacts = null;
        try {
            message = "success";
            code = "200";
            contacts = conCoreServ.findPageList(userName, page, 20);
        } catch (Exception e) {
            e.printStackTrace();
             message = "fail";
             code = "500";
        }

        result.put("message",message);
        result.put("code",code);
        JSONArray jsonArray = JSONArray.fromObject(contacts);
        result.put("contacts",jsonArray);
        ajax(result.toString(),response);
    }

    /**
     * 批量删除联系人
     * @param response
     * @param request
     */
    @RequestMapping(value="/contact/deleteContact",method=RequestMethod.POST)
    public void deleteContact(HttpServletResponse response,HttpServletRequest request){
        JSONObject result = new JSONObject();
        String message = "fail";
        String code = "400";
        try {
        String json = request.getParameter("json");
        JSONObject jsonObject = JSONObject.fromObject(json);
        /*String userIds = jsonObject.getString("userIds");
            userIds = userIds.substring(1,userIds.length()-1) ;*/
     List<Long> userIds = JSONArray.toList(jsonObject.getJSONArray("userIds"));
        String userName = jsonObject.getString("userName");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("userIds",userIds);
        map.put("userName",userName);
            conCoreServ.deleteByIdAndUserName(map);
            message = "success";
            code = "200";
        } catch (Exception e) {
            e.printStackTrace();
             message = "fail";
             code = "500";
        }

        result.put("message",message);
        result.put("code",code);
        ajax(result.toString(),response);

    }
	public static void main(String[] args) {
		List<Contact> list = new ArrayList<Contact>();
		list.add(new Contact("1","1"));
		list.add(new Contact("2","2"));
		list.add(new Contact("3","3"));
		JSONObject obj = new JSONObject();
		obj.put("userName", "李师兄");
		obj.put("contacts", list);
		System.out.println("json格式："+obj.toString());
		//将指定格式的json数据转换为数组
		JSONObject jsonObject =  JSONObject.fromObject(obj.toString());
		String contacts=  jsonObject.getString("contacts");
		String userName = jsonObject.getString("userName");
		System.out.println("arg0:"+userName);
		JSONArray jsonarray = JSONArray.fromObject(contacts);

		List<Contact> array = JSONArray.toList(jsonarray, Contact.class);
		for (Contact c : array) {
			System.out.println("arg1:"+c.getEmail());
		}

	}

	public void ajax(String context,HttpServletResponse response){
		try {
			response.setContentType("text/html" + ";charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.getWriter().write(context);
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  获取所有的联系人  二期
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="/contact/queryContactAll",method=RequestMethod.POST)
	public void queryContactAll(HttpServletResponse response,HttpServletRequest request){
		String message = "fail";
		String code = "400";
		JSONObject result =   new JSONObject();
		List<Contact> contacts = null;
		try {
			String json =	request.getParameter("json");
			JSONObject jsonObject = JSONObject.fromObject(json);
			String userName = jsonObject.getString("userName");
			if (userName.length()==12 ){
				userName = userName.substring(1);
			}
			userName = "%"+userName+"%";
			contacts = conCoreServ.queryContactAll(userName);
			message = "success";
			code = "200";
		} catch (Exception e) {
			e.printStackTrace();
			contacts = new ArrayList<Contact>();
			message = "fail";
			code = "500";
		}
		result.put("message",message);
		result.put("code",code);
		result.put("contacts",JSONArray.fromObject(contacts));
		System.out.println("queryContactAll===>"+result.toString());
		ajax(result.toString(),response);
	}

	/**
	 * 批量删除联系人  二期
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="/contact/deleteContactList",method=RequestMethod.POST)
	public void deleteContactList(HttpServletResponse response,HttpServletRequest request){
		JSONObject result = new JSONObject();
		String message = "fail";
		String code = "400";
		try {
			String json = request.getParameter("json");
			JSONObject jsonObject = JSONObject.fromObject(json);

			List<Long> userIds = JSONArray.toList(jsonObject.getJSONArray("userIds"));
			String userName = jsonObject.getString("userName");
			HashMap<String, Object> map = new HashMap<>();
			map.put("userIds",userIds);
			map.put("userName","%"+userName+"%");
			int count = conCoreServ.removeByContactIdAndUserName(map);
			System.out.println("删除联系人数量:"+count);
			message = "success";
			code = "200";
		} catch (Exception e) {
			e.printStackTrace();
			message = "fail";
			code = "500";
		}

		result.put("message",message);
		result.put("code",code);
		ajax(result.toString(),response);

	}

}
