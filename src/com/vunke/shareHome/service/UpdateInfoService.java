package com.vunke.shareHome.service;

import com.vunke.shareHome.model.UpdateInfo;

/**
 * Created by Administrator on 2016/8/30.
 */
public interface UpdateInfoService {
    UpdateInfo findUpdateInfoByType(String type) throws Exception;
}
