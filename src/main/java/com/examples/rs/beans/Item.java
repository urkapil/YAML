package com.examples.rs.beans;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName = "Lambda_Warmer")
public class Item {

	private String functionName;
	private Integer targetInstances;
	private Integer activeInstances;
	private Long version;

	@DynamoDBHashKey(attributeName = "Function_Name")
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@DynamoDBAttribute(attributeName = "Target_Instances")
	public Integer getTargetInstances() {
		return targetInstances;
	}

	public void setTargetInstances(Integer targetInstances) {
		this.targetInstances = targetInstances;
	}

	@DynamoDBAttribute(attributeName = "Active_Instances")
	public Integer getActiveInstances() {
		return activeInstances;
	}

	public void setActiveInstances(Integer activeInstances) {
		this.activeInstances = activeInstances;
	}

	@DynamoDBVersionAttribute
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String toString() {

		StringBuilder str = new StringBuilder();

		str.append(" { function_name :").append(functionName).append(", active:").append(activeInstances)
				.append(", target:").append(targetInstances).append(", version:").append(version).append("}");
		return str.toString();
	}

}