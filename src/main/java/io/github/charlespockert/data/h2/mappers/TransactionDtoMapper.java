package io.github.charlespockert.data.h2.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.charlespockert.data.UuidUtil;
import io.github.charlespockert.data.dto.TransactionDto;

public class TransactionDtoMapper extends DtoMapper<TransactionDto> {

	@Override
	public TransactionDto map(ResultSet resultSet) throws SQLException {
		TransactionDto dto = new TransactionDto();

		dto.amount = resultSet.getFloat("amount");
		dto.companyId = resultSet.getInt("company_id");
		dto.date = resultSet.getDate("date");
		dto.id = resultSet.getInt("id");
		dto.type = resultSet.getInt("type");
		dto.uuid = UuidUtil.asUuid(resultSet.getBytes("uuid"));

		return dto;
	}

}
