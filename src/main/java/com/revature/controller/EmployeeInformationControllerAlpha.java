package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.service.EmployeeServiceAlpha;
import com.revature.util.FinalUtil;

public class EmployeeInformationControllerAlpha implements EmployeeInformationController {

	private static Logger logger = Logger.getLogger(HomeControllerAlpha.class);

	private static EmployeeInformationController employeeInformationController = new EmployeeInformationControllerAlpha();

	private EmployeeInformationControllerAlpha() {}

	public static EmployeeInformationController getInstance() {
		return employeeInformationController;
	}

	@Override
	public Object registerEmployee(HttpServletRequest request) {

        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		if(loggedEmployee == null) {
			return "login.html";
			
		}
		if(loggedEmployee.getEmployeeRole().getId() == 1) {
			return "403.html";
			
		}
		
		if(request.getMethod().equals("GET")) {
			return "register.html";
		}

		EmployeeRole employeeRole = new EmployeeRole(1,"EMPLOYEE");
		Employee employee = new Employee(0, 
				request.getParameter("firstName"), 
				request.getParameter("lastName"), 
				request.getParameter("username"),	
				request.getParameter("password"), 
				request.getParameter("email"), employeeRole
				);
		
		logger.trace(employee);

		if(!EmployeeServiceAlpha.getInstance().isUsernameTaken(employee)) {
			if(EmployeeServiceAlpha.getInstance().createEmployee(employee)) {
				return new ClientMessage(FinalUtil.CLIENT_MESSAGE_REGISTRATION_SUCCESSFUL);
			}
		} else {
			return new ClientMessage(FinalUtil.CLIENT_MESSAGE_USERNAME_TAKEN);
		}
		return new ClientMessage(FinalUtil.CLIENT_MESSAGE_REGISTRATION_FAILED);
	}

	@Override
	public Object updateEmployee(HttpServletRequest request) {

		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployees");

		if(loggedEmployee == null) {
			return "login.html";
		}

		Employee employee = new Employee (
				loggedEmployee.getId(),
				request.getParameter("firstName"),
				request.getParameter("lastName"),
				request.getParameter("username"),
				null,
				request.getParameter("email"),
				loggedEmployee.getEmployeeRole());

		if(EmployeeServiceAlpha.getInstance().updateEmployeeInformation(employee)) {
			return new ClientMessage("INFORMATION SUCCESSFULLY UPDATED");
		} else {
			return new ClientMessage("INFORMATION FAILED TO UPDATE. TRY AGAIN.");
		}			
	}

	@Override
	public Object viewEmployeeInformation(HttpServletRequest request) {

		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");

		if(loggedEmployee == null) {
			return "login.html";

		}

		if(loggedEmployee.getEmployeeRole().getId() == 1){
			if(request.getParameter("fetch") == null) {
				return "lackey-profile.html";
			} else {
				return EmployeeServiceAlpha.getInstance().getEmployeeInformation(loggedEmployee);
			}
		}

		else {
			if(request.getParameter("fetch") == null) {
				return "leader-profile.html";
			} else {
				return EmployeeServiceAlpha.getInstance().getEmployeeInformation(loggedEmployee);
			}
		}


	}

	@Override
	public Object viewAllEmployees(HttpServletRequest request) {
        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		if(loggedEmployee == null ) {
		   
			return "login.html";
			
		}
		if(loggedEmployee.getEmployeeRole().getId() == 1){
			
			logger.trace("Only Managers can view this.");
			return "403.html";
			
		}

		if(request.getParameter("fetch") == null) {
			return "leader-employees-list.html";
		} else {
		return EmployeeServiceAlpha.getInstance().getAllEmployeesInformation();
		}
	}

	@Override
	public Object usernameExists(HttpServletRequest request) {
        Employee employee = new Employee(request.getParameter("username"));
		   if(EmployeeServiceAlpha.getInstance().isUsernameTaken(employee)){
			   return new ClientMessage("This username already exists..");
		   }
		   else{
			   return new ClientMessage("This username can be used.");
		   }
	}
}