package com.dodo.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dodo.system.service.AdminService;
import com.dodo.system.service.HomeService;
import com.dodo.system.vo.EmpVO;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    /*관리자 기능, 유저등록,삭제*/
	@Autowired
	private AdminService adminService;
	@Autowired
	private HomeService homeService;
	
	private static final String VIEW_PREFIX = "admin/";
	
    @GetMapping("/home")
    public String loadHomePage(ModelMap model,HttpServletRequest request) throws Exception{
    	 
    	 model.addAttribute("roleName",request.getAttribute("role_name"));
    	 adminService.tripList(model,1); 
    	 return VIEW_PREFIX+"admin-home";
    }

    @GetMapping("/user/sign-up")
    public String loadSignUpPage(@ModelAttribute("empVO") EmpVO empvo,
    		ModelMap model,HttpServletRequest request) throws Exception{
    	
    	model.addAttribute("roleName",request.getAttribute("role_name"));
    	return VIEW_PREFIX+"admin-signup";
    }
    
    @PostMapping("/user/sign-up")
    public String doSignUp(@Valid @ModelAttribute("empVO") EmpVO empvo,
    		BindingResult br,HttpServletRequest request,ModelMap model) throws Exception{
    
    	model.addAttribute("roleName",request.getAttribute("role_name"));
    	
    	if (br.hasErrors()) {
             return VIEW_PREFIX+"admin-signup";
         }

    	if(!homeService.checkEmp(empvo)){
            br.rejectValue("id", "empVO.id", "이미 가입된 아이디입니다.");
            return VIEW_PREFIX+"admin-signup";
        }
    	homeService.saveEmp(empvo);
    	adminService.tripList(model,1);
    	return VIEW_PREFIX+"admin-home";
    }
    
    @GetMapping("/detail-view/emp/{id}")
    public String loadDetailEmpView(HttpServletRequest request,ModelMap model,
    							@PathVariable("id") String id) throws Exception{
    	
    	model.addAttribute("roleName",request.getAttribute("role_name"));
    	model.addAttribute("empVO",adminService.findByEmpId(id));
    	return VIEW_PREFIX+"emp-detail";
    }
    
}

