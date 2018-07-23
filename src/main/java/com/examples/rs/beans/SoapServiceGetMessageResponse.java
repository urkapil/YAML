package com.examples.rs.beans;

/**
 * 
 * @author Prashanth Errabelli
 *
 */
public class SoapServiceGetMessageResponse {

	private ApiServiceFallBack serviceFallBack;

	int result;

	public SoapServiceGetMessageResponse(int result, ApiServiceFallBack serviceFallBack) {
		super();
		this.serviceFallBack = serviceFallBack;
		this.result = result;
	}

	public int getResult() {
		return this.result;
	}

	public ApiServiceFallBack getServiceFallBack() {
		return serviceFallBack;
	}
}