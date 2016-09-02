package com.vunke.shareHome.action;

import com.basicframe.utils.CommonUtil;
import com.vunke.basicframe.log.LogGPUtilRe;
import com.vunke.basicframe.log.model.OperatorLog;
import com.vunke.common.DataBase;
import com.vunke.shareHome.model.MsgInfo;
import com.vunke.shareHome.model.SysServiceMsg;
import com.vunke.shareHome.model.UserInfo;
import com.vunke.shareHome.service.OpenAcService;
import com.vunke.shareHome.service.SendMsg;
import com.vunke.shareHome.service.SysServiceMsgService;
import com.vunke.shareHome.service.UserInfoService;
import com.vunke.shareHome.util.Encrypt3DES;
import com.vunke.shareHome.util.UserTools;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class SendMsgAction extends BaseAction {

    @Resource
    private SendMsg sendMsgServ;

    @Resource
    private OpenAcService openAcServ;

    @Resource
    private LogGPUtilRe logGPUtilRe;

    @Resource
    private UserInfoService userInfoServ;

    @Resource
    private SysServiceMsgService sysServiceMsgServ;
    private OperatorLog log;

    /**
     * 获取从客户端传递过来的手机号码
     * 往该手机号发送一条短信
     *
     * @return 发送验证码
     */
    @RequestMapping(value = "/sendMsg/getCode", method = RequestMethod.POST)
    public void getPhone(HttpServletRequest req, HttpServletResponse resp) {
        String code = "400";
        String message = "fail";
        resp.setCharacterEncoding("UTF-8");
        try {
            String jsonStr = req.getParameter("json");
            JSONObject jsonObj = JSONObject.fromObject(jsonStr);
            String phone = jsonObj.getString("mobile");// 接收手机号码


            System.out.println("/sendMsg/getCode===>" + jsonStr.toString());
            if (null != phone && !"".equals(phone)) {
                MsgInfo param = new MsgInfo();
                param.setMobile_num(phone);
                param.setIf_alive("1");
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                String smsid = phone + sdf.format(new Date());
                Random random = new Random();
                String random_num = random.nextInt(1000000) + "";// 获取一个六位数的验证码
                if (random_num.length() == 5) random_num = random_num + "6";
                //两个号码用于测试,固定验证码为123456
                if (phone.equals("18216018635")||phone.equals("18216018636")){
                    random_num="123456";
                }

                MsgInfo msginfo = new MsgInfo();
                msginfo.setCreate_time(sdf.format(new Date()));
                msginfo.setMobile_num(phone);
                msginfo.setMsg_id(sendMsgServ.getMsgId());
                msginfo.setRandom_num(random_num);
                //插入数据库
                int i = sendMsgServ.create(msginfo);
                // 发送短信
                String msg = "您的验证码为：" + random_num + ",有效时间为10分钟。【天翼想家】";
                System.out.println(random_num);
                String send_code = sendMsgServ.sendSMS("073184203359", "abcd1234", phone, smsid, msg);
                System.out.println(send_code);
                if ("0".equals(send_code)) {
                    code = "200";
                    message = "success";
                } else {
                    code = "400";
                    message = "验证发送失败";
                }
            } else {
                code = "400";
                message = "手机号为空";
            }
        } catch (Exception e) {
            e.printStackTrace();
            code = "500";
            message = "fail";
        }
        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("message", message);
        ajaxJson(result.toString(), resp);
    }

    @RequestMapping(value = "/sendServiceMsg")
    public void sendServiceMsg(HttpServletRequest req, HttpServletResponse resp) {
        String code = "400";
        String message = "fail";
        String remoteAddr = req.getRemoteAddr();
        if (!remoteAddr.startsWith("192.168.198")) {
            System.out.println("ip非法:" + remoteAddr);
            return;
        }
        resp.setCharacterEncoding("UTF-8");
        Calendar calendar = Calendar.getInstance();
        String time = DateFormatUtils.format(calendar, "yyyy年MM月dd日 HH:mm:ss");
        try {

            String msgContent = req.getParameter("msgContent");
            /*JSONObject jsonObj = JSONObject.fromObject(jsonStr);
           // String phone = jsonObj.getString("mobile");// 接收手机号码
            String msgContent = "";
            if (jsonObj.has("msgContent")){
                msgContent = jsonObj.getString("msgContent");// msg
            }*/
            List<SysServiceMsg> sysServiceMsgs = sysServiceMsgServ.queryServiceMsg();
            if (sysServiceMsgs != null) {
                int size = sysServiceMsgs.size();
                int sumCode = 0;
                for (int i = 0; i < size; i++) {
                    SysServiceMsg sysServiceMsg = sysServiceMsgs.get(i);
                    String msg = sysServiceMsg.getMsgContent();
                    String msgPhone = sysServiceMsg.getMsgPhone();
                    String content = msgContent + (msg != null ? msg : "") + ",IP:" + remoteAddr + "," + time;
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                    String smsid = msgPhone + sdf.format(Calendar.getInstance().getTime());
                    String send_code = sendMsgServ.sendSMS("073184203359", "abcd1234", msgPhone, smsid, content);
                    System.out.println("send_code:" + send_code);
                    sumCode += Integer.parseInt(send_code);
                }
                if (sumCode == 0) {
                    code = "200";
                    message = "success";
                } else {
                    code = "400";
                    message = "验证发送失败";
                }
            } else {
                code = "400";
                message = "数据库没有配置这个手机号码，或手机号码状态无效";
            }
        } catch (Exception e) {
            e.printStackTrace();
            code = "500";
            message = "fail";
        }
        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("message", message);
        ajaxJson(result.toString(), resp);
    }

    /**
     * 1、收取用户发过来的验证码，找到数据库中那个有效的验证码
     * 2、验证验证码是否正确，正确返回success，错误返回fail
     *
     * @throws IOException 获取用户名和密码，然后去开户
     */
    @RequestMapping(value = "/sendMsg/signup", method = RequestMethod.POST)
    public void getSecurityCode(@RequestParam("json") String jsonStr, ModelMap modelMap, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        String message = "fail";
        String errorcode = "400";
        String data = "";
        JSONObject result = new JSONObject();
        try {
            JSONObject jsonObj = JSONObject.fromObject(jsonStr);
            String code = jsonObj.getString("smsCode");// 验证码
            String userType = jsonObj.getString("userType");//8 tv端 , 9手机端
            String username = jsonObj.getString("username");//用户名
            String password = jsonObj.getString("password");
            boolean has_requestCode = jsonObj.has("requestCode");
            String requestCode = "";
            if (has_requestCode) {
                requestCode = jsonObj.getString("requestCode");
            }
            if (!checkPass(password)) {
                result.put("code", "400");
                result.put("message", "密码格式错误");
                ajaxJson(result.toString(), resp);
                return;
            }

            String reqStr = "smsCode:" + code + ";userType" + userType + ";username" + username + password + requestCode;
            HttpSession session = req.getSession();
            String sessionStr = (String) session.getAttribute("reqStr");
            long lastTime = 0;
            String lastTime1 = session.getAttribute("lastTime") + "";
            System.out.println(lastTime1 + "lasttime1");
            if (!"".equals(lastTime1) && null != lastTime1 && !"null".equals(lastTime1)) {
                lastTime = Long.parseLong(lastTime1);
            }
            session.setAttribute("reqStr", reqStr);
            long currentTimeMillis = System.currentTimeMillis();
            session.setAttribute("lastTime", currentTimeMillis);
            System.out.println(reqStr + ":" + sessionStr);
            if (reqStr.equals(sessionStr) && currentTimeMillis - lastTime < 3000) {
                return;
            }

            System.out.println("/sendMsg/signup===>" + "smsCode:" + code + ";userType" + userType + ";username" + username);

            MsgInfo msginfo = sendMsgServ.queryRandom(username);
            if (null != msginfo) {
                if ("8".equals(userType) || "9".equals(userType)) {
                    if (msginfo.getRandom_num().equals(code)) {
                        //开户
                        Map<String, Object> map = openAc(jsonStr, req, resp);
                        result.putAll(map);
                        if ("success".equals(map.get("imsCode"))) {
                            message = "success";
                            errorcode = "200";
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.YEAR, +2);
                            String dateStr = DateFormatUtils.format(calendar, "yyyy年MM月dd日");

                            data = "恭喜您已获赠天翼想家电视版两年使用授权(10元/每月,价值240元),\n" + "到期时间:" + dateStr;
                            UserInfo user = new UserInfo("11831726" + userType + username, password, "01", "9".equals(userType) ? "0" : "1", username, req.getRemoteAddr());
                            user.setRequestCode(requestCode);
                            user.setOutTime(calendar.getTime());//过期时间
                            UserInfo userInfo = userInfoServ.queryUserInfo("11831726" + userType + username);
                            if (null == userInfo) {
                                userInfoServ.createUser(user);
                            } else {
                                userInfoServ.updateUserInfo(user);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            message = "error";
            errorcode = "500";
            e.printStackTrace();
        }

        System.out.println("返回的imsCode:" + result.get("imsCode"));
        result.put("code", errorcode);
        result.put("message", message);
        result.put("data", data);
        ajaxJson(result.toString(), resp);

    }

    /**
     * 验证验证码是否正确
     *
     * @param json
     * @param request
     * @param response
     */
    @RequestMapping(value = "/sendMsg/vaildateSmsCode", method = RequestMethod.POST)
    public void vaildateSmsCode(@RequestParam("json") String json, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        String code = "400";
        String message = "fail";
        try {
            JSONObject jsonObject = JSONObject.fromObject(json);
            String userName = jsonObject.getString("userName");
            String smsCode = jsonObject.getString("smsCode");
            MsgInfo msginfo = sendMsgServ.queryRandom(userName);
            if (null != msginfo && smsCode.equals(msginfo.getRandom_num())) {
                code = "200";
                message = "success";

            } else {
                message = "验证码错误";
            }
        } catch (Exception e) {
            e.printStackTrace();
            code = "500";
            message = "fail";
        }
        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("message", message);
        ajaxJson(result.toString(), response);
    }


    @RequestMapping(value = "/sendMsg/updatePass", method = RequestMethod.POST)
    public void updatePass(@RequestParam("json") String jsonStr, ModelMap modelMap, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        System.out.println("/sendMsg/updatePass===>" + jsonStr);
        String message = "fail";
        String errorcode = "400";

        JSONObject result = new JSONObject();
        try {
            JSONObject jsonObj = JSONObject.fromObject(jsonStr);
            String code = jsonObj.getString("smsCode");// 验证码
            String userType = jsonObj.getString("userType");//8 tv端 , 9手机端
            String username = jsonObj.getString("username");//用户名
            String password = jsonObj.getString("password");
            if (!checkPass(password)) {
                result.put("code", "400");
                result.put("message", "密码格式错误");
                ajaxJson(result.toString(), resp);
                return;
            }
            MsgInfo msginfo = sendMsgServ.queryRandom(username);
            if (null != msginfo)
                if ("8".equals(userType) || "9".equals(userType)) {
                    if (msginfo.getRandom_num().equals(code)) {
                        //开户
                        Map<String, Object> map = updatePass(jsonStr, req, resp);
                        result.putAll(map);
                        if ("success".equals(map.get("imsCode"))) {
                            message = "success";
                            errorcode = "200";

                        }
                    }
                }
        } catch (Exception e) {
            message = "error";
            errorcode = "500";
            e.printStackTrace();
        }

        System.out.println("返回的imsCode:" + result.get("imsCode"));
        result.put("code", errorcode);
        result.put("message", message);

        ajaxJson(result.toString(), resp);
    }

    public Map<String, Object> openAc(String json, HttpServletRequest request, HttpServletResponse response) throws Exception {


        Map<String, Object> result = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = JSONObject.fromObject(json);
            System.out.println("开户接口:---->" + json);
            String type = jsonObject.getString("userType");
            String phone = jsonObject.getString("username");
            String password = jsonObject.getString("password");


            //操作值
            String operatorValue = "type:" + type + ";phone:" + phone + ";password:" + password;
            //操作动作
            String operatorAction = "IMS,TMS开户";
            log = LogGPUtilRe.buildLog(operatorValue, operatorAction, request.getRemoteAddr(), "1");

            if ("8".equals(type) || "9".equals(type)) {
                String openTmsCode = openAcServ.openTms(type, phone, password);
                if ("0".equals(openTmsCode)){


                String imsCode = openAcServ.openIms(type, phone, password);


                System.out.println("执行后imsCode:" + imsCode + ";openTmsCode:" + openTmsCode);
                if ("0".equals(imsCode)) result.put("imsCode", "success");
                else result.put("imsCode", sendMsgServ.getResultDesc("ims", imsCode));
                System.out.println("imsCode:" + result.get("imsCode"));
                }else {
                    result.put("imsCode", "fail");
                }

            }
        } catch (Exception e) {
            throw e;
        } finally {
            logGPUtilRe.create(log);
        }
        return result;
    }


    public boolean checkPass(String pass) {
        if (pass.length() < 6 || pass.length() > 18) {
            return false;
        }
        String regEx1 = "^[`~!@#$%^&*()+=|{}':;',.<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]{1,}$";
        String regEx2 = "^[0-9]{1,}$";
        String regEx3 = "^[a-z]{1,}$";
        String regEx4 = "^[A-Z]{1,}$";
        int i = 0;
        if (pass.matches(regEx1))
            i++;
        if (pass.matches(regEx2))
            i++;
        if (pass.matches(regEx3))
            i++;
        if (pass.matches(regEx4))
            i++;
        return i == 0;
    }



    public Map<String, Object> updatePass(String json, HttpServletRequest request, HttpServletResponse response) {
        OperatorLog log = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = JSONObject.fromObject(json);
            String type = jsonObject.getString("userType");
            String phone = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            System.out.println("phone:" + phone);
            System.out.println("type:" + type);
            //操作值
            String operatorValue = "type:" + type + ";phone:" + phone;
            //操作动作
            String operatorAction = "IMS,TMS修改密码";
            log = LogGPUtilRe.buildLog(operatorValue, operatorAction, request.getRemoteAddr(), "1");

            if ("8".equals(type) || "9".equals(type)) {
                String updateTmsCode = openAcServ.updateTms(type, phone, password);
                //TMS  succees
                if ("0".equals(updateTmsCode)) {

                    String imsCode = openAcServ.updateIms(type, phone, password);

                    if ("0".equals(imsCode)) {
                        result.put("imsCode", "success");
                        UserInfo user = new UserInfo("11831726" + type + phone, password, "01", "9".equals(type) ? "0" : "1", phone, request.getRemoteAddr());
                        userInfoServ.modify(user);
                    }
                    else {
                        result.put("imsCode", sendMsgServ.getResultDesc("ims", imsCode));
                    }

                }else {
                    result.put("imsCode", "fail");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logGPUtilRe.create(log);
        }
        return result;
    }


    /**
     * 用于新版本无需密码登录
     *
     * @param json
     * @param request
     * @param response
     */
    @RequestMapping(value = "/sendMsg/login", method = RequestMethod.POST)
    public void login(@RequestParam("json") String json, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        String code = "400";
        String message = "fail";
        String encodePass = "";
        String data = "";
        try {
            JSONObject jsonObject = JSONObject.fromObject(json);
            String phone = jsonObject.getString("username");
            String smsCode = jsonObject.getString("smsCode");
            String userType = jsonObject.getString("userType");
            boolean isIos = jsonObject.has("ios");

            MsgInfo msginfo = sendMsgServ.queryRandom(phone);
            if (null != msginfo) {
                String create_time = msginfo.getCreate_time();
                Date date = DateUtils.parseDate(create_time, new String[]{"yyyy-MM-dd.HH.mm.ss.SS"});
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, -10);
                Date time = calendar.getTime();

                if (time.after(date)) {//大于10分钟
                    code = "400";
                    message = "验证码已过期，请重新发送验证码";
                }
                //验证码未过期
                else {

                    if (smsCode.equals(msginfo.getRandom_num())) {
                        code = "200";
                        message = "success";
                        //修改密码 , 随机生成8位数密码（小写字母与数字）
                        String password = "a1"+CommonUtil.getRandomString(8);
                        System.out.println("password-->"+password);
                        jsonObject.put("password", password);
                        if (isIos){
                            //ios密码加密
                            encodePass = UserTools.encodeInfo(password, DataBase.KEY_IOS);
                        }else {
                            //android密码加密
                            encodePass = Encrypt3DES.getInstance().encrypt(password);
                        }
                        Map<String, Object> map = updatePass(jsonObject.toString(), request, response);
                        //修改成功
                        if ("success".equals(map.get("imsCode"))) {
                            message = "success";
                            code = "200";
                            UserInfo user = new UserInfo("11831726" + userType + phone, password, "01", "9".equals(userType) ? "0" : "1", phone, request.getRemoteAddr());
                            userInfoServ.modify(user);
                            //删除与此账号相关的验证码
                            sendMsgServ.deleteByMoblie(phone);
                        }
                        //修改失败，调用开户
                        else {
                            //开户
                            Map<String, Object> map2 = openAc(jsonObject.toString(), request, response);

                            if ("success".equals(map2.get("imsCode"))) {
                                message = "success";
                                code = "200";
                                //删除与此账号相关的验证码
                                sendMsgServ.deleteByMoblie(phone);
                                calendar = Calendar.getInstance();
                                calendar.add(Calendar.YEAR, +2);
                                String dateStr = DateFormatUtils.format(calendar, "yyyy年MM月dd日");
                                data = "恭喜您已获赠天翼想家电视版两年使用授权(10元/每月,价值240元),\n" + "到期时间:" + dateStr;
                                UserInfo user = new UserInfo("11831726" + userType + phone, password, "01", "9".equals(userType) ? "0" : "1", phone, request.getRemoteAddr());
                                //user.setRequestCode(requestCode);
                                user.setOutTime(calendar.getTime());//过期时间

                                UserInfo userInfo = userInfoServ.queryUserInfo("11831726" + userType + phone);
                                if (null == userInfo) {
                                    userInfoServ.createUser(user);
                                } else {
                                    userInfoServ.updateUserInfo(user);
                                }
                            } else {
                                encodePass = "";
                                message = "修改密码与注册均失败，请联系管理员";
                                code = "400";
                            }
                        }
                    } else {
                        message = "验证码错误";
                        code = "400";
                    }

                }

            } else {
                message = "请先发送验证码";
            }


        } catch (Exception e) {
            e.printStackTrace();
            code = "500";
            message = "fail";
        }


        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("message", message);
        result.put("encodePass", encodePass);
        result.put("data", data);
        System.out.println(result.toString());
        ajaxJson(result.toString(), response);
    }

}
