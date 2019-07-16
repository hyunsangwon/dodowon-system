package com.dodo.system.service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.dodo.system.domain.PageHandler;
import com.dodo.system.mapper.DocsMapper;
import com.dodo.system.vo.HolidayVO;
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

	public HolidayVO findByHolidayNo(int no) throws Exception{
		return docsMapper.findByHolidayNo(no);
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

		HolidayVO holidayVO = new HolidayVO();
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
	
	private HashMap<String,Object> requestHandler(HttpServletRequest request){
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		Enumeration e = request.getParameterNames();//파라미터 이름들이 Enumeration클래스로 등록
		String tmp = "";
		while(e.hasMoreElements()){ //파라미터 이름이 있을때 까지!!
			tmp = (String) e.nextElement();
			hm.put(tmp.toLowerCase(), request.getParameter(tmp));
		}
		return hm;
	}

}
