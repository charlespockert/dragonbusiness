package io.github.charlespockert.data.h2.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.charlespockert.data.dto.CompanySummaryDto;

public class CompanySummaryDtoMapper implements DtoMapper<CompanySummaryDto> {

	@Override
	public CompanySummaryDto map(ResultSet resultSet) throws SQLException {
		CompanySummaryDto dto = new CompanySummaryDto();

		dto.name = resultSet.getString("name");
		dto.owner = resultSet.getString("owner");
		dto.employeeCount = resultSet.getInt("employeecount");
		dto.value = resultSet.getFloat("value");

		return dto;
	}

}
