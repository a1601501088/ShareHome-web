package com.vunke.shareHome.service.impl;

import com.vunke.shareHome.dao.UpdateInfoMapper;
import com.vunke.shareHome.model.UpdateInfo;
import com.vunke.shareHome.service.UpdateInfoService;
import com.vunke.shareHome.util.Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/8/30.
 */
@Service("updateInfoServ")
public class UpdateInfoServiceImpl implements UpdateInfoService {
    @Resource
    private UpdateInfoMapper updateInfoMapper;

    @Override
    public UpdateInfo findUpdateInfoByType(String type) throws Exception {
        if (Util.isEmpty(type)){
           return null;
        }
        return  updateInfoMapper.findUpdateInfoByType(type);

    }
}
