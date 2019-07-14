package com.dodo.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dodo.system.domain.UploadFileResponse;
import com.dodo.system.service.ImgService;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/img")
public class ImgController {

	@Autowired
	private ImgService imgService;
	
	@GetMapping("/view")
	public String uploadPage() {
		return "emp/uploadView";
	}
	
	@PostMapping("/upload")
    public void uploadFile(@RequestParam("img") MultipartFile file) {
        String fileName = imgService.storeFile(file);
        System.out.println(fileName);
    }

}