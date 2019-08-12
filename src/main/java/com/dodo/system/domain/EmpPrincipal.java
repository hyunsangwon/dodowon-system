package com.dodo.system.domain;

import com.dodo.system.vo.EmpVO;
import com.dodo.system.vo.RoleVO;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@ToString
@Getter
public class EmpPrincipal implements UserDetails {

    private EmpVO empVO;

    private RoleVO roleVO;

    public EmpPrincipal(EmpVO empVO,RoleVO roleVO){
        this.empVO = empVO;
        this.roleVO = roleVO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new EmpGrant(roleVO.getRole_name()));//일반 배열을 ArrayList로 변환
    }

    @Override
    public String getPassword() {
        return empVO.getPassword();
    }

    @Override
    public String getUsername() {
        return empVO.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmpId(){
        return empVO.getId();
    }

    public int getEmpNo() {
        return empVO.getNo();     
    }
    
    
}
