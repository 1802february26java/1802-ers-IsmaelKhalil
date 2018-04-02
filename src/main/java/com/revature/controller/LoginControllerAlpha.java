package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.service.EmployeeServiceAlpha;
import com.revature.util.FinalUtil;


public class LoginControllerAlpha implements LoginController {
    
	private static Logger logger = Logger.getLogger(LoginControllerAlpha.class);
	
    private static LoginController loginController = new LoginControllerAlpha();
	
	private LoginControllerAlpha() {}
	
	public static LoginController getInstance() {
		return loginController;
	}
	
	@Override
	public Object login(HttpServletRequest request) {
		if(request.getMethod().equals("GET")) {
			return "login.html";
		}

		Employee loggedEmployee = EmployeeServiceAlpha.getInstance().authenticate(
					new Employee(request.getParameter(FinalUtil.EMPLOYEE_USERNAME),
								 request.getParameter(FinalUtil.EMPLOYEE_PASSWORD))
				);

		if(loggedEmployee.getId() == 0) {
			return new ClientMessage(FinalUtil.LOGIN_FAIL);
		}
		
		request.getSession().setAttribute("loggedEmployee", loggedEmployee);
		
		return loggedEmployee;
		
	}

	
	@Override
	public String logout(HttpServletRequest request) {
		
		request.getSession().invalidate();
		return "login.html";
	}

}
