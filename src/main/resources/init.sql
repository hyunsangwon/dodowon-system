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
    emp_id INTEGER(4) NOT NULL COMMENT '직원 정보',
    holiday_type VARCHAR(20) NOT NULL COMMENT '휴가 종류',
    holiday_start DATE NOT NULL COMMENT '휴가 시작일',
    holiday_end DATE NOT NULL COMMENT '휴가 종료일',
    holiday_status CHAR(1) DEFAULT 'i'COMMENT '결재 여부 (y= 승인, n= 반려, i= 대기중)',
    holiday_sign_date DATETIME COMMENT '결재 날짜',
    holiday_reason VARCHAR(100) COMMENT '휴가 사유',
    replacement VARCHAR(20) COMMENT '업무 대체자',
    FOREIGN KEY (emp_id) REFERENCES emp (no)

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#INSERT INTO role (no, role_name) VALUES (1, 'ADMIN');
#INSERT INTO role (no, role_name) VALUES (2, 'MANAGER');
#INSERT INTO role (no, role_name) VALUES (3, 'USER');