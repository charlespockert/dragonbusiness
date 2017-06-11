package io.github.charlespockert.data.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class ApplicationDto {

	public int applicationId;

	public UUID employeeId;

	public int companyId;

	public Timestamp date;

	public ApplicationStatus status;

	public String rejectionReason;
}
