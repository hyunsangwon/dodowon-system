package com.dodo.system.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.dodo.system.domain.PageHandler;
import com.dodo.system.mapper.EmpMapper;
import com.dodo.system.vo.EmpVO;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Service
public class AdminService {
	
	@Autowired
	private EmpMapper empMapper;
	
	public EmpVO findByEmpId(String id) throws Exception {
		return empMapper.findByEmpId(id);
	}

	public void empList(ModelMap map,int pageNum){

		int limitCount=((pageNum - 1 ) * 10);
		int contentNum =10;
		int totalCnt = empMapper.totalCntEmp();
		PageHandler pageHandler = pageHandler(totalCnt,pageNum,contentNum);

		List<EmpVO> list = empMapper.EmpList(limitCount, contentNum);

		for(int x=0; x<list.size(); x++){
			int no = (totalCnt-limitCount)-x;
			list.get(x).setBoard_no(no);
		}
		map.addAttribute("list",list);
		map.addAttribute("size",list.size());
		map.addAttribute("pageHandler",pageHandler);
		
	}
	
	public List<EmpVO> deptFindAll(String deptName) {
		return empMapper.deptFindAll(deptName);
	}
	
	public Workbook excelDown() throws IOException{
		
		int totalCnt = empMapper.totalCntEmp();
		List<EmpVO> list = empMapper.EmpList(0,totalCnt);
		
        Workbook workbook = new HSSFWorkbook();//Excel Down 시작
        Sheet sheet = workbook.createSheet("유저 목록"); //시트 생성
        
        Row row = null; //행
        Cell cell = null; //열
        int rowNo = 0; //열 번호

        CellStyle headStyle = workbook.createCellStyle();//테이블 헤더 스타일
        // 가는 경계선을 가집니다.
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        // 배경색은 노란색입니다.
        headStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 데이터는 가운데 정렬합니다.
        headStyle.setAlignment(HorizontalAlignment.CENTER);

        // 데이터용 경계 스타일 테두리만 지정
        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);

        // 헤더 생성
        row = sheet.createRow(rowNo++);

        cell = row.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue("번호");

        cell = row.createCell(1);
        cell.setCellStyle(headStyle);
        cell.setCellValue("이름");

        cell = row.createCell(2);
        cell.setCellStyle(headStyle);
        cell.setCellValue("부서");

        cell = row.createCell(3);
        cell.setCellStyle(headStyle);
        cell.setCellValue("직위");
        
        
        for(EmpVO empvo : list) { //데이터 생성
        	row = sheet.createRow(rowNo++);
        	cell = row.createCell(0);
        	cell.setCellStyle(bodyStyle);
            cell.setCellValue(""+empvo.getNo());

            cell = row.createCell(1);
        	cell.setCellStyle(bodyStyle);
            cell.setCellValue(""+empvo.getName());
            
            cell = row.createCell(2);
        	cell.setCellStyle(bodyStyle);
            cell.setCellValue(""+empvo.getDept_name());
            
            cell = row.createCell(3);
        	cell.setCellStyle(bodyStyle);
            cell.setCellValue(""+empvo.getEmp_rank());          
        }
        
        return workbook;
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
