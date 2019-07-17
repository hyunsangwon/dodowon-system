package com.dodo.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dodo.system.vo.EmpVO;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Repository
public interface AdminMapper {
	
	public int totalCntEmp ();
	
	public List<EmpVO> EmpList( @Param("limitcount") int limitcount,
			 					@Param("contentnum") int contentnum);
}
