package com.dodo.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dodo.system.vo.HolidayVO;
import com.dodo.system.vo.TripEtcVO;
import com.dodo.system.vo.TripProposerVO;
import com.dodo.system.vo.TripVO;

import java.util.List;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Repository
public interface DocsMapper {
	
	public int setHoliday(HolidayVO holidayVO);

	/*paging*/
	public int totalCntHoliday(@Param("emp_no") int emp_no,
							   @Param("holiday_status") String holiday_status);

	public List<HolidayVO> holidayList(@Param("emp_no") int emp_no,
									   @Param("limitcount") int limitcount,
									   @Param("contentnum") int contentnum,
									   @Param("holiday_status") String holiday_status);


	public HolidayVO findByHolidayNo(@Param("no") int no);

	public void removeHoliday(@Param("no") int no);
	public int updateHoliday(HolidayVO holidayVO);

	public int setTrip(TripVO tripVo);
	public int setTripProposer(TripProposerVO tripProposerVO);
	public int setEtc(TripEtcVO tripEtcVO);
	
	public int getTripNo(TripVO tripVo);
	
	
}
