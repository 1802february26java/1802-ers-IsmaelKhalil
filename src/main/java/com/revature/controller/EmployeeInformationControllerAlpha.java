package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.service.EmployeeServiceAlpha;


public class EmployeeInformationControllerAlpha implements EmployeeInformationController {

	private static Logger logger = Logger.getLogger(LoginControllerAlpha.class);

	private static EmployeeInformationController employeeInformationController = new EmployeeInformationControllerAlpha();
	
	private EmployeeInformationControllerAlpha() {}
	
	public static EmployeeInformationController getInstance() {
		
		return employeeInformationController;
		
	}
	
	
	@Override
	public Object registerEmployee(HttpServletRequest request) {
		
        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		/* If customer is not logged in */
		if(loggedEmployee == null ) {
			return "login.html";
			
		}
		if(loggedEmployee.getEmployeeRole().getId() == 1){
			return "403.html";
			
		}
		
		if (request.getMethod().equals("GET")) {
			return "register.html";
			
		}

		/* Logic for POST */
		EmployeeRole employeeRole = new EmployeeRole(1,"EMPLOYEE");
		
		Employee employee = new Employee(999, request.getParameter("firstName"),
		     request.getParameter("lastName"),request.getParameter("username"),
		     request.getParameter("password"),request.getParameter("email"),employeeRole);
		
             logger.trace(employee);;

		if (EmployeeServiceAlpha.getInstance().createEmployee(employee)) {
			
			return new ClientMessage("REGISTRATION SUCCESSFUL");
			
		} else {
			
			return new ClientMessage("SOMETHING WENT WRONG");
			
		}
		
	}

	@Override
	public Object updateEmployee(HttpServletRequest request) {
		
        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		/* If customer is not logged in */
		if(loggedEmployee == null ) {
			return "login.html";
			
		}
		
		loggedEmployee.setFirstName(request.getParameter("firstName"));
		loggedEmployee.setLastName(request.getParameter("lastName"));
		//loggedEmployee.setPassword(request.getParameter("password"));
	    loggedEmployee.setEmail(request.getParameter("email"));
		
		if (EmployeeServiceAlpha.getInstance().updateEmployeeInformation(loggedEmployee)) {
			return new ClientMessage("UPDATE EMPLOYEE INFORMATION SUCCESSFUL");
		} else {
			return new ClientMessage("SOMETHING WENT WRONG");
		}
		
		
	}

	@Override
	public Object viewEmployeeInformation(HttpServletRequest request) {
		
        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		if(loggedEmployee == null ) {
			return "login.html";
			
		}
		
		if(loggedEmployee.getEmployeeRole().getId()==1){
		    if(request.getParameter("fetch") == null) {
			     return "lackey-profile.html";
		    } else {
		        return EmployeeServiceAlpha.getInstance().getEmployeeInformation(loggedEmployee);
		         }
		  }
		
		else{
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
		
		/* If customer is not logged in */
		if(loggedEmployee == null ) {
			
			return "login.html";
			
		}
		//If he/she is a employee not a manager
		if(loggedEmployee.getEmployeeRole().getId() == 1){
			return "403.html";
			
		}

		/* Client is requesting the view. */
		if(request.getParameter("fetch") == null) {
			return "leader-view-employees.html";
		} else {
		return EmployeeServiceAlpha.getInstance().getAllEmployeesInformation();
		}
	}

	
	@Override
	public Object usernameExists(HttpServletRequest request) {
		   
           Employee employee = new Employee(request.getParameter("username"));
		   if(EmployeeServiceAlpha.getInstance().isUsernameTaken(employee)){
			   return new ClientMessage("This username has been taken.");
		   }
		   else{
			   return new ClientMessage("This username has not been taken.");
		   }
			   
	}
}