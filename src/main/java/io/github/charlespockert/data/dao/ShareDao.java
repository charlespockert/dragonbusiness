package io.github.charlespockert.data.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import io.github.charlespockert.data.dto.ShareDto;

public interface ShareDao {
	public ShareDto get(UUID employeeId, int companyId) throws SQLException;

	public List<ShareDto> getByEmployeeId(UUID employeeId) throws SQLException;

	public List<ShareDto> getByCompanyId(int companyId) throws SQLException;

	public void create(int companyId, UUID employeeId, int amount) throws SQLException;

	public void update(int companyId, UUID employeeId, int newAmount) throws SQLException;

	public void delete(int companyId, UUID employeeId) throws SQLException;
}
