package com.vunke.shareHome.service;

import org.springframework.stereotype.Service;


@Service("openAcServ")
public class OpenAcService {

	
	public String openIms(String code, String phone, String password){
		Ims ims = new Ims();
		String resultCode = ims.openIms(code, phone, password);	
			return resultCode ;
	}
		
	public String openTms(String code, String phone, String password){
		TMS tms = new TMS();
		return tms.openTms(code, phone, password);
	}
	
	public String updateIms(String code, String phone, String password) throws Exception{
		Ims ims = new Ims();
		String resultCode = ims.updatePass(code, phone, password);
		return resultCode ;
	}

	public String updateTms(String code, String phone, String password) throws Exception{
		TMS tms = new TMS();
		return tms.updatePass(code, phone, password);
	}
}
