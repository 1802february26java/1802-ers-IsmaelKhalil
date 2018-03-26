package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.model.Employee;

public class HomeControllerAlpha implements HomeController {
	
	private static Logger logger = Logger.getLogger(HomeControllerAlpha.class);
	
	@Override
	public String showEmployeeHome(HttpServletRequest request) {
		logger.trace("Showing employee home");
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		if (employee != null) {
			logger.trace("Showing home");
			return "home.html";
		} else {
			logger.trace("Sending back to login");
			return "login.html";
		}
	}

}