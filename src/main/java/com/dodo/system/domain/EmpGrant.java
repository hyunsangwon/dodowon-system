package com.dodo.system.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
public class EmpGrant implements GrantedAuthority {

    private String roleName;

    public EmpGrant(String roleName){
        this.roleName = roleName;
    }

    @Override
    public String getAuthority() {
        return roleName;
    }
}
