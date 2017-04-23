package io.github.charlespockert.data.h2.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.charlespockert.data.UuidUtil;
import io.github.charlespockert.data.dto.ShareDto;

public class ShareDtoMapper extends DtoMapper<ShareDto> {

	@Override
	public ShareDto map(ResultSet resultSet) throws SQLException {
		ShareDto dto = new ShareDto();

		dto.companyId = resultSet.getInt("company_id");
		dto.uuid = UuidUtil.asUuid(resultSet.getBytes("uuid"));
		dto.count = resultSet.getInt("count");

		return dto;
	}
}
