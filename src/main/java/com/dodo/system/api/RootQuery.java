package com.dodo.system.api;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.dodo.system.service.EmpService;
import com.dodo.system.vo.EmpVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
