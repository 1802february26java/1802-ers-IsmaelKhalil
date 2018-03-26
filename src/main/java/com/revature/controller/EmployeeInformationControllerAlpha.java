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
		if(request.getMethod().equals("GET")) {
			return "register.html";
		}

		// Logic for POST

		Employee employee = new Employee(0, 
				request.getParameter("firstName"), 
				request.getParameter("lastName"), 
				request.getParameter("username"),	
				request.getParameter("password"), 
				request.getParameter("email"),
				new EmployeeRole(Integer.parseInt(request.getParameter("employeeRoleID")))
				);
		
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
		if (request.getMethod().equals("GET")){
			return "login.html";
		}

		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");

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
		if (request.getMethod().equals("GET")){
			return "register.html";
		}

		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");

		if(loggedEmployee == null) {
			return "login.html";
		} else {
			return EmployeeServiceAlpha.getInstance().getEmployeeInformation(loggedEmployee);
		}
	}

	@Override
	public Object viewAllEmployees(HttpServletRequest request) {
		if (request.getMethod().equals("GET")){
			return "register.html";
		}

		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");

		if(loggedEmployee == null) {
			return "login.html";
		} else if(loggedEmployee.getEmployeeRole().getId() == 2) {
			return EmployeeServiceAlpha.getInstance().getAllEmployeesInformation();
		} else {
			return "404.html";
		}
	}

	@Override
	public Object usernameExists(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}


	private void sendEmailToEmployee(Employee employee){

		String subject = "New Employee Registration";
		String body = "Here is your credential to ERS website.\n"+
				"Username: "+employee.getUsername()+"\n"
				+"Password: "+employee.getPassword()+"\n"
				+"Please visit below ERS link to login.\n"
				+"http://localhost:8085/ERS/";
		String email = employee.getEmail();

		//	      EmailThread runnableThread = new EmailThread(subject,body,email);
		//	        
		//	        Thread t = new Thread(runnableThread);
		//	        
		//	        t.start(); 
	}

}