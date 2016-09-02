package com.vunke.shareHome.dao;

import com.vunke.shareHome.model.UpdateInfo;

public interface UpdateInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(UpdateInfo record);

    int insertSelective(UpdateInfo record);

    UpdateInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UpdateInfo record);

    int updateByPrimaryKey(UpdateInfo record);

    UpdateInfo findUpdateInfoByType(String type);
}