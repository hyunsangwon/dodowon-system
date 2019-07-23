package com.dodo.system.mapper;

import java.util.List;

import com.dodo.system.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

	public int removeHoliday(@Param("no") int no);
	public int updateHoliday(HolidayVO holidayVO);

	public int setTrip(TripInputVO tripInputVO);
	public int setTripProposer(TripProposerVO tripProposerVO);
	public int setEtc(TripEtcVO tripEtcVO);
	
	public int getTripNo(TripInputVO tripInputVO);
	
	public List<TripDetailVO> findByDocsTripNo(@Param("no") int no);
	public List<TripEtcVO> findByEtcTripNo(@Param("trip_no") int trip_no);
	
	public void removeTrip(@Param("no") int no);
	
	
	public int updateTrip(TripInputVO tripInputVO);
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

	
	public int updateDocsStatus(@Param("status") String status,
									@Param("docs_no") int docs_no,
									@Param("docs_type") String docs_type);
	
	public String getTripApproval(@Param("docs_no") int docs_no);
	
}
