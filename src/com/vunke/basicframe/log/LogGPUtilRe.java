package com.vunke.basicframe.log;

import com.vunke.basicframe.log.dao.OperatorLogMapperRe;
import com.vunke.basicframe.log.model.OperatorLog;
import com.vunke.basicframe.log.service.OperatorLogServ;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>Description: 日志工具类</p>
 *
 * <p>Copyright: Copyright (c) 2011</p>
 *
 * <p>Company: xmp</p>
 *
 * @author 唐颖杰
 * @version 1.0
 */
@Component("logGPUtilRe")
public class LogGPUtilRe {
	
	@Resource
	private OperatorLogMapperRe operatorLogMapperRe;
	
	@Resource
	private OperatorLogServ operLogServ;
	
	/**
	 * 日志单独提交
	 */
	public void create(OperatorLog vo){
		operLogServ.createLog(vo);
	}

	/**
	 * 与业务一起提交
	 */
	public void insert(OperatorLog vo){
		operatorLogMapperRe.insert(vo);
	}
	
	
	public static OperatorLog buildLog(String operatorValue,String operatorAction,String ip,String logType){
		OperatorLog operLog = new OperatorLog();
		operLog.setOperatorAction(operatorAction);
		operLog.setOperatorValue(operatorValue);
		operLog.setLogType(logType);//正常日志还是异常日志
		operLog.setOperatorIP(ip);
		operLog.setOperatorTime(new Date().toLocaleString());
		return operLog;
	}
}
