package io.github.charlespockert.data.h2.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.charlespockert.data.UuidUtil;
import io.github.charlespockert.data.dto.EmployeeDto;

public class EmployeeDtoMapper extends DtoMapper<EmployeeDto> {

	@Override
	public EmployeeDto map(ResultSet resultSet) throws SQLException {
		EmployeeDto dto = new EmployeeDto();

		dto.uuid = UuidUtil.asUuid(resultSet.getBytes("uuid"));
		dto.name = resultSet.getString("name");
		dto.rank = resultSet.getInt("rank");

		return dto;
	}

}
