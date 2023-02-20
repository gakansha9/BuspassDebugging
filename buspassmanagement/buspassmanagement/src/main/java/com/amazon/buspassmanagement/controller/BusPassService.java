package com.amazon.buspassmanagement.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.amazon.buspassmanagement.BusPassSession;
import com.amazon.buspassmanagement.CustomDefinedException;
import com.amazon.buspassmanagement.db.BusPassDAO;
import com.amazon.buspassmanagement.model.BusPass;

public class BusPassService {

	BusPassDAO passDAO = new BusPassDAO();

	// Create it as a Singleton
	private static BusPassService passService = new BusPassService();
	Scanner scanner = new Scanner(System.in);

	public static BusPassService getInstance() {
		return passService;
	}

	private BusPassService() {

	}

	// Handler for the Bus Pass :)
	public void requestPass() throws CustomDefinedException {
		BusPass pass = new BusPass();
		pass.getDetails(false);

		// Add the User ID Implicitly.
		pass.uid = BusPassSession.user.id;

		// As initially record will be inserted by User where it is a request
		pass.status = 1; // initial status as requested :)

		String passCheck = "Select * from buspass where status in (1,2) and routeid="+pass.routeId+" and uid="+pass.uid;
		List<BusPass> passes = passDAO.retrieve(passCheck);

		System.out.println("Pass result: "+passes.isEmpty());
		if(passes.isEmpty()==false) {
			throw new CustomDefinedException("User already has a live / requested pass on route "+pass.routeId);
		}

		String suspendedPassCheck = "Select * from buspass where routeid="+pass.routeId+" and status=4 and uid="+pass.uid; 
		List<BusPass> suspendedPasses = passDAO.retrieve(suspendedPassCheck);
		if(suspendedPasses.isEmpty()==false) {
			throw new CustomDefinedException("User already has a Suspended pass on route "+pass.routeId);
		}

		
		int result = passDAO.insert(pass);
		String message = (result > 0) ? "Pass Requested Successfully" : "Request for Pass Failed. Try Again..";
		System.out.println(message);
	}

	public void deletePass() {
		BusPass pass = new BusPass();
		System.out.println("Enter Pass ID to be deleted: ");
		pass.id = scanner.nextInt();
		int result = passDAO.delete(pass);
		String message = (result > 0) ? "Pass Deleted Successfully" : "Deleting Pass Failed. Try Again..";
		System.out.println(message);
	}

	/*
	 * 
	 * Extra Task: IFF : You wish to UpSkill :)
	 * 
	 * Scenario: Open the same application in 2 different terminals 1 logged in by
	 * user another logged in by admin
	 * 
	 * If admin, approves or rejects the pass -> User should be notified :)
	 * 
	 * Reference Link
	 * https://github.com/ishantk/AmazonAtlas22/blob/master/Session8/src/com/amazon/
	 * atlas/casestudy/YoutubeApp.java
	 * 
	 */

	public void approveRejectPassRequest() {

		BusPass pass = new BusPass();

		System.out.println("Enter Pass ID: ");
		pass.id = scanner.nextInt();

		System.out.println("2: Approve");
		System.out.println("3: Cancel");
		System.out.println("Enter Approval Choice: ");
		pass.status = scanner.nextInt();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Calendar calendar = Calendar.getInstance();
		Date date1 = calendar.getTime();
		pass.approvedRejectedOn = dateFormat.format(date1);

		if (pass.status == 2) {
			calendar.add(Calendar.YEAR, 1);
			Date date2 = calendar.getTime();
			pass.validTill = dateFormat.format(date2);
		} else {
			pass.validTill = pass.approvedRejectedOn;
		}

		int result = passDAO.update(pass);
		String message = (result > 0) ? "Pass Request Updated Successfully"
				: "Updating Pass Request Failed. Try Again..";
		System.out.println(message);
	}

	public void viewPassRequests() {

		System.out.println("Enter Route ID to get All the Pass Reqeuests for a Route");
		System.out.println("Or 0 for All Bus Pass Requests");
		System.out.println("Enter Route ID: ");

		int routeId = scanner.nextInt();

		List<BusPass> objects = null;

		if (routeId == 0) {
			objects = passDAO.retrieve();
		} else {
			String sql = "SELECT * from BusPass where routeId = " + routeId;
			objects = passDAO.retrieve(sql);
		}

		for (BusPass object : objects) {
			object.prettyPrint();
		}
	}

	public void viewPassRequestsByUser(int uid) {

		String sql = "SELECT * from BusPass where uid = " + uid;
		List<BusPass> objects = passDAO.retrieve(sql);

		for (BusPass object : objects) {
			object.prettyPrint();
		}
	}

	public void viewExpiredPass() {

		String sql = "select * from Buspass where validTill < CURRENT_TIMESTAMP;";
		List<BusPass> objects = passDAO.retrieve(sql);

		for (BusPass object : objects) {
			object.prettyPrint();
		}
	}
	
	
	public void viewBetweenDatePass() {
		System.out.println("Enter from date (In yyyy/mm/dd Format): ");
		String fromdate = scanner.nextLine();
		
		System.out.println("Enter to date (In yyyy/mm/dd Format): ");
		String todate = scanner.nextLine();

		String sql = "select * from Buspass where cast(requestedOn as date) between '"+fromdate+"' and '"+todate+"';";
		List<BusPass> objects = passDAO.retrieve(sql);

		for (BusPass object : objects) {
			object.prettyPrint();
		}
	}

	public void suspendPass() {
		// TODO Auto-generated method stub
		System.out.println();
		System.out.println("Enter the Bus Pass ID...");
		int passId = scanner.nextInt();

		System.out.println("Enter the duration for suspension [IN MONTHS]");
		int suspensionDuration = scanner.nextInt();

		// Step 1: get existing details

		String sql = "SELECT * from BusPass WHERE id = " + passId;

		List<BusPass> bPass = passDAO.retrieve(sql);

		// Step 2: modify details

		BusPass pass = new BusPass();
		pass = bPass.get(0);
		pass.prettyPrint();

		pass.status = 4;
		try {
			Date currDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(pass.validTill);

			// LocalDate currDate = LocalDate.parse(pass.validTill);
			// System.out.println(currDate.toString());

			Calendar cal = Calendar.getInstance();
			cal.setTime(currDate);

			cal.add(Calendar.MONTH, suspensionDuration);

			Date newDate = cal.getTime();

			// System.out.println(newDate);

			java.sql.Timestamp ts = new java.sql.Timestamp(newDate.getTime());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    System.out.println(formatter.format(ts).toString());
//	System.out.println(ts.toString());
			pass.validTill = formatter.format(ts).toString();

			cal.setTime(new Date());
			cal.add(Calendar.MONTH, suspensionDuration);

			Date newApprovalDate = cal.getTime();
			java.sql.Timestamp ts1 = new java.sql.Timestamp(newApprovalDate.getTime());
			pass.approvedRejectedOn = formatter.format(ts1).toString();

			// Step 3: Change values in DB

			pass.prettyPrint();
			int result = passDAO.update(pass);
			String message = (result > 0) ? "Pass Request Updated Successfully"
					: "Updating Pass Request Failed. Try Again..";

			System.out.println(message);
		} catch (Exception e) {
			System.err.println("Something Went Wrong: " + e);
		}

	}
}
