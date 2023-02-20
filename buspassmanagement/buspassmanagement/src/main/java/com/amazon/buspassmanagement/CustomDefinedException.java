package com.amazon.buspassmanagement;

public class CustomDefinedException extends Exception {

	String message;

	public CustomDefinedException(String str) {
		super(str);
		this.message = str;
	}

	public String toString() {
		return ("Custom Exception Occured: " + message);
	}

}
