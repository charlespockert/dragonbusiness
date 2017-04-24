package io.github.charlespockert.data.dto;

import java.util.Date;
import java.util.UUID;

public class EmployeeDto {
	public UUID uuid;
	public String name;
	public EmployeeRank rank;
	public int company_id;
	public Date employmentStart;
}
