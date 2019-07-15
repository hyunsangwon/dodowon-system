package com.dodo.system.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Getter
@Setter
public class TripProposerVO {
	
	private int trip_no; //출장 문서 FK
	private String dept_name;
	private String emp_rank;
	private String name;
	private int private_num; //개인 번호
	private String account; // 지급계좌
	private String replacement; //업무 대행자
	
}
