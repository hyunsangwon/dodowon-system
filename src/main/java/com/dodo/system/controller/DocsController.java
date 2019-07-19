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

	/**
	 * 
	 * @param model
	 * @param request
	 * @param holidayVO
	 * @return
	 * @throws Exception
	 * @URL : /home/docs/reg-holiday
	 */
    @GetMapping("/reg-holiday")
    public String loadHolidayPage(@ModelAttribute("holidayVO") HolidayVO holidayVO) throws Exception{
    	
    	 return VIEW_PREFIX+"holiday";
    }
    /**
     * @param model
     * @param request
     * @param holidayVO
     * @param br
     * @return
     * @throws Exception
     * @URL : /home/docs/reg-holiday
     */
    @PostMapping("/reg-holiday")
    public String doHolidayReg(ModelMap model, @Valid @ModelAttribute("holidayVO") HolidayVO holidayVO,
                               BindingResult br) throws Exception{

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
    
        return "redirect:/home/docs/holiday/i/1";
    }

    /**
     * @param model
     * @param request
     * @param docsStatus
     * @param pageNum
     * @return
     * @throws Exception
     * @URL : /home/docs/holiday/i/1 결재
     * @URL : /home/docs/holiday/n/1 반려
     * @URL : /home/docs/holiday/y/1 승인
     */
    @GetMapping("/holiday/{docsStatus}/{pageNum}")
    public String doPage(ModelMap model,HttpServletRequest request,
                         @PathVariable("docsStatus") String docsStatus,
                         @PathVariable("pageNum") int pageNum) throws Exception{

        int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());
        docsService.holidayList(model,pageNum,emp_no,docsStatus);
        return "home";
    }

    /**
     * @param model
     * @param request
     * @param docsType
     * @param no
     * @param docsStatus
     * @return
     * @throws Exception
     * @URL : /home/docs/holiday/detail-view/i/1
     * @URL : /home/docs/holiday/detail-view/n/1
     * @URL : /home/docs/holiday/detail-view/y/1
     * @URL : /home/docs/trip/detail-view/i/1
     * @URL : /home/docs/trip/detail-view/n/1
     * @URL : /home/docs/trip/detail-view/y/1
     */
    @GetMapping("/{docsType}/detail-view/{docsStatus}/{no}")
    public String loadDetailHoliday(ModelMap model,HttpServletRequest request,
    								@PathVariable("docsType") String docsType,
    								@PathVariable("no") int no,
                                    @PathVariable("docsStatus") String docsStatus) throws Exception {

        model.addAttribute("docsStatus",docsStatus);

        if(docsType.equals("holiday")){
            model.addAttribute("holidayVO",docsService.findByHolidayNo(no));
            return VIEW_PREFIX+"holiday-detail";
        }else{
            docsService.findByTripNo(model,no);
            return VIEW_PREFIX+"trip-detail";
        }
    }
    

    /**
     * @param model
     * @param request
     * @param holidayVO
     * @param br
     * @return
     * @throws Exception
     * @URL : /home/docs/modify-holiday
     */
    @PostMapping("/modify-holiday")
    public String doSetHoliday(ModelMap model,HttpServletRequest request,
                               @Valid @ModelAttribute("holidayVO") HolidayVO holidayVO,
                               BindingResult br) throws Exception{
    	
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
        return "redirect:/home/docs/holiday/i/1";
    }
    
	/*출장 기안 수정*/
    @PostMapping("/modify-trip")
    public String doSetTrip(ModelMap model,HttpServletRequest request) throws Exception{
    
    	int flag = docsService.updateTrip(request);
    	if(flag > 0) {
    		 model.addAttribute("msg","수정되었습니다.");
    	}	
        return "redirect:/home/docs/trip/i/1";
    }
    

    /*문서 기안 삭제*/
    @GetMapping("/{docsStatus}/remove/{no}")
    public String doRemoveHoliday(ModelMap model,HttpServletRequest request,
    							  @PathVariable("docsStatus") String docsStatus,
                                  @PathVariable("no") int no ) throws Exception{
    	
    	if(docsStatus.equals("holiday")) {     
            docsService.removeDocs(no,docsStatus);
            return "redirect:/home/docs/holiday/i/1";
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
        return VIEW_PREFIX+"trip-home";
    }

    @GetMapping("/reg-trip")
    public String loadTripPage() throws Exception{
        return VIEW_PREFIX+"trip";
    }

    /*출장 등록*/
    @PostMapping("/reg-trip")
    public String doTripReg(ModelMap model, HttpServletRequest request) throws Exception {

        int emp_no = Integer.parseInt(request.getAttribute("emp_no").toString());           
        int flag =  docsService.saveTripDocs(request,emp_no);      
        
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
    						 @PathVariable("pageNum") int pageNum) throws Exception{

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
