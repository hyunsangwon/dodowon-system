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
        /*사원 가입시 pk값 처리 생각해야됨*/

        /*emp table insert*/
        empVO.setPassword(bCryptPasswordEncoder.encode(empVO.getPassword()));
        empMapper.setEmp(empVO);

        RoleVO roleVO = roleMapper.getRoleInfo(empVO.getRole_name());

        /*emp_role table insert*/
        EmpRoleVO empRoleVO = new EmpRoleVO();
        empRoleVO.setRole_id(roleVO.getNo());
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
