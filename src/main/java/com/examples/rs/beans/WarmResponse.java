package com.examples.rs.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WarmResponse {
	private boolean isNew;
	private String id;
	private int invocationId;

	private String lastAccessDate;

	private static final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss:SSS zzz");

	public WarmResponse() {

	}

	public WarmResponse(String id, int invocationId, boolean isNew, Date lastAccess) {
		super();
		this.isNew = isNew;
		this.id = id;
		this.invocationId = invocationId;
		this.lastAccessDate = formatter.format(lastAccess);

	}

	/**
	 * @return the isNew
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the lastAccess
	 */
	public String getLastAccess() {
		return lastAccessDate;
	}

	/**
	 * @return the invocationId
	 */
	public int getInvocationId() {
		return invocationId;
	}
}