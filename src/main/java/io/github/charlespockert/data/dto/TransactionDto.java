package io.github.charlespockert.data.dto;

import java.util.Date;
import java.util.UUID;

import io.github.charlespockert.entities.TransactionType;

public class TransactionDto {
	public int id;
	public UUID uuid;
	public int companyId;
	public Date date;
	public float amount;
	public int type;
}
