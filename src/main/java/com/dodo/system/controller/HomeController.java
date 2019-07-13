package com.dodo.system.controller;

import com.dodo.system.domain.EmpPrincipal;
import com.dodo.system.service.DocsService;
import com.dodo.system.service.HomeService;
import com.dodo.system.vo.EmpVO;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
public class HomeController {
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
        model.addAttribute("roleName",role_name);
        if(role_name.equals("ADMIN")){
            return "admin/admin-home";
        }
        int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
        docsService.holidayList(model,1,emp_no,"i");
        return "home";
    }



}
