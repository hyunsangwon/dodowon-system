package com.dodo.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dodo.system.mapper.DocsMapper;
import com.dodo.system.vo.HolidayVO;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Service
public class DocsService {

	@Autowired
	private DocsMapper docsMapper;
	
	public int saveHolidayDocs(HolidayVO holidayVO) throws Exception{
		return docsMapper.setHoliday(holidayVO);
	}


}
