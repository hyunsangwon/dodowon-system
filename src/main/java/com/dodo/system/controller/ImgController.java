package com.dodo.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/img")
public class ImgController {
    /*img 업로드,다운로드,파일변환 */
	public static String uploadDir = System.getProperty("user.dir")+"/uploads";
	
	@GetMapping("/view")
	public String UploadPage(ModelMap model) {
		return "uploadView";
	}
	
	
}
