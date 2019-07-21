package com.dodo.system.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Author Sangwon Hyun on 2019-07-20
 */
@Getter
@Setter
@ToString
public class TripInputVO {

    private int no;//사원 번호
    private String docs_no; //신청번호
    private int team_cnt; //출장 인원
    /*개별정보*/
    private String[] name;
    private String[] dept_name; //소속 부서
    private String[] emp_rank; //직급
    private String[] private_num; //개인번호
    private String[] replacement; //직무 대행자
    private String[] account; //지급 계좌

    /*공통정보*/
    private String location; //출장 지역
    private String reason; //출장 목적
    private String bt_start; //출장 시작 날짜
    private String bt_end; //출잘 종료 날짜
    private int food_money; //식비
    private int room_charge; //숙박비
    private int tran_train_cost; //교통비 기차
    private int tran_car_cost; // 교통비 차
    private int tran_airplane_cost; // 교통비 비행기
    private int tran_bus_cost; //교통비 버스
    private int tran_local_cost; //현지 교통비
    private int etc; //기타 금액

    /*발인 정보*/
    private int etc_cnt; //발인 입력란 Count
    private String[] g_num; //계정 번호
    private String[] help; //협조
    private String[] b_num; //발의 번호
}
