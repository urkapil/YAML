package com.errabelli.lambda.spring.rs.currency.ws.client;

import javax.annotation.PostConstruct;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.errabelli.lambda.spring.rs.beans.ApiServiceFallBack;
import com.errabelli.lambda.spring.rs.beans.SoapServiceGetMessageResponse;

/**
 * 
 * @author Prashanth Errabelli
 *
 */

@Component
public class CalculatorServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(CalculatorServiceClient.class);

	private static final String CONTEXT_PATH = "org.tempuri";

	// @Value("${hystrix.command.NotificationServiceGetMessage.execution.isolation.thread.timeoutInMilliseconds}")
	private int httpConnectionReadTimeout;

	@Autowired
	public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

	@PostConstruct
	public void init() {

		// Single template should be fine, kept to add flexibility while adding
		// variable timeout
		// getMessageWebServiceTemplate =
		// this.buildWebServiceTemplate(poolingHttpClientConnectionManager,
		// defaultRequestConfig, saajSoapMessageFactory, CONTEXT_PATH,
		// httpConnectionReadTimeout);
		// deleteMessageWebServiceTemplate =
		// this.buildWebServiceTemplate(poolingHttpClientConnectionManager,
		// defaultRequestConfig, saajSoapMessageFactory, CONTEXT_PATH,
		// httpConnectionReadTimeout);
	}

	// @HystrixCommand(commandKey = "NotificationServiceGetMessage",
	// fallbackMethod = "getNotificationMessagesFallBack", threadPoolKey =
	// "NotificationServiceGetMessagePool")
	public SoapServiceGetMessageResponse add(int a, int b) {

		return null;
	}

	public SoapServiceGetMessageResponse getNotificationMessagesFallBack(int a, int b, Throwable t) {

		if (t != null) {
			logger.error("Hystrix : During Notification GetMessage soap service call ", t);
		}

		ApiServiceFallBack serviceFallBack = null;
		/*
		 * if (t instanceof SoapFaultClientException) { SoapFaultClientException
		 * soapfault = (SoapFaultClientException) t; String faultMsg =
		 * soapfault.getFaultCode() + ":" + soapfault.getFaultCode().toString();
		 * serviceFallBack = new ApiServiceFallBack(faultMsg); } else {
		 */
		serviceFallBack = new ApiServiceFallBack("Backend api service is either down or under maintanence. ");

		// }
		return new SoapServiceGetMessageResponse(-1, serviceFallBack);
	}
}
