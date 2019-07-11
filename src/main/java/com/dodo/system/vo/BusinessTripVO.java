package com.dodo.system.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Getter
@Setter
public class BusinessTripVO {

	private int no;
	private int emp_no;
	private String dept_name;
	private String emp_rank;
	private String name;
	private String phone;
	private String account; // 지급계좌
	private String bt_location; //출장 지역
	private String bt_reason; //출장 목적
	private String bt_date; //출장 기간 07/08 ~ 07/10
	private String bt_term; //출장 일정  2박 3일
	private String food_money; //식비
	private String room_charge; //숙박비
	private String tran_cost; //교통비
	private String tran_local_cost; //현지 교통비
	private String etc; //기타
	private String g_num; //계정 번호
	private String help; //협조
	private String b_num; //발의 번호
	
}
