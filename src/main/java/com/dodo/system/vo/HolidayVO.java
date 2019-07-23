package com.dodo.system.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Getter
@Setter
public class HolidayVO {

    private int no;
    private int holiday_no;
    private int board_no; //페이징 처리할때
    private String emp_id; //회원아이디
    private String holiday_type; //휴가 종류
    private String holiday_start; //휴가 시작
    private String holiday_end; // 휴가 종료
    private String holiday_status; //휴가 결재 여부
    private String holiday_sign_date; // 결재 날짜
    private String replacement; //업무 대체자
    private String holiday_reason; //휴가 사유
    private String holiday_reg_date; //기안 날짜
    private String sign_img_name; //싸인 이미지 이름 
    /*emp*/
    private int emp_no; //회원 pk
    @Size(min=10,max=11,message = "10~11자리의 숫자만 입력가능합니다.")
    private String phone; //비상 연락망
    private String dept_name; //소속 부서
    private String name;//이름
    private String emp_rank;//직위
    private String m_approver; //중간 승인자
    private String f_approver; // 최종 승인자
}
