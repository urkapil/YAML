package com.errabelli.lambda.spring.rs.currency.web.controller;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.errabelli.lambda.spring.rs.beans.Item;
import com.errabelli.lambda.spring.rs.beans.WarmResponse;
import com.errabelli.lambda.spring.rs.business.CalculatorService;

/**
 * 
 * @author Prashanth Errabelli
 *
 */
@RestController
public class CalculatorController {

	private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

	private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
	private static DynamoDBMapper mapper = new DynamoDBMapper(client);

	private static Date lastAccess = null;
	private static String containerId = null;

	@Autowired
	CalculatorService service;

	@RequestMapping(value = "/calculator/add/{a}/{b}", method = {
			RequestMethod.GET }, produces = "application/hal+json")
	public ResponseEntity<Integer> add(@PathVariable int a, @PathVariable int b, HttpServletRequest request) {

		logger.debug("starts");

		logger.info("Example CalculatorProcessor (/add) ->  a :{}, b:{}", a, b);

		logger.debug("ends");

		return new ResponseEntity<Integer>(service.add(a, b), HttpStatus.OK);
	}

	@RequestMapping(value = "/calculator/warm/{id}", method = { RequestMethod.GET }, produces = "application/hal+json")

	public ResponseEntity<WarmResponse> warm(@PathVariable int id, HttpServletRequest request) {

		logger.debug("starts");

		if (lastAccess == null) {
			if (containerId == null) {
				containerId = id + '_' + String.valueOf(new Date().getTime());
			}
			logger.info("Example CalculatorProcessor (/warm),containerId {}, id {}", containerId, id);

			lastAccess = new Date();
			logger.info("I am up and running id {}", id);
			return new ResponseEntity<WarmResponse>(new WarmResponse(containerId, id, true, lastAccess), HttpStatus.OK);
		} else {
			logger.info("Example CalculatorProcessor (/warm),containerId {}, id {}", containerId, id);

			lastAccess = new Date();
			logger.info("I am up and running id {}", id);
			return new ResponseEntity<WarmResponse>(new WarmResponse(containerId, id, false, lastAccess),
					HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/calculator/warmSleep/{id}/{ims}/{sms}", method = {
			RequestMethod.GET }, produces = "application/hal+json")

	public ResponseEntity<WarmResponse> warmSleep(@PathVariable int ims, @PathVariable int sms, @PathVariable int id,
			HttpServletRequest request) {

		logger.debug("starts");

		if (lastAccess == null) {

			if (containerId == null) {
				containerId = id + '_' + String.valueOf(new Date().getTime());
			}
			logger.info("Example CalculatorProcessor (/warmSleep),containerId {}, invocationId {} sleeping for {} ms ",
					containerId, id, ims);
			try {
				Thread.sleep(ims);
			} catch (Exception e) {
				logger.error("interupted ", e);
			}

			logger.info("I am up and running id {}", id);

			lastAccess = new Date();
			return new ResponseEntity<WarmResponse>(new WarmResponse(containerId, id, true, lastAccess), HttpStatus.OK);
		} else {
			lastAccess = new Date();
			try {
				Thread.sleep(sms);
			} catch (Exception e) {
				logger.error("interupted ", e);
			}

			logger.info("I am up and running id {}", id);

			return new ResponseEntity<WarmResponse>(new WarmResponse(containerId, id, false, lastAccess),
					HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/calculator/warmdb", method = { RequestMethod.GET }, produces = "application/hal+json")

	public ResponseEntity<String> warmer(HttpServletRequest request) {

		logger.debug("starts");

		logger.info("Example CalculatorProcessor (/warmdb), ");

		Item item = getItem(client, mapper);
		if (item != null) {

			boolean shouldSpin = (item.getActiveInstances() < item.getTargetInstances());
			logger.debug("{} == {} ==> {}", item.getActiveInstances(), item.getTargetInstances(),
					(item.getActiveInstances() == item.getTargetInstances()));
			if (shouldSpin) {
				logger.info("invoking increment active instances for {} ", item);

				item = incrementItem(client, item, mapper);
				shouldSpin = (item.getActiveInstances() < item.getTargetInstances());
			}
			while (shouldSpin) {
				logger.info("waiting for other lambdas to warm12 up {} ", item);

				try {
					Thread.sleep(500);
				} catch (Exception e) {
					logger.error("interupted ", e);
				}

				item = getItem(client, mapper);
				shouldSpin = (item.getActiveInstances() < item.getTargetInstances());
				// logic to exit... wait for finite amount of time
			}
			logger.info("all instances of lambda warmed up {} ", item);

		}

		return new ResponseEntity<String>("warmed", HttpStatus.OK);
	}

	private Item getItem(AmazonDynamoDB client, DynamoDBMapper mapper) {

		logger.info("Attempting to read the item...");

		// Load a catalog item.
		Item item = mapper.load(Item.class, "Example_Calc");
		logger.info("GetItem succeeded: " + item);

		return item;

	}

	private Item incrementItem(AmazonDynamoDB client, Item item, DynamoDBMapper mapper) {
		boolean updated = false;

		while (!updated) {
			logger.info("attempting to increment active inctances for {} ", item);

			try {
				item.setActiveInstances(item.getActiveInstances() + 1);
				saveItem(client, item, mapper);
				updated = true;
			} catch (Exception e) {
				item = getItem(client, mapper);
				logger.error("error updating active instance", e);
			}
		}

		logger.info("increment active instance succeeded: " + item);
		return item;
	}

	private void saveItem(AmazonDynamoDB client, Item item, DynamoDBMapper mapper) {
		logger.info("Updating the item...");

		// Save the item.
		mapper.save(item, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.UPDATE));
		logger.info("updateItem succeeded: " + item);
	}

	public ResponseEntity<String> warm(HttpServletRequest request) {

		logger.debug("starts");

		logger.info("Example CalculatorProcessor (/warm), ");

		// AmazonDynamoDBClientBuilder.standard()
		// .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://s3-us-west-2.amazonaws.com/dynamodb-local", "us-west-1"))
		// .build();
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

		/*
		 * logger.info("Attempting to start the scan..."); Table table =
		 * 
		 * ItemCollection<ScanOutcome> items = table.scan(); logger.info(
		 * "complete scan the table...");
		 * 
		 * Iterator<Item> iterator = items.iterator(); logger.info(
		 * "complete getting iterator of the result...");
		 * 
		 * Item item = null; while (iterator.hasNext()) { item =
		 * iterator.next(); logger.debug(item.toJSONPretty()); }
		 */

		// QuerySpec spec = new
		// QuerySpec().withKeyConditionExpression("FUNCTION_NAME IS NOT NULL");

		// String name = "EXAMPLE_CALCULATOR";

		// GetItemSpec spec = new GetItemSpec().withPrimaryKey("FUNCTION_NAME",
		// name);
		/*
		 * QuerySpec spec = new QuerySpec().withKeyConditionExpression(
		 * "Id = :v_id") .withValueMap(new ValueMap().withString(":v_id",
		 * replyId)); ItemCollection<QueryOutcome> items = table.query(spec);
		 * 
		 * System.out.println("\nfindRepliesForAThread results:");
		 * 
		 * Iterator<Item> iterator = items.iterator(); while
		 * (iterator.hasNext()) {
		 * System.out.println(iterator.next().toJSONPretty()); }
		 */

		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("Function_Name", new AttributeValue().withS("Example_Calc"));
		GetItemRequest getItemRequest = new GetItemRequest().withTableName("Lambda_Warmer").withKey(key);

		try {
			java.util.Map<String, AttributeValue> item = getItem(client, getItemRequest);
			logger.info("GetItem succeeded: " + item);

			int activeInstances = -1;
			int targetInstances = -1;

			try {
				activeInstances = Integer.parseInt((item.get("Active_Instances")).getN());

			} catch (NumberFormatException nfe) {

			}
			try {
				targetInstances = Integer.parseInt((item.get("Target_Instances")).getN());

			} catch (NumberFormatException nfe) {

			}

			logger.info("GetItem values, active:{}, target:{} ", activeInstances, targetInstances);

		} catch (Exception e) {
			logger.error("Unable to read item: EXAMPLE_CALC", e);
		}

		UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Function_Name", "Example_Calc")
				.withUpdateExpression("set Active_Instances = :r").withValueMap(new ValueMap().withInt(":r", 10))
				.withReturnValues(ReturnValue.UPDATED_NEW);

		try {
			logger.info("Attempting to get the table...");
			DynamoDB dynamoDB = new DynamoDB(client);
			Table table = dynamoDB.getTable("Lambda_Warmer");
			logger.info("completed getting the table...");

			logger.info("Updating the item...");
			UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
			logger.info("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());

		} catch (Exception e) {
			System.err.println("Unable to update item: EXAMPLE_CALC");
			System.err.println(e.getMessage());
		}

		logger.debug("ends");

		return new ResponseEntity<String>("warmed", HttpStatus.OK);
	}

	private java.util.Map<String, AttributeValue> getItem(AmazonDynamoDB client, GetItemRequest getItemRequest) {

		try {
			logger.info("Attempting to read the item...");

			// Item outcome = table.getItem("FUNCTION_NAMW", "EXAMPLE_CALC");
			GetItemResult result = client.getItem(getItemRequest);
			logger.info("GetItem succeeded: " + result.getItem());

			return result.getItem();

		} catch (Exception e) {
			logger.error("Unable to read item: EXAMPLE_CALC", e);
		}
		return null;
	}

	@RequestMapping(value = "/calculator/multiply/{a}/{b}", method = {
			RequestMethod.GET }, produces = "application/hal+json")
	public ResponseEntity<Integer> multiply(@PathVariable int a, @PathVariable int b, HttpServletRequest request) {

		logger.debug("starts");

		logger.info("Example CalculatorProcessor (/subtract) ->  a :{}, b:{}", a, b);

		logger.debug("ends");

		return new ResponseEntity<Integer>(service.multiply(a, b), HttpStatus.OK);
	}

	@RequestMapping(value = "/calculator/subtract/{a}/{b}", method = {
			RequestMethod.GET }, produces = "application/hal+json")
	public ResponseEntity<Integer> subtract(@PathVariable int a, @PathVariable int b, HttpServletRequest request) {

		logger.debug("starts");

		logger.info("Example CalculatorProcessor (/subtract) ->  a :{}, b:{}", a, b);

		logger.debug("ends");

		return new ResponseEntity<Integer>(service.subtract(a, b), HttpStatus.OK);
	}

	@RequestMapping(value = "/calculator/divide/{a}/{b}", method = {
			RequestMethod.GET }, produces = "application/hal+json")
	public ResponseEntity<Integer> divide(@PathVariable int a, @PathVariable int b, HttpServletRequest request) {

		logger.debug("starts");

		logger.info("Example CalculatorProcessor (/divide) ->  a :{}, b:{}", a, b);

		logger.debug("ends");

		return new ResponseEntity<Integer>(service.divide(a, b), HttpStatus.OK);
	}

}
