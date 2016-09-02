package com.vunke.shareHome.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.vunke.shareHome.model.EnumData;

public interface EnumDataMapper{

	  List<EnumData> selectByED(EnumData vo) throws DataAccessException;
	  
	  EnumData selectByKey(EnumData vo);
}
