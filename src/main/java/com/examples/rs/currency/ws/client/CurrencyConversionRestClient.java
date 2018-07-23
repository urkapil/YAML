package com.examples.rs.currency.ws.client;

import java.math.BigDecimal;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author UGSC321
 *
 */
@Component
public final class CurrencyConversionRestClient {

	private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionRestClient.class);

	@Autowired
	@Qualifier("accountsRestTemplate")
	RestTemplate restTemplate;

	private static final String PORTALCOMPANYID = "portalCompanyId";
	private static final String ACCOUNTTYPE = "accountType";
	private static final String AUTHORIZATION = "Authorization";
	private static final String CLIENT_ID = "Client_Id";

	protected String getServiceEndpoint() {
		return "https://free.currencyconverterapi.com/api/v5/convert?q=";
	}

	// @HystrixCommand(commandKey = "RsCurrencyRate", fallbackMethod =
	// "RsCurrencyRateFallBack", threadPoolKey = "RsCurrencyRate")
	public BigDecimal getCurrencyConversionRate(String from, String to) {

		logger.info("In CurrencyConversionRestClient#getCurrencyConversionRate : Start");

		String uri = getServiceEndpoint() + from + "_" + to + "&compact=ultra";

		logger.info("In CurrencyConversionRestClient#getCurrencyConversionRate : URI {}", uri);
		String result = restTemplate.getForObject(uri, String.class);

		logger.info("Response from conversion Service : {} ", result);

		Double conversionRate = -1d;

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			HashMap myMap = objectMapper.readValue(result, HashMap.class);
			System.out.println("Map is: " + myMap);
			conversionRate = (Double) myMap.get(from + "_" + to);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Response from conversion Service : {} ", result);
		logger.info("In CurrencyConversionRestClient#getCurrencyConversionRate : Ends");

		return new BigDecimal(conversionRate);
	}

	/*
	 * @EnableExecutionTimeLogging public AccountsClientResponse
	 * getAccountsFallback(AccountsClientRequest accountClientRequest, Throwable
	 * t) { AccountsClientResponse clientResponse = new
	 * AccountsClientResponse(); ClientServiceFallBack serviceFallBack = new
	 * ClientServiceFallBack(""); // ApiConstant.FALL_BACK_MESSAGE if (t !=
	 * null) { logger.error("HystrixFallBack : During Accounts Service Call",
	 * t); serviceFallBack.setFailureCode(HttpStatus.INTERNAL_SERVER_ERROR); }
	 * else { serviceFallBack.setFailureCode(HttpStatus.GATEWAY_TIMEOUT); }
	 * clientResponse.setServiceFallBack(serviceFallBack);
	 * 
	 * return clientResponse; }
	 */
}
