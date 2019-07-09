package com.dodo.system.service;

import com.dodo.system.domain.EmpPrincipal;
import com.dodo.system.mapper.EmpMapper;
import com.dodo.system.mapper.RoleMapper;
import com.dodo.system.vo.EmpRoleVO;
import com.dodo.system.vo.EmpVO;
import com.dodo.system.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Service
public class HomeService implements UserDetailsService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void saveEmp(EmpVO empVO) throws Exception{
		/*가입된 회원인지 체크 */
    		
        /*emp table insert*/
        empVO.setPassword(bCryptPasswordEncoder.encode(empVO.getPassword()));
        empMapper.setEmp(empVO);
        
        /*emp_role table insert*/
        RoleVO roleVO = roleMapper.getRoleInfo(empVO.getRole_name());
        EmpRoleVO empRoleVO = new EmpRoleVO();
        empRoleVO.setRole_id(roleVO.getNo());
        empVO = empMapper.findByEmpId(empVO.getId());
        empRoleVO.setEmp_id(empVO.getNo());
        roleMapper.setEmpRole(empRoleVO);
    }

    @Override
    public UserDetails loadUserByUsername(String empId) throws UsernameNotFoundException {
        EmpVO empVO = empMapper.findByEmpId(empId);
        RoleVO roleVO = roleMapper.findByEmpNo(empVO.getNo());

        if(empVO == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        return new EmpPrincipal(empVO,roleVO);
    }

}
