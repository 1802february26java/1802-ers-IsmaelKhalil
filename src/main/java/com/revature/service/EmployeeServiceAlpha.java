package com.revature.service;

import java.util.Set;

import com.revature.model.Employee;
import com.revature.model.EmployeeToken;
import com.revature.repository.EmployeeRepositoryJdbc;

public class EmployeeServiceAlpha implements EmployeeService {

	@Override
	public Employee authenticate(Employee employee) {
		//Information on the database
		Employee loggedEmployee = EmployeeRepositoryJdbc.getInstance().select(employee.getUsername());

		//Wont work because the password is in the form of a hash
		//What we have stored in the database is the Username + Password hash. We can't compare the blank password
		//provided by the user against the hash. Therefore, we have to obtain the hash of the user input.
		//If the hashes are the same, user is authenticated.
		if(loggedEmployee.getPassword().equals(EmployeeRepositoryJdbc.getInstance().getPasswordHash(employee))) {
			return loggedEmployee;
		}
		return null;
	}

	@Override
	public Employee getEmployeeInformation(Employee employee) {
		return EmployeeRepositoryJdbc.getInstance().select(employee.getId());
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
		if(loggedEmployee.getUsername().equals(employee.getUsername())) {
			return false;
		} else {
			return true;
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
