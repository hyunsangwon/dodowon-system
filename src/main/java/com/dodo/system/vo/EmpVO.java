package com.dodo.system.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Getter
@Setter
@ToString
public class EmpVO {
	
	private int board_no; //페이징 처리할때
    private int no;
    private String id;
    @Size(min=4,message = "비밀번호는 최소 4글자 이상 입력하셔야 합니다.")
    private String password;
    @Size(min=4,message = "비밀번호는 최소 4글자 이상 입력하셔야 합니다.")
    private String c_password; //비밀번호 재확인
    private String name;
    @Email(regexp = "^(.+)@(.+)$",message = "이메일 양식이 잘못되었습니다.")
    private String email;
    @Size(min=10,max=11,message = "10~11자리의 숫자만 입력가능합니다.")
    private String phone;
    private String company; //소속 회사
    private String dept_name; //부서 이름
    private String emp_rank; //회사 직급
    private int holiday; //휴가 일
    private String role_name; // 권한 이름
    private String sign_img_name;//싸인 이미지 이름
    private int reference; // 참조자
}
