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
import com.dodo.system.vo.EmpVO;

@Component
public class Interceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(Interceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
				
		logger.debug("======================================================");
		logger.debug("Call " + request.getRequestURI());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		EmpPrincipal empPrincipal = (EmpPrincipal) auth.getPrincipal();
		request.setAttribute("role_name",empPrincipal.getRoleVO().getRole_name());
		request.setAttribute("emp_no",empPrincipal.getEmpNo());
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		EmpPrincipal empPrincipal = (EmpPrincipal) auth.getPrincipal();		
		modelAndView.addObject("roleName", empPrincipal.getRoleVO().getRole_name());
		modelAndView.addObject("empVO", empPrincipal.getEmpVO());
		logger.debug("======================================================");

		/*
		 * if(request.getAttribute("empVO") != null) { EmpVO empVO = (EmpVO)
		 * request.getAttribute("empVO"); }
		 */
	}

}
