package com.dodo.system.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dodo.system.service.AdminService;
import com.dodo.system.service.HomeService;
import com.dodo.system.vo.EmpVO;
import com.dodo.system.vo.ErrorLogVO;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	
    /*관리자 기능, 유저등록,삭제*/
	@Autowired
	private AdminService adminService;
	@Autowired
	private HomeService homeService;
	
	private static final String VIEW_PREFIX = "admin/";

    @GetMapping("/home/emp-list/{pageNum}")
    public String loadHomePage(ModelMap model,
    							@PathVariable("pageNum") int pageNum) throws Exception{
    	 
    	 adminService.empList(model,pageNum); 
    	 return VIEW_PREFIX+"admin-home";
    }
    
    @PostMapping("/emp-list/excel")
    public void downloadExcelFile(HttpServletResponse response)throws IOException {
    	
        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("유저정보","UTF-8")+".xls");

        Workbook workBook = adminService.excelDown();
        
        workBook.write(response.getOutputStream());
        workBook.close();  
        
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
    
    @GetMapping("/user/sign-up")
    public String loadSignUpPage(@ModelAttribute("empVO") EmpVO empvo,
    		ModelMap model,HttpServletRequest request) throws Exception{
    	
    	return VIEW_PREFIX+"admin-signup";
    }
    
    @PostMapping("/user/sign-up")
    public String doSignUp(@Valid @ModelAttribute("empVO") EmpVO empvo,
    		BindingResult br,HttpServletRequest request,ModelMap model) throws Exception{
    	
    	if (br.hasErrors()) {
             return VIEW_PREFIX+"admin-signup";
         }

    	if(!homeService.checkEmp(empvo)){
            br.rejectValue("id", "empVO.id", "이미 가입된 아이디입니다.");
            return VIEW_PREFIX+"admin-signup";
        }
    	homeService.saveEmp(empvo);
    	return "redirect:/admin/home/emp-list/1";
    }
    
    @GetMapping("/detail-view/emp/{id}")
    public String loadDetailEmpView(HttpServletRequest request,ModelMap model,
    							@PathVariable("id") String id) throws Exception{

        EmpVO empVO = adminService.findByEmpId(id);
        model.addAttribute("empVO",empVO);
     
    	return VIEW_PREFIX+"emp-detail";
    }
    
    
    @PostMapping("/find/dept")
    public @ResponseBody List<EmpVO> findAllDept(@RequestBody EmpVO empVO){
    	return adminService.deptFindAll(empVO.getDept_name());
    }
    
    @GetMapping("/log/error/{pageNum}")
    public String loadLogErrorView(ModelMap model,
    							@PathVariable("pageNum") int pageNum) {
    	adminService.errorList(model,pageNum);
    	return VIEW_PREFIX+"log-error";
    }
    
    @PostMapping("/log/ajax/error")
    public @ResponseBody List<ErrorLogVO> errorListCnt(){
    	return adminService.errorListCount();
    }
    
    
}

