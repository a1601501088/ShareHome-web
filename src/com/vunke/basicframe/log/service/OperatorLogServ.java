package com.vunke.basicframe.log.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.vunke.basicframe.log.dao.OperatorLogMapperRe;
import com.vunke.basicframe.log.model.OperatorLog;

@Service("operLogServ")
public class OperatorLogServ {

	@Resource
	private OperatorLogMapperRe operatorLogMapperRe;
	
	public void createLog(OperatorLog vo){
		operatorLogMapperRe.insert(vo);
	}
	
}
