package com.vunke.shareHome.service;

import com.vunke.shareHome.dao.UserInfoMapper;
import com.vunke.shareHome.model.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userInfoServ")
public class UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 用于插入用户信息
     */
    public boolean createUser(UserInfo vo) {
        int count = userInfoMapper.insert(vo);
        return count < 1 ? false : true;
    }

    /**
     * 用于修改密码
     *
     * @param vo 包含用户名跟密码
     * @return 是否修改成功
     */
    public boolean modify(UserInfo vo) {
        int count = userInfoMapper.updatePass(vo);
        return count < 1 ? false : true;
    }

    public UserInfo queryUserInfo(String userDn) {
        return userInfoMapper.queryUserInfo(userDn);
    }

    public int updateUserInfo(UserInfo userInfo) {
        return userInfoMapper.updateUserInfo(userInfo);
    }

    public static boolean checkChar(char src) {
        boolean flag = false;
        switch (src) {
            case '\"':
                flag = true;
                break;
            case '<':
                flag = true;
                break;
            case '>':
                flag = true;
                break;
            case '\'':
                flag = true;
                break;
            case '&':
                flag = true;
                break;
            case '%':
                flag = true;
                break;
            case '_':
                flag = true;
                break;
            case '#':
                flag = true;
                break;
            case '?':
                flag = true;
                break;
        }
        return flag;
    }

    /**
     * 查找号码是否在user_info表中
     */
    public List<UserInfo> findUserInfoByUserMoblie(String moblie) throws Exception{
        List<UserInfo> userInfoByUserMoblie = userInfoMapper.findUserInfoByUserMoblie(moblie);
       return userInfoByUserMoblie;

    }
}
