package com.dodo.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dodo.system.vo.HolidayVO;
import com.dodo.system.vo.ReportingListVO;
import com.dodo.system.vo.TripDetailVO;
import com.dodo.system.vo.TripEtcVO;
import com.dodo.system.vo.TripProposerVO;
import com.dodo.system.vo.TripVO;

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

	public int totalCntTrip(@Param("emp_no") int emp_no,
							@Param("trip_status") String trip_status);

	public List<TripVO> tripList(@Param("emp_no") int emp_no,
								 @Param("limitcount") int limitcount,
								 @Param("contentnum") int contentnum,
								 @Param("trip_status") String trip_status);


	public HolidayVO findByHolidayNo(@Param("holiday_no") int holiday_no);

	public void removeHoliday(@Param("no") int no);
	public int updateHoliday(HolidayVO holidayVO);

	public int setTrip(TripVO tripVo);
	public int setTripProposer(TripProposerVO tripProposerVO);
	public int setEtc(TripEtcVO tripEtcVO);
	
	public int getTripNo(TripVO tripVo);
	
	public List<TripDetailVO> findByDocsTripNo(@Param("no") int no);
	public List<TripEtcVO> findByEtcTripNo(@Param("trip_no") int trip_no);
	
	public void removeTrip(@Param("no") int no);
	
	
	public int updateTrip(TripDetailVO tripDetailVO);
	public int updateTripProposer(TripDetailVO tripDetailVO);
	public int updateTripEtc(TripEtcVO tripEtcVO);
	
	public List<TripProposerVO>findByTripProposerNo(@Param("trip_no") int trip_no);
	public List<TripEtcVO> findByTripEtcNo(@Param("trip_no") int trip_no);
	
	
	public List<Integer> totalReportingCnt(@Param("emp_no") int emp_no,
										   @Param("docs_status") String docs_status,
										   @Param("page_name") String pageName);
	
	public List<ReportingListVO> reportingList(@Param("emp_no") int emp_no,
												@Param("docs_status") String docs_status,
											    @Param("page_name") String pageName,
											    @Param("limitcount") int limitcount,
												 @Param("contentnum") int contentnum);

	
}
