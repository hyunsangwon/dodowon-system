package com.dodo.system.mapper;

import org.springframework.stereotype.Repository;

import com.dodo.system.vo.HolidayVO;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Repository
public interface DocsMapper {
	
	public int setHoliday(HolidayVO holidayVO);
	
}
