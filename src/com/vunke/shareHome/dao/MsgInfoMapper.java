package com.vunke.shareHome.dao;

import com.vunke.shareHome.model.MsgInfo;


public interface MsgInfoMapper {


	int insert(MsgInfo msg);
	String selectSequence();
	MsgInfo selectByPhone(MsgInfo msg);
	MsgInfo selectRandom(String msg);

	int deleteByMoblie(String moblie) throws Exception;
	
}
