package com.dodo.system.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dodo.system.service.EmpService;
import com.dodo.system.service.ImgService;
import com.dodo.system.vo.EmpVO;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/home/emp")
public class EmpController {
    /* 유저 업데이트,조회 */
	private static final String VIEW_PREFIX = "emp/";
	
	@Autowired
	private EmpService empService;

    /*내정보 보기*/
    @GetMapping("/my-info")
    public String loadMyinfo(ModelMap model, HttpServletRequest request,
    		 				@ModelAttribute("empVO") EmpVO empvo) throws Exception{
		
    	model.addAttribute("empVO",empService.findByEmpId(request.getAttribute("emp_id").toString()));	
        return VIEW_PREFIX+"myinfo";
    }
    
    /*내정보 수정*/
    @PostMapping("/my-info")
    public String updateMyInfo(ModelMap model,HttpServletRequest request,
    		@Valid @ModelAttribute("empVO") EmpVO empvo,BindingResult br) throws Exception{
    			
		 if (br.hasErrors()) { 
			 return VIEW_PREFIX+"myinfo"; 
		 }	 
		 
    	 int flag = empService.updateMyInfo(empvo);
    	 if(flag > 0) { 		 
    		 request.setAttribute("empId",empvo.getId());	 
    	 }
    	    	 
    	 return "redirect:/home/docs/holiday/i/1";
    }
    
    /*비밀번호 변경 페이지*/
    @GetMapping("/my-info/password")
    public String loadMyPass(ModelMap model,HttpServletRequest request,
                             @ModelAttribute("empVO") EmpVO empvo) throws Exception{
    	
    	
        return VIEW_PREFIX+"password";
    }
    /*비밀번호 변경*/
    @PostMapping("/my-info/password")
    public String updatePassword(@Valid @ModelAttribute("empVO") EmpVO empvo,
    		BindingResult br) throws Exception{
    	
   	 if (br.hasErrors()) { 
		 return VIEW_PREFIX+"password"; 
	 }	 
  
    int flag = empService.updateMyPassword(empvo);
    
    if(flag > 0) {
    		//자동 로그아웃 구현 (다시 로그인 하세요~)
    	return "redirect:/logout";
    }
    	return "redirect:/home/docs/holiday/i/1";
    }
    
    @GetMapping("/download-img")
    public void downloadImg(HttpServletRequest request,
    							HttpServletResponse response) throws Exception{
		
    	final String DIR = "D:/img/";	
    	int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
    	EmpVO empVO =empService.getImgName(emp_no);
    	String imgName = empVO.getSign_img_name();
    	
    	File file = new File(DIR+imgName);
    	
    	InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
    	String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
    	
    	if(mimeType == null) {
    		mimeType = "applcation/octet-stream";
    	}
    	
    	response.setContentType(mimeType);
    	response.setContentLength((int)file.length());
    	response.setHeader("Content-Disposition",String.format("attachment; filename=\"%s\"",file.getName()));
		/* FileCopyUtils.copy는 파일 자체를 웹브라우저에서 읽어들인다. */

    	FileCopyUtils.copy(inputStream,response.getOutputStream());
    }

	@GetMapping("test-img")
	public ResponseEntity<InputStreamResource> downloadImageTest(HttpServletRequest request) throws IOException{

		int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
		EmpVO empVO =empService.getImgName(emp_no);
		String imgName = empVO.getSign_img_name();

		final String DIR = "D:/img/";
		File file = new File(DIR+imgName);
		InputStreamResource inputStream =  new InputStreamResource(new FileInputStream(file));

		String mineType = request.getServletContext().getMimeType(file.getAbsolutePath());	
		if(mineType == null){
			mineType = "application/octet-stream";
		}

        return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+file.getName())
				.contentType(MediaType.parseMediaType(mineType))
				.contentLength(file.length())
				.body(inputStream);
	}

	
	
	@GetMapping("test-img02")
	public void downloadImageTest02(HttpServletRequest request,
									HttpServletResponse response) throws IOException{
		
		final String DIRECTORY = "D:/img/";	
    	int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
    	EmpVO empVO =empService.getImgName(emp_no);
    	String imgName = empVO.getSign_img_name();
    	
    	File file = new File(DIRECTORY+imgName);
    	
    	
    	String mineType = request.getServletContext().getMimeType(imgName);   	 	
    	MediaType mediaType = null;
    	try {
    		mediaType = MediaType.parseMediaType(mineType);
    	}catch(Exception e) {
    		mediaType = MediaType.APPLICATION_OCTET_STREAM;
    	}
    
    	response.setContentType(mediaType.getType());
    	response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
    	response.setContentLength((int) file.length());
    	
	}

	/*이미지 업로드*/
	@Autowired
	private ImgService imgService;

	@PostMapping("/upload")
    public String uploadFile(ModelMap model,HttpServletRequest request,
    						@RequestParam("img") MultipartFile file,
    						@RequestParam("no") int no) {
		
        String fileName = imgService.storeFile(file);
        empService.updateImage(no,fileName);
        
        return "redirect:/home/docs/holiday/i/1";
    }
	
	
	@GetMapping("/sangwon")
	public String sangwon() {
		return "login";
	}
	
}
