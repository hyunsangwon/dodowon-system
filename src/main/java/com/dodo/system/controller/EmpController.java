package com.dodo.system.controller;

import com.dodo.system.vo.EmpVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    @GetMapping("/my-info")
    public String loadMyinfo(ModelMap model, HttpServletRequest request,
    		 				@ModelAttribute("empVO") EmpVO empvo) throws Exception{
    
    	model.addAttribute("my",request.getAttribute("emp_vo"));
        model.addAttribute("roleName",request.getAttribute("role_name"));
        
        return "emp/myinfo";
    }
    /*내정보 수정*/

    /*비밀번호 변경 페이지*/
    @GetMapping("/my-info/password")
    public String loadMyPass(ModelMap model,HttpServletRequest request,
                             @ModelAttribute("empVO") EmpVO empvo) throws Exception{
        model.addAttribute("roleName",request.getAttribute("role_name"));
        return "emp/password";
    }
    /*비밀번호 변경*/


    /*이미지 업로드*/
}
