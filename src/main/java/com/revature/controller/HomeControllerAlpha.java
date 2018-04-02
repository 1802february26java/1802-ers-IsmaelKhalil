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
		
		if(loggedEmployee == null ) {
			return "login.html";
			
		}
		
		if(loggedEmployee.getEmployeeRole().getId() == 1){
			return "lackey-home.html";
		}
		
		else{
			return "leader-home.html";
		}
	}

}
