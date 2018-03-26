package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.model.EmployeeToken;
import com.revature.util.ConnectionUtil;

public class EmployeeRepositoryJdbc implements EmployeeRepository {

	private static Logger logger = Logger.getLogger(EmployeeRepositoryJdbc.class);

	/*Singleton transformation of JDBC implementation object */
	private static EmployeeRepositoryJdbc employeeDaoJdbc = new EmployeeRepositoryJdbc();

	private EmployeeRepositoryJdbc() {

	}

	public static EmployeeRepositoryJdbc getInstance() {
		if(employeeDaoJdbc == null) {
			employeeDaoJdbc = new EmployeeRepositoryJdbc();
		}

		return employeeDaoJdbc;
	}

	@Override
	public boolean insert(Employee employee) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "INSERT INTO USER_T VALUES(NULL,?,?,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(command);

			//Set attributes to be inserted
			statement.setString(++statementIndex, employee.getFirstName().toUpperCase());
			statement.setString(++statementIndex, employee.getLastName().toUpperCase());
			statement.setString(++statementIndex, employee.getUsername().toLowerCase());
			statement.setString(++statementIndex, employee.getPassword());
			statement.setString(++statementIndex, employee.getEmail().toLowerCase());
			statement.setInt(++statementIndex, employee.getEmployeeRole().getId());

			if(statement.executeUpdate() > 0) {
				logger.trace("Successfully created a new employee");
				return true;
			}
		} catch (SQLException e) {
			logger.warn("Employee creation failed", e);
		}
		return false;
	}

	@Override
	public boolean update(Employee employee) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "UPDATE USER_T SET U_FIRSTNAME = ?, U_LASTNAME = ?, U_USERNAME = ?, U_PASSWORD = ?, U_EMAIL = ?, UR_ID = ?";

			PreparedStatement statement = connection.prepareStatement(command);

			//Set attributes to be inserted
			statement.setString(++statementIndex, employee.getFirstName().toUpperCase());
			statement.setString(++statementIndex, employee.getLastName().toUpperCase());
			statement.setString(++statementIndex, employee.getUsername().toLowerCase());
			statement.setString(++statementIndex, employee.getPassword());
			statement.setString(++statementIndex, employee.getEmail().toLowerCase());
			statement.setInt(++statementIndex, employee.getEmployeeRole().getId());

			if(statement.executeUpdate() > 0) {
				logger.trace("Successfully updated employee");
				return true;
			}
		} catch (SQLException e) {
			logger.warn("Employee update failed", e);
		}
		return false;
	}

	@Override
	public Employee select(int employeeId) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "SELECT * FROM USER_T, USER_ROLE WHERE U_ID = ? AND USER_T.UR_ID = USER_ROLE.UR_ID";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setInt(++statementIndex, employeeId);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				Employee employee = new Employee();

				employee.setId(result.getInt("U_ID"));
				employee.setFirstName(result.getString("U_FIRST_NAME"));
				employee.setLastName(result.getString("U_LAST_NAME"));
				employee.setUsername(result.getString("U_USERNAME"));
				employee.setPassword(result.getString("U_PASSWORD"));
				employee.setEmail(result.getString("U_EMAIL"));
				employee.setEmployeeRole(new EmployeeRole(result.getInt("UR_ID"), result.getString("UR_TYPE")));

				return employee;
			}
		} catch (SQLException e) {
			logger.warn("Employee retrieval failed due to exception being thrown.", e);
		}
		return null;
	}

	@Override
	public Employee select(String username) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "SELECT * FROM USER_T, USER_ROLE WHERE U_USERNAME = ? AND USER_T.UR_ID = USER_ROLE.UR_ID";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setString(++statementIndex, username);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				Employee employee = new Employee();

				employee.setId(result.getInt("U_ID"));
				employee.setFirstName(result.getString("U_FIRST_NAME"));
				employee.setLastName(result.getString("U_LAST_NAME"));
				employee.setUsername(result.getString("U_USERNAME"));
				employee.setPassword(result.getString("U_PASSWORD"));
				employee.setEmail(result.getString("U_EMAIL"));
				employee.setEmployeeRole(new EmployeeRole(result.getInt("UR_ID"), result.getString("UR_TYPE")));

				return employee;
			}
		} catch (SQLException e) {
			logger.warn("Employee retrieval failed due to exception being thrown.", e);
		}
		return null;
	}

	@Override
	public Set<Employee> selectAll() {
		Set<Employee> employees = new HashSet<>();
		try(Connection connection = ConnectionUtil.getConnection()) {
			final String command = "SELECT * FROM USER_T, USER_ROLE";
			PreparedStatement statement = connection.prepareStatement(command);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				Employee employee = new Employee();

				employee.setId(result.getInt("U_ID"));
				employee.setFirstName(result.getString("U_FIRST_NAME"));
				employee.setLastName(result.getString("U_LAST_NAME"));
				employee.setUsername(result.getString("U_USERNAME"));
				employee.setPassword(result.getString("U_PASSWORD"));
				employee.setEmail(result.getString("U_EMAIL"));
				employee.setEmployeeRole(new EmployeeRole(result.getInt("UR_ID"), result.getString("UR_TYPE")));

				employees.add(employee);;
			}
		} catch (SQLException e) {
			logger.warn("Exception selecting all employees", e);
		} 
		return employees;
	}

    @Override
    public String getPasswordHash(Employee employee) {
        logger.trace("Getting password hash");
        try(Connection connection = ConnectionUtil.getConnection()) {
            int statementIndex = 0;
            String command = "SELECT GET_HASH(?) AS HASH FROM DUAL";
            PreparedStatement statement = connection.prepareStatement(command);
            statement.setString(++statementIndex, employee.getPassword());
            ResultSet result = statement.executeQuery();
            if(result.next()) {
                return result.getString("HASH");
            }
        } catch (SQLException e) {
            logger.warn("Exception getting customer hash", e);
        } 
        return new String();
    }

	@Override
	public boolean insertEmployeeToken(EmployeeToken employeeToken) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "INSERT INTO PASSWORD_RECOVERY VALUES(NULL,NULL,?,?)";

			PreparedStatement statement = connection.prepareStatement(command);

			statement.setTimestamp(++statementIndex, Timestamp.valueOf(employeeToken.getCreationDate()));
			statement.setInt(++statementIndex, employeeToken.getId());

			if(statement.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			logger.warn("Token creation failed, exception thrown", e);
		}
		return false;
	}

	@Override
	public boolean deleteEmployeeToken(EmployeeToken employeeToken) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "DELETE FROM PASSWORD_RECOVERY WHERE PR_ID = ?";

			PreparedStatement statement = connection.prepareStatement(command);
			
			statement.setInt(++statementIndex, employeeToken.getId());

			if(statement.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			logger.warn("Token deletion failed, exception thrown", e);
		}
		return false;
	}

	@Override
	public EmployeeToken selectEmployeeToken(EmployeeToken employeeToken) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "SELECT * FROM PASSWORD_RECOVERY WHERE PR_ID = ?";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setInt(++statementIndex, employeeToken.getRequester().getId());
			ResultSet result = statement.executeQuery();

			if(result.next()) {
				return new EmployeeToken(
						result.getInt("PR_ID"),
						result.getString("PR_TOKEN"),
						result.getTimestamp("PR_TIME").toLocalDateTime(),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("U_ID"))
						);
			}
		} catch (SQLException e) {
			logger.warn("Employee retrieval failed due to exception being thrown.", e);
		}
		return new EmployeeToken();
	}


}


