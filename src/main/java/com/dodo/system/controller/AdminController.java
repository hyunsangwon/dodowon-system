package com.dodo.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    /*관리자 기능*/

    @GetMapping("/home")
    public String loadHomePage(ModelMap model) throws Exception{

        return "home";
    }

}

