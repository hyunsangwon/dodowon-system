package com.dodo.system.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dodo.system.service.HomeService;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
public class HomeController implements ErrorController{
    /* global setting login,error,log ...*/
    @Autowired
    private HomeService homeService;
    
    @RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
    public String loadLoginPage02() {
    	return "login";
    }
    
    @GetMapping("/login-fail")
    public String loadLoginPagefail(ModelMap model) {
    	model.addAttribute("error",true);
    	return "login";
    }
    
    @GetMapping("/system/home")
    public String loadHomePage(HttpServletRequest request,HttpSession session) throws Exception{
    	
        String role_name = request.getAttribute("role_name").toString();  
        session.setAttribute("empNo",request.getAttribute("emp_no").toString());
        
        if(role_name.equals("ADMIN")){     	
            return "redirect:/admin/home/emp-list/1";
        }        
        return "redirect:/home/docs/holiday/i/1";
    }

    @RequestMapping("/error")
    public String handleError(ModelMap model,HttpServletRequest request) {

		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		String errorNumber = status.toString();
			
		switch(errorNumber){
			case "404" : model.addAttribute("msg","404"); return "error/error";
			case "500" : model.addAttribute("msg","500"); return "error/error";
			case "403" : model.addAttribute("msg","403"); return "error/error";
			case "400" : model.addAttribute("msg","400"); return "error/error";
			default : model.addAttribute("msg","default");return "error/error";
		}
		
    }
    
    @PostMapping("/valid-recaptcha")
    public @ResponseBody String validRecaptcha(HttpServletRequest request){
    	String result = null;
    	String response = request.getParameter("g-recaptcha-response");
    	boolean isRecaptcha = homeService.verifyRecaptcha(response);

    	if(isRecaptcha) {
    		result = "success";
    	}else {
    		result = "false";
    	}  	
    	return result;
    }

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
