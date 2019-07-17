package com.dodo.system.controller;

import com.dodo.system.service.DocsService;
import com.dodo.system.service.EmpService;
import com.dodo.system.vo.TripVO;
import com.dodo.system.vo.HolidayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Controller
@RequestMapping("/home/docs")
public class DocsController {
    /*휴가,출장 문서 등록,업데이트,삭제,조회 */
	private static final String VIEW_PREFIX = "emp/";

	@Autowired
    private EmpService empService;

	@Autowired
	private DocsService docsService;

    @GetMapping("/reg-holiday")
    public String loadHolidayPage(ModelMap model, HttpServletRequest request,
                                  @ModelAttribute("holidayVO") HolidayVO holidayVO) throws Exception{

    	 model.addAttribute("roleName",request.getAttribute("role_name"));
    	 model.addAttribute("empVO",request.getAttribute("emp_vo"));

    	 return VIEW_PREFIX+"holiday";
    }
    /*휴가 등록*/
    @PostMapping("/reg-holiday")
    public String doHolidayReg(ModelMap model, HttpServletRequest request,
                               @Valid @ModelAttribute("holidayVO") HolidayVO holidayVO,
                               BindingResult br) throws Exception{

        model.addAttribute("roleName",request.getAttribute("role_name"));
        model.addAttribute("empVO",request.getAttribute("emp_vo"));

        if (br.hasErrors()) {
            return VIEW_PREFIX+"holiday";
        }

        if(!empService.isEmpHolidayCheck(holidayVO)){
            br.rejectValue("holiday_end", "holidayVO.holiday_end", "남은 휴가일수 보다 많이 입력하셨습니다.");
            return VIEW_PREFIX+"holiday";
        }

        int flag = docsService.saveHolidayDocs(holidayVO);

        if(flag > 0) {
        	 model.addAttribute("msg","등록되었습니다.");
        }

        int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
        docsService.holidayList(model,1,emp_no,"i");
       
        return "home";
    }

    /*휴가 결재 진행,반려,완료 리스트*/
    @GetMapping("/holiday/{docsStatus}/{pageNum}")
    public String doPage(ModelMap model,HttpServletRequest request,
                         @PathVariable("docsStatus") String docsStatus,
                         @PathVariable("pageNum") int pageNum) throws Exception{

        model.addAttribute("roleName",request.getAttribute("role_name"));
        int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
        docsService.holidayList(model,pageNum,emp_no,docsStatus);

        return "home";
    }

    /*결재문서 기안 상세보기*/
    @GetMapping("/{docsType}/detail-view/{docsStatus}/{no}")
    public String loadDetailHoliday(ModelMap model,HttpServletRequest request,
    								@PathVariable("docsType") String docsType,
    								@PathVariable("no") int no,
                                    @PathVariable("docsStatus") String docsStatus) throws Exception {

        model.addAttribute("roleName",request.getAttribute("role_name"));
        model.addAttribute("empVO",request.getAttribute("emp_vo"));
        model.addAttribute("docsStatus",docsStatus);

        if(docsType.equals("holiday")){
            model.addAttribute("holidayVO",docsService.findByHolidayNo(no));
            return "emp/holiday-detail";
        }else{
            docsService.findByTripNo(model,no);
            return "emp/trip-detail";
        }
    }
    /*휴가 기안 수정
    */
    @PostMapping("/modify-holiday")
    public String doSetHoliday(ModelMap model,HttpServletRequest request,
                               @Valid @ModelAttribute("holidayVO") HolidayVO holidayVO,
                               BindingResult br) throws Exception{
        model.addAttribute("roleName",request.getAttribute("role_name"));

        if (br.hasErrors()) {
            return VIEW_PREFIX+"holiday";
        }

        if(!empService.isEmpHolidayCheck(holidayVO)){
            br.rejectValue("holiday_end", "holidayVO.holiday_end", "남은 휴가일수 보다 많이 입력하셨습니다.");
            return VIEW_PREFIX+"holiday";
        }

        if(docsService.updateHoliday(holidayVO) > 0){
            model.addAttribute("msg","수정되었습니다.");
        }

        int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
        docsService.holidayList(model,1,emp_no,"i");

        return "home";
    }
	/*출장 기안 수정*/
    @PostMapping("/modify-trip")
    public String doSetTrip(ModelMap model,HttpServletRequest request) throws Exception{
    
    	docsService.updateTrip(request);	
        int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
        docsService.tripList(model,1,emp_no,"i");
        model.addAttribute("roleName", request.getAttribute("role_name"));
    	return "emp/trip-home";
    }
    

    /*문서 기안 삭제*/
    @GetMapping("/{docsStatus}/remove/{no}")
    public String doRemoveHoliday(ModelMap model,HttpServletRequest request,
    							  @PathVariable("docsStatus") String docsStatus,
                                  @PathVariable("no") int no ) throws Exception{
    	
    	model.addAttribute("roleName",request.getAttribute("role_name"));
    	if(docsStatus.equals("holiday")) {     
            docsService.removeDocs(no,docsStatus);
            return "redirect:/home";
    	}else {
    		docsService.removeDocs(no,docsStatus);
            return "redirect:/home/docs/trip/i/1";
    	}
    }
    
    /*출장 리스트 */
    @GetMapping("/trip/{docsStatus}/{pageNum}")
    public String doPageTrip(ModelMap model,HttpServletRequest request,
                             @PathVariable("docsStatus") String docsStatus,
                             @PathVariable("pageNum") int pageNum) throws Exception {
    	
        int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
        docsService.tripList(model,pageNum,emp_no,docsStatus);
        model.addAttribute("roleName", request.getAttribute("role_name"));
        return "emp/trip-home";
    }

    @GetMapping("/reg-trip")
    public String loadTripPage(ModelMap model,HttpServletRequest request) throws Exception{
        model.addAttribute("roleName",request.getAttribute("role_name"));
        return VIEW_PREFIX+"trip";
    }

    /*출장 등록*/
    @PostMapping("/reg-trip")
    public String doTripReg(ModelMap model, HttpServletRequest request) throws Exception {

        model.addAttribute("roleName",request.getAttribute("role_name"));
        int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());    
       
        int flag =  docsService.saveTripDocs(request,emp_no);      
        if(flag > 0) {
        	model.addAttribute("msg","등록되었습니다.");
        }
		docsService.tripList(model,1,emp_no,"i"); 
        return "emp/trip-home";
    }

    /*결재 해야될,완료 메뉴 & 참조 페이지
     * reporting -> 결재
     * reference -> 참조
     *  */
    @GetMapping("{pageName}/list/{docsStatus}/{pageNum}")
    public String loadDocsList(ModelMap model,HttpServletRequest request,
    						 @PathVariable("pageName") String pageName,
    						 @PathVariable("docsStatus") String docsStatus,
    						 @PathVariable("pageNum") int pageNum) throws Exception{

        String role_name = request.getAttribute("role_name").toString();
    	model.addAttribute("roleName",role_name);
        model.addAttribute("pageName",pageName);
    	int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
    	
    	docsService.reportingList(model, pageNum, emp_no, docsStatus,pageName);
        return "emp/docs-list";
    }

    
    /*결재선택 페이지 이동 & 참조 페이지 */
    @GetMapping("/reporting-list/detail-view/{docsType}/{docsStatus}/{docsNo}")
    public String loadDocsListDetailView(ModelMap model,HttpServletRequest request,
                                         @PathVariable("docsType") String docsType,
                                         @PathVariable("docsStatus") String docsStatus,
                                         @PathVariable("docsNo") int docsNo) throws Exception{

        model.addAttribute("roleName",request.getAttribute("role_name"));
        model.addAttribute("docsStatus",docsStatus);
        if(docsType.equals("trip")){
            docsService.findByTripNo(model,docsNo);
            return "emp/trip-status";
        }else{
            model.addAttribute("holidayVO",docsService.findByHolidayNo(docsNo));
            return "emp/holiday-status";
        }

    }

    /*결재 승인&반려 */
    @GetMapping("/approval/{docsType}/{docsNo}/{decision}")
    public String doDecisionDocs(ModelMap model,HttpServletRequest request,
                                 @PathVariable("docsType") String docsType,
                                 @PathVariable("docsNo") int docsNo,
                                 @PathVariable("decision") int decision) throws Exception{

        model.addAttribute("roleName",request.getAttribute("role_name"));

        if(docsType.equals("trip")){

        }else{

        }
        return null;
    }
  
}
