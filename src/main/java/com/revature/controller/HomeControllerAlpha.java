package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.model.Employee;

public class HomeControllerAlpha implements HomeController {
	
	private static Logger logger = Logger.getLogger(HomeControllerAlpha.class);

	private static HomeController homeController = new HomeControllerAlpha();
	
	private HomeControllerAlpha() {}
	
	public static HomeController getInstance() {
		return homeController;
	}

	@Override
	public String showEmployeeHome(HttpServletRequest request) {
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		/* If employee is not logged in */
		if(loggedEmployee == null) {
			logger.trace("Employee not found. You will be redirected to the login page.");
			return "login.html";
		}
		
		return "home.html";
	}

}