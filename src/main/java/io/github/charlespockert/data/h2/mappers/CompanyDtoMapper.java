package io.github.charlespockert.data.h2.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.charlespockert.data.dto.CompanyDto;

public class CompanyDtoMapper extends DtoMapper<CompanyDto> {

	@Override
	public CompanyDto map(ResultSet resultSet) throws SQLException {
		CompanyDto dto = new CompanyDto();

		dto.id = resultSet.getInt("id");
		dto.name = resultSet.getString("name");
		dto.bankrupt = resultSet.getBoolean("bankrupt");
		dto.sharesIssued = resultSet.getInt("shares_issued");
		dto.value = resultSet.getFloat("value");

		return dto;
	}

}
