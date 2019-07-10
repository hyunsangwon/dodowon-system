package com.dodo.system.controller;

import com.dodo.system.domain.EmpPrincipal;
import com.dodo.system.service.HomeService;
import com.dodo.system.vo.EmpVO;
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
    private HomeService homeService;

    @GetMapping("/access-denied")
    public String loadExceptionPage() throws Exception{
        return "error/access-denied";
    }

    @PostMapping("/sign-up")
    public void addUserByAdmin(@RequestBody EmpVO empVO) throws Exception {
        homeService.saveEmp(empVO);
    }

    @GetMapping("/home")
    public String loadHomePage(ModelMap model) throws Exception{

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        EmpPrincipal empPrincipal = (EmpPrincipal) auth.getPrincipal();

        String role_name = empPrincipal.getRoleVO().getRole_name();     
        model.addAttribute("roleName",role_name);
        
        if(role_name.equals("ADMIN")){
            return "admin/admin-test";
        }
        return "home";
    }
}
