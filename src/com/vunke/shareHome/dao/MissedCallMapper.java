package com.vunke.shareHome.dao;

import com.vunke.shareHome.model.MissedCall;

public interface MissedCallMapper {
    int deleteByPrimaryKey(String id);

    int insert(MissedCall record);

    int insertSelective(MissedCall record);

    MissedCall selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MissedCall record);

    int updateByPrimaryKey(MissedCall record);
}