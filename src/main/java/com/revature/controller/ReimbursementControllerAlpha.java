package com.revature.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.service.ReimbursementServiceAlpha;

public class ReimbursementControllerAlpha implements ReimbursementController {

	private static Logger logger = Logger.getLogger(ReimbursementControllerAlpha.class);

	private static ReimbursementController reimbursementController = new ReimbursementControllerAlpha();
	
	private ReimbursementControllerAlpha() {}
	
	public static ReimbursementController getInstance() {
		
		return reimbursementController;
		
	}
	
	@Override
	public Object submitRequest(HttpServletRequest request) throws IOException, ServletException {
		
        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		if(loggedEmployee == null ) {
			return "login.html";
			
		}
		if(loggedEmployee.getEmployeeRole().getId() == 2) {
			return "403.html";
			
		}
		
		if (request.getMethod().equals("GET")) {
			return "lackey-reimbursement.html";
			
		}


		ReimbursementStatus status = new ReimbursementStatus(1,"PENDING");
		ReimbursementType type = new ReimbursementType(Integer.parseInt(
				request.getParameter("reimbursementTypeId")),request.getParameter("reimbursementTypeName"));
		Employee manager = new Employee(41,"BOW","SER","bowser","1234","bowser",new EmployeeRole(2,"MANAGER"));
		
		Reimbursement reimbursement = new Reimbursement(99,LocalDateTime.now(),null,
				Double.parseDouble(request.getParameter("amount")),request.getParameter("description"),
				loggedEmployee,manager,status,type);
       logger.trace("broke here "+reimbursement);
                
		if (ReimbursementServiceAlpha.getInstance().submitRequest(reimbursement)) {			
			return new ClientMessage("A REIMBURSEMENT HAS BEEN CREATED SUCCESSFULLY");
		} else {
			return new ClientMessage("SOMETHING WENT WRONG");
		}
		
			
	}

	@Override
	public Object singleRequest(HttpServletRequest request) throws IOException {
		
        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");

		if(loggedEmployee == null ) {
			return "login.html";
			
		}
		
		Reimbursement reimbursement = new Reimbursement(Integer.parseInt(request.getParameter("reimbursementId")));
		return ReimbursementServiceAlpha.getInstance().getSingleRequest(reimbursement);
		
		
	}

	@Override
	public Object multipleRequests(HttpServletRequest request) {
		
        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		if(loggedEmployee == null ) {
		   
			return "login.html";
			
		}
		
		if(loggedEmployee.getEmployeeRole().getId() == 1) {
		    if(request.getParameter("fetch") == null) {
			     return "lackey-pending.html";
		    }
		    else if(request.getParameter("fetch").equals("resolved")){
		         return "lackey-resolved.html";
		    }
		    
		    else if(request.getParameter("fetch").equals("finalized")){
				return ReimbursementServiceAlpha.getInstance().getUserFinalizedRequests(loggedEmployee);
			}
			else if (request.getParameter("fetch").equals("pending")){
				logger.trace(loggedEmployee);
			    return ReimbursementServiceAlpha.getInstance().getUserPendingRequests(loggedEmployee);  
			}
		 
			else{
				Set<Reimbursement> set = new HashSet<Reimbursement>(ReimbursementServiceAlpha.getInstance().getUserPendingRequests(loggedEmployee));
				set.addAll(ReimbursementServiceAlpha.getInstance().getUserFinalizedRequests(loggedEmployee));				
				return set;
			}
		}
		
		else{
			if(request.getParameter("fetch") == null) {
			     return "leader-pending.html";
		    }
			
		    else if(request.getParameter("fetch").equals("resolved")){
		         return "leader-resolved.html";
		    }
		    
			else if(request.getParameter("fetch").equals("finalized")){
				return ReimbursementServiceAlpha.getInstance().getAllResolvedRequests();
			}
			else if (request.getParameter("fetch").equals("pending")){
				logger.trace("controller: "+ReimbursementServiceAlpha.getInstance().getAllPendingRequests());
			return ReimbursementServiceAlpha.getInstance().getAllPendingRequests();
			}
		    
		    
		 	else if (request.getParameter("fetch").equals("viewSelected")) {
		 	return "leader-selected.html";
		 	}
		    
		 	else if (request.getParameter("fetch").equals("viewSelectedList")){    
			 	Employee selectedEmployee = new Employee(Integer.parseInt(request.getParameter("selectedEmployeeId")));	
				Set<Reimbursement> set = new HashSet<Reimbursement>(ReimbursementServiceAlpha.getInstance().getUserPendingRequests(selectedEmployee));
				set.addAll(ReimbursementServiceAlpha.getInstance().getUserFinalizedRequests(selectedEmployee));				
		 		return set;
			 	}
		 			
			else{
				return "leader-pending.html";
		
			}
			
			
		}
		
		
	}

	@Override
	public Object finalizeRequest(HttpServletRequest request) throws IOException {
		
        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		if(loggedEmployee == null ) {
	
			return "login.html";
			
		}
		if(loggedEmployee.getEmployeeRole().getId()==1){
			
			return "403.html";
			
		}
		
		Reimbursement reimbursement = new Reimbursement(Integer.parseInt(request.getParameter("reimbursementId")));
		ReimbursementStatus status = new ReimbursementStatus(Integer.parseInt(request.getParameter("statusId")),request.getParameter("status"));
		Reimbursement reimbursementToUpdate = ReimbursementServiceAlpha.getInstance().getSingleRequest(reimbursement);
		reimbursementToUpdate.setStatus(status);
		reimbursementToUpdate.setResolved(LocalDateTime.now());
		
		
		if (ReimbursementServiceAlpha.getInstance().finalizeRequest(reimbursementToUpdate)) {	
			
			return new ClientMessage("A REIMBURSEMENT HAS BEEN UPDATED SUCCESSFULLY");
		} else {
			return new ClientMessage("SOMETHING WENT WRONG");
		}
		
	}

	@Override
	public Object getRequestTypes(HttpServletRequest request) {
		
		
        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		if(loggedEmployee == null ) {
			return "login.html";
			
		}
		return ReimbursementServiceAlpha.getInstance().getReimbursementTypes();
		 
	}
}