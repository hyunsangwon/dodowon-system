package com.dodo.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.dodo.system.domain.PageHandler;
import com.dodo.system.mapper.AdminMapper;
import com.dodo.system.vo.EmpVO;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Service
public class AdminService {
	
	@Autowired
	private AdminMapper adminMapper;
	
	public void tripList(ModelMap map,int pageNum){

		int limitCount=((pageNum - 1 ) * 10);
		int contentNum =10;
		int totalCnt = adminMapper.totalCntEmp();
		PageHandler pageHandler = pageHandler(totalCnt,pageNum,contentNum);

		List<EmpVO> list = adminMapper.EmpList(limitCount, contentNum);

		for(int x=0; x<list.size(); x++){
			int no = (totalCnt-limitCount)-x;
			list.get(x).setBoard_no(no);
		}
		map.addAttribute("list",list);
		map.addAttribute("size",list.size());
		map.addAttribute("pageHandler",pageHandler);
		
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
