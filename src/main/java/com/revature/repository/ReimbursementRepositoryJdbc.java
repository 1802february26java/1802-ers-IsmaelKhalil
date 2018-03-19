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
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.util.ConnectionUtil;

public class ReimbursementRepositoryJdbc implements ReimbursementRepository {

	private static Logger logger = Logger.getLogger(ReimbursementRepositoryJdbc.class);

	/*Singleton transformation of JDBC implementation object */
	private static ReimbursementRepositoryJdbc repository = new ReimbursementRepositoryJdbc();

	private ReimbursementRepositoryJdbc() {

	}

	public static ReimbursementRepositoryJdbc getInstance() {
		return repository;
	}


	@Override
	public boolean insert(Reimbursement reimbursement) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "INSERT INTO REIMBURSEMENT VALUES(NULL,?,?,?,?,NULL,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(command);

			//Set attributes to be inserted
			statement.setTimestamp(++statementIndex, Timestamp.valueOf(reimbursement.getRequested()));
			statement.setTimestamp(++statementIndex, Timestamp.valueOf(reimbursement.getResolved()));
			statement.setDouble(++statementIndex, reimbursement.getAmount());
			statement.setString(++statementIndex, reimbursement.getDescription());
			statement.setInt(++statementIndex, reimbursement.getRequester().getId());
			statement.setInt(++statementIndex, reimbursement.getApprover().getId());
			statement.setInt(++statementIndex, reimbursement.getStatus().getId());
			statement.setInt(++statementIndex, reimbursement.getType().getId());

			if(statement.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			logger.warn("Exception thrown while creating a new reimbursement request", e);
		}
		return false;
	}


	@Override
	public boolean update(Reimbursement reimbursement) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "UPDATE REIMBURSEMENT SET R_RESOLVED = ?, R_AMOUNT = ?, R_DESCRIPTION = ?, R_RECEIPT = NULL, RS_ID = ?, RT_ID = ? WHERE R_ID = ?";

			PreparedStatement statement = connection.prepareStatement(command);

			//Set attributes to be inserted
			statement.setTimestamp(++statementIndex, Timestamp.valueOf(reimbursement.getResolved()));
			statement.setDouble(++statementIndex, reimbursement.getAmount());
			statement.setString(++statementIndex, reimbursement.getDescription());
			statement.setInt(++statementIndex, reimbursement.getStatus().getId());
			statement.setInt(++statementIndex, reimbursement.getType().getId());
			statement.setInt(++statementIndex, reimbursement.getId());

			if(statement.executeUpdate() > 0) {
				logger.trace("Successfully updated reimbursement request");
				return true;
			}
		} catch (SQLException e) {
			logger.warn("Exception thrown while updating reimbursement request", e);
		}
		return false;
	}

	@Override
	public Reimbursement select(int reimbursementId) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "SELECT * FROM REIMBURSEMENT WHERE R_ID = ?";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setInt(++statementIndex, reimbursementId);
			ResultSet result = statement.executeQuery();

			if(result.next()) {
				Reimbursement reimbursement = new Reimbursement();

				result.getInt("R_ID");
				result.getTimestamp("R_REQUESTED").toLocalDateTime();
				result.getTimestamp("R_RESOLVED").toLocalDateTime();
				result.getDouble("R_AMOUNT");
				result.getDouble("R_DESCRIPTION");
				EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID"));
				EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID"));
				new ReimbursementStatus(
						result.getInt("RS_ID"),
						result.getString("RS_STATUS")
						);
				new ReimbursementType(
						result.getInt("RT_ID"),
						result.getString("RT_TYPE")
						);
				if(result.getString("R_RESOLVED") != null) {
					reimbursement.setResolved(result.getTimestamp("R_RESOLVED").toLocalDateTime());
				}
				return reimbursement;
			}
		} catch (SQLException e) {
			logger.warn("Employee retrieval failed due to exception being thrown.", e);
		}
		return new Reimbursement();
	}

	@Override
	public Set<Reimbursement> selectPending(int employeeId) {
		logger.trace("Selecting pending reimbursements.");
		Set<Reimbursement> reimbursements = new HashSet<>();
		int statementIndex = 0;
		try(Connection connection = ConnectionUtil.getConnection()) {
			final String command = "SELECT * FROM REIMBURSEMENT R INNER JOIN REIMBURSEMENT_STATUS RS ON R.RS_ID = RS.RS_ID" 
					+ "INNER JOIN REIMBURSEMENT_TYPE RT ON R.RT_ID = RT.RT_ID WHERE R.EMPLOYEE_ID = ? AND R.RS_ID = ?";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setInt(++statementIndex, employeeId);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				Reimbursement reimbursement = new Reimbursement();

				result.getInt("R_ID");
				result.getTimestamp("R_REQUESTED").toLocalDateTime();
				result.getDouble("R_AMOUNT");
				result.getDouble("R_DESCRIPTION");
				EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID"));
				EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID"));
				new ReimbursementStatus(
						result.getInt("RS_ID"),
						result.getString("RS_STATUS")
						);
				new ReimbursementType(
						result.getInt("RT_ID"),
						result.getString("RT_TYPE")
						);
				reimbursements.add(reimbursement);;
			}
			return reimbursements;
		} catch (SQLException e) {
			logger.warn("Exception selecting pending requests.", e);
		} 
		return null;
	}

	@Override
	public Set<Reimbursement> selectFinalized(int employeeId) {
		logger.trace("Selecting finalized reimbursements.");
		Set<Reimbursement> reimbursements = new HashSet<>();
		int statementIndex = 0;
		try(Connection connection = ConnectionUtil.getConnection()) {
			final String command = "SELECT * FROM REIMBURSEMENT R INNER JOIN REIMBURSEMENT_STATUS RS ON R.RS_ID = RS.RS_ID" 
					+ "INNER JOIN REIMBURSEMENT_TYPE RT ON R.RT_ID = RT.RT_ID WHERE R.EMPLOYEE_ID = ? AND R.RS_ID = ?";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setInt(++statementIndex, employeeId);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				Reimbursement reimbursement = new Reimbursement();

				result.getInt("R_ID");
				result.getTimestamp("R_RESOLVED").toLocalDateTime();
				result.getDouble("R_AMOUNT");
				result.getDouble("R_DESCRIPTION");
				EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID"));
				EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID"));
				new ReimbursementStatus(
						result.getInt("RS_ID"),
						result.getString("RS_STATUS")
						);
				new ReimbursementType(
						result.getInt("RT_ID"),
						result.getString("RT_TYPE")
						);
				reimbursements.add(reimbursement);;
			}
			return reimbursements;
		} catch (SQLException e) {
			logger.warn("Exception selecting resolved requests.", e);
		} 
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllPending() {
		logger.trace("Selecting all pending reimbursements.");
		Set<Reimbursement> reimbursements = new HashSet<>();
		int statementIndex = 0;
		try(Connection connection = ConnectionUtil.getConnection()) {
			final String command = "SELECT * FROM REIMBURSEMENT R INNER JOIN REIMBURSEMENT_STATUS RS ON R.RS_ID = RS.RS_ID" 
					+ "INNER JOIN REIMBURSEMENT_TYPE RT ON R.RT_ID = RT.RT_ID WHERE R.RS_ID = ?";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setInt(++statementIndex, 1);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				Reimbursement reimbursement = new Reimbursement();

				result.getInt("R_ID");
				result.getTimestamp("R_REQUESTED").toLocalDateTime();
				result.getDouble("R_AMOUNT");
				result.getDouble("R_DESCRIPTION");
				EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID"));
				EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID"));
				new ReimbursementStatus(
						result.getInt("RS_ID"),
						result.getString("RS_STATUS")
						);
				new ReimbursementType(
						result.getInt("RT_ID"),
						result.getString("RT_TYPE")
						);
				reimbursements.add(reimbursement);;
			}
			return reimbursements;
		} catch (SQLException e) {
			logger.warn("Exception selecting pending requests for all employees.", e);
		} 
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllFinalized() {
		logger.trace("Selecting all finalized reimbursements.");
		Set<Reimbursement> reimbursements = new HashSet<>();
		int statementIndex = 0;
		try(Connection connection = ConnectionUtil.getConnection()) {
			final String command = "SELECT * FROM REIMBURSEMENT R INNER JOIN REIMBURSEMENT_STATUS RS ON R.RS_ID = RS.RS_ID" 
					+ "INNER JOIN REIMBURSEMENT_TYPE RT ON R.RT_ID = RT.RT_ID WHERE R.RS_ID = ?";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setInt(++statementIndex, 1);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				Reimbursement reimbursement = new Reimbursement();

				result.getInt("R_ID");
				result.getTimestamp("R_RESOLVED").toLocalDateTime();
				result.getDouble("R_AMOUNT");
				result.getDouble("R_DESCRIPTION");
				EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID"));
				EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID"));
				new ReimbursementStatus(
						result.getInt("RS_ID"),
						result.getString("RS_STATUS")
						);
				new ReimbursementType(
						result.getInt("RT_ID"),
						result.getString("RT_TYPE")
						);
				reimbursements.add(reimbursement);;
			}
			return reimbursements;
		} catch (SQLException e) {
			logger.warn("Exception selecting resolved requests for all employees.", e);
		} 
		return null;
	}

	@Override
	public Set<ReimbursementType> selectTypes() {
		logger.trace("Selecting types.");
		Set<ReimbursementType> reimbursementTypes = new HashSet<>();
		int statementIndex = 0;
		try(Connection connection = ConnectionUtil.getConnection()) {
			final String command = "SELECT * FROM REIMBURSEMENT_TYPE";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setInt(++statementIndex, 1);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				reimbursementTypes.add(new ReimbursementType(
				
					result.getInt("RT_ID"),
					result.getString("RT_TYPE")
				));
			}
			return reimbursementTypes;
		} catch (SQLException e) {
			logger.warn("Exception selecting types.", e);
		} 
		return null;
	}

}
