package io.github.charlespockert.data.h2.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.charlespockert.data.dto.CompanyIdentifierDto;

public class CompanyIdentifierDtoMapper implements DtoMapper<CompanyIdentifierDto> {

	@Override
	public CompanyIdentifierDto map(ResultSet resultSet) throws SQLException {
		CompanyIdentifierDto dto = new CompanyIdentifierDto();

		dto.id = resultSet.getInt("id");
		dto.name = resultSet.getString("name");

		return dto;
	}

}
