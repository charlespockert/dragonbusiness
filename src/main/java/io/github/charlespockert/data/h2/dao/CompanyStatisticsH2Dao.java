package io.github.charlespockert.data.h2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;

import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.dao.CompanyStatisticsDao;
import io.github.charlespockert.data.dto.CompanyPerformanceDto;
import io.github.charlespockert.data.dto.CompanySummaryDto;
import io.github.charlespockert.data.h2.DatabaseMapper;
import io.github.charlespockert.data.h2.DbUtil;

public class CompanyStatisticsH2Dao extends DaoBase implements CompanyStatisticsDao {

	public CompanyStatisticsH2Dao(ConnectionManager connectionManager, Logger logger, DatabaseMapper mapper) {
		super(connectionManager, logger, mapper);
	}

	@Override
	public List<CompanySummaryDto> getSummary(String filter) throws SQLException {
		if (filter == null)
			filter = "";

		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select company.name, employee.name as owner, (select count(*) from employee where employee.company_id = company.id) as employeecount, company.value from company inner join employee on employee.company_id = company.id and employee.rank = 3 and company.name like ?",
					"%" + filter + "%");
			List<CompanySummaryDto> summaries = mapper.populateMany(statement, CompanySummaryDto.class);

			return summaries;
		}
	}

	@Override
	public CompanyPerformanceDto getTopTurnoverByPeriod(int periodId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select id, name, bonuses, dividends, (turnover - overheads) / value * 100 as growth, overheads,  turnover - overheads as profit, salary, turnover, value + (turnover - overheads) as value "
							+ " from (select company.id, company.name, company.value,"
							+ "		sum(CASE WHEN type in (0) THEN amount ELSE 0 END) as salary,"
							+ "		sum(CASE WHEN type in (1) THEN amount ELSE 0 END) as bonuses,"
							+ "		sum(CASE WHEN type in (2) THEN amount ELSE 0 END) as dividends,"
							+ "		sum(CASE WHEN type in (3) THEN amount ELSE 0 END) as turnover,"
							+ "		sum(CASE WHEN type in (0,1,2) THEN amount ELSE 0 END) as overheads"
							+ "	from company inner join transaction on transaction.company_id = company.id"
							+ "	inner join period on (transaction.date between period.start_date and period.end_date) or (transaction.date >= period.start_date and period.end_date is null)"
							+ "	where period.id = ?)",
					periodId);

			return mapper.populateSingle(statement, CompanyPerformanceDto.class);
		}
	}

	@Override
	public CompanyPerformanceDto getTopGrowthByPeriod(int periodId) throws SQLException {
		return null;
	}

	@Override
	public List<CompanyPerformanceDto> getAllPerformance() throws SQLException {
		return null;
	}

	@Override
	public CompanyPerformanceDto getPerformance(int companyId, int periodId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = conn.prepareStatement(
					"select company_id, period_id, company_name, bonuses, dividends, growth, overheads, profit, salary, turnover, value from performance where company_id = ? and period_id = ?");
			statement.setInt(1, companyId);
			statement.setInt(2, periodId);

			return mapper.populateSingle(statement, CompanyPerformanceDto.class);
		}
	}

}
