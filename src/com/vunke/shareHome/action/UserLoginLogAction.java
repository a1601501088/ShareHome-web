package com.vunke.shareHome.action;

import com.vunke.shareHome.model.UserLoginLog;
import com.vunke.shareHome.service.UserLoginLogService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/11.
 */
@Controller
public class UserLoginLogAction extends BaseAction {
    @Resource
    private UserLoginLogService userLoginLogServ;

    /**
     * 登录记录
     *
     * @param jsonStr
     * @param req
     * @param resp
     * @throws IOException
     */
    @RequestMapping(value = "/loginLog", method = RequestMethod.POST)
    public void loginLog(@RequestParam("json") String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        System.out.println("/loginLog===>" + jsonStr);
        String message = "fail";
        String code = "400";
        JSONObject json = JSONObject.fromObject(jsonStr);

        try {
            String userName = json.getString("userName");
            String userType = json.getString("userType");
            String appVesionName = json.getString("appVesionName");
            String appMotifyTime = json.getString("appMotifyTime");
            String appVesionCode = json.getString("appVesionCode");
            String userIp = request.getRemoteAddr();
            Date date = String2Date(appMotifyTime);
            if (date != null) {
                UserLoginLog userLoginLog = new UserLoginLog(userName, userType, appVesionName, date, appVesionCode, userIp);
                userLoginLogServ.createLoginLog(userLoginLog);
                code = "200";
                message = "seccuss";
            } else {
                message = "修改时期格式错误";
            }
        } catch (Exception e) {
            e.printStackTrace();
            code = "500";
        }
        JSONObject result = new JSONObject();
        result.put("code",code);
        result.put("message",message);
        ajaxJson(result.toString(),response);
    }
}
