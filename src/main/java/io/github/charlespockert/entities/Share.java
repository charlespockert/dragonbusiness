package io.github.charlespockert.entities;

import java.util.UUID;

public class Share {
	private int companyid;
	private UUID uuid;
	private int count;
	
	public int getCompanyid() {
		return companyid;
	}
	
	public void setCompanyid(int companyid) {
		this.companyid = companyid;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
}
