package com.dodo.system.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Getter
@Setter
public class EmpVO {
    private int no;
    private String id;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String company; //소속 회사
    private String dept_name; //부서 이름
    private String emp_rank; //회사 직급
    private int holiday; //휴가 일
    private String sign_status; //싸인 등록 여부
    private String role_name;
}
