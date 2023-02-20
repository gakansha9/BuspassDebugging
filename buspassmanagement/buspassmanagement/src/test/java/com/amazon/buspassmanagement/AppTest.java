package com.amazon.buspassmanagement;

import org.junit.Assert;
import org.junit.Test;
import com.amazon.buspassmanagement.controller.ValidityServices;
import com.amazon.buspassmanagement.db.DB;
import com.amazon.buspassmanagement.model.BusPass;
import com.amazon.buspassmanagement.model.Feedback;
import com.amazon.buspassmanagement.model.Stop;
import com.amazon.buspassmanagement.model.Vehicle;

// Reference Link to Use JUnit as Testing Tool in your Project
// https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit.html

public class AppTest {

	ValidityServices vService = ValidityServices.getInstance();
	DB db = DB.getInstance();

	@Test
	public void testValidBusPass() {
		BusPass passes = new BusPass();
		passes.id = 10;
		passes.validTill = "2023-02-17 15:19:00.843";
		boolean result = vService.passValidity(passes);
		Assert.assertEquals(true, result);
	}

	@Test
	public void testVehicleAtRoute() {
		Vehicle vehicle = new Vehicle();
		vehicle.routeId = 4;
		boolean result = vService.vehicalAtRoute(vehicle);
		Assert.assertEquals(true, result);
	}

	@Test
	public void testStopAtRoute() {
		Stop stop = new Stop();
		stop.routeId = 4;
		boolean result = vService.stopAtRoute(stop);
		Assert.assertEquals(true, result);
	}

	@Test
	public void testtwoStopAtRoute() {
		Stop stop = new Stop();
		stop.routeId = 4;
		boolean result = vService.stopAtRoute(stop);
		Assert.assertEquals(true, result);
	}

	@Test
	public void testUserComplaint() {
		Feedback feedback = new Feedback();
		feedback.userId = 13;
		feedback.type = 2;
		boolean result = vService.complaintCheck(feedback);
		Assert.assertEquals(true, result);
	}

}
