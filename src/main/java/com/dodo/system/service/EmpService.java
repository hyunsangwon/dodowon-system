package com.dodo.system.service;

import com.dodo.system.mapper.EmpMapper;
import com.dodo.system.vo.EmpVO;
import com.dodo.system.vo.HolidayVO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Service
public class EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public int updateMyInfo(EmpVO empVO) {   	
    	return empMapper.updateMyInfo(empVO);
    }
   
    public EmpVO findByEmpId(String id) {
    	return empMapper.findByEmpId(id);
    }
    
    public EmpVO getImgName(int no) {
    	return empMapper.getImgName(no);
    }
    
    public int updateImage(int no,String sign_img_name) {
    	return empMapper.updateMyImage(no,sign_img_name);
    }
    
    public int updateMyPassword(EmpVO empVO) {
    	
    	empVO.setPassword(bCryptPasswordEncoder.encode(empVO.getPassword()));	
    	return empMapper.updateMyPassword(empVO);
    }
    
    public boolean isEmpHolidayCheck(HolidayVO holidayVO) throws Exception{

        String startDay = holidayVO.getHoliday_start();
        String endDay = holidayVO.getHoliday_end();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = formatter.parse(startDay);
        Date endDate = formatter.parse(endDay);

        // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
        long diff = endDate.getTime() - beginDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        
        EmpVO empVO = empMapper.findByEmpId(holidayVO.getEmp_id());
                   
        if(empVO.getHoliday() < diffDays) { //남은 휴가보다 많이 입력함
        	return false;
        }

        return true;
    }

    public List<EmpVO> empFindAll(){
        return empMapper.empFindAll();
    }

}

