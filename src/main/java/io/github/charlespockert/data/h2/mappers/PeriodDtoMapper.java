package io.github.charlespockert.data.h2.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.charlespockert.data.dto.PeriodDto;

public class PeriodDtoMapper implements DtoMapper<PeriodDto> {

	@Override
	public PeriodDto map(ResultSet resultSet) throws SQLException {
		PeriodDto dto = new PeriodDto();

		dto.id = resultSet.getInt("id");
		dto.endDate = resultSet.getTimestamp("end_date");
		dto.startDate = resultSet.getTimestamp("start_date");

		return dto;
	}

}
