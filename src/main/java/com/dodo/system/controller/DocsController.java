package com.dodo.system.controller;

import com.dodo.system.service.DocsService;
import com.dodo.system.service.EmpService;
import com.dodo.system.vo.BusinessTripVO;
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

    /*휴가 기안 상세보기*/
    @GetMapping("/holiday/detail-view/{no}")
    public String loadDetailHoliday(ModelMap model,HttpServletRequest request,
                                    @PathVariable("no") int no) throws Exception {
        model.addAttribute("roleName",request.getAttribute("role_name"));
        model.addAttribute("empVO",request.getAttribute("emp_vo"));

        HolidayVO holidayVO = docsService.findByHolidayNo(no);
        model.addAttribute("holidayVO",holidayVO);

        return "emp/holiday-detail";
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

    /*휴가 기안 삭제*/
    @GetMapping("/holiday/remove/{no}")
    public String doRemoveHoliday(ModelMap model,HttpServletRequest request,
                                  @PathVariable("no") int no ) throws Exception{
        model.addAttribute("roleName",request.getAttribute("role_name"));
        docsService.removeDocs(no,"holiday");
        return "redirect:/home";
    }

    @GetMapping("/trip/{docsStatus}/{pageNum}")
    public String doPageTrip(ModelMap model,HttpServletRequest request,
                             @PathVariable("docsStatus") String docsStatus,
                             @PathVariable("pageNum") int pageNum) throws Exception {

        model.addAttribute("roleName", request.getAttribute("role_name"));
        return "emp/trip-home";
    }

    @GetMapping("/reg-trip")
    public String loadTripPage(ModelMap model,HttpServletRequest request,
                               @ModelAttribute("businessTripVO") BusinessTripVO businessTripVO) throws Exception{
        model.addAttribute("roleName",request.getAttribute("role_name"));
        return VIEW_PREFIX+"trip";
    }
    /*출장 등록*/
    @PostMapping("/reg-trip")
    public String doTripReg(ModelMap model, HttpServletRequest request,
                            @Valid @ModelAttribute("businessTripVO") BusinessTripVO businessTripVO,
                            BindingResult br) throws Exception {

        model.addAttribute("roleName",request.getAttribute("role_name"));
        return "home";
    }

    /*결재 해야될,완료 메뉴*/
    @GetMapping("/reporting-list")
    public String loadDocsList(ModelMap model,HttpServletRequest request) throws Exception{
        model.addAttribute("roleName",request.getAttribute("role_name"));
        return "emp/docs-list";
    }

    @GetMapping("/reporting-list/detail-view/{type}/{no}")
    public String loadDocsListDetailView(ModelMap model,HttpServletRequest request,
                                         @PathVariable("type") String docs_type,
                                         @PathVariable("no") int no) throws Exception{

        model.addAttribute("roleName",request.getAttribute("role_name"));

        if(docs_type.equals("trip")){
            return "emp/trip-status";
        }
        return "emp/holiday-status";
    }

    /*참조 결재*/
    @GetMapping("/reference")
    public String loadReferDocs(ModelMap model,HttpServletRequest request){
        model.addAttribute("roleName",request.getAttribute("role_name"));
        return "emp/docs-reference";
    }
}
