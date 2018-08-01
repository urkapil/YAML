package com.errabelli.lambda.spring.rs.business;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Prashanth Errabelli
 *
 */
@Service
public class CurrencyService extends BaseBusinessService {

	@Autowired
	CurrencyConversionProcessor processor;

	private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

	public BigDecimal convert(String from, String to, BigDecimal amount) {

		return processor.convert(amount, processor.getConversionRate(from, to));

	}

	public BigDecimal getConversionRate(String from, String to) {

		return processor.getConversionRate(from, to);

	}

}
