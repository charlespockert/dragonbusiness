package io.github.charlespockert.data.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import io.github.charlespockert.data.dto.EmployeeDto;
import io.github.charlespockert.data.dto.EmployeeRank;

public interface EmployeeDao {
	public boolean existsByName(String name) throws SQLException;

	public boolean exists(UUID uuid) throws SQLException;

	public EmployeeDto getByName(String name) throws SQLException;

	public EmployeeDto getById(UUID uuid) throws SQLException;

	public List<EmployeeDto> getByCompanyId(int companyId) throws SQLException;

	public List<EmployeeDto> getAll() throws SQLException;

	public List<EmployeeDto> getByRank(EmployeeRank rank) throws SQLException;

	public EmployeeDto getCompanyOwner(int companyId) throws SQLException;

	public void create(UUID uuid, int companyId, String name, EmployeeRank rank) throws SQLException;

	public void update(UUID uuid, String name) throws SQLException;

	public void delete(UUID uuid) throws SQLException;
}
