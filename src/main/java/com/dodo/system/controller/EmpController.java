package com.dodo.system.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	/*이미지 업로드*/
	@Autowired
	private ImgService imgService;
	
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
    	return "redirect:/logout";
    }
    	return "redirect:/home/docs/holiday/i/1";
    }

	@GetMapping("download-img")
	public ResponseEntity<InputStreamResource> downloadImage(HttpServletRequest request,
																	HttpSession session) throws IOException{

		int emp_no = Integer.parseInt(session.getAttribute("empNo").toString());
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
	
	
	@GetMapping("download-img/{empNo}")
	public ResponseEntity<InputStreamResource> downloadEmpImage(HttpServletRequest request,
								@PathVariable("empNo") int empNo) throws IOException{
	
		EmpVO empVO =empService.getImgName(empNo);
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
	
	/* @ResponseBody를 설정하는 순간 return되는 값을 뷰 리졸버가 아닌 *메세지 컨버터가 관리한다.
	 * */
	@PostMapping("/upload-img")
    public @ResponseBody String uploadFile(ModelMap model,HttpServletRequest request,
    						@RequestParam("img") MultipartFile file,
    						@RequestParam("no") int no) {
		
		String result = null;	
        String fileName = imgService.storeFile(file);
        int flag = empService.updateImage(no,fileName);     
        if(flag > 0) {
        	result = "success";
        }
        return result;
    }
}