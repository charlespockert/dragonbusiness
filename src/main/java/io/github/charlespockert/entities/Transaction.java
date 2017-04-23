package io.github.charlespockert.entities;

import java.util.Date;
import java.util.UUID;

public class Transaction {
	private int id;
	private UUID uuid;
	private int companyId;
	private Date date;
	private float amount;
	private int type;

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	public int setCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(int companyid) {
		this.companyId = companyid;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public float getAmount() {
		return amount;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
}
