package com.dodo.system.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Getter
@Setter
public class TripVO {

	private int no;
	private int board_no; //페이징 처리할때
	private int emp_no; //신청자
	private int team_cnt; //몇명이서 출장 가는지
	private String docs_no; //출장 번호
	private String location; //출장 지역
	private String reason; //출장 목적
	private String bt_start; //출장 시작
	private String bt_end; // 출장 종료
	private int food_money; //식비
	private int room_charge; //숙박비
	private int tran_cost; //교통비
	private int tran_local_cost; //현지 교통비
	private int etc; //기타 비용
	private String trip_reg_date; //기안 날짜
	private String trip_status; //결재 여부
	
}
