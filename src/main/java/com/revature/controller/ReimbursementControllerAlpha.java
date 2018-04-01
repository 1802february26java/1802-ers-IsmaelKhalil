package com.revature.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.service.ReimbursementServiceAlpha;

public class ReimbursementControllerAlpha implements ReimbursementController {

	private static final Logger logger = Logger.getLogger(ReimbursementControllerAlpha.class);
	private static ReimbursementControllerAlpha reimbursementController = new ReimbursementControllerAlpha();

	private ReimbursementControllerAlpha()
	{

	}

	public static ReimbursementControllerAlpha getInstance()
	{
		return reimbursementController;
	}

	@Override
	public Object submitRequest(HttpServletRequest request) {
		logger.trace("Submitting a request.");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");

		if(loggedEmployee == null) {
			return "login.html";
		} else if (loggedEmployee.getEmployeeRole().getId() == 2) {
			return "leader-home.html";
		} else if (request.getMethod().equals("GET")){
			return "submit.html";
		} 

		logger.trace(loggedEmployee.toString());
		Employee manager = new Employee ();

		Reimbursement reimbursement = new Reimbursement(
				0,
				LocalDateTime.now(),
				null,
				Double.parseDouble(request.getParameter("amount")),
				request.getParameter("description"),
				loggedEmployee,
				null,
				new ReimbursementStatus(1, "PENDING"),
				new ReimbursementType( Integer.parseInt(request.getParameter("reimbursementTypeId")),
						request.getParameter("reimbursementTypeName"))
				);

		if(ReimbursementServiceAlpha.getInstance().submitRequest(reimbursement)) {
			return new ClientMessage("SUBMITTED REQUEST");
		} else {
			return new ClientMessage("REQUEST FAILED TO SUBMIT. TRY AGAIN.");
		}			
	}

	@Override
	public Object singleRequest(HttpServletRequest request) {
		logger.trace("ReimbursementControllerAlpha.submitRequest");

		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee);");

		if(loggedEmployee == null) {
			return "login.html";
		}

		Reimbursement reimbursement = new Reimbursement(
				Integer.parseInt(request.getParameter("reimbursementId")),
				LocalDateTime.now(),
				null,
				15.00,
				null,
				null,
				null,
				new ReimbursementStatus(),
				new ReimbursementType());

		Reimbursement r = ReimbursementServiceAlpha.getInstance().getSingleRequest(reimbursement);
		if(r != reimbursement) {
			return new ClientMessage("REIMBURSEMENT SUCCESSFUL");
		}

		return new ClientMessage("REIMBURSEMENT FAILED");
	}

	@Override
	public Object multipleRequests(HttpServletRequest request) {
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee);");

		if(loggedEmployee == null) {
			return "login.html";
		}

		if ( request.getParameter("fetch") == null && loggedEmployee.getEmployeeRole().getId() == 1) {
			return "reimbursement.html";
		} else if ( request.getParameter("fetch") == null && loggedEmployee.getEmployeeRole().getId() == 1) {
			return "managerReimbursement.html";
		} else if ( request.getParameter("fetch").equals("pending") && loggedEmployee.getEmployeeRole().getId() == 1) {
			return ReimbursementServiceAlpha.getInstance().getUserPendingRequests(loggedEmployee);
		} else if ( request.getParameter("fetch").equals("resolved") && loggedEmployee.getEmployeeRole().getId() == 1) {
			return ReimbursementServiceAlpha.getInstance().getUserFinalizedRequests(loggedEmployee);
		} else if ( request.getParameter("fetch").equals("pending") && loggedEmployee.getEmployeeRole().getId() == 2) {
			return ReimbursementServiceAlpha.getInstance().getAllPendingRequests();
		} else if ( request.getParameter("fetch").equals("resolved") && loggedEmployee.getEmployeeRole().getId() == 2) {
			return ReimbursementServiceAlpha.getInstance().getAllResolvedRequests();
		}
		return null;
	}

	@Override
	public Object finalizeRequest(HttpServletRequest request) {
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee);");

		if(loggedEmployee == null) {
			return "login.html";
		}

		if(loggedEmployee.getEmployeeRole().getId() == 2){			
			logger.trace("Finalizing a reimbursement can only be done by a Manager.");
			return "403.html";
		}

		if (request.getMethod() == "GET") {
			return "submit.html";
		}

		Reimbursement reimbursement = new Reimbursement();

		if (loggedEmployee.getEmployeeRole().getId() == 2)
		{
			if (ReimbursementServiceAlpha.getInstance().finalizeRequest(reimbursement) && reimbursement.getStatus().getStatus().equals(3)) {
				return new ClientMessage("Reimbursement has been approved");
			}
			else if (ReimbursementServiceAlpha.getInstance().finalizeRequest(reimbursement) && reimbursement.getStatus().getStatus().equals(2)) {
				return new ClientMessage("Reimbursement has been declined");
			}
		}

		return new ClientMessage("Employee is not a manager");
	}

	
	@Override
	public Object getRequestTypes(HttpServletRequest request) {
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee);");

		if(loggedEmployee == null) {
			return "login.html";
		}
		
		return ReimbursementServiceAlpha.getInstance().getReimbursementTypes();
	}
	
	}
