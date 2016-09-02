package com.vunke.shareHome.service;

import com.vunke.shareHome.model.UserLoginLog;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */

public interface UserLoginLogService {
     int createLoginLog(UserLoginLog userLoginLog) throws Exception;
     List<UserLoginLog> findUserLoginLog(String username)throws Exception;
}
