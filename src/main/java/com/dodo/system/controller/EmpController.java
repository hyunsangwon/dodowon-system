package com.dodo.system.controller;

import com.dodo.system.service.ImgService;
import com.dodo.system.vo.EmpVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/home/emp")
public class EmpController {
    /* 유저 업데이트,조회 */
	private static final String VIEW_PREFIX = "emp/";
	
    /*내정보 보기*/
    @GetMapping("/my-info")
    public String loadMyinfo(ModelMap model, HttpServletRequest request,
    		 				@ModelAttribute("empVO") EmpVO empvo) throws Exception{
         
        return VIEW_PREFIX+"myinfo";
    }
    /*내정보 수정*/

    /*비밀번호 변경 페이지*/
    @GetMapping("/my-info/password")
    public String loadMyPass(ModelMap model,HttpServletRequest request,
                             @ModelAttribute("empVO") EmpVO empvo) throws Exception{
        return VIEW_PREFIX+"password";
    }
    /*비밀번호 변경*/

    /*이미지 업로드*/
	@Autowired
	private ImgService imgService;
	
	@GetMapping("/img-view")
	public String uploadPage() {
		return VIEW_PREFIX+"uploadView";
	}
	
	@PostMapping("/upload")
    public String uploadFile(ModelMap model,HttpServletRequest request,
    						@RequestParam("img") MultipartFile file) {
        String fileName = imgService.storeFile(file);
        return VIEW_PREFIX+"uploadView";
    }
}
