package com.dodo.system.service;

import com.dodo.system.mapper.EmpMapper;
import com.dodo.system.vo.EmpVO;
import com.dodo.system.vo.HolidayVO;

import java.text.SimpleDateFormat;
import java.util.Date;

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
       
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date FirstDate = format.parse(startDay);
        Date SecondDate = format.parse(endDay);
        
        long calDateDays = (FirstDate.getTime() - SecondDate.getTime())/ ( 24*60*60*1000);
        int inputHoliday = (int) Math.abs(calDateDays);//form에서 넘겨 받은 휴가일
        
        EmpVO empVO = empMapper.findByEmpId(holidayVO.getEmp_id());
                   
        if(empVO.getHoliday() < inputHoliday) { //남은 휴가보다 많이 입력함
        	return false;
        }

        return true;
    }
    

}

