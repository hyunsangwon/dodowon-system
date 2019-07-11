package com.dodo.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dodo.system.service.DocsService;
import com.dodo.system.service.EmpService;
import com.dodo.system.vo.HolidayVO;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/home/docs")
public class DocsController {
    /*휴가,출장 문서 등록,업데이트,삭제,조회 */
	
	private static final String VIEW_PREFIX = "emp/";

	@Autowired
    private EmpService empService;

	@Autowired
	private DocsService docsService;
	
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

        model.addAttribute("roleName",request.getAttribute("role_name"));
        /*잘못된 휴가 입력,잘못된 연락처 입력*/
        if (br.hasErrors()) {
            return VIEW_PREFIX+"holiday";
        }
        if(!empService.isEmpHolidayCheck(holidayVO)){
            br.rejectValue("holiday_end", "holidayVO.holiday_end", "남은 휴가일수 보다 많이 입력하셨습니다.");
            return VIEW_PREFIX+"holiday";
        }
        
        docsService.saveHolidayDocs(holidayVO);
        
        return "admin/admin-home";
    }

}
