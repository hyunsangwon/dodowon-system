package com.dodo.system.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.dodo.system.domain.PageHandler;
import com.dodo.system.mapper.DocsMapper;
import com.dodo.system.vo.HolidayVO;
import com.dodo.system.vo.ReportingListVO;
import com.dodo.system.vo.TripDetailVO;
import com.dodo.system.vo.TripEtcVO;
import com.dodo.system.vo.TripProposerVO;
import com.dodo.system.vo.TripVO;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Service
public class DocsService {

	@Autowired
	private DocsMapper docsMapper;
	
	public int saveHolidayDocs(HolidayVO holidayVO) throws Exception{
		return docsMapper.setHoliday(holidayVO);
	}

	public HolidayVO findByHolidayNo(int holiday_no) throws Exception{
		return docsMapper.findByHolidayNo(holiday_no);
	}

	public void findByTripNo(ModelMap map,int no) throws Exception{
		
		List<TripDetailVO> list = docsMapper.findByDocsTripNo(no);
		int tripNo = list.get(0).getNo();
		
		map.addAttribute("list",list);		
		map.addAttribute("tripNo",tripNo);	
		
		List<TripEtcVO> etcList = docsMapper.findByEtcTripNo(tripNo);
		
		if(etcList != null) {
			map.addAttribute("etcList",etcList);
		}	
	}

	public void holidayList(ModelMap map,int pageNum,int empNo,String docsStatus){

		int limitCount=((pageNum - 1 ) * 10);
		int contentNum =10;
		int totalCnt = docsMapper.totalCntHoliday(empNo,docsStatus);
		PageHandler pageHandler = pageHandler(totalCnt,
				pageNum,contentNum);
		
		List<HolidayVO> list = docsMapper.holidayList(empNo,limitCount,contentNum,docsStatus);

		for(int x=0; x<list.size(); x++){
			int no = (totalCnt-limitCount)-x;
			list.get(x).setBoard_no(no);
		}

		map.addAttribute("list",list);
		map.addAttribute("size",list.size());
		map.addAttribute("pageHandler",pageHandler);
		map.addAttribute("docsStatus",docsStatus);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public int saveTripDocs(HttpServletRequest request,int empNo) throws Exception{
		TripVO tripVo = new TripVO();	
		tripVo.setEmp_no(empNo);
		tripVo.setDocs_no(request.getParameter("trip_no"));
		tripVo.setLocation(request.getParameter("location"));
		tripVo.setReason(request.getParameter("reason"));
		tripVo.setBt_start(request.getParameter("bt_start"));
		tripVo.setBt_end(request.getParameter("bt_end"));
		tripVo.setFood_money(Integer.parseInt(request.getParameter("food_money")));
		tripVo.setRoom_charge(Integer.parseInt(request.getParameter("room_charge")));
		tripVo.setTran_cost(Integer.parseInt(request.getParameter("tran_cost")));
		tripVo.setTran_local_cost(Integer.parseInt(request.getParameter("tran_local_cost")));
		tripVo.setEtc(Integer.parseInt(request.getParameter("etc")));
		tripVo.setTeam_cnt(Integer.parseInt(request.getParameter("team_cnt")));

		int flag = docsMapper.setTrip(tripVo);
		if(flag > 0) {
			int trip_no = docsMapper.getTripNo(tripVo);
			saveTripProposer(request,trip_no);
			if(!request.getParameter("g_num0").equals("")) {
				saveTripETC(request,trip_no);
			}
		}
		return flag;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public void saveTripProposer(HttpServletRequest request,int trip_no) throws Exception{
	
		int teamCnt = Integer.parseInt(request.getParameter("team_cnt"));
			
		TripProposerVO tripProposerVO = new TripProposerVO();
		tripProposerVO.setTrip_no(trip_no);
		
		for (int x = 0; x < teamCnt; x++) {		
			tripProposerVO.setDept_name(request.getParameter("dept_name" + x));
			tripProposerVO.setEmp_rank(request.getParameter("emp_rank" + x));
			tripProposerVO.setName(request.getParameter("name" + x));
			tripProposerVO.setPrivate_num(Integer.parseInt(request.getParameter("private_num" + x)));
			tripProposerVO.setReplacement(request.getParameter("replacement" + x));
			tripProposerVO.setAccount(request.getParameter("account" + x));
		    docsMapper.setTripProposer(tripProposerVO);
		}
	}
	/*수정해야됨*/
	public void saveTripETC(HttpServletRequest request,int trip_no) throws Exception{
		
		int etcCnt = 2;
		TripEtcVO etcVO = new TripEtcVO();
		etcVO.setTrip_no(trip_no);
		for(int x=0; x<1; x++) {
			etcVO.setG_num(request.getParameter("g_num" + x));//계정 번호
			etcVO.setHelp(request.getParameter("help"+x));//협조
			etcVO.setB_num(request.getParameter("b_num"+x));//발의 번호
			docsMapper.setEtc(etcVO);
		}
	}

	/* 문서 삭제*/
	public void removeDocs(int no,String docsName)throws Exception{
		if (docsName.equals("holiday")) {
			docsMapper.removeHoliday(no);
		} else {
			docsMapper.removeTrip(no);
		}
	}

	public int updateHoliday(HolidayVO holidayVO)throws Exception{
		return docsMapper.updateHoliday(holidayVO);
	}

	public int updateTrip(HttpServletRequest request) throws Exception{
	
		int teamCnt = Integer.parseInt(request.getParameter("team_cnt"));
		int no = Integer.parseInt(request.getParameter("no"));
		
		TripDetailVO tripDetailVO = new TripDetailVO();
		tripDetailVO.setNo(no);
		tripDetailVO.setDocs_no(request.getParameter("trip_no"));
		tripDetailVO.setLocation(request.getParameter("location"));
		tripDetailVO.setReason(request.getParameter("reason"));
		tripDetailVO.setBt_start(request.getParameter("bt_start"));
		tripDetailVO.setBt_end(request.getParameter("bt_end"));
		tripDetailVO.setFood_money(Integer.parseInt(request.getParameter("food_money")));
		tripDetailVO.setRoom_charge(Integer.parseInt(request.getParameter("room_charge")));
		tripDetailVO.setTran_cost(Integer.parseInt(request.getParameter("tran_cost")));
		tripDetailVO.setTran_local_cost(Integer.parseInt(request.getParameter("tran_local_cost")));
		tripDetailVO.setEtc(Integer.parseInt(request.getParameter("etc")));
		tripDetailVO.setTeam_cnt(teamCnt);
		
		int flag = docsMapper.updateTrip(tripDetailVO);
		if(flag > 0) {		
			List<TripProposerVO> list = docsMapper.findByTripProposerNo(no);
			for(int x=0; x<list.size(); x++) {
				tripDetailVO.setProposer_no(list.get(x).getProposer_no());
				tripDetailVO.setDept_name(request.getParameter("dept_name" + x));
				tripDetailVO.setEmp_rank(request.getParameter("emp_rank" + x));
				tripDetailVO.setName(request.getParameter("name" + x));
				tripDetailVO.setPrivate_num(Integer.parseInt(request.getParameter("private_num" + x)));
				tripDetailVO.setReplacement(request.getParameter("replacement" + x));
				tripDetailVO.setAccount(request.getParameter("account" + x));
				docsMapper.updateTripProposer(tripDetailVO);
			}
			List<TripEtcVO> etcList = docsMapper.findByTripEtcNo(no);
			if(etcList != null) {
				updateTripEtc(request);
			}
			
		}
		return flag;
	}
	
	private void updateTripEtc(HttpServletRequest request) throws Exception{
		System.out.println("call~");
	}
	
	public void tripList(ModelMap map,int pageNum,int empNo,String docsStatus){

		int limitCount=((pageNum - 1 ) * 10);
		int contentNum =10;
		int totalCnt = docsMapper.totalCntTrip(empNo,docsStatus);
		PageHandler pageHandler = pageHandler(totalCnt,pageNum,contentNum);

		List<TripVO> list = docsMapper.tripList(empNo,limitCount,contentNum,docsStatus);

		for(int x=0; x<list.size(); x++){
			int no = (totalCnt-limitCount)-x;
			list.get(x).setBoard_no(no);
		}
		map.addAttribute("list",list);
		map.addAttribute("size",list.size());
		map.addAttribute("pageHandler",pageHandler);
		map.addAttribute("docsStatus",docsStatus);
	}
	
	public void reportingList(ModelMap map,int pageNum,int empNo,String docsStatus,String pageName) {
		int limitCount=((pageNum - 1 ) * 10);
		int contentNum =10;
		List<Integer> listCnt = docsMapper.totalReportingCnt(empNo,docsStatus,pageName);
		int totalCnt = 0;
		
		for(int x=0; x< listCnt.size(); x++) {
			totalCnt += listCnt.get(x);
		}
		
		PageHandler pageHandler = pageHandler(totalCnt,pageNum,contentNum);	
		
		/* docsStatus a일 경우 처리해야됨 */
		List<ReportingListVO> reportingList = 
				docsMapper.reportingList(empNo,docsStatus,pageName,limitCount,contentNum);
		
		for(int x=0; x<reportingList.size(); x++){
			int no = (totalCnt-limitCount)-x;
			reportingList.get(x).setBoard_no(no);
		}
		
		map.addAttribute("list",reportingList);
		map.addAttribute("size",reportingList.size());
		map.addAttribute("pageHandler",pageHandler);
		map.addAttribute("docsStatus",docsStatus);
	}
	
	
	public void DoApprovalDocs(String docsType,int docsNo,String decision) throws Exception{	
		/* 0. m.approval 이 전결할지 말지 체크 
		 * 1. 승인을 하기전에 문서올린 직원이 f_approval가 null 여부 체크 
		 * 2. null 이면 상태값 i -> a, 아니면 i -> y
		 */
		if(docsType.equals("holiday")) {		
			if(!decision.equals("n")) {
				HolidayVO holidayVO = docsMapper.findByHolidayNo(docsNo);
				if(holidayVO.getF_approver() != null) { //최종승인자가 존재한다면
					 decision = "a"; // 1차 승인 			
				 }			
			 }
			 docsMapper.updateDocsStatus(decision,docsNo,docsType);
		}else {
			if(!decision.equals("n")) {
				if(docsMapper.getTripApproval(docsNo) != null) {
					 decision = "a"; // 1차 승인 	
				}
			}
			docsMapper.updateDocsStatus(decision,docsNo,docsType);		
		}
	}

	private PageHandler pageHandler(int totalCount,int pageNum,int contentNum){

		PageHandler pageHandler = new PageHandler();
		pageHandler.setTotalcount(totalCount);
		pageHandler.setPagenum(pageNum);
		pageHandler.setContentnum(contentNum);
		pageHandler.setCurrentblock(pageNum);
		pageHandler.setLastblock(pageHandler.getTotalcount());
		pageHandler.prevnext(pageNum);
		pageHandler.setStartPage(pageHandler.getCurrentblock());
		pageHandler.setEndPage(pageHandler.getLastblock(),pageHandler.getCurrentblock());

		return pageHandler;
	}
	

}