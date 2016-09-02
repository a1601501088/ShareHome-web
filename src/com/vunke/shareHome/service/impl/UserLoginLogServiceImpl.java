package com.vunke.shareHome.service.impl;

import com.vunke.shareHome.dao.UserLoginLogMapper;
import com.vunke.shareHome.model.UserLoginLog;
import com.vunke.shareHome.service.UserLoginLogService;
import com.vunke.shareHome.util.Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
@Service("userLoginLogServ")
public class UserLoginLogServiceImpl implements UserLoginLogService {
    @Resource
    private UserLoginLogMapper userLoginLogMapper;
    public int createLoginLog(UserLoginLog userLoginLog) throws Exception{
        return userLoginLogMapper.insertSelective(userLoginLog);
    }

    @Override
    public List<UserLoginLog> findUserLoginLog(String username) throws Exception {
        if (Util.isEmpty(username)){
            throw  new RuntimeException("账号不能为空");
        }
        username = "%"+username+"%";
        return   userLoginLogMapper.findUserLoginLog(username);

    }
}
