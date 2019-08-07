package com.dodo.system.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorLogVO {
	
	private int no;
	private String error_status;
	private String error_msg;
	private String error_url;
	private String error_time;
	
	private int error_cnt;
	private int board_no;
}
