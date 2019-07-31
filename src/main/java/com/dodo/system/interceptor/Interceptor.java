package com.dodo.system.interceptor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dodo.system.domain.EmpPrincipal;
import com.dodo.system.mapper.DocsMapper;

@Component
public class Interceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(Interceptor.class);
	
	@Autowired
	private DocsMapper docsMapper;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
				
		logger.debug("====================================================== PreHandle");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		EmpPrincipal empPrincipal = (EmpPrincipal) auth.getPrincipal();
		request.setAttribute("role_name",empPrincipal.getRoleVO().getRole_name());
		request.setAttribute("emp_no",empPrincipal.getEmpNo());
		request.setAttribute("emp_id",empPrincipal.getEmpVO().getId());
		request.setAttribute("company",empPrincipal.getEmpVO().getCompany());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		EmpPrincipal empPrincipal = (EmpPrincipal) auth.getPrincipal();				
		String roleName = empPrincipal.getRoleVO().getRole_name();
		int empNo = empPrincipal.getEmpNo();
		
		modelAndView.addObject("roleName",roleName);
		modelAndView.addObject("empNo",empNo);
		
		SimpleDateFormat format = new SimpleDateFormat ("HH:mm");
		Calendar time = Calendar.getInstance();
		modelAndView.addObject("now",format.format(time.getTime()));

		if(!roleName.equals("MANAGER")){
			List<Integer> listCnt = docsMapper.totalReportingCnt(empNo,"reporting","all","all");
			int totalCnt = 0;
			for(int x=0; x< listCnt.size(); x++) {
				totalCnt += listCnt.get(x);
			}
			/* 결재해야될 문서 알람 개수 */
			modelAndView.addObject("msgCnt",totalCnt);
		}
		logger.debug("====================================================== PostHandle");
	}

}
