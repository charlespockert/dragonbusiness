package io.github.charlespockert.data.h2.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.charlespockert.data.dto.CompanyPerformanceDto;

public class CompanyPerformanceDtoMapper implements DtoMapper<CompanyPerformanceDto> {

	@Override
	public CompanyPerformanceDto map(ResultSet resultSet) throws SQLException {
		CompanyPerformanceDto dto = new CompanyPerformanceDto();

		dto.companyId = resultSet.getInt("company_id");
		dto.periodId = resultSet.getInt("period_id");
		dto.companyName = resultSet.getString("company_name");
		dto.bonuses = resultSet.getBigDecimal("bonuses");
		dto.dividends = resultSet.getBigDecimal("dividends");
		dto.growth = resultSet.getBigDecimal("growth");
		dto.overheads = resultSet.getBigDecimal("overheads");
		dto.profit = resultSet.getBigDecimal("profit");
		dto.salary = resultSet.getBigDecimal("salary");
		dto.turnover = resultSet.getBigDecimal("turnover");
		dto.value = resultSet.getBigDecimal("value");

		return dto;
	}

}
