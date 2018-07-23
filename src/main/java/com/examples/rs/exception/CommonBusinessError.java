package com.examples.rs.exception;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author Prashanth Errabelli
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonBusinessError {

	private List<ApplicationError> errors;

	public List<ApplicationError> getErrors() {
		return errors;
	}

	public void setErrors(List<ApplicationError> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
