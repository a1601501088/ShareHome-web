package com.vunke.shareHome.dao;


import com.vunke.shareHome.model.SysServiceMsg;

import java.util.List;

public interface SysServiceMsgMapper {
    int insert(SysServiceMsg record);

    int insertSelective(SysServiceMsg record);

    List<SysServiceMsg> queryServiceMsg();
}