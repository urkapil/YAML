package com.examples.rs.exception;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author Prashanth Errabelli
 *
 */
public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5967732900659212852L;

	private String errorTitleMessage;
	private String errorDetailMessage;
	private String errorPath;
	private String applicationErrorCode;
	private HttpStatus errorCode;

	public BusinessException(String errorTitle, String errorDetail, String errorPath, HttpStatus errorCode) {

		super(errorDetail);
		this.errorTitleMessage = errorTitle;
		this.errorDetailMessage = errorDetail;
		this.errorPath = errorPath;
		this.errorCode = errorCode;
	}

	public BusinessException(String errorTitle, String errorDetail, String errorPath, String applicationErrorCode,
			HttpStatus errorCode) {

		super(errorDetail);
		this.errorTitleMessage = errorTitle;
		this.errorDetailMessage = errorDetail;
		this.errorPath = errorPath;
		this.applicationErrorCode = applicationErrorCode;
		this.errorCode = errorCode;
	}

	public BusinessException(String errorTitle, String errorDetail, String errorPath, HttpStatus errorCode,
			Throwable cause) {

		super(errorDetail, cause);
		this.errorTitleMessage = errorTitle;
		this.errorDetailMessage = errorDetail;
		this.errorPath = errorPath;
		this.errorCode = errorCode;
	}

	public BusinessException(String errorPath, String applicationErrorCode) {
		super();
		this.errorPath = errorPath;
		this.applicationErrorCode = applicationErrorCode;
	}

	public BusinessException(String applicationErrorCode) {
		super();
		this.applicationErrorCode = applicationErrorCode;
	}

	public String getErrorTitleMessage() {
		return errorTitleMessage;
	}

	public String getErrorDetailMessage() {
		return errorDetailMessage;
	}

	public String getErrorPath() {
		return errorPath;
	}

	public String getApplicationErrorCode() {
		return applicationErrorCode;
	}

	public HttpStatus getErrorCode() {
		return errorCode;
	}

}
