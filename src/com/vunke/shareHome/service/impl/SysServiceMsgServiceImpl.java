package com.vunke.shareHome.service.impl;

import com.vunke.shareHome.dao.SysServiceMsgMapper;
import com.vunke.shareHome.model.SysServiceMsg;
import com.vunke.shareHome.service.SysServiceMsgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
@Service("sysServiceMsgServ")
public class SysServiceMsgServiceImpl implements SysServiceMsgService{
    @Resource
    private SysServiceMsgMapper sysServiceMsgMapper;
    @Override
    public List<SysServiceMsg> queryServiceMsg() throws Exception {
        return sysServiceMsgMapper.queryServiceMsg();
    }
}
