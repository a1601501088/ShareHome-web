package com.vunke.contact.service.impl;

import com.vunke.contact.dao.UserFriendMapper;
import com.vunke.contact.model.ClientContact;
import com.vunke.contact.model.UserFriend;
import com.vunke.contact.service.UserFriendService;
import com.vunke.shareHome.model.UserInfo;
import com.vunke.shareHome.model.UserLoginLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/12.
 */
@Service("userFriendServ")
public class UserFriendServiceImpl implements UserFriendService {
    @Resource
    private UserFriendMapper userFriendMapper;

    @Override
    public int createFriend(UserFriend userFriend) throws Exception {
       int insert = userFriendMapper.insertSelective(userFriend);
        return insert;
    }

    @Override
    public int modifyFriendById(UserFriend userFriend) throws Exception{
        return userFriendMapper.updateFriendById(userFriend);
    }

    @Override
    public List<UserInfo> queryFriend(Map<String, Object> map) throws Exception {
        return userFriendMapper.queryFriend(map);
    }

    @Override
    public UserFriend queryFriendByDB(String userName, String loginUserName) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("userName",userName);
        map.put("friendAccount",loginUserName);
        return userFriendMapper.queryFriendByDB(map);
    }

    @Override
    public List<UserFriend> queryFriendRe(String userName,List<UserLoginLog> userLoginLogs) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("userName",userName);
        map.put("friendAccounts",userLoginLogs);
        return userFriendMapper.queryFriendRe(map);
    }

    @Override
    public List<UserInfo> queryShareHomeContact(List<ClientContact> list) throws Exception {
        return userFriendMapper.queryShareHomeContact(list);
    }
}
