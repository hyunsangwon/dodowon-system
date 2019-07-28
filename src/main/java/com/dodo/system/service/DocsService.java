package com.dodo.system.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dodo.system.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.dodo.system.domain.PageHandler;
import com.dodo.system.mapper.DocsMapper;
import com.dodo.system.mapper.EmpMapper;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Service
public class DocsService {

	@Autowired
	private DocsMapper docsMapper;
	
	@Autowired
	private EmpMapper empMapper;
	
	public int saveHolidayDocs(HolidayVO holidayVO) throws Exception{
		return docsMapper.setHoliday(holidayVO);
	}

	public HolidayVO findByHolidayNo(int holiday_no) throws Exception{
		return docsMapper.findByHolidayNo(holiday_no);
	}

	public void findByTripNo(ModelMap map,int tripNo) throws Exception{
		
		List<TripDetailVO> list = docsMapper.findByDocsTripNo(tripNo);
        long diffDays = CalcDays(list.get(0).getBt_start(),list.get(0).getBt_end());
        
        map.addAttribute("diffDays",diffDays);
		map.addAttribute("list",list);		
		map.addAttribute("tripNo",tripNo);
		
		List<TripEtcVO> etcList = docsMapper.findByEtcTripNo(tripNo);
		map.addAttribute("etcList",null);
		map.addAttribute("etcListSize",0);
		if(etcList != null) {
			map.addAttribute("etcList",etcList);
			map.addAttribute("etcListSize",etcList.size());
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
	public int saveTripDocs(TripInputVO tripInputVO) throws Exception{

		int flag = docsMapper.setTrip(tripInputVO);

		if(flag > 0) {
			int trip_no = docsMapper.getTripNo(tripInputVO);
			saveTripProposer(tripInputVO,trip_no);
			if(tripInputVO.getEtc_cnt() > 0){
				saveTripETC(tripInputVO,trip_no);
			}
		}
		return flag;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public void saveTripProposer(TripInputVO tripInputVO,int trip_no) throws Exception{
		String[] deptName = tripInputVO.getDept_name();
		String[] empRank = tripInputVO.getEmp_rank();
		String[] name= tripInputVO.getName();
		String[] privateNum = tripInputVO.getPrivate_num();
		String[] replacement = tripInputVO.getReplacement();
		String[] account = tripInputVO.getAccount();

		TripProposerVO tripProposerVO = new TripProposerVO();
		tripProposerVO.setTrip_no(trip_no);

		for(int x=0; x<tripInputVO.getTeam_cnt(); x++){
			tripProposerVO.setDept_name(deptName[x]);
			tripProposerVO.setEmp_rank(empRank[x]);
			tripProposerVO.setName(name[x]);
			tripProposerVO.setPrivate_num(privateNum[x]);
			tripProposerVO.setReplacement(replacement[x]);
			tripProposerVO.setAccount(account[x]);
			docsMapper.setTripProposer(tripProposerVO);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public void saveTripETC(TripInputVO tripInputVO,int trip_no) throws Exception{
		String[] b_num = tripInputVO.getB_num();
		String[] help = tripInputVO.getHelp();
		String[] g_num = tripInputVO.getG_num();

		TripEtcVO tripEtcVO = new TripEtcVO();
		tripEtcVO.setTrip_no(trip_no);

		for(int x=0; x< tripInputVO.getEtc_cnt(); x++){
			tripEtcVO.setB_num(b_num[x]);
			tripEtcVO.setHelp(help[x]);
			tripEtcVO.setG_num(g_num[x]);
			docsMapper.setEtc(tripEtcVO);
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

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public int updateTrip(TripInputVO tripInputVO) throws Exception{

		/*update docs_trip*/
		int flag = docsMapper.updateTrip(tripInputVO);

		if(flag > 0){
		/*  Update docs_trip_proposer*/
			List<TripProposerVO> list = docsMapper.findByTripProposerNo(tripInputVO.getNo());
			TripDetailVO tripDetailVO = new TripDetailVO();

			String[] deptName = tripInputVO.getDept_name();
			String[] empRank = tripInputVO.getEmp_rank();
			String[] name= tripInputVO.getName();
			String[] privateNum = tripInputVO.getPrivate_num();
			String[] replacement = tripInputVO.getReplacement();
			String[] account = tripInputVO.getAccount();

			for(int x=0; x<list.size(); x++) {
				tripDetailVO.setProposer_no(list.get(x).getProposer_no());//docs_trip_proposer FK값
				tripDetailVO.setDept_name(deptName[x]);
				tripDetailVO.setEmp_rank(empRank[x]);
				tripDetailVO.setName(name[x]);
				tripDetailVO.setPrivate_num(privateNum[x]);
				tripDetailVO.setReplacement(replacement[x]);
				tripDetailVO.setAccount(account[x]);
				docsMapper.updateTripProposer(tripDetailVO);
			}
			/*update docs_trip_etc*/
			if(tripInputVO.getEtc_cnt() > 0){
				updateTripEtc(tripInputVO);
			}
		}
		return flag;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public void updateTripEtc(TripInputVO tripInputVO) throws Exception{

			List<TripEtcVO> etcList = docsMapper.findByTripEtcNo(tripInputVO.getNo());
			TripEtcVO tripEtcVO = new TripEtcVO();

			String[] g_num = tripInputVO.getG_num();
			String[] help = tripInputVO.getHelp();
			String[] b_bum = tripInputVO.getB_num();

			for(int x=0; x<tripInputVO.getEtc_cnt(); x++){
				tripEtcVO.setEtc_no(etcList.get(x).getEtc_no());
				tripEtcVO.setG_num(g_num[x]);
				tripEtcVO.setHelp(help[x]);
				tripEtcVO.setB_num(b_bum[x]);
				docsMapper.updateTripEtc(tripEtcVO);
			}

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
	
	public void reportingList(ModelMap map,int pageNum,int empNo,String docsStatus,
								String condition,String value,String pageName) {
		
		int limitCount=((pageNum - 1 ) * 10);
		int contentNum =10;
		List<Integer> listCnt = docsMapper.totalReportingCnt(empNo,docsStatus,pageName,condition,value);
		int totalCnt = 0;
		
		for(int x=0; x< listCnt.size(); x++) {
			totalCnt += listCnt.get(x);
		}
		
		PageHandler pageHandler = pageHandler(totalCnt,pageNum,contentNum);	
		
		/* docsStatus a일 경우 처리해야됨 */
		List<ReportingListVO> reportingList = 
				docsMapper.reportingList(empNo,docsStatus,pageName,
						limitCount,contentNum,condition,value);
		
		for(int x=0; x<reportingList.size(); x++){
			int no = (totalCnt-limitCount)-x;
			reportingList.get(x).setBoard_no(no);
		}
		
		map.addAttribute("list",reportingList);
		map.addAttribute("size",reportingList.size());
		map.addAttribute("pageHandler",pageHandler);
		map.addAttribute("docsStatus",docsStatus);
		map.addAttribute("pageName", pageName);
		map.addAttribute("condition", condition);
		map.addAttribute("value", value);
	}

	public void SetModelMap(int listSize,List list,ModelMap map,PageHandler pageHandler,String docsStatus){

		map.addAttribute("list",list);
		map.addAttribute("size",listSize);
		map.addAttribute("pageHandler",pageHandler);
		map.addAttribute("docsStatus",docsStatus);
	}

	
	
	public void DoApprovalDocs(String docsType,int docsNo,String decision,int myEmpNo) throws Exception{
		/* 0. m.approval 이 전결할지 말지 체크 
		 * 1. 승인을 하기전에 문서올린 직원이 f_approval가 null 여부 체크 
		 * 2. null 이면 상태값 i -> a, 아니면 i -> y
		 */
		if(docsType.equals("holiday")) {
			 docsMapper.updateDocsStatus(decision,docsNo,docsType);
		}else {
			if(!decision.equals("n")) {
				String f_approver = docsMapper.getTripApproval(docsNo);
				if(f_approver != null) {
					if(myEmpNo == Integer.parseInt(f_approver)){
						decision = "y"; // 1차 승인
					}else {
						decision = "a"; // 1차 승인
					}
				}
			}
			docsMapper.updateDocsStatus(decision,docsNo,docsType);
		}
	}
	
	/*휴가 승인시 휴가일수 차감*/
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public void DoApprovalHoliday(int diffDays,int docsNo,int empNo,int myEmpNo){
		
		String decision = "y";
		
		HolidayVO holidayVO = docsMapper.findByHolidayNo(docsNo);
		if(holidayVO.getF_approver() != null) { //non-null 중간 -> 최종(y) , null 중간 (y)
			if(myEmpNo == Integer.parseInt(holidayVO.getF_approver())){
				decision = "y"; // 1차 승인
			}else{
				decision = "a"; // 1차 승인
			}
		 }

		docsMapper.updateDocsStatus(decision,docsNo,"holiday");
		
		/*최종 승인이라면  휴가일을 업데이트한다.*/
		if(decision.equals("y")) {
			 EmpVO empVO = empMapper.getHoliday(empNo);
			 int holiday = empVO.getHoliday();
			 holiday = holiday-diffDays;
			 empMapper.updateHoliday(empNo,holiday);
		}
	}
	
	
	public long CalcDays(String startDay,String endDay)throws Exception {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = formatter.parse(startDay);
        Date endDate = formatter.parse(endDay);

        // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
        long diff = endDate.getTime() - beginDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        
        return diffDays;
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