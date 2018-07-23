package com.examples.rs.beans;

/**
 * 
 * @author Prashanth Errabelli
 *
 */
public class ApiServiceFallBack {
	String message;

	public ApiServiceFallBack() {
		super();
	}

	public ApiServiceFallBack(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ServiceFallBack [message=" + message + "]";
	}

}
