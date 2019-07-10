package com.dodo.system.service;

import com.dodo.system.mapper.EmpMapper;
import com.dodo.system.vo.HolidayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Service
public class EmpService {

    @Autowired
    private EmpMapper empMapper;

    public boolean isEmpHolidayCheck(HolidayVO holidayVO) throws Exception{

        String startDay = holidayVO.getHoliday_start();
        String endDay = holidayVO.getHoliday_end();
        /* -처리 */


        return true;
    }

}

