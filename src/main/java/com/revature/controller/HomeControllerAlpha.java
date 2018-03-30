package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import com.revature.model.Employee;

public class HomeControllerAlpha implements HomeController {

	@Override
	public String showEmployeeHome(HttpServletRequest request) {
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");

		/* If customer is not logged in */
		if(loggedEmployee == null) {
			return "login.html";
		}


		// manager == 2; if employee == 1
		if(loggedEmployee.getEmployeeRole().getId() == 2) {
			return "leader-home.html";
		}

		else{
			return "lackey-home.html";
		}
	}
}