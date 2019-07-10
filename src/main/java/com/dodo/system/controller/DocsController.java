package com.dodo.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.dodo.system.vo.EmpVO;
import com.dodo.system.vo.HolidayVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/home/docs")
public class DocsController {
    /*휴가,출장 문서 등록,업데이트,삭제,조회 */
	
	private static final String VIEW_PREFIX = "emp/";
	
    @GetMapping("/reg-holiday")
    public String loadHolidayPage(ModelMap model, HttpServletRequest request,
                                  @ModelAttribute("holidayVO") HolidayVO holidayVO) throws Exception{
    	
    	 model.addAttribute("roleName",request.getAttribute("role_name"));
    	 model.addAttribute("empVO",request.getAttribute("emp_vo"));
    	 
    	 return VIEW_PREFIX+"holiday";
    }
    
    @GetMapping("/reg-trip")
    public String loadTripPage(ModelMap model,HttpServletRequest request) throws Exception{
    	 model.addAttribute("roleName",request.getAttribute("role_name"));   	
    	 return VIEW_PREFIX+"trip";
    }

    @PostMapping("/reg-holiday")
    public String doHolidayReg(ModelMap model, HttpServletRequest request,
                               @Valid @ModelAttribute("holidayVO") HolidayVO holidayVO,
                               BindingResult br) throws Exception{

        /*잘못된 휴가 입력,잘못된 연락처 입력*/


        model.addAttribute("roleName",request.getAttribute("role_name"));
        return VIEW_PREFIX+"holiday";
    }

}
