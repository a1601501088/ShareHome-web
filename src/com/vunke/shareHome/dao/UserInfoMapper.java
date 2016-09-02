package com.vunke.shareHome.dao;

import com.vunke.shareHome.model.UserInfo;

import java.util.List;


public interface UserInfoMapper {

	int insert(UserInfo user);
	
	int updatePass(UserInfo user);

	UserInfo queryUserInfo(String userDn);

	int updateUserInfo(UserInfo userInfo);

	List<UserInfo> findUserInfoByUserMoblie(String moblie);
}