package io.github.charlespockert.data.h2.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.charlespockert.data.UuidUtil;
import io.github.charlespockert.data.dto.EmployeeDto;
import io.github.charlespockert.data.dto.EmployeeRank;

public class EmployeeDtoMapper extends DtoMapper<EmployeeDto> {

	@Override
	public EmployeeDto map(ResultSet resultSet) throws SQLException {
		EmployeeDto dto = new EmployeeDto();

		dto.uuid = UuidUtil.asUuid(resultSet.getBytes("uuid"));
		dto.name = resultSet.getString("name");
		dto.rank = EmployeeRank.values()[resultSet.getInt("rank")];
		dto.company_id = resultSet.getInt("company_id");
		dto.employmentStart = resultSet.getDate("employment_start");

		return dto;
	}

}
