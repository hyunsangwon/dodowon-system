package com.dodo.system.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dodo.system.domain.EmpPrincipal;
@Component
public class Interceptor implements HandlerInterceptor{
	
	private static final Logger logger = LoggerFactory.getLogger(Interceptor.class);
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
 
			//logger.debug("-------- preHandel call /----------");
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			EmpPrincipal empPrincipal = (EmpPrincipal) auth.getPrincipal();
			request.setAttribute("role_name",empPrincipal.getRoleVO().getRole_name());
			
			System.out.println("Your IP : ---------------"+request.getRemoteAddr());
			System.out.println("-------- preHandel call /-----------------------");
            return true;
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        
    		//logger.debug("-------- postHandel call /----------");
    		System.out.println("-------- postHandel call /-----------------------");
    }
    
}
