package com.revature.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeToken;
import com.revature.repository.EmployeeRepositoryJdbc;


public class EmployeeServiceAlpha implements EmployeeService {
   
	private static Logger logger = Logger.getLogger(EmployeeServiceAlpha.class);
	
    private static EmployeeService employeeService = new EmployeeServiceAlpha();
	
	private EmployeeServiceAlpha() { }
	
	public static EmployeeService getInstance() {
		return employeeService;
	}
	
	
	@Override
	public Employee authenticate(Employee employee) {
	
		Employee loggedEmployee = EmployeeRepositoryJdbc.getInstance().select(employee.getUsername());
		
		if(loggedEmployee.getId() == 0) {
			loggedEmployee.setPassword("");
		}
		
	    if(loggedEmployee.getPassword().equals(EmployeeRepositoryJdbc.getInstance().getPasswordHash(employee))) {
	    	
		     return loggedEmployee;
				
		}
				
		     return new Employee();
	}

	
	@Override
	public Employee getEmployeeInformation(Employee employee) {
		
		if (employee.getId()>0){
		return EmployeeRepositoryJdbc.getInstance().select(employee.getId());
		}
		else{
	    return EmployeeRepositoryJdbc.getInstance().select(employee.getUsername());
		}
		
	}

	
	@Override
	public Set<Employee> getAllEmployeesInformation() {
		
		return EmployeeRepositoryJdbc.getInstance().selectAll();
		
	}

	
	@Override
	public boolean createEmployee(Employee employee) {
		
		return EmployeeRepositoryJdbc.getInstance().insert(employee);
		
	}

	
	@Override
	public boolean updateEmployeeInformation(Employee employee) {
		
		return EmployeeRepositoryJdbc.getInstance().update(employee);
		
	}

	
	@Override
	public boolean updatePassword(Employee employee) {
		
		employee.setPassword(EmployeeRepositoryJdbc.getInstance().getPasswordHash(employee));
		
		return EmployeeRepositoryJdbc.getInstance().update(employee);
		
	}

	
	@Override
	public boolean isUsernameTaken(Employee employee) {
		
	Employee existedEmployee = EmployeeRepositoryJdbc.getInstance().select(employee.getUsername());
		
	    if(existedEmployee.getUsername() == null){
	    	logger.trace("Username is available.");
	    	return false;
	    	
	    }
	    else{
			logger.trace("Username exists.");
			return true;
		}
		
	}

	
	@Override
	public boolean createPasswordToken(Employee employee) {
		return false;		 
	}

	
	@Override
	public boolean deletePasswordToken(EmployeeToken employeeToken) {
		return false;		 
	}

	
	@Override
	public boolean isTokenExpired(EmployeeToken employeeToken) {
		return false;		 
	}
}