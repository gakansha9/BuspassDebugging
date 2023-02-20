package com.amazon.buspassmanagement.controller;

import java.util.List;

import com.amazon.buspassmanagement.db.BusPassDAO;
import com.amazon.buspassmanagement.db.FeedbackDAO;
import com.amazon.buspassmanagement.db.StopDAO;
import com.amazon.buspassmanagement.db.UserDAO;
import com.amazon.buspassmanagement.db.VehicleDAO;
import com.amazon.buspassmanagement.model.BusPass;
import com.amazon.buspassmanagement.model.Feedback;
import com.amazon.buspassmanagement.model.Stop;
import com.amazon.buspassmanagement.model.Vehicle;

public class ValidityServices {

	private static ValidityServices service = new ValidityServices();
	UserDAO dao = new UserDAO();
	BusPassDAO bdao = new BusPassDAO();
	VehicleDAO vdao = new VehicleDAO();
	StopDAO sdao = new StopDAO();
	FeedbackDAO fdao = new FeedbackDAO();
	
	public static ValidityServices getInstance() {
		return service;
	}

	public boolean passValidity(BusPass pass) {

		String sql = "SELECT * FROM buspass WHERE id = " + pass.id + " AND validTill = '" + pass.validTill + "'";
		List<BusPass> passes = bdao.retrieve(sql);
		if (passes.size() > 0) {
			return true;
		}

		return false;
	}

	public boolean vehicalAtRoute(Vehicle vehicle) {

		String sql = "SELECT * FROM vehicles WHERE routeId = " + vehicle.routeId;
		List<Vehicle> vehicles = vdao.retrieve(sql);
		if (vehicles.size() > 0) {
			return true;
		}

		return false;
	}

	public boolean stopAtRoute(Stop stop) {

		String sql = "SELECT * FROM stops WHERE routeId = " + stop.routeId;
		List<Stop> stops = sdao.retrieve(sql);
		if (stops.size() > 0) {
			return true;
		}

		return false;
	}
	
	
	public boolean twoStopAtRoute(Stop stop) {

		String sql = "SELECT count(*)FROM stops WHERE routeId = " + stop.routeId ;
		List<Stop> stops = sdao.retrieve(sql);
		if (stops.size() >= 2) {
			return true;
		}

		return false;
	}
	
	
	public boolean complaintCheck(Feedback feedback) {

		String sql = "SELECT * FROM feedback WHERE userid = " + feedback.userId + " AND type = " +feedback.type + "";
		List<Feedback> feedbacks = fdao.retrieve(sql);
		if (feedbacks.size() > 0) {
			return true;
		}

		return false;
	}

}
