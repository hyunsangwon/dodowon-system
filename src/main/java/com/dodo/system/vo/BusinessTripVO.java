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

	/*출창 최대인원 4명*/
	private int emp_no;

	/*개인 입력란*/
	private String dept_name;
	private String emp_rank;
	private String name;
	private String private_num; //개인 번호
	private String account; // 지급계좌
	private String replacement; //업무 대행자

	private String dept_name1;
	private String emp_rank1;
	private String name1;
	private String private_num1;
	private String account1;
	private String replacement1;

	private String dept_name2;
	private String emp_rank2;
	private String name2;
	private String private_num2;
	private String account2;
	private String replacement2;

	private String dept_name3;
	private String emp_rank3;
	private String name3;
	private String private_num3;
	private String account3;
	private String replacement3;

	/*공통 입력란*/
	private String bt_location; //출장 지역
	private String bt_reason; //출장 목적
	private String bt_start; //출장 시작
	private String bt_end; // 출장 종료
	private String food_money; //식비
	private String room_charge; //숙박비
	private String tran_cost; //교통비
	private String tran_local_cost; //현지 교통비
	private String etc; //기타 비용
	private String g_num; //계정 번호
	private String help; //협조
	private String b_num; //발의 번호
	
}
