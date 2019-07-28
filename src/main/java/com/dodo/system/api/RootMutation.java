package com.dodo.system.api;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.dodo.system.service.HomeService;
import com.dodo.system.vo.EmpVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author Sangwon Hyun on 2019-07-28
 */
@Component
public class RootMutation implements GraphQLMutationResolver {

    @Autowired
    private HomeService homeService;

    public String empSave(EmpVO empVO) throws Exception{
        homeService.saveEmp(empVO);
        return "success";
    }

}
