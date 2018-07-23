package com.examples.rs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.serverless.proxy.internal.testutils.Timer;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class StreamLambdaHandler implements RequestStreamHandler {
	private static SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
	static {
		String environmentName = System.getenv("DAI_ENV");

		try {

			System.out.println("environment = " + environmentName);
			handler = SpringLambdaContainerHandler.getAwsProxyHandler(ExampleApplication2.class);

		} catch (Exception e) {
			// if we fail here. We re-throw the exception to force another cold
			// start
			e.printStackTrace();
			throw new RuntimeException("Could not initialize Spring Boot application, env = " + environmentName, e);
		}
	}

	public StreamLambdaHandler() {
		// we enable the timer for debugging. This SHOULD NOT be enabled in
		// production.
		Timer.enable();
	}

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		handler.proxyStream(inputStream, outputStream, context);

		// just in case it wasn't closed by the mapper
		outputStream.close();
	}
}
