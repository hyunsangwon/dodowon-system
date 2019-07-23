package com.dodo.system.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dodo.system.service.DocsService;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
public class HomeController implements ErrorController{
    /* global setting login,error,log ...*/

    @Autowired
    private DocsService docsService;

    @GetMapping("/access-denied")
    public String loadExceptionPage() throws Exception{
        return "error/access-denied";
    }

    @GetMapping("/home")
    public String loadHomePage(ModelMap model,HttpServletRequest request) throws Exception{
    	
        String role_name = request.getAttribute("role_name").toString();  
        
        if(role_name.equals("ADMIN")){     	
            return "redirect:/admin/home";
        }        
        return "redirect:/home/docs/holiday/i/1";
    }

    @RequestMapping("/error")
    public String handleError(ModelMap model,HttpServletRequest request) {

		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		String errorNumber = status.toString();
		
		switch(errorNumber) {
			case "404" : model.addAttribute("msg","404"); return "error/error";
			case "500" : model.addAttribute("msg","500"); return "error/error";
			case "403" : model.addAttribute("msg","403"); return "error/error";
			case "400" : model.addAttribute("msg","400"); return "error/error";
			default : model.addAttribute("msg","default");return "error/error";
		}
		
    }

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return "/error";
	}
    

}
