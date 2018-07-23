package com.examples.rs.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 
 * @author Prashanth Errabelli
 *
 */
@ControllerAdvice
public class CalculatorExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<Object> handleBusinessException(RuntimeException exception, WebRequest request) {
		BusinessException businessException = (BusinessException) exception;

		CommonBusinessError commonBusinessError = new CommonBusinessError();
		List<ApplicationError> applicationErrors = new ArrayList<ApplicationError>();
		ApplicationError applicationError = new ApplicationError();
		applicationError.setId(UUID.randomUUID().toString());
		applicationError.setTitle(businessException.getErrorTitleMessage());
		applicationError.setDetail(businessException.getErrorDetailMessage());
		applicationError.setCode(businessException.getApplicationErrorCode());
		applicationError.setStatus(businessException.getErrorCode().toString());
		applicationErrors.add(applicationError);
		commonBusinessError.setErrors(applicationErrors);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return handleExceptionInternal(exception, commonBusinessError, headers, businessException.getErrorCode(),
				request);
	}

	@ExceptionHandler(NullPointerException.class)
	protected ResponseEntity<Object> handleNullPointerException(RuntimeException exception, WebRequest request) {
		NullPointerException nullPointerException = (NullPointerException) exception;

		CommonBusinessError commonBusinessError = new CommonBusinessError();
		List<ApplicationError> applicationErrors = new ArrayList<ApplicationError>();
		ApplicationError applicationError = new ApplicationError();
		applicationError.setTitle(nullPointerException.getMessage());
		applicationError.setDetail(nullPointerException.getMessage());

		applicationError.setStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));

		applicationErrors.add(applicationError);
		commonBusinessError.setErrors(applicationErrors);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return handleExceptionInternal(exception, commonBusinessError, headers, HttpStatus.INTERNAL_SERVER_ERROR,
				request);
	}
}
