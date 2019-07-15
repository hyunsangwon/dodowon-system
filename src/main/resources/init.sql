#CREATE DATABASE IF NOT EXISTS dodo_system DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
#USE dodo_system;

#CREATE USER IF NOT EXISTS 'dodo_admin'@'%' IDENTIFIED BY 'IeY&ei+s!a3imees';
#GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER ON dodo_system.* TO 'dodo_admin'@'%';

CREATE TABLE IF NOT EXISTS emp
(
    no INTEGER(4) AUTO_INCREMENT NOT NULL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(20) NOT NULL,
    email VARCHAR(30),
    phone VARCHAR(20),
    company VARCHAR(20) COMMENT '소속 회사',
    dept_name VARCHAR(20) COMMENT '부서 이름',
    emp_rank VARCHAR(10) COMMENT '직급',
    holiday INTEGER(4) COMMENT '휴가일',
    sign_img_name VARCHAR(20) DEFAULT 'default' COMMENT '싸인 이미지 이름',
    m_approver INTEGER(4) COMMENT '중간 승인자',
    f_approver INTEGER(4) COMMENT '최종 승인자',
    reference INTEGER(4) COMMENT '참조자',
    FOREIGN KEY (m_approver) REFERENCES emp (no),
    FOREIGN KEY (f_approver) REFERENCES emp (no),
    FOREIGN KEY (reference) REFERENCES emp (no)

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS role
(
    no INTEGER(4) NOT NULL PRIMARY KEY,
    role_name VARCHAR(20) NOT NULL

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS emp_role
(
    emp_id INTEGER(4) NOT NULL,
    role_id INTEGER(4) NOT NULL,
    FOREIGN KEY (emp_id) REFERENCES emp (no),
    FOREIGN KEY (role_id) REFERENCES role (no)

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS docs_holiday
(
    no INTEGER(4) AUTO_INCREMENT NOT NULL PRIMARY KEY,
    emp_no INTEGER(4) NOT NULL COMMENT '직원 정보',
    holiday_type VARCHAR(20) NOT NULL COMMENT '휴가 종류',
    holiday_start DATE NOT NULL COMMENT '휴가 시작일',
    holiday_end DATE NOT NULL COMMENT '휴가 종료일',
    holiday_status CHAR(1) DEFAULT 'i'COMMENT '결재 여부 (y= 승인, n= 반려, i= 대기중)',
    holiday_sign_date DATETIME COMMENT '결재 날짜',
    holiday_reason VARCHAR(100) COMMENT '휴가 사유',
    replacement VARCHAR(20) COMMENT '업무 대체자',
    holiday_reg_date datetime default CURRENT_TIMESTAMP,
    FOREIGN KEY (emp_no) REFERENCES emp (no)

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS docs_trip
(
  no INTEGER(4) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  emp_no INTEGER(4) NOT NULL COMMENT '직원 정보',
  docs_no VARCHAR(30) NOT NULL COMMENT '신청번호',
  location VARCHAR(30) NOT NULL COMMENT '출장지역',
  reason VARCHAR(50) COMMENT '출장목적',
  bt_start DATE NOT NULL COMMENT '출장 시작일',
  bt_end DATE NOT NULL COMMENT '출장 종료일',
  food_money INTEGER COMMENT '식비',
  room_charge INTEGER COMMENT '숙박비',
  tran_cost INTEGER COMMENT '교통비',
  tran_local_cost INTEGER COMMENT '현지 교통비',
  etc INTEGER COMMENT '기타 비용',
  trip_reg_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  trip_status CHAR(1) DEFAULT 'i' COMMENT '결재 여부 (y= 승인, n= 반려, i= 대기중)',
  FOREIGN KEY (emp_no) REFERENCES emp (no)
  
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS docs_trip_proposer
(
  trip_no INTEGER(4) NOT NULL COMMENT '문서 번호',
  dept_name VARCHAR(20) NOT NULL COMMENT '소속부서',
  emp_rank VARCHAR(10) COMMENT '직급',
  name VARCHAR(20) NOT NULL COMMENT '직원이름',
  private_num INTEGER NOT NULL COMMENT '개인번호',
  replacement VARCHAR(20) COMMENT '직무 대행자',
  account VARCHAR(40) COMMENT '계좌번호',
  FOREIGN KEY (trip_no) REFERENCES docs_trip (NO)
  
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS docs_trip_etc
(
 trip_no INTEGER(4) NOT NULL COMMENT '문서 번호',
 g_num VARCHAR(30) COMMENT '계정 번호',
 HELP VARCHAR(20) COMMENT '협조',
 b_num VARCHAR(30) COMMENT '발의 번호',
 FOREIGN KEY (trip_no) REFERENCES docs_trip (NO)
 
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

#INSERT INTO role (no, role_name) VALUES (1, 'ADMIN');
#INSERT INTO role (no, role_name) VALUES (2, 'MANAGER');
#INSERT INTO role (no, role_name) VALUES (3, 'USER');