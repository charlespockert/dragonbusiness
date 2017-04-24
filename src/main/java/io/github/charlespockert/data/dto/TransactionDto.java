package io.github.charlespockert.data.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public class TransactionDto {
	public int id;
	public UUID uuid;
	public int companyId;
	public Timestamp date;
	public BigDecimal amount;
	public TransactionType type;
}
