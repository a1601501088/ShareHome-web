package com.vunke.contact.action;

import com.vunke.contact.model.ClientContact;
import com.vunke.contact.model.ShareContact;
import com.vunke.contact.model.UserFriend;
import com.vunke.contact.service.UserFriendService;
import com.vunke.shareHome.action.BaseAction;
import com.vunke.shareHome.model.UserInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2016/5/13.
 */
@Controller
public class UserFriendAction extends BaseAction {
    @Resource
    private UserFriendService userFriendServ;


    @RequestMapping(value = "/getUserFriend", method = RequestMethod.POST)
    public void getUserFriend(@RequestParam("json") String jsonStr, HttpServletResponse response, HttpServletRequest request) {
        System.out.println("getUserFriend-->" + jsonStr);
        String code = "400";
        String message = "fail";
        List<UserInfo> userInfos = null;
        List<UserFriend> userFriends = null;
        List<ShareContact> shareContacts = null;
        long startTime = System.currentTimeMillis();
        try {
            JSONObject json = JSONObject.fromObject(jsonStr);
            String userName = json.getString("userName");
            boolean has1 = json.has("shareContacts");
            if (has1) {
                JSONArray jsonArray = json.getJSONArray("shareContacts");
                shareContacts = JSONArray.toList(jsonArray, ShareContact.class);
            }


            JSONArray jsonArray2 = json.getJSONArray("clientContacts");
            List<ClientContact> clientContacts = JSONArray.toList(jsonArray2, ClientContact.class);

            Map<String, Object> map = new HashMap<>();
            map.put("shareContacts", shareContacts);
            map.put("clientContacts", clientContacts);

            //找到所有新的朋友(去重了)
            userInfos = userFriendServ.queryFriend(map);
            //CopyOnWriteArrayList<ClientContact> copyOnWriteArrayList = new CopyOnWriteArrayList(clientContacts);

            //创建新的朋友列表
            userFriends = new ArrayList<>();
            // System.out.println("userInfos:" + userInfos.size() + userInfos.toString());
            //  List<UserFriend> userFriendsRe = userFriendServ.queryFriendRe(userName, userLoginLogs);
            //Calendar calendar = Calendar.getInstance();
           /* for (UserFriend userFriend :userFriendsRe ) {
                // 2.数据库中有,新朋友中没有:此新朋友已添加
                //  UserFriend userFriend = userFriendsRe.get(i);
                userFriend.setStatus("1");
                userFriend.setObtainTime(calendar.getTime());
                if (!userFriends.contains(userFriend)){
                    userFriends.add(userFriend);
                }
                userFriendServ.modifyFriendById(userFriend);
            }*/
            String lastPhone = "";//记录上次的电话号码
            for (UserInfo userInfo : userInfos) {
                String loginUserName = userInfo.getUserDn();
                if (loginUserName.startsWith("11831726")) {
                    loginUserName = loginUserName.substring(8);
                }
                String subUserName = userInfo.getUserMobile();
                for (ClientContact clientContact : clientContacts) {
                    String phone = clientContact.getPhone();
                    if (lastPhone.equals(phone)) continue;
                    lastPhone = phone;
                    //找到在手机联系人中对应的手机号
                    if (subUserName.equals(phone)) {
                        //0未添加 1已添加 2已添加后要删除了
                        UserFriend userFriend = new UserFriend(userName, clientContact.getName(), loginUserName, "", "1", "");
                        /**
                         *  查找USER_FRIEND数据库，1.数据库中有,新朋友中也有:以前添加过，后来删除了，
                         *                     2.数据库中有,新朋友中没有:此新朋友已添加
                         *                     3.数据库中没有,新朋友中有:此新朋友未添加
                         */
                        //以新朋友账号和登录的用户账号为查询条件，查找USER_FRIEND数据库
                        UserFriend userFriendDB = userFriendServ.queryFriendByDB(userName, loginUserName);
                        //3.数据库中没有,新朋友中有:此新朋友未添加
                        if (null == userFriendDB) {
                            userFriend.setStatus("0");
                            userFriendServ.createFriend(userFriend);
                        } else {
                            // 1.数据库中有,新朋友中也有:以前添加过，后来删除了，
                            userFriend.setStatus("2");
                            userFriend.setFriendId(userFriendDB.getFriendId());
                            userFriend.setFriendHead(userFriendDB.getFriendHead());
                            userFriend.setRemark(userFriendDB.getRemark());
                            userFriendServ.modifyFriendById(userFriend);
                        }
                        if (!userFriends.contains(userFriend)) {
                            userFriends.add(userFriend);
                        }
                        //  copyOnWriteArrayList.remove(clientContact);
                    }

                }
            }
            code = "200";
            message = "success";
        } catch (Exception e) {
            e.printStackTrace();
            code = "500";
        }
        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("message", message);
        result.put("data", userFriends);
        long endTime =   System.currentTimeMillis();
        System.out.println("获取新的朋友所用时间:"+(endTime-startTime));
        System.out.println("userFriends----->"+result.toString());
        ajaxJson(result.toString(), response);
    }

    /**
     * 获取手机通讯录中的开能想家业务的联系人(二期)
     * @param jsonStr
     * @param response
     * @param request
     */
    @RequestMapping(value = "/getShareContacts", method = RequestMethod.POST)
    public void getShareContacts(@RequestParam("json") String jsonStr, HttpServletResponse response, HttpServletRequest request) {
        String code = "400";
        String message = "fail";
        List<UserInfo> userInfos = null;
        JSONObject json = JSONObject.fromObject(jsonStr);
        JSONArray jsonArray = json.getJSONArray("clientContacts");
        List<ClientContact> clientContacts = JSONArray.toList(jsonArray, ClientContact.class);
        //从手机联系人中筛选中想家联系人
        try {
            userInfos = userFriendServ.queryShareHomeContact(clientContacts);
            code = "200";
            message = "success";
        } catch (Exception e) {
            e.printStackTrace();
            code = "500";
        }
        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("message", message);
        result.put("shareContacts", userInfos);
        ajaxJson(result.toString(), response);
    }
    /*单文件上传*/
    @RequestMapping(value = "/uploadFile")
    public void uploadFile(@RequestParam("userName")String userName ,@RequestParam("txt") MultipartFile txt,HttpServletResponse response) {
        String code = "400";
        String message = "fail";
        System.out.println("uploadFile==>");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, +1);
        String originalFilename = txt.getOriginalFilename();
        if (txt != null && originalFilename != null && originalFilename.length() > 0) {
            try {
                //tomcat容器的上一级目录
                String pre_tomcat_path = System.getProperty("user.dir").replace("bin", "webapps").split("webapps")[0];
                String txt_path = pre_tomcat_path+"/ShareHomeLogger/"+userName+"/" + calendar.get(Calendar.YEAR) + "year/" + calendar.get(Calendar.MONTH) + "month/" + calendar.get(Calendar.DATE) + "day/" + calendar.get(Calendar.HOUR_OF_DAY) + "hours/";
                File file = new File(txt_path);
                if (!file.exists()) { //如果文件不存在创建文件夹
                    file.mkdirs(); //创建文件夹
                }
                String new_File_name = calendar.get(Calendar.MINUTE) + "分-" + calendar.get(Calendar.SECOND) + "秒-" + originalFilename;
                System.out.println("new_File_name:" + new_File_name);
                File newFile = new File(txt_path + new_File_name);
                //将内存中的数据写入磁盘
                System.out.println("log-file==>"+newFile.getAbsolutePath());
                txt.transferTo(newFile);
                 code = "200";
                 message = "success";
            } catch (IOException e) {
                e.printStackTrace();
                code = "500";
            }
        } else {
            code = "400";
            message = "txt == null || originalFilename == null || originalFilename.length() <= 0";
        }

        JSONObject result = new JSONObject();
        result.put("code",code);
        result.put("message",message);
        ajaxJson(result.toString(),response);
    }
/*多文件上传*/
    @RequestMapping(value = "/multipleFile")
    public void multipleFile(@RequestParam("userName")String userName ,HttpServletResponse response,HttpServletRequest request) {
        String code = "400";
        String message = "fail";
        System.out.println("uploadFile==>");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, +1);
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        boolean multipart = multipartResolver.isMultipart(request);
        if (multipart){
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while(iter.hasNext()){
                //记录上传过程起始时的时间，用来计算上传时间
                int pre = (int) System.currentTimeMillis();
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if(file != null){
                    //取得当前上传文件的文件名称
                    String originalFilename = file.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if(originalFilename!=null&&originalFilename.trim() !=""){
                        //重命名上传后的文件名
                        String fileName = calendar.get(Calendar.MINUTE) + "分-" + calendar.get(Calendar.SECOND) + "秒-" + originalFilename;
                        //定义上传路径
                        String pre_tomcat_path = System.getProperty("user.dir").replace("bin", "webapps").split("webapps")[0];
                        String txt_path = pre_tomcat_path+"/ShareHomeLogger/"+userName+"/" + calendar.get(Calendar.YEAR) + "year/" + calendar.get(Calendar.MONTH) + "month/" + calendar.get(Calendar.DATE) + "day/" + calendar.get(Calendar.HOUR_OF_DAY) + "hours/";
                        File file_path = new File(txt_path);
                        if (!file_path.exists()) { //如果文件不存在创建文件夹
                            file_path.mkdirs(); //创建文件夹
                        }
                        String path = txt_path + fileName;
                        File localFile = new File(path);
                        try {
                            file.transferTo(localFile);
                            code = "400";

                        } catch (IOException e) {
                            e.printStackTrace();
                             code = "500";
                        }
                    }
                }

                //记录上传该文件后的时间
                int finaltime = (int) System.currentTimeMillis();
                System.out.println("文件上传时间:"+(finaltime - pre));
            }
        }

        JSONObject result = new JSONObject();
        result.put("code",code);
        result.put("message",message);
        ajaxJson(result.toString(),response);
    }

}
