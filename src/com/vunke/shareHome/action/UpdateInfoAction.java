package com.vunke.shareHome.action;

import com.vunke.shareHome.model.UpdateInfo;
import com.vunke.shareHome.service.UpdateInfoService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/8/30.
 */
@Controller
public class UpdateInfoAction extends BaseAction {
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    @Resource
    private UpdateInfoService updateInfoServ;

    @RequestMapping(value = "/getUpdateInfo",method = RequestMethod.POST)
    public void getUpdateInfo(@RequestParam("json") String jsonStr, HttpServletRequest request, HttpServletResponse  response) {
        String code = "400";
        String message  = "fail";
        logger.info("入参信息==>"+jsonStr);
        JSONObject result = new JSONObject();
        try {
            JSONObject json = JSONObject.fromObject(jsonStr);
            String type = json.getString("type");
            UpdateInfo updateInfo = updateInfoServ.findUpdateInfoByType(type);
            if (updateInfo!=null){
                String data = updateInfo.getData();
                result.put("data",data);
                code = "200";
                message = "success";
            }else {
                code = "400";
                message = "没有找到更新信息";
            }

        } catch (Exception e) {
            e.printStackTrace();
            code = "500";
            message = "系统错误";
        }
        result.put("code",code);
        result.put("message",message);

        logger.info("返回信息==>"+result);
        ajaxJson(result,response);
    }
}
