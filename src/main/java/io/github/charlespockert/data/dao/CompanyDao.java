package io.github.charlespockert.data.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import io.github.charlespockert.data.dto.*;

public interface CompanyDao {
	public boolean exists(String name) throws SQLException;

	public boolean exists(int companyId) throws SQLException;

	public List<CompanyIdentifierDto> getIdentifiers() throws SQLException;

	public EmployeeDto getOwner(int companyId) throws SQLException;

	public CompanyDto getByName(String name) throws SQLException;

	public CompanyDto getById(int companyId) throws SQLException;

	public CompanyDto getByEmployeeId(UUID uuid) throws SQLException;

	public List<CompanyDto> getAll(String filter) throws SQLException;

	public int create(String name, UUID employeeId, String employeeName) throws SQLException;

	public void update(CompanyDto company) throws SQLException;

	public void delete(CompanyDto company) throws SQLException;
}
