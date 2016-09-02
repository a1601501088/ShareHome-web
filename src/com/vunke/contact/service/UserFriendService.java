package com.vunke.contact.service;

import com.vunke.contact.model.ClientContact;
import com.vunke.contact.model.UserFriend;
import com.vunke.shareHome.model.UserInfo;
import com.vunke.shareHome.model.UserLoginLog;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/12.
 */
public interface UserFriendService {

    int createFriend(UserFriend userFriend) throws Exception;
    int modifyFriendById(UserFriend userFriend) throws  Exception;
    List<UserInfo> queryFriend(Map<String,Object> map) throws Exception;


    UserFriend queryFriendByDB(String userName, String loginUserName) throws Exception;

    List<UserFriend> queryFriendRe(String userName,List<UserLoginLog> userLoginLogs) throws Exception;

    List<UserInfo> queryShareHomeContact(List<ClientContact> map) throws Exception;

}
