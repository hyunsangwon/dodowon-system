package com.dodo.system.controller;

import com.dodo.system.service.DocsService;
import com.dodo.system.service.EmpService;
import com.dodo.system.vo.HolidayVO;
import com.dodo.system.vo.TripInputVO;
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
    public String loadHolidayPage(@ModelAttribute("holidayVO") HolidayVO holidayVO,
    								ModelMap model,HttpServletRequest request){
    	
		/* 회사마다 양식이 다르다면 여기서 Interceptor에 있는 회사 이름가져와서 체크 */
    	model.addAttribute("empVO",empService.findByEmpId(request.getAttribute("emp_id").toString()));	
    	 return VIEW_PREFIX+"holiday";
    }

    @PostMapping("/reg-holiday")
    public String doHolidayReg(ModelMap model,HttpServletRequest request,
    						  @Valid @ModelAttribute("holidayVO") HolidayVO holidayVO,
                               BindingResult br) throws Exception{

        if (br.hasErrors()) {
        	model.addAttribute("empVO",empService.findByEmpId(request.getAttribute("emp_id").toString()));
            return VIEW_PREFIX+"holiday";
        }

        if(!empService.isEmpHolidayCheck(holidayVO)){
            br.rejectValue("holiday_end", "holidayVO.holiday_end", "남은 휴가일수 보다 많이 입력하셨습니다.");
            model.addAttribute("empVO",empService.findByEmpId(request.getAttribute("emp_id").toString()));
            return VIEW_PREFIX+"holiday";
        }

        int flag = docsService.saveHolidayDocs(holidayVO);

        if(flag > 0) {
        	 model.addAttribute("msg","등록되었습니다.");
        }
    
        return "redirect:/home/docs/holiday/i/1";
    }


    @GetMapping("/holiday/{docsStatus}/{pageNum}")
    public String doPage(ModelMap model,HttpServletRequest request,
                         @PathVariable("docsStatus") String docsStatus,
                         @PathVariable("pageNum") int pageNum){
    	
        int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
        docsService.holidayList(model,pageNum,emp_no,docsStatus);
        return "home";
    }
    
    /*문서 기안 삭제*/
    @GetMapping("/docs-detail/{docsName}/remove/{no}")
    public String doRemoveHoliday(
    							  @PathVariable("docsName") String docsName,
                                  @PathVariable("no") int no ) throws Exception{
    	
    	if(docsName.equals("holiday")) {
            docsService.removeDocs(no,docsName);
            return "redirect:/home/docs/holiday/i/1";
    	}else {
    		docsService.removeDocs(no,docsName);
            return "redirect:/home/docs/trip/i/1";
    	}
    }

    @GetMapping("/{docsType}/detail-view/{docsStatus}/{no}")
    public String loadDetailHoliday(ModelMap model,HttpServletRequest request,
    								@PathVariable("docsType") String docsType,
    								@PathVariable("no") int no,
                                    @PathVariable("docsStatus") String docsStatus) throws Exception {

        model.addAttribute("docsStatus",docsStatus);

        if(docsType.equals("holiday")){
            model.addAttribute("holidayVO",docsService.findByHolidayNo(no));
            model.addAttribute("empVO",empService.findByEmpId(request.getAttribute("emp_id").toString()));
            return VIEW_PREFIX+"holiday-detail";
        }else{
            docsService.findByTripNo(model,no);
            return VIEW_PREFIX+"trip-detail";
        }
    }

    @PostMapping("/modify-holiday")
    public String doSetHoliday(ModelMap model,HttpServletRequest request,
                               @Valid @ModelAttribute("holidayVO") HolidayVO holidayVO,
                               BindingResult br) throws Exception{
    	
    	if (br.hasErrors()) {       
    		model.addAttribute("empVO",empService.findByEmpId(request.getAttribute("emp_id").toString()));
            return VIEW_PREFIX+"holiday";
        }

        if(!empService.isEmpHolidayCheck(holidayVO)){
            br.rejectValue("holiday_end", "holidayVO.holiday_end", "남은 휴가일수 보다 많이 입력하셨습니다.");
            model.addAttribute("empVO",empService.findByEmpId(request.getAttribute("emp_id").toString()));
            return VIEW_PREFIX+"holiday";
        }
        
        if(docsService.updateHoliday(holidayVO) > 0){
            model.addAttribute("msg","수정되었습니다.");
        }      
        return "redirect:/home/docs/holiday/i/1";
    }
    
	/*출장 기안 수정*/
    @PostMapping("/modify-trip")
    public String doSetTrip(ModelMap model,@ModelAttribute TripInputVO tripInputVO) throws Exception{

    	int flag = docsService.updateTrip(tripInputVO);
    	if(flag > 0) {
    		 model.addAttribute("msg","수정되었습니다.");
    	}
        return "redirect:/home/docs/trip/i/1";
    }
    
    
    /*출장 리스트 */
    @GetMapping("/trip/{docsStatus}/{pageNum}")
    public String doPageTrip(ModelMap model,HttpServletRequest request,
                             @PathVariable("docsStatus") String docsStatus,
                             @PathVariable("pageNum") int pageNum) {
    	
        int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
        docsService.tripList(model,pageNum,emp_no,docsStatus);
        return VIEW_PREFIX+"trip-home";
    }

    @GetMapping("/reg-trip")
    public String loadTripPage() {
        return VIEW_PREFIX+"trip";
    }

    /*출장 등록*/
    @PostMapping("/reg-trip")
    public String doTripReg(ModelMap model, HttpServletRequest request,
                            @ModelAttribute TripInputVO tripInputVO) throws Exception {

        tripInputVO.setNo(Integer.parseInt(request.getAttribute("emp_no").toString()));

        int flag =  docsService.saveTripDocs(tripInputVO);
        
        if(flag > 0) {
        	model.addAttribute("msg","등록되었습니다.");
        }
        
        return "redirect:/home/docs/trip/i/1";
    }

    /*결재 해야될,완료 메뉴 & 참조 페이지
     * reporting -> 결재
     * reference -> 참조
     *  */
    @GetMapping("{pageName}/list/{docsStatus}/{pageNum}")
    public String loadDocsList(ModelMap model,HttpServletRequest request,
    						 @PathVariable("pageName") String pageName,
    						 @PathVariable("docsStatus") String docsStatus,
    						 @PathVariable("pageNum") int pageNum) {

        model.addAttribute("pageName",pageName);
    	int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());   	
    	docsService.reportingList(model, pageNum, emp_no, docsStatus,pageName);
        return VIEW_PREFIX+"docs-list";
    }

    
    /*결재선택 페이지 이동 & 참조 페이지 */
    @GetMapping("/reporting-list/detail-view/{docsType}/{docsStatus}/{docsNo}")
    public String loadDocsListDetailView(ModelMap model,
                                         @PathVariable("docsType") String docsType,
                                         @PathVariable("docsStatus") String docsStatus,
                                         @PathVariable("docsNo") int docsNo) throws Exception{

        model.addAttribute("docsStatus",docsStatus);
        
        if(docsType.equals("trip")){
            docsService.findByTripNo(model,docsNo);
            return VIEW_PREFIX+"trip-status";
        }else{
            model.addAttribute("holidayVO",docsService.findByHolidayNo(docsNo));
            return VIEW_PREFIX+"holiday-status";
        }
    }

    /*결재 승인&반려 */
    @GetMapping("/approval/{docsType}/{docsNo}/{decision}")
    public String doDecisionDocs(
                                 @PathVariable("docsType") String docsType,//문서 종류
                                 @PathVariable("docsNo") int docsNo, //문서 고유 번호
                                 @PathVariable("decision") String decision //반려? 승인? 전결?
                                 ) throws Exception{
       
        docsService.DoApprovalDocs(docsType,docsNo,decision);
        
        return "redirect:/home/docs/reporting/list/i/1";
    }
  
}
