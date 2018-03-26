package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.service.EmployeeServiceAlpha;
import com.revature.util.ConnectionUtil;
import com.revature.util.FinalUtil;

public class LoginControllerAlpha implements LoginController {
	//logger
		private static Logger logger = Logger.getLogger(ConnectionUtil.class);

	@Override
	public Object login(HttpServletRequest request) {
		logger.trace("Logging in");
		if (request.getMethod().equals("GET")) {
			return "login.html";
		}
		Employee employee = new Employee();
		employee.setUsername(request.getParameter("username"));
		employee.setPassword(request.getParameter("password"));
		employee = EmployeeServiceAlpha.getInstance().authenticate(employee);
		if (employee == null) {
			return new ClientMessage(FinalUtil.LOGIN_FAIL);
		} else {
			request.getSession().setAttribute("employee", employee);
			return "home.html";
		}
	}

	@Override
	public String logout(HttpServletRequest request) {
		
		request.getSession().invalidate();
		return "login.html";
	}

}
