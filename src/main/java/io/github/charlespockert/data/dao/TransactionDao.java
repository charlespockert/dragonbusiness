package io.github.charlespockert.data.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import io.github.charlespockert.data.dto.TransactionDto;
import io.github.charlespockert.data.dto.TransactionType;

public interface TransactionDao {
	public TransactionDto get(int id) throws SQLException;

	public List<TransactionDto> getByEmployeeId(UUID employeeId) throws SQLException;

	public List<TransactionDto> getByEmployeeIdAndPeriod(UUID employeeId, int periodId) throws SQLException;

	public List<TransactionDto> getByCompanyId(int periodId) throws SQLException;

	public List<TransactionDto> getByCompanyIdAndPeriod(int companyId, int periodId) throws SQLException;

	public List<TransactionDto> getByPeriodId(int periodId) throws SQLException;

	public int create(UUID employeeId, int companyId, Timestamp date, BigDecimal amount,
			TransactionType type) throws SQLException;

}
