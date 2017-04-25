package io.github.charlespockert.data.h2.dao;

import java.math.BigDecimal;
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
import io.github.charlespockert.data.dto.TransactionType;
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

			// Get existing period
			PreparedStatement getPeriod = DbUtil.prepareStatement(conn, "select id from period where end_date is null");
			ResultSet result = getPeriod.executeQuery();
			result.next();
			int periodId = result.getInt("id");

			Timestamp ts = Timestamp.from(Instant.now());
			PreparedStatement closeStatement = DbUtil.prepareStatement(conn,
					"update period set end_date = ? where id = ?", ts, periodId);
			closeStatement.execute();

			PreparedStatement newStatement = DbUtil.prepareStatement(conn,
					"insert into period (start_date, end_date) values (?, NULL)", ts);
			newStatement.executeUpdate();
			ResultSet updateResult = newStatement.getGeneratedKeys();
			updateResult.next();
			int newPeriodId = updateResult.getInt(1);

			// Take a snapshot of all companies financial positions
			PreparedStatement snapshotCompanies = conn.prepareStatement(
					"insert into performance (company_id, period_id, company_name, bonuses, dividends, growth, overheads, profit, salary, turnover, value) "
							+ " select company_id, period_id, name, bonuses, dividends, CASE WHEN value > 0 THEN (turnover - overheads) / value * 100 ELSE 100 END as growth, overheads,  turnover - overheads as profit, salary, turnover, value + (turnover - overheads) as value "
							+ " from (select company.id as company_id, company.name, company.value, period.id as period_id,"
							+ " sum(CASE WHEN type in (0) THEN amount ELSE 0 END) as salary,"
							+ " sum(CASE WHEN type in (1) THEN amount ELSE 0 END) as bonuses,"
							+ " sum(CASE WHEN type in (2) THEN amount ELSE 0 END) as dividends,"
							+ " sum(CASE WHEN type in (3) THEN amount ELSE 0 END) as turnover,"
							+ " sum(CASE WHEN type in (0,1,2) THEN amount ELSE 0 END) as overheads" + " from company"
							+ " left join period on period.id = ?"
							+ "	left join transaction on transaction.date between period.start_date and period.end_date and transaction.company_id = company.id"
							+ " group by company.id, company.name, company.value)");
			snapshotCompanies.setInt(1, periodId);
			snapshotCompanies.executeUpdate();

			// Update company value to match that of the snapshots
			PreparedStatement updateValues = conn.prepareStatement(
					"update company set value = isnull((select value from performance where performance.company_id = company.id and performance.period_id = ?), 0)");
			updateValues.setInt(1, periodId);
			updateValues.executeUpdate();

			conn.commit();
			return newPeriodId;
		} catch (SQLException e) {
			throw e;
		}
	}
}
