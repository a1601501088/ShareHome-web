package com.vunke.shareHome.action;

import com.vunke.contact.model.Contact;
import com.vunke.contact.service.ContactCoreService;
import com.vunke.shareHome.model.MissedCall;
import com.vunke.shareHome.model.TemplateText;
import com.vunke.shareHome.model.UserInfo;
import com.vunke.shareHome.model.UserLoginLog;
import com.vunke.shareHome.service.*;
import com.vunke.shareHome.util.Util;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/8/29.
 * 未接来电提醒
 */
@Controller
public class MissedCallAction extends BaseAction {
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    @Resource
    private MissedCallService missedCallServ;
    @Resource
    private TemplateTextService TemplateTextServ;
    @Resource
    private ContactCoreService conCoreServ;
    @Resource
    private SendMsg sendMsgServ;
    @Resource
    private UserInfoService userInfoServ;
    @Resource
    private UserLoginLogService userLoginLogServ;

    /**
     * 2016-8-30
     * 未接来电提醒接口 用于给客户端调用
     */
    @RequestMapping(value = "/missedCall", method = RequestMethod.POST)
    public void missedCall(@RequestParam("json") String jsonStr, HttpServletRequest request, HttpServletResponse response) {
        String code = "400";
        String message = "fail";
        String sessionId = "";
        //获取客户端传来的参数，并json解析
        logger.info("接收客户端传来的数据==>" + jsonStr);
        JSONObject json = JSONObject.fromObject(jsonStr);
        String callingPhone = json.getString("callingPhone");
        String callingName = "未知号码";
        String calledPhone = json.getString("calledPhone");
        String calledName = json.getString("calledName");
        String callTime = json.getString("callTime");
        String callingIp = request.getRemoteAddr();
        String callingType = json.getString("callingType");
        String calledType = json.getString("calledType");

        try {
            long call_time = Long.parseLong(callTime);
            callTime = DateFormatUtils.format(call_time, "yyyy-MM-dd HH:mm:ss");
        } catch (NumberFormatException e) {

        }
        //主叫号码与被叫号码相同
        if (callingPhone.equals(calledPhone)) {
            code = "400";
            message = "主叫号码不能与被叫相同";
        } else {
            //查找未接来电模版信息条件
            String titile = "未接来电提醒";
            String actionType = "1";
            try {
                //判断传来的被叫号码是否为手机号码
                boolean isPhone = Util.isPhone(calledPhone);
                logger.info("被叫号码是否是正确的手机号码==>" + isPhone);
                //如果是手机号码
                if (isPhone) {
                    //从联系人数据库中找出主叫号码在被叫号码联系人中的姓名
                    Contact contact = conCoreServ.findContact(calledPhone, callingPhone);

                    if (contact != null) {
                        callingName = contact.getContactName();
                    }

                    //判断此号码是否为想家应用号码
                    boolean isShareHomeNumber = isShareHomeNumber(calledPhone);
                    logger.info("被叫号码是否是想家应用号码==>" + isShareHomeNumber);
                    TemplateText templateText = null;
                    //如果是想家应用号码
                    if (isShareHomeNumber) {
                        //从数TEMPLATE_TEXT表中查询title="未接来电提醒"并action_type="1"的数据
                        actionType = "1";
                    }
                    //如果不是想家应用号码
                    else {
                        //从数TEMPLATE_TEXT表中查询title="未接来电提醒"并action_type="2"的数据
                        actionType = "2";
                    }
                    //从数TEMPLATE_TEXT表中查询
                    templateText = TemplateTextServ.findTemplate(titile, actionType);
                    //如果从TEMPLATE_TEXT表找到此数据
                    if (templateText != null) {
                        String contentMsg = templateText.getContentMsg();
                        contentMsg = contentMsg.replace("${callingPhone}", callingPhone)
                                .replace("${callingName}", callingName).replace("${calledPhone}", calledPhone)
                                .replace("${calledName}", calledName).replace("${callingType}", callingType.equals("9")?"手机":"电视")
                                .replace("${calledType}", calledType.equals("9")?"手机":"电视").replace("${callTime}", callTime);
                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                        String smsid = calledPhone + sdf.format(Calendar.getInstance().getTime());
                        //使用超级信使发送短信给用户
                        String send_code = sendMsgServ.sendSMS("073184203359", "abcd1234", calledPhone, smsid, contentMsg);

                        if ("0".equals(send_code)) {
                            code = "200";
                            message = "success";
                            sessionId = Util.getUUID();
                            //把发送信息插入数据库
                            try {
                                MissedCall missedCall = new MissedCall(sessionId, callingPhone, callingName, calledPhone, calledName, callTime, callingIp, callingType, calledType);
                                System.out.println(missedCall.toString());
                                missedCallServ.saveMissedCall(missedCall);
                            } catch (Exception e) {
                                e.printStackTrace();
                                sessionId = "";
                                logger.info("未接电话提醒数据保存失败");
                            }
                        } else {
                            code = "400";
                            message = "短信发送失败";
                        }

                    }
                    //如果没有从TEMPLATE_TEXT表找到此数据
                    else {
                        code = "400";
                        message = "没有找到对应的模版信息";
                    }

                }
                //如果不是手机号码
                else {
                    code = "400";
                    message = "被叫号码不是正确的手机号码";
                }
            } catch (Exception e) {
                e.printStackTrace();
                code = "500";
                message = "系统错误";
            }
        }
        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("message", message);
        result.put("sessionId", sessionId);
        logger.info("返回信息==>"+result.toString());
        ajaxJson(result.toString(), response);
    }

    /**
     * 判断是否为想家号码
     *
     * @return
     */
    public boolean isShareHomeNumber(String str) {
        //先查user_info表
        List<UserInfo> userInfoByUserMoblie = null;
        try {
            userInfoByUserMoblie = userInfoServ.findUserInfoByUserMoblie(str);

            if (userInfoByUserMoblie.size() > 0) {
                return true;
            }

            //如果没有查到，则查USER_LOGIN_LOG表
            List<UserLoginLog> userLoginLogs = userLoginLogServ.findUserLoginLog(str);
            if (userLoginLogs.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }
}
