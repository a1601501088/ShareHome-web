package com.vunke.basicframe.log.dao;

import com.vunke.basicframe.log.model.OperatorLog;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;


/**
 * <p>Description: 操作日志Mapper接口</p>
 *
 * <p>Copyright: Copyright (c) 2011</p>
 *
 * <p>Company: xmp</p>
 *
 * @author 唐颖杰
 * @version 1.0
 */
public interface OperatorLogMapperRe {
	
	/**
     * 根据条件查询分页数据的总数
     * 
     * @param map 
     * 			分页条件参数
     * @return 总条数
     * @throws DataAccessException
     * @author 唐颖杰
     * @date： 2011-8-20
     * @modify：
     */
    public int selectTotalRows(Map<String, Object> map) throws DataAccessException;
    
    /**
     * 根据条件查询分页数据
     * 
     * @param map 
     * 			分页条件参数
     * @return 分页数据List
     * @throws DataAccessException
     * @author 唐颖杰
     * @date： 2011-8-20
     * @modify：
     */
    public List<OperatorLog> selectPageList(Map<String, Object> map) throws DataAccessException;
    
    
	/**
	 * 保存实体对象
	 * 
	 * @param vo 
	 * 			实体对象
	 * @return 受影响的行数
	 * @throws DataAccessException
	* @author 唐颖杰
	 * @date： 2011-8-20
	 * @modify：
	 */
	public int insert(OperatorLog vo) throws DataAccessException;
	
	
	
}
