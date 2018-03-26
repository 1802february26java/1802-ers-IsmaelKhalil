package com.revature.service;

import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeToken;
import com.revature.repository.EmployeeRepository;
import com.revature.repository.EmployeeRepositoryJdbc;

public class EmployeeServiceAlpha implements EmployeeService {
	   
		private static Logger logger = Logger.getLogger(EmployeeServiceAlpha.class);
		
	    private static EmployeeService employeeService = new EmployeeServiceAlpha();

		private EmployeeRepository repository = EmployeeRepositoryJdbc.getInstance();
		
		private EmployeeServiceAlpha() { }
		
		public static EmployeeService getInstance() {
			return employeeService;
		}

	@Override
	public Employee authenticate(Employee employee) {
	
        Employee loggedEmployee = repository.select(employee.getUsername());
                
        if (loggedEmployee != null && loggedEmployee.getPassword().equals(repository.getPasswordHash(employee))) {
            return loggedEmployee;
        }
        return null;
    }


	@Override
	public Employee getEmployeeInformation(Employee employee) {
		return repository.select(employee.getId());
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
		Employee loggedEmployee = EmployeeRepositoryJdbc.getInstance().select(employee.getUsername());
		if(loggedEmployee != null && loggedEmployee.getUsername().equals(employee.getUsername())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean createPasswordToken(Employee employee) {
		return EmployeeRepositoryJdbc.getInstance().insertEmployeeToken(new EmployeeToken());
	}

	@Override
	public boolean deletePasswordToken(EmployeeToken employeeToken) {
	    return EmployeeRepositoryJdbc.getInstance().deleteEmployeeToken(employeeToken);
	}

	@Override
	public boolean isTokenExpired(EmployeeToken employeeToken) {
		EmployeeToken token = EmployeeRepositoryJdbc.getInstance().selectEmployeeToken(employeeToken);
		
		if(token.getId() == 0) {
			return false;
		} else {
			return true;
		}
	}

}
