package io.github.charlespockert.data.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import io.github.charlespockert.data.dto.ApplicationDto;
import io.github.charlespockert.data.dto.ApplicationStatus;

public interface ApplicationDao {
	public ApplicationDto getById(int applicationId) throws SQLException;

	public List<ApplicationDto> getByEmployeeId(UUID employeeId) throws SQLException;

	public List<ApplicationDto> getByCompanyId(int companyId) throws SQLException;

	public int create(int companyId, UUID employeeId) throws SQLException;

	public void update(int applicationId, ApplicationStatus status, String rejectionReason) throws SQLException;

	public void delete(int applicationId) throws SQLException;
}
