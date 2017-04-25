package io.github.charlespockert.data.h2.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.UuidUtil;
import io.github.charlespockert.data.dao.TransactionDao;
import io.github.charlespockert.data.dto.TransactionDto;
import io.github.charlespockert.data.dto.TransactionType;
import io.github.charlespockert.data.h2.DatabaseMapper;
import io.github.charlespockert.data.h2.DbUtil;

public class TransactionH2Dao extends DaoBase implements TransactionDao {

	public TransactionH2Dao(ConnectionManager connectionManager, Logger logger, DatabaseMapper mapper) {
		super(connectionManager, logger, mapper);
	}

	@Override
	public TransactionDto get(int id) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from transaction where id = ?", id);
			return mapper.populateSingle(statement, TransactionDto.class);
		}
	}

	@Override
	public List<TransactionDto> getByEmployeeId(UUID employeeId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from transaction where uuid = ?",
					employeeId);
			return mapper.populateMany(statement, TransactionDto.class);
		}
	}

	@Override
	public List<TransactionDto> getByEmployeeIdAndPeriod(UUID employeeId, int periodId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from transaction where uuid = ?",
					UuidUtil.asBytes(employeeId));
			return mapper.populateMany(statement, TransactionDto.class);
		}
	}

	@Override
	public List<TransactionDto> getByCompanyIdAndPeriod(int companyId, int periodId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select * from transaction where company_id = ?", companyId, periodId);
			return mapper.populateMany(statement, TransactionDto.class);
		}
	}

	@Override
	public List<TransactionDto> getByCompanyId(int companyId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select * from transaction where company_id = ?", companyId);
			return mapper.populateMany(statement, TransactionDto.class);
		}
	}

	@Override
	public List<TransactionDto> getByPeriodId(int periodId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select * from transaction where company_id = ?", periodId);
			return mapper.populateMany(statement, TransactionDto.class);
		}
	}

	@Override
	public int create(UUID employeeId, int companyId, Timestamp date, BigDecimal amount, TransactionType type)
			throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {

			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"insert into transaction (uuid, company_id, date, amount, type) VALUES (?, ?, ?, ?, ?)",
					UuidUtil.asBytes(employeeId), companyId, date, amount, type);
			statement.executeUpdate();
			ResultSet updateResult = statement.getGeneratedKeys();
			updateResult.next();
			int transactionId = updateResult.getInt(1);

			return transactionId;
		}
	}
}
