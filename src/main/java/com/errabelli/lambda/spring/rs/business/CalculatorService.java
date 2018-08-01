package com.errabelli.lambda.spring.rs.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Prashanth Errabelli
 *
 */
@Service
public class CalculatorService extends BaseBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

	public int add(int a, int b) {

		return a + b;
	}

	public int subtract(int a, int b) {

		return a - b;
	}

	public int multiply(int a, int b) {

		return a * b;
	}

	public int divide(int a, int b) {

		return a / b;
	}
}
