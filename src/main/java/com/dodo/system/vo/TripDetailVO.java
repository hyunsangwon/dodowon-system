package com.dodo.system.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Author Sangwon Hyun on 2019-07-16
 */
@Getter
@Setter
@ToString
public class TripDetailVO {
	
	/*고유번호*/
	private int no;
	/*개인정보*/
	private int proposer_no;
	private String dept_name;
	private String emp_rank;
	private String name;
	private String private_num; //개인 번호
	private String account; // 지급계좌
	private String replacement; //업무 대행자
	private String trip_reg_date;//문서 기안날짜
    private String m_approver; // 협조자
    private String f_approver; // 결재자
    private String trip_status;
    
	/* 공통 정보 */
	private String docs_no; //출장 번호
	private String location; //출장 지역
	private String reason; //출장 목적
	private String bt_start; //출장 시작
	private String bt_end; // 출장 종료
	private int team_cnt; //몇명이서 출장 가는지
	
	private int etc_no;
	private int food_money; //식비
	private int room_charge; //숙박비
	private int tran_cost; //교통비
	private int tran_train_cost; //기차 비용
	private int tran_car_cost; //자동차 비용
	private int tran_airplane_cost;//비행기 비용
	private int tran_bus_cost; //버스 비용
	private int tran_local_cost; //현지 교통비
	private int etc; //기타 비용
}
