package io.github.charlespockert.data.h2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;

import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.dao.PeriodDao;
import io.github.charlespockert.data.dto.PeriodDto;
import io.github.charlespockert.data.h2.DatabaseMapper;
import io.github.charlespockert.data.h2.DbUtil;

public class PeriodH2Dao extends DaoBase implements PeriodDao {

	public PeriodH2Dao(ConnectionManager connectionManager, Logger logger, DatabaseMapper mapper) {
		super(connectionManager, logger, mapper);
	}

	@Override
	public PeriodDto getById(int id) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from period where id = ?", id);
			return mapper.populateSingle(statement, PeriodDto.class);
		}
	}

	@Override
	public List<PeriodDto> getAll() throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from period");
			return mapper.populateMany(statement, PeriodDto.class);
		}
	}

	@Override
	public PeriodDto getByTimestamp(Timestamp timestamp) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select * from period where start_date <= ? and end_date > ?", timestamp, timestamp);
			return mapper.populateSingle(statement, PeriodDto.class);
		}
	}

	@Override
	public PeriodDto getCurrent() throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from period where end_date is null");
			return mapper.populateSingle(statement, PeriodDto.class);
		}
	}

	@Override
	public int create() throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			conn.setAutoCommit(false);

			// Close any existing periods
			Timestamp ts = Timestamp.from(Instant.now());
			PreparedStatement closeStatement = DbUtil.prepareStatement(conn,
					"update period set end_date = ? where end_date is null", ts);
			closeStatement.execute();

			PreparedStatement newStatement = DbUtil.prepareStatement(conn,
					"insert into period (start_date, end_date) values (?, NULL)", ts);
			newStatement.executeUpdate();
			ResultSet updateResult = newStatement.getGeneratedKeys();
			updateResult.next();
			int periodId = updateResult.getInt(1);
			conn.commit();
			return periodId;
		} catch (SQLException e) {
			throw e;
		}
	}
}
