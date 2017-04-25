package io.github.charlespockert.data.dao;

import java.sql.SQLException;
import java.util.List;

import io.github.charlespockert.data.dto.CompanyPerformanceDto;
import io.github.charlespockert.data.dto.CompanySummaryDto;

public interface CompanyStatisticsDao {

	public List<CompanySummaryDto> getSummary(String filter) throws SQLException;

	public CompanyPerformanceDto getTopTurnoverByPeriod(int periodId) throws SQLException;

	public CompanyPerformanceDto getTopGrowthByPeriod(int periodId) throws SQLException;

	public List<CompanyPerformanceDto> getAllPerformance() throws SQLException;

	public CompanyPerformanceDto getPerformance(int companyId, int periodId) throws SQLException;
}
