package com.vunke.shareHome.service.impl;

import com.vunke.shareHome.dao.MissedCallMapper;
import com.vunke.shareHome.model.MissedCall;
import com.vunke.shareHome.service.MissedCallService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/8/30.
 */
@Service("missedCallServ")
public class MissedCallServiceImpl implements MissedCallService{

    @Resource
    private MissedCallMapper missedCallMapper;


    @Override
    public int saveMissedCall(MissedCall missedCall) throws Exception{
        return  missedCallMapper.insertSelective(missedCall);

    }
}
