package com.vunke.shareHome.service;

import com.vunke.shareHome.model.MissedCall;

/**
 * Created by Administrator on 2016/8/30.
 */
public interface MissedCallService {
        int saveMissedCall(MissedCall missedCall) throws Exception;
}
