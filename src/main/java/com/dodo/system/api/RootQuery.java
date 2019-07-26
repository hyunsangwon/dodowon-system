package com.dodo.system.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.dodo.system.service.EmpService;
import com.dodo.system.vo.EmpVO;

/**
 * Author Sangwon Hyun on 2019-07-25
 */
@Component
public class RootQuery implements GraphQLQueryResolver {

    @Autowired
    private EmpService empService;

    public List<EmpVO> allEmps(){
        return empService.empFindAll();
    }
}
