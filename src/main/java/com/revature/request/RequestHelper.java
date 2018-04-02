package com.revature.request;

import javax.servlet.http.HttpServletRequest;

import com.revature.controller.EmployeeInformationControllerAlpha;
import com.revature.controller.ErrorControllerAlpha;
import com.revature.controller.LoginControllerAlpha;
import com.revature.controller.ReimbursementControllerAlpha;
import com.revature.controller.HomeControllerAlpha;

/**
 * The RequestHelper class is consulted by the MasterServlet and provides
 * him with a view URL or actual data that needs to be transferred to the
 * client.
 * 
 * It will execute a controller method depending on the requested URI.
 * 
 * Recommended to change this logic to consume a ControllerFactory.
 * 
 * @author Revature LLC
 */
public class RequestHelper {
	private static RequestHelper requestHelper;

	private RequestHelper() {}

	public static RequestHelper getRequestHelper() {
		if(requestHelper == null) {
			return new RequestHelper();
		}
		else {
			return requestHelper;
		}
	}
	
	/**
	 * Checks the URI within the request object passed by the MasterServlet
	 * and executes the right controller with a switch statement.
	 * 
	 * @param request
	 * 		  The request object which contains the solicited URI.
	 * @return A String containing the URI where the user should be
	 * forwarded, or data (any object) for AJAX requests.
	 */
	public Object process(HttpServletRequest request) {
		switch(request.getRequestURI())
		{
		case "/ERS/login.do":
			return new LoginControllerAlpha().login(request);		
		case "/ERS/register.do":
			return EmployeeInformationControllerAlpha.getInstance().registerEmployee(request);	
		case "/ERS/lackey-home.do":
			return new HomeControllerAlpha().showEmployeeHome(request);	
		case "/ERS/leader-home.do":
			return new HomeControllerAlpha().showEmployeeHome(request);
		case "/ERS/submit.do":
			return ReimbursementControllerAlpha.getInstance().submitRequest(request);
	    case "/ERS/pending.do":
			return ReimbursementControllerAlpha.getInstance().multipleRequests(request);
	    case "/ERS/viewInfo.do":
		return EmployeeInformationControllerAlpha.getInstance().viewEmployeeInformation(request);
	    case "/ERS/updateInfo.do":
		return EmployeeInformationControllerAlpha.getInstance().updateEmployee(request);
		case "/ERS/viewEmployeeList.do":
			return EmployeeInformationControllerAlpha.getInstance().viewAllEmployees(request);
	    case "/ERS/listEmployees.do":
		return EmployeeInformationControllerAlpha.getInstance().viewAllEmployees(request);		
		case "/ERS/logout.do":
			return new LoginControllerAlpha().logout(request);	
			
			
			
			
		default:
			return new ErrorControllerAlpha().showError(request);
		}
	}
}
