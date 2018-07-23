package com.examples.rs.business;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.examples.rs.currency.ws.client.CurrencyConversionRestClient;

/**
 * 
 * @author Prashanth Errabelli
 *
 */

@Component
public class CurrencyConversionProcessor {

	@Autowired
	CurrencyConversionRestClient client;

	public BigDecimal convert(BigDecimal amount, BigDecimal rate) {

		return new BigDecimal(amount.floatValue() * rate.floatValue());
	}

	public BigDecimal getConversionRate(String from, String to) {

		BigDecimal decimal = client.getCurrencyConversionRate(from, to);

		return new BigDecimal(decimal.floatValue());
	}

}
