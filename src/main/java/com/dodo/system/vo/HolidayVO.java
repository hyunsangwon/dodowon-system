package com.dodo.system.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Getter
@Setter
public class HolidayVO {

    private int no;
    private int emp_id;
    private String holiday_type; //휴가 종류
    private String holiday_start; //휴가 시작
    private String holiday_end; // 휴가 종료
    private String holiday_status; //휴가 결재 여부
    private String holiday_sign_manager; //휴가 결재 사수
    private String holiday_sign_date; // 결재 날짜
    private String replacement; //업무 대체자
}
