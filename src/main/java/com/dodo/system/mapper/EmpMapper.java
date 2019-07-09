package com.dodo.system.mapper;

import com.dodo.system.vo.EmpVO;
import com.dodo.system.vo.RoleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Repository
public interface EmpMapper {

    public EmpVO findByEmpId (@Param("id") String id);
    public int setEmp (EmpVO empVO);
    
}
