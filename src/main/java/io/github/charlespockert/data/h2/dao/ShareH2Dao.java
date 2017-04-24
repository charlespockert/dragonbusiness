package io.github.charlespockert.data.h2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.UuidUtil;
import io.github.charlespockert.data.dao.ShareDao;
import io.github.charlespockert.data.dto.ShareDto;
import io.github.charlespockert.data.h2.DatabaseMapper;
import io.github.charlespockert.data.h2.DbUtil;

public class ShareH2Dao extends DaoBase implements ShareDao {

	public ShareH2Dao(ConnectionManager connectionManager, Logger logger, DatabaseMapper mapper) {
		super(connectionManager, logger, mapper);
	}

	@Override
	public ShareDto get(UUID employeeId, int companyId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select * from share where uuid = ? and company_id = ?", UuidUtil.asBytes(employeeId), companyId);
			return mapper.populateSingle(statement, ShareDto.class);
		}
	}

	@Override
	public List<ShareDto> getByEmployeeId(UUID employeeId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from share where uuid = ?",
					UuidUtil.asBytes(employeeId));
			return mapper.populateMany(statement, ShareDto.class);
		}
	}

	@Override
	public List<ShareDto> getByCompanyId(int companyId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from share where company_id = ?",
					companyId);
			return mapper.populateMany(statement, ShareDto.class);
		}
	}

	@Override
	public void create(int companyId, UUID employeeId, int count) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"insert into share (company_id, uuid, count) VALUES (?, ? ,?)", companyId,
					UuidUtil.asBytes(employeeId), count);
			statement.execute();
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public void update(int companyId, UUID employeeId, int newCount) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"update share set count = ? where company_id = ? and uuid = ?", companyId,
					UuidUtil.asBytes(employeeId), newCount);
			statement.execute();
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public void delete(int companyId, UUID employeeId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"delete share where company_id = ? and uuid = ?", companyId, UuidUtil.asBytes(employeeId));
			statement.execute();
		} catch (SQLException e) {
			throw e;
		}
	}

}
