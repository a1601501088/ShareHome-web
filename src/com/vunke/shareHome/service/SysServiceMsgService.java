package com.vunke.shareHome.service;

import com.vunke.shareHome.model.SysServiceMsg;

import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
public interface SysServiceMsgService {
    List<SysServiceMsg> queryServiceMsg() throws  Exception;
}
