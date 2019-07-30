package com.dodo.system.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.dodo.system.service.HomeService;
import com.dodo.system.vo.EmpVO;

/**
 * Author Sangwon Hyun on 2019-07-28
 */
@Component
public class RootMutation implements GraphQLMutationResolver {

    @Autowired
    private HomeService homeService;

    public String empSave(EmpVO empVO) throws Exception{
    	int flag = homeService.saveEmp(empVO);
    	if(flag > 0) {
    		return "success";
    	}else{
    		return "fail";
    	}
    }

}
