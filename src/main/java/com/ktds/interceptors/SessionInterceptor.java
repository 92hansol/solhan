package com.ktds.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ktds.member.constatnts.Member;


public class SessionInterceptor extends HandlerInterceptorAdapter {
	// 로그를 쓰기위한 코드 
	private final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object controller)
			throws Exception {

		String contextPath = request.getContextPath();

		if (request.getSession().getAttribute(Member.USER) == null) {

			
			logger.info(request.getRequestURI() + "안돼 돌아가!");
			
			response.sendRedirect(contextPath + "/login"); // 멤버 유저가 null이면 로그인 하고와!
			return false;
		}

		return true; // return ture일때만 Object controller 파라미터한테 요청가

	}

}
