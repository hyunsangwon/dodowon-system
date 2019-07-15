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
	private String location; //출장 지역
	private String reason; //출장 목적
	private String bt_start; //출장 시작
	private String bt_end; // 출장 종료
	private String food_money; //식비
	private String room_charge; //숙박비
	private String tran_cost; //교통비
	private String tran_local_cost; //현지 교통비
	private String etc; //기타 비용
	
}
