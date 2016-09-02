package com.vunke.shareHome.dao;


import com.vunke.shareHome.model.UserLoginLog;

import java.util.List;

public interface UserLoginLogMapper {
    int insert(UserLoginLog record);

    int insertSelective(UserLoginLog record);

    List<UserLoginLog> findUserLoginLog(String username);
}