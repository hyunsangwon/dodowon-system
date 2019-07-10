package com.dodo.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/docs")
public class DocsController {
    /*휴가,출장 문서 등록,업데이트,삭제,조회 */
	
	private static final String VIEW_PREFIX = "emp/";
	
    @GetMapping("/reg-holiday")
    public String loadHolidayPage(ModelMap model,HttpServletRequest request) throws Exception{
    	
    	 model.addAttribute("roleName",request.getAttribute("role_name"));
    	 model.addAttribute("empVO",request.getAttribute("emp_vo"));
    	 
    	 return VIEW_PREFIX+"holiday";
    }
    
    @GetMapping("/reg-trip")
    public String loadTripPage(ModelMap model,HttpServletRequest request) throws Exception{
    	 model.addAttribute("roleName",request.getAttribute("role_name"));   	
    	 return VIEW_PREFIX+"trip";
    }
    
}
