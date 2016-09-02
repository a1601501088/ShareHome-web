package com.vunke.contact.dao;


import com.vunke.contact.model.ClientContact;
import com.vunke.contact.model.UserFriend;
import com.vunke.shareHome.model.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserFriendMapper {
    int insert( List<UserFriend> record);

    int insertSelective(UserFriend record);

    List<UserInfo> queryFriend(Map<String,Object> map);

    UserFriend queryFriendByDB(Map<String, Object> map);

    int updateFriendById(UserFriend userFriend);

    List<UserFriend> queryFriendRe(Map<String, Object> map);

    List<UserInfo> queryShareHomeContact(List<ClientContact> list);

}