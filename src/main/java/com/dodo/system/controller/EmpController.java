package com.dodo.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/home/emp")
public class EmpController {
    /* 유저 업데이트,조회 */

    /*내정보 보기*/
    @GetMapping("/Info")
    public String loadMyinfo(ModelMap model, HttpServletRequest request) throws Exception{
        model.addAttribute("roleName",request.getAttribute("role_name"));
        return "emp/myinfo";
    }
    /*내정보 수정*/

    /*비밀번호 변경 페이지*/
    @GetMapping("/Info/password")
    public String loadMyPass(ModelMap model,HttpServletRequest request) throws Exception{
        model.addAttribute("roleName",request.getAttribute("role_name"));
        return "emp/password";
    }
    /*비밀번호 변경*/

    /*이미지 업로드*/
}
