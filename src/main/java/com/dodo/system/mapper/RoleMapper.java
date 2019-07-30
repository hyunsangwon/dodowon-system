package com.dodo.system.mapper;

import com.dodo.system.vo.EmpRoleVO;
import com.dodo.system.vo.RoleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Repository
public interface RoleMapper {

    public int setEmpRole(EmpRoleVO empRoleVO);
    public RoleVO findByEmpNo (@Param("no") int no);
    public RoleVO getRoleInfo(@Param("role_name") String role_name);
}
