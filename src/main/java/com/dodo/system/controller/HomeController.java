package com.dodo.system.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.dodo.system.vo.ErrorLogVO;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
public class HomeController implements ErrorController{
    /* global setting login,error,log ...*/
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private static final String VIEW_PREFIX = "error/";
	
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
		String errorUrl = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString();				
		String errorException = null;
		
		if(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION) != null) {
			errorException = (String) ExceptionUtils.getRootCauseMessage((Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
		}else {
			errorException = "other errors!";
		}
		String errorNumber = status.toString();
		
		ErrorLogVO errorLogVO = new ErrorLogVO();
		errorLogVO.setError_status(errorNumber);
		errorLogVO.setError_url(errorUrl);
		errorLogVO.setError_msg(errorException);
		
		int flag = homeService.setLogError(errorLogVO);
		if(flag > 0) {
			logger.debug("---- error log insert ----");
		}
		
		switch(errorNumber){
			case "404" : model.addAttribute("msg","404"); return VIEW_PREFIX+"error";
			case "500" : model.addAttribute("msg","500"); return VIEW_PREFIX+"error";
			case "403" : model.addAttribute("msg","403"); return VIEW_PREFIX+"error";
			case "400" : model.addAttribute("msg","400"); return VIEW_PREFIX+"error";
			default : model.addAttribute("msg","default");return VIEW_PREFIX+"error";
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
